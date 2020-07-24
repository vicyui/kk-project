package com.kk.api.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kk.api.model.AjaxResult;
import com.kk.api.model.SysConfigCache;
import com.kk.api.system.entity.SysConfig;
import com.kk.api.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author kk
 * @since 2020-07-03
 */
@RestController
@Api(tags = "系统配置相关Controller")
@RequestMapping(produces = "application/json; charset=utf-8")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SysConfigController {

	@Resource
	private ISysConfigService sysConfigService;

	private Map<String, String> configMap = SysConfigCache.getConfigMap();

	@GetMapping("/system/list")
	@ApiOperation("获取配置列表")
	public AjaxResult list() {
		return AjaxResult.success(sysConfigService.map());
	}

	@GetMapping("/app/ver")
	@ApiOperation("app版本号")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "version", value = "版本号", required = true, dataType = "String", paramType = "query")
	})
	public AjaxResult version(@RequestParam String version) {
		if (StringUtils.isEmpty(version)) {
			return AjaxResult.failed("参数为空");
		}
		SysConfig config = sysConfigService.getOne(new QueryWrapper<SysConfig>().eq("name", "app_version"));
		if (!version.equals(config.getValue())) {
			return AjaxResult.failed();
		}
		return AjaxResult.success();
	}

	@PostMapping("/admin/login")
	@ApiOperation(value = "admin登陆", notes = "{\"username\":\"string\",\"password\":\"string\"}")
	public AjaxResult login(@RequestBody Map<String, String> param) {
		if (!configMap.containsKey(param.get("username")))
			return AjaxResult.failed();
		if (!configMap.get(param.get("username")).equals(param.get("password")))
			return AjaxResult.failed();
		return AjaxResult.success();
	}

	@PostMapping("/admin/update")
	@ApiOperation(value = "更新系统配置", notes = "{\"name\":\"string\",\"value\":\"string\"}")
	public AjaxResult updateSetting(@RequestBody Map<String, String> param) {
		SysConfig sysConfig = new SysConfig();
		sysConfig.setValue(param.get("value"));
		sysConfig.setUpdateDate(LocalDateTime.now());
		sysConfigService.update(sysConfig, new UpdateWrapper<SysConfig>().eq("name", param.get("name")));
		SysConfigCache.updateConfigMap(sysConfigService.map());
		return AjaxResult.success();
	}
}
