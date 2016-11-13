package com.lenovo.wanba.zk.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lenovo.wanba.common.db.GummyBizNameDBConfig;
import com.lenovo.wanba.common.db.GummyDataSourceConfig;
import com.lenovo.wanba.zk.model.DbConfigModel;
import com.lenovo.wanba.zk.model.DbConfigServerModel;
import com.lenovo.wanba.zk.model.ZkNode;

@Service
public class DbConfigService {

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
	public List<DbConfigModel> getDbConfigList(String path) throws KeeperException, InterruptedException {
		List<ZkNode> znodeList = zkAjaxService.getChildZNodes(path);
		List<DbConfigModel> dbConfigList = null;
		if (znodeList != null) {

			dbConfigList = new ArrayList<DbConfigModel>();
			Gson gson = new Gson();
			Set<String> znodeDataSet = new HashSet<String>();

			for (ZkNode znode : znodeList) {
				GummyBizNameDBConfig biznameDBCfg = null;
				String znodeData = znode.getData();

				biznameDBCfg = gson.fromJson(znodeData, GummyBizNameDBConfig.class);
				if (biznameDBCfg != null) {
					if (!znodeDataSet.add(znodeData)) {
						for (DbConfigModel tmpDbConfig : dbConfigList) {
							if (StringUtils.equals(tmpDbConfig.getDbname(), biznameDBCfg.getDatabaseName())) {
								tmpDbConfig.getBizNameList().add(znode.getName());
							}
						}
						continue;
					}

					genetateDbConfigModel(dbConfigList, znode, biznameDBCfg);
				}
			}
		}

		return dbConfigList;
	}

	/**
	 * 生成DBCONFIGModel
	 * 
	 * @param dbConfigList
	 * @param znode
	 * @param biznameDBCfg
	 */
	private void genetateDbConfigModel(List<DbConfigModel> dbConfigList, ZkNode znode, GummyBizNameDBConfig biznameDBCfg) {
		if (biznameDBCfg != null) {
			DbConfigModel dbConfig = new DbConfigModel();

			List<String> bizNameList = new ArrayList<String>();
			bizNameList.add(znode.getName());
			dbConfig.setBizNameList(bizNameList);

			dbConfig.setDbname(biznameDBCfg.getDatabaseName());
			// dbConfig.setMode(biznameDBCfg.getMode());

			List<GummyDataSourceConfig> gummyDataSourceConfigList = biznameDBCfg.getServers();
			if (gummyDataSourceConfigList != null) {
				List<DbConfigServerModel> serverList = new ArrayList<DbConfigServerModel>();

				for (GummyDataSourceConfig gummyDataSourceConfig : gummyDataSourceConfigList) {
					dbConfig.setInitialSize(gummyDataSourceConfig.getInitialSize());
					dbConfig.setMaxActive(gummyDataSourceConfig.getMaxActive());
					dbConfig.setMaxWait(gummyDataSourceConfig.getMaxWait());

					dbConfig.setDbstatus("avalibled");
					if (!StringUtils.equals(gummyDataSourceConfig.getStatus(), "enabled")) {
						dbConfig.setDbstatus("unvalibled");
					}

					DbConfigServerModel server = new DbConfigServerModel();
					server.setHost(gummyDataSourceConfig.getHost());
					server.setPort(gummyDataSourceConfig.getPort());
					server.setUser(gummyDataSourceConfig.getUser());
					server.setPassword(gummyDataSourceConfig.getPassword());
					server.setStatus(gummyDataSourceConfig.getStatus());
					// server.setToken(gummyDataSourceConfig.getToken());
					// server.setWrflag(gummyDataSourceConfig.getWrflag());
					serverList.add(server);
				}

				dbConfig.setServerList(serverList);
			}

			dbConfigList.add(dbConfig);
		}
	}

