package com.okboss.api.website.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.okboss.api.model.AjaxResult;
import com.okboss.api.website.entity.Contact;
import com.okboss.api.website.entity.Subscribe;
import com.okboss.api.website.service.ISubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kk
 * @since 2020-06-22
 */
@Api(tags = "订阅相关Controller")
@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SubscribeController {

	@Resource
	private ISubscribeService subscribeService;

	@ApiOperation("新增订阅")
	@PostMapping("/website/subscribe/add")
	public AjaxResult add(@RequestBody Subscribe subscribe) {
		subscribe.setCreateDate(LocalDateTime.now());
		return AjaxResult.success(subscribeService.save(subscribe));
	}

	@ApiOperation(value = "获取订阅列表", notes = "{\"email\":\"string\",\"current\":\"integer\",\"pageSize\":\"integer\"}")
	@PostMapping("/admin/subscribe/page")
	public AjaxResult page(@RequestBody Map<String, String> param) {
		QueryWrapper<Subscribe> wrapper = new QueryWrapper<>();
		if (param.containsKey("email") && !StringUtils.isEmpty(param.get("email")))
			wrapper.or().like("email", param.get("email"));
		Integer current = param.containsKey("current") && !StringUtils.isEmpty(param.get("current")) ? Integer.parseInt(param.get("current")) : 1;
		Integer pageSize = param.containsKey("pageSize") && !StringUtils.isEmpty(param.get("pageSize")) ? Integer.parseInt(param.get("pageSize")) : 10;
		Page<Subscribe> page = new Page<>();
		page.setCurrent(current);
		page.setSize(pageSize);
		return AjaxResult.success(subscribeService.page(page, wrapper.orderByDesc("create_date")));
	}
}
