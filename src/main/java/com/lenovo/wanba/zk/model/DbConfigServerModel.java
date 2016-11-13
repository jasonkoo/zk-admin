package com.lenovo.wanba.zk.model;

import javax.validation.constraints.NotNull;

public class DbConfigServerModel {

	@NotNull
	private String host;

	@NotNull
	private int port;

	@NotNull
	private String user;

	@NotNull
	private String password;

	// 服务器状态
	@NotNull
	private String status;

	// // 读写标志
	// @NotNull
	// private String wrflag;
	//
	// // 令牌
	// @NotNull
	// private String token = "N";

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	// /**
	// * @return the wrflg
	// */
	// public String getWrflag() {
	// return wrflag;
	// }
	//
	// /**
	// * @param wrflag
	// * the wrflag to set
	// */
	// public void setWrflag(String wrflag) {
	// this.wrflag = wrflag;
	// }
	//
	// /**
	// * @return the token
	// */
	// public String getToken() {
	// return token;
	// }
	//
	// /**
	// * @param token
	// * the token to set
	// */
	// public void setToken(String token) {
	// this.token = token;
	// }

}
