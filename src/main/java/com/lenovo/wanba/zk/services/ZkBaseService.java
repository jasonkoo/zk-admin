package com.lenovo.wanba.zk.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.lenovo.wanba.zk.model.ZkNode;

public class ZkBaseService {

	private ZooKeeper zk = null;

	/**
	 * 初始化：创建连接
	 */
	public ZkBaseService(final String connectString, final int sessionTimeout) {

		ZooKeeper old = zk;
		try {
			zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
				// 监控所有被触发的事件
				public void process(WatchedEvent event) {
					if (event.getState() == KeeperState.Expired) {
						new ZkBaseService(connectString, sessionTimeout);
					}
				}
			});
		} catch (IOException e) {
			throw new RuntimeException("Error occurs while creating ZooKeeper instance.", e);
		} finally { // 确保老的资源能顺利被释放
			if (old != null) {
				try {
					old.close(); // 将旧的关闭掉
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 取得节点信息
	 * 
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public ZkNode getZNode(String path) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, false);
		ZkNode znode = null;
		if (stat != null) {
			znode = new ZkNode();
			znode.setStat(stat);
			try {
				String data = zk.getData(path, false, null) == null ? "" : new String(zk.getData(path, false, null),
				        "utf-8");
				znode.setData(data);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			znode.setPath(path);
			znode.setName("/".equals(path) ? "/" : path.substring(path.lastIndexOf('/') + 1));
			znode.setId(StringUtils.replace(path, "/", "△"));
			znode.setCtime(DateFormatUtils.format(stat.getCtime(), "yyyy-MM-dd HH:mm:ss"));
			znode.setMtime(DateFormatUtils.format(stat.getMtime(), "yyyy-MM-dd HH:mm:ss"));
		}
		return znode;
	}

	/**
	 * 取得子节点信息
	 * 
	 * @param path
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public List<ZkNode> getChildZNodes(String path) throws KeeperException, InterruptedException {
		List<String> nameList = zk.getChildren(path, false);
		List<ZkNode> znodeList = null;
		if (nameList != null && nameList.size() > 0) {
			znodeList = new ArrayList<ZkNode>();
			for (String name : nameList) {
				znodeList.add(this.getZNode(StringUtils.endsWith(path, "/") ? path + name : path + "/" + name));
			}
		}
		return znodeList;
	}

	/**
	 * 取得某一节点及递归子节点的路径栈
	 * 
	 * @param pathStack
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public Stack<String> getZnodePathStack(Stack<String> pathStack, String path) throws KeeperException,
	        InterruptedException {
		List<ZkNode> children = this.getChildZNodes(path);
		if (children == null) {
			pathStack.push(path);
		} else {
			pathStack.push(path);
			for (ZkNode child : children) {
				this.getZnodePathStack(pathStack, child.getPath());
			}
		}

		return pathStack;
	}

	/**
	 * 更新节点信息
	 * 
	 * @param path
	 * @param data
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void updateZNode(String path, String data) throws KeeperException, InterruptedException {
		// ZookeeperManager.getInstance().setData(path, data);
		zk.setData(path, data.getBytes(Charset.forName("utf-8")), -1);
	}

	/**
	 * 删除节点
	 * 
	 * @param path
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void deleteZNode(String path) throws InterruptedException, KeeperException {
		zk.delete(path, -1);
	}

	/**
	 * 删除节点及所有递归子节点
	 * 
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void deleteZnodeByIte(String path) throws KeeperException, InterruptedException {
		Stack<String> pathStack = new Stack<String>();

		pathStack = this.getZnodePathStack(pathStack, path);

		while (!pathStack.empty()) {
			this.deleteZNode(pathStack.pop());
		}
	}

	/**
	 * 增加节点
	 * 
	 * @param path
	 * @param data
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void addZNode(String path, String data) throws KeeperException, InterruptedException {
		if (this.getZNode(path) != null) {
			this.updateZNode(path, data);
		} else {
			zk.create(path, data.getBytes(Charset.forName("utf-8")), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
	}

}
