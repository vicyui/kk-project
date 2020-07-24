package com.kk.api.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.api.model.AjaxResult;
import com.kk.api.app.entity.TradeLog;
import com.kk.api.app.entity.vo.TradeLogVO;
import com.kk.api.app.service.ITradeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * @author kk
 * @since 2020-07-01
 */
@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
@Api(tags = "交易流水相关Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TradeLogController {

	@Resource
	ITradeLogService tradeLogService;

	@GetMapping("/app/tradeLog/data")
	@ApiOperation("获取空投数据")
	public AjaxResult airdropData() {
		return AjaxResult.success(tradeLogService.airdropData());
	}

	@GetMapping("/app/tradeLog/listByUser")
	@ApiOperation("根据用户id获取用户交易数据")
	@ApiImplicitParams({
//			@ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header")
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "current", value = "页码", dataType = "Long", paramType = "query")
	})
	public AjaxResult listByUser(@RequestParam Long userId, @RequestParam(required = false) Long current) {
		Page<TradeLog> page = new Page<>();
		page.setCurrent(Objects.isNull(current) ? 1 : current);
		return AjaxResult.success(tradeLogService.pageByUserId(page, userId));
	}

	@PostMapping("/app/tradeLog/trade")
	@ApiOperation(value = "转出", notes = "{\"id\":\"\",\"pin\":\"\",\"address\":\"\",\"amount\":\"\"，\"remark\":\"\"}")
	public AjaxResult tradeOut(@RequestBody Map<String, String> param) {
		if (param.isEmpty()) {
			return AjaxResult.failed("参数为空");
		}
		if (StringUtils.isEmpty(param.get("id"))) {
			return AjaxResult.failed("id为空");
		}
		if (StringUtils.isEmpty(param.get("pin"))) {
			return AjaxResult.failed("交易密码为空");
		}
		if (StringUtils.isEmpty(param.get("address"))) {
			return AjaxResult.failed("转出地址为空");
		}
		if (ObjectUtils.isEmpty(param.get("amount"))) {
			return AjaxResult.failed("转出金额为空");
		}
		return tradeLogService.trade(Long.parseLong(param.get("id")), param.get("pin"), param.get("address"), new BigDecimal(param.get("amount")), param.get("remark"));
	}

	@GetMapping("/app/tradeLog/info")
	@ApiOperation("根据id获取交易数据")
	@ApiImplicitParams({
//			@ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header")
			@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "userId", value = "用户", required = true, dataType = "Long", paramType = "query")
	})
	public AjaxResult selectVOById(@RequestParam Long id, @RequestParam Long userId) {
		return tradeLogService.selectVOById(id, userId);
	}

	@PostMapping("/admin/tradeLog/page")
	@ApiOperation(value = "获取用户交易数据", notes = "{\"toUsername\":\"\",\"fromUsername\":\"\",\"coinType\":\"\",\"tradeTpyeId\":\"\",\"begin\":\"\",\"end\":\"\",\"current\":\"\",\"pageSize\":\"\"}")
	public AjaxResult page(@RequestBody Map<String, String> param) {
		Integer current = param.containsKey("current") && !StringUtils.isEmpty(param.get("current")) ? Integer.parseInt(param.get("current")) : 1;
		Integer pageSize = param.containsKey("pageSize") && !StringUtils.isEmpty(param.get("pageSize")) ? Integer.parseInt(param.get("pageSize")) : 10;
		Page<TradeLogVO> page = new Page<>();
		page.setCurrent(current);
		page.setSize(pageSize);
		return AjaxResult.success(tradeLogService.pageList(page, param));
	}
}
