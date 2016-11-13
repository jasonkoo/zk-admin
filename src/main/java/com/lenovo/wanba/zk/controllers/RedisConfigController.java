package com.lenovo.wanba.zk.controllers;

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

import com.lenovo.push.reids.api.vo.RedisConf;
import com.lenovo.wanba.zk.services.RedisConfigService;
import com.lenovo.wanba.zk.util.Constant;

@Controller
@RequestMapping(value = "/redisConfig")
public class RedisConfigController {

	private static final String REDIS_CONFIG_PAGE = "redisConfig";

	private static final String ERROR_MSG = "errMsg";

	@Autowired
	private RedisConfigService redisConfigService;

	/**
	 * 页面初始化 取得/configuration/redisInstance下的所有子节点信息
	 * 
	 * @param model
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String show(Model model) throws KeeperException, InterruptedException {
		List<RedisConf> list = redisConfigService.getRedisConfigList(Constant.PATH_REDIS_CONFIG);
		model.addAttribute("redisConfigList", list);
		model.addAttribute("path", Constant.PATH_REDIS_CONFIG);

		return Constant.PAGE_REDISCONFIG;
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
		RedisConf redisConfig = redisConfigService.getRedisConfig(Constant.PATH_REDIS_CONFIG, dbname);

		if (redisConfig == null) {
			redisConfig = new RedisConf();
		}

		model.addAttribute(REDIS_CONFIG_PAGE, redisConfig);
		model.addAttribute("path", Constant.PATH_REDIS_CONFIG);
		return Constant.PAGE_REDISCONFIG_EDIT;
	}

	/**
	 * 更新数据库参数
	 * 
	 * @param redisConfig
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Model model, @Valid RedisConf redisConfig, BindingResult result, @RequestParam String oldDbName) {
		if (result.hasErrors()) {
			model.addAttribute(ERROR_MSG, "所有字段不能为空");
			model.addAttribute(REDIS_CONFIG_PAGE, redisConfig);
			return Constant.PAGE_REDISCONFIG_EDIT;
		}
		try {
			redisConfigService.updateRedisConfig(Constant.PATH_REDIS_CONFIG, oldDbName, redisConfig);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(ERROR_MSG, e.getMessage() == null ? e.toString() : e.getMessage());
			model.addAttribute(REDIS_CONFIG_PAGE, redisConfig);
			return Constant.PAGE_REDISCONFIG_EDIT;
		}
		return "redirect:/redisConfig";
	}

}
