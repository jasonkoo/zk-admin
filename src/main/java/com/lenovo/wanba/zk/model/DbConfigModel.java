package com.lenovo.wanba.zk.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public class DbConfigModel {

	// 数据库名
	@NotNull
	private String dbname;

	// // 读写模式
	// @NotNull
	// private int mode;

	// 连接池初始化大小
	@NotNull
	private int initialSize;

	// 连接池最大连接数
	@NotNull
	private int maxActive;

	// 连接池最大等待时间
	@NotNull
	private int maxWait;

	// db状态
	@NotNull
	private String dbstatus = "avalibled";

	// 业务列表
	@NotNull
	private List<String> bizNameList;

	// server
	@NotNull
	private List<DbConfigServerModel> serverList;

	/**
	 * @return the dbname
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @param dbname
	 *            the dbname to set
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	// /**
	// * @return the mode
	// */
	// public int getMode() {
	// return mode;
	// }
	//
	// /**
	// * @param mode
	// * the mode to set
	// */
	// public void setMode(int mode) {
	// this.mode = mode;
	// }

	/**
	 * @return the initialSize
	 */
	public int getInitialSize() {
		return initialSize;
	}

	/**
	 * @param initialSize
	 *            the initialSize to set
	 */
	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	/**
	 * @return the maxActive
	 */
	public int getMaxActive() {
		return maxActive;
	}

	/**
	 * @param maxActive
	 *            the maxActive to set
	 */
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * @return the maxWait
	 */
	public int getMaxWait() {
		return maxWait;
	}

	/**
	 * @param maxWait
	 *            the maxWait to set
	 */
	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	/**
	 * @return the bizNameList
	 */
	public List<String> getBizNameList() {
		if(bizNameList==null){
			return new ArrayList<String>();
		}
		return bizNameList;
	}

	/**
	 * @param bizNameList
	 *            the bizNameList to set
	 */
	public void setBizNameList(List<String> bizNameList) {
		this.bizNameList = bizNameList;
	}

	/**
	 * @return the serverList
	 */
	public List<DbConfigServerModel> getServerList() {
		if(serverList==null){
			return new ArrayList<DbConfigServerModel>();
		}
		return serverList;
	}

	/**
	 * @param serverList
	 *            the serverList to set
	 */
	public void setServerList(List<DbConfigServerModel> serverList) {
		this.serverList = serverList;
	}

	/**
	 * @return the dbstatus
	 */
	public String getDbstatus() {
		return dbstatus;
	}

	/**
	 * @param dbstatus
	 *            the dbstatus to set
	 */
	public void setDbstatus(String dbstatus) {
		this.dbstatus = dbstatus;
	}

}
