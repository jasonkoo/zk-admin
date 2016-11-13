package com.lenovo.wanba.common.db;

import java.util.Collections;
import java.util.List;

public class GummyBizNameDBConfig {

	private int mode;
	private List<GummyDataSourceConfig> servers;

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public List<GummyDataSourceConfig> getServers() {
		if (servers == null) {
			servers = Collections.emptyList();
		}
		return servers;
	}

	public void setServers(List<GummyDataSourceConfig> servers) {
		this.servers = servers;
	}

	public String getDatabaseName() {
		return servers != null && servers.size() > 0 ? servers.get(0).getDatabase() : null;
	}

	// public static void main(String[] args) {
	// String json =
	// "{\"mode\":1,\"servers\":[{\"type\":\"mysql\",\"database\":\"msg\",\"host\":\"10.2.25.254\",\"port\":3306,\"user\":\"jphappy\",\"password\":\"test\",\"wrflag\":\"w\",\"initialSize\":1,\"maxActive\":16,\"maxWait\":10000},{\"type\":\"mysql\",\"database\":\"msg\",\"host\":\"10.2.25.253\",\"port\":3306,\"user\":\"jphappy\",\"password\":\"test\",\"wrflag\":\"r\",\"initialSize\":1,\"maxActive\":20,\"maxWait\":10000}]}";
	// GummyBizNameDBConfig a = new com.google.gson.Gson().fromJson(json,
	// GummyBizNameDBConfig.class);
	//
	// System.out.println(new com.google.gson.Gson().toJson(a));
	// }

	/*
	 * TODO 增加验证方法 验证要求： 1）各配置的database名称必须相同 2）必须都存在一个有效的读配置和一个写配置 3）wrflag, token需要同mode的值符合初始定义的逻辑
	 * 
	 * 细节： 1）mode的值只能为：-1/0/1/2 2）...
	 */
}
