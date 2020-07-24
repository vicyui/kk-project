package com.kk.api.website.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.api.model.AjaxResult;
import com.kk.api.website.entity.Contact;
import com.kk.api.website.service.IContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author kk
 * @since 2020-06-19
 */
@Api(tags = "企业服务相关Controller")
@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactController {

	@Resource
	private IContactService contactService;

	@ApiOperation(value = "获取企业服务列表", notes = "{\"company\":\"string\",\"contacts\":\"string\",\"current\":\"integer\",\"pageSize\":\"integer\"}")
	@PostMapping("/admin/contact/page")
	public AjaxResult page(@RequestBody Map<String, String> param) {
		QueryWrapper<Contact> wrapper = new QueryWrapper<>();
		if (param.containsKey("company") && !StringUtils.isEmpty(param.get("company")))
			wrapper.or().like("company", param.get("company"));
		if (param.containsKey("contacts") && !StringUtils.isEmpty(param.get("contacts")))
			wrapper.or().like("contacts", param.get("contacts"));
		Integer current = param.containsKey("current") && !StringUtils.isEmpty(param.get("current")) ? Integer.parseInt(param.get("current")) : 1;
		Integer pageSize = param.containsKey("pageSize") && !StringUtils.isEmpty(param.get("pageSize")) ? Integer.parseInt(param.get("pageSize")) : 10;
		Page<Contact> page = new Page<>();
		page.setCurrent(current);
		page.setSize(pageSize);
		return AjaxResult.success(contactService.page(page, wrapper.orderByDesc("create_date")));
	}

	@ApiOperation("新增企业服务")
	@PostMapping("/website/contact/add")
	public AjaxResult add(@RequestBody Contact contact) {
		contact.setCreateDate(LocalDateTime.now());
		boolean result = contactService.save(contact);
		return AjaxResult.success(result);
	}
}
