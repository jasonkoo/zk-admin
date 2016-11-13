package com.lenovo.wanba.zk.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lenovo.push.reids.api.vo.RedisConf;
import com.lenovo.wanba.zk.model.ZkNode;

@Service
public class RedisConfigService {

	@Autowired
	private ZkBaseService zkAjaxService;

	/**
	 * 取得数据库配置列表
	 * 
	 * @param path
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public List<RedisConf> getRedisConfigList(String path) throws KeeperException, InterruptedException {
		List<ZkNode> znodeList = zkAjaxService.getChildZNodes(path);
		List<RedisConf> redisConfigList = null;
		if (znodeList != null) {
			redisConfigList = new ArrayList<RedisConf>();
			Gson gson = new Gson();
			for (ZkNode znode : znodeList) {
				RedisConf redisCfg = gson.fromJson(znode.getData(), RedisConf.class);
				redisConfigList.add(redisCfg);
			}
		}

		return redisConfigList;
	}

	/**
	 * 取得数据库配置
	 * 
	 * @param path
	 * @param name
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public RedisConf getRedisConfig(String path, String name) throws KeeperException, InterruptedException {
		List<RedisConf> redisConfigList = this.getRedisConfigList(path);
		if (redisConfigList != null) {
			for (RedisConf redisConfig : redisConfigList) {
				if (StringUtils.equals(name, redisConfig.getName())) {
					return redisConfig;
				}
			}
		}
		return null;
	}

	/**
	 * 更新数据库配置
	 * 
	 * @param path
	 * @param dbname
	 * @param redisConfig
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void updateRedisConfig(String path, String dbname, RedisConf redisConfig) throws KeeperException,
	        InterruptedException {

		// 新节点信息
		Gson gson = new Gson();
		String newData = gson.toJson(redisConfig);
		if (dbname == null || dbname.trim().length() == 0) {
			addByDbName(path, redisConfig, newData);
		} else {
			updateByDbName(path, dbname, redisConfig, newData);
		}

	}

	/**
	 * 通过DBName新增
	 * 
	 * @param path
	 * @param redisConfig
	 * @param newData
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private void addByDbName(String path, RedisConf redisConfig, String newData) throws KeeperException,
	        InterruptedException {
		String redisName = redisConfig.getName();
		if (zkAjaxService.getZNode(path) == null) {
			zkAjaxService.addZNode(path, "");
		}
		zkAjaxService.addZNode(path + "/" + redisName, newData);
	}

	/**
	 * 通过dbName修改zkpath的值
	 * 
	 * @param path
	 * @param dbname
	 * @param redisConfig
	 * @param newData
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private void updateByDbName(String path, String dbname, RedisConf redisConfig, String newData)
	        throws KeeperException, InterruptedException {
		if (!dbname.equals(redisConfig.getName())) {
			if (zkAjaxService.getZNode(path + "/" + dbname) != null) {
				zkAjaxService.deleteZNode(path + "/" + dbname);
			}
			zkAjaxService.addZNode(path + "/" + redisConfig.getName(), newData);
		} else {
			zkAjaxService.addZNode(path + "/" + dbname, newData);
		}
	}
}