	/**
	 * 取得数据库配置
	 * 
	 * @param path
	 * @param dbname
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public DbConfigModel getDbConfig(String path, String dbname) throws KeeperException, InterruptedException {
		List<DbConfigModel> dbConfigList = this.getDbConfigList(path);
		if (dbConfigList != null) {
			for (DbConfigModel dbConfig : dbConfigList) {
				if (StringUtils.equals(dbname, dbConfig.getDbname())) {
					return dbConfig;
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
	 * @param dbConfig
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public void updateDbConfig(String path, String dbname, DbConfigModel dbConfig) throws Exception {
		// 数据格式变更
		GummyBizNameDBConfig gummyDbConfig = new GummyBizNameDBConfig();
		// gummyDbConfig.setMode(dbConfig.getMode());

		List<DbConfigServerModel> serverList = dbConfig.getServerList();
		List<GummyDataSourceConfig> gummyDataSourceConfigList = generateDbConfigServerModelList(dbConfig, serverList);
		gummyDbConfig.setServers(gummyDataSourceConfigList);

		// 新节点信息
		Gson gson = new Gson();
		String newData = gson.toJson(gummyDbConfig);

		// 取得原节点
		DbConfigModel oldDbConfig = this.getDbConfig(path, dbname);

		// 根据用户提交的bizNameList，判断节点变化
		List<String> oldBizNameList = new ArrayList<String>();
		if (oldDbConfig != null) {
			oldBizNameList = oldDbConfig.getBizNameList();
		}
		List<String> bizNameList = dbConfig.getBizNameList();
		List<String> sameBizNameList = generateSameBizNameList(oldBizNameList, bizNameList);

		// 需要更新的节点
		updateNode(path, newData, sameBizNameList);

		// 需要删除的节点
		deleteNode(path, oldBizNameList, sameBizNameList);

		// 需要添加的节点
		addNode(path, newData, bizNameList, sameBizNameList);
	}

	/**
	 * 生成DBConfigServerModelList
	 * 
	 * @param dbConfig
	 * @param serverList
	 * @return
	 */
	private List<GummyDataSourceConfig> generateDbConfigServerModelList(DbConfigModel dbConfig,
	        List<DbConfigServerModel> serverList) {
		List<GummyDataSourceConfig> gummyDataSourceConfigList = new ArrayList<GummyDataSourceConfig>();
		for (DbConfigServerModel server : serverList) {
			GummyDataSourceConfig gummyDataSourceConfig = new GummyDataSourceConfig();
			gummyDataSourceConfig.setDatabase(dbConfig.getDbname());
			gummyDataSourceConfig.setHost(server.getHost());
			gummyDataSourceConfig.setInitialSize(dbConfig.getInitialSize());
			gummyDataSourceConfig.setMaxActive(dbConfig.getMaxActive());
			gummyDataSourceConfig.setMaxWait(dbConfig.getMaxWait());
			gummyDataSourceConfig.setPassword(server.getPassword());
			gummyDataSourceConfig.setPort(server.getPort());
			gummyDataSourceConfig.setStatus(server.getStatus());
			// gummyDataSourceConfig.setToken(server.getToken());
			gummyDataSourceConfig.setUser(server.getUser());
			// gummyDataSourceConfig.setWrflag(server.getWrflag());
			gummyDataSourceConfigList.add(gummyDataSourceConfig);
		}
		return gummyDataSourceConfigList;
	}

	/**
	 * 新增结点
	 * 
	 * @param path
	 * @param newData
	 * @param bizNameList
	 * @param sameBizNameList
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private void addNode(String path, String newData, List<String> bizNameList, List<String> sameBizNameList)
	        throws KeeperException, InterruptedException {
		bizNameList.removeAll(sameBizNameList);

		for (String addBizName : bizNameList) {
			zkAjaxService.addZNode(path + "/" + addBizName, newData);
		}
	}

	/**
	 * 删除结点
	 * 
	 * @param path
	 * @param oldBizNameList
	 * @param sameBizNameList
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	private void deleteNode(String path, List<String> oldBizNameList, List<String> sameBizNameList)
	        throws InterruptedException, KeeperException {
		oldBizNameList.removeAll(sameBizNameList);

		for (String delBizName : oldBizNameList) {
			zkAjaxService.deleteZNode(path + "/" + delBizName);
		}
	}

	/**
	 * 更新结点
	 * 
	 * @param path
	 * @param newData
	 * @param oldBizNameList
	 * @param bizNameList
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private List<String> updateNode(String path, String newData, List<String> sameBizNameList) throws KeeperException,
	        InterruptedException {
		for (String updBizName : sameBizNameList) {
			zkAjaxService.updateZNode(path + "/" + updBizName, newData);
		}
		return sameBizNameList;
	}

	/**
	 * 生成相同的别名List
	 * 
	 * @param oldBizNameList
	 * @param bizNameList
	 * @return
	 */
	private List<String> generateSameBizNameList(List<String> oldBizNameList, List<String> bizNameList) {
		List<String> sameBizNameList = new ArrayList<String>();
		for (String tmp : oldBizNameList) {
			sameBizNameList.add(tmp);
		}
		sameBizNameList.retainAll(bizNameList);
		return sameBizNameList;
	}
}
