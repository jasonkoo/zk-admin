package com.lenovo.wanba.common.db;

/**
 * 数据源配置信息
 * 
 * @author fengxiao
 */
public class GummyDataSourceConfig {

	private String type;
	private String database;
	private String host;
	private int port;
	private String user;
	private String password;
	private String wrflag;
	private String status = "disabled";
	private int initialSize;
	private int maxActive;
	private int maxWait;
	// Y表拥有令牌表读写在该server，B表没有令牌的机器不被app访问到(该字段对读写在一台机器里有效,mode=2)
	private String token = "N";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getWrflag() {
		return wrflag;
	}

	public void setWrflag(String wrflag) {
		this.wrflag = wrflag;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean hasToken(int mode) {
		return (mode == 1 && "Y".equals(token)) || checkModeValue(mode);
	}
	
	private boolean checkModeValue(int mode){
		return mode==0 || mode==2;
	}

}
