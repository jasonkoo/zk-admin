package com.lenovo.wanba.zk.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lenovo.wanba.zk.model.DbConfigModel;
import com.lenovo.wanba.zk.model.DbConfigServerModel;
import com.lenovo.wanba.zk.services.DbConfigService;
import com.lenovo.wanba.zk.util.Constant;

@Controller
@RequestMapping(value = "/dbConfig")
public class DbConfigController {

	private static final String DB_CONFIG_PAGE = "dbConfig";

	private static final String ERROR_MSG = "errMsg";

	@Autowired
	private DbConfigService dbConfigService;

	/**
	 * 页面初始化 取得/configuration/dbInstance下的所有子节点信息
	 * 
	 * @param model
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String show(Model model) throws KeeperException, InterruptedException {
		List<DbConfigModel> list = dbConfigService.getDbConfigList(Constant.PATH_DB_CONFIG);
		model.addAttribute("dbConfigList", list);
		model.addAttribute("path", Constant.PATH_DB_CONFIG);

		return Constant.PAGE_DBCONFIG;
	}

	/**
	 * 编辑数据库参数
	 * 
	 * @param dbname
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, @RequestParam String dbname) throws KeeperException, InterruptedException {
		DbConfigModel dbConfig = dbConfigService.getDbConfig(Constant.PATH_DB_CONFIG, dbname);

		if (dbConfig != null) {
			List<DbConfigServerModel> serverList = dbConfig.getServerList();
			List<DbConfigServerModel> tmpServerList = new ArrayList<DbConfigServerModel>();
			if (serverList == null || serverList.size() == 0) {
				tmpServerList.add(new DbConfigServerModel());
				tmpServerList.add(new DbConfigServerModel());
				dbConfig.setServerList(tmpServerList);
			} else if (serverList.size() == 1) {
				tmpServerList.add(new DbConfigServerModel());
				dbConfig.setServerList(tmpServerList);
			}
		} else {
			dbConfig = new DbConfigModel();
			List<DbConfigServerModel> tmpServerList = new ArrayList<DbConfigServerModel>();
			tmpServerList.add(new DbConfigServerModel());
			tmpServerList.add(new DbConfigServerModel());
			dbConfig.setServerList(tmpServerList);
		}

		model.addAttribute(DB_CONFIG_PAGE, dbConfig);
		model.addAttribute("path", Constant.PATH_DB_CONFIG);
		return Constant.PAGE_DBCONFIG_EDIT;
	}

	/**
	 * 更新数据库参数
	 * 
	 * @param dbConfig
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Model model, @Valid DbConfigModel dbConfig, BindingResult result,
	        @RequestParam String oldDbName) throws Exception {
		if (result.hasErrors()) {
			model.addAttribute(ERROR_MSG, "所有字段不能为空，注意连接池属性的输入范围");
			model.addAttribute(DB_CONFIG_PAGE, dbConfig);
			return Constant.PAGE_DBCONFIG_EDIT;
		}
		dbConfigService.updateDbConfig(Constant.PATH_DB_CONFIG, oldDbName, dbConfig);
		return "redirect:/dbConfig";
	}

}
