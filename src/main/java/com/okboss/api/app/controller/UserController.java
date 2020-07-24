package com.okboss.api.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.okboss.api.app.entity.TradeLog;
import com.okboss.api.app.entity.User;
import com.okboss.api.app.entity.vo.UserVO;
import com.okboss.api.app.service.ITradeLogService;
import com.okboss.api.app.service.IUserService;
import com.okboss.api.model.SysConfigCache;
import com.okboss.api.utils.*;
import com.okboss.api.utils.sms.MeitangyunClient;
import com.okboss.api.utils.sms.UcpaasClient;
import com.okboss.api.constant.BaseConstant;
import com.okboss.api.model.AjaxResult;
import com.okboss.api.model.TokenModel;
import com.okboss.api.system.service.ISysConfigService;
import com.okboss.api.website.entity.Contact;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @author kk
 * @since 2020-06-28
 */
@Api(tags = "用户相关Controller")
@RestController
@RequestMapping(produces = "application/json; charset=utf-8")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Resource
	private IUserService userService;
	@Resource
	private ITradeLogService tradeLogService;
	@Resource
	private RedisUtil redisUtil;

	private Map<String, String> configMap = SysConfigCache.getConfigMap();

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/login")
	@ApiOperation(value = "注册/登陆", notes = "{\"username\":\"string\",\"valifyCode\":\"string\",\"inviteCode\":\"string\"}")
	@ApiImplicitParam(name = "param", value = "参数集合", dataType = "json", paramType = "body")
	public AjaxResult login(@RequestBody Map<String, Object> param) {
		if (param.isEmpty()) {
			return AjaxResult.failed("参数为空");
		}

		if (!param.containsKey("username") || param.get("username").toString().length() == 0) {
			return AjaxResult.failed("手机号为空");
		}
		String username = param.get("username").toString();

		if (!param.containsKey("valifyCode") || param.get("valifyCode").toString().length() == 0) {
			return AjaxResult.failed("验证码为空");
		}

		String redisValifyCode = redisUtil.getStr(username);
		if (StringUtils.isEmpty(redisValifyCode)) {
			return AjaxResult.failed("验证码已过期");
		}
		if (!redisValifyCode.equals(param.get("valifyCode").toString())) {
			return AjaxResult.failed("验证码有误");
		}

		User user = userService.getOne(new QueryWrapper<User>().eq("username", username));

		boolean result;
		if (Objects.isNull(user)) {
			// 推荐码
			String inviteCode;
			int count;
			do {
				inviteCode = CommonUtil.getRandomCode(6);
				count = userService.count(new QueryWrapper<User>().eq("invite", inviteCode));
			} while (count != 0);
			user = new User(username, inviteCode);

			// 钱包地址
			String walletAddress;
			do {
				walletAddress = BaseConstant.MAINNET_DEFAULT_ADDRESS_PREFIX + CommonUtil.getRandomCode(BaseConstant.ADDRESS_LENGTH - BaseConstant.MAINNET_DEFAULT_ADDRESS_PREFIX.length());
				count = userService.count(new QueryWrapper<User>().eq("address", walletAddress));
			} while (count != 0);
			user.setAddress(walletAddress);
			user.setCreateDate(LocalDateTime.now());
			user.setUpdateDate(LocalDateTime.now());
			result = userService.save(user);
			if (!result) {
				return AjaxResult.failed("注册失败");
			}

			// 如果有邀请码
			if (param.containsKey("inviteCode")
					&& param.get("inviteCode").toString().length() > 0
					&& !"string".equals(param.get("inviteCode").toString().toLowerCase())) {
				User pUser = userService.getOne(new QueryWrapper<User>().eq("invite", param.get("inviteCode").toString()));
				if (!Objects.isNull(pUser)) {
					if (StringUtils.isEmpty(pUser.getChildList())) {
						pUser.setChildList("" + user.getId());
					} else {
						pUser.setChildList(pUser.getChildList() + "," + user.getId());
					}

					BigDecimal l1Reward = new BigDecimal(configMap.get(BaseConstant.LEVEL_ONE_REWARD));
					pUser.setL1Reward(pUser.getL1Reward().add(l1Reward));
					pUser.setAsset(pUser.getAsset().add(l1Reward));
					pUser.setCreateDate(null);
					userService.update(pUser, new QueryWrapper<User>().eq("id", pUser.getId()));
					//写入流水
					tradeLogService.save(new TradeLog(pUser.getId(), BaseConstant.ADMIN_ID, l1Reward, BaseConstant.TRADE_TYPE_REWARD_L1, LocalDateTime.now(), "", BaseConstant.COIN_TYPE_BOSS));

					// 如果推荐人父级id不为0，则有二级推荐人奖励
					if (0 != pUser.getPid()) {
						User gpUser = userService.getOne(new QueryWrapper<User>().eq("id", pUser.getPid()));
						if (!Objects.isNull(gpUser)) {
							BigDecimal l2Reward = new BigDecimal(configMap.get(BaseConstant.LEVEL_TWO_REWARD));
							gpUser.setL2Reward(gpUser.getL2Reward().add(l2Reward));
							gpUser.setAsset(gpUser.getAsset().add(l2Reward));
							gpUser.setCreateDate(null);
							userService.update(gpUser, new QueryWrapper<User>().eq("id", gpUser.getId()));
							//写入流水
							tradeLogService.save(new TradeLog(gpUser.getId(), BaseConstant.ADMIN_ID, l2Reward, BaseConstant.TRADE_TYPE_REWARD_L2, LocalDateTime.now(), "", BaseConstant.COIN_TYPE_BOSS));
						}
					}
					// 绑定注册用户的父级ID为推荐人id
					user.setPid(pUser.getId());
					user.setCreateDate(null);
					userService.updateById(user);
				}
			}

			user = userService.getOne(new QueryWrapper<User>().eq("username", username));
		}

		if (0 == user.getIsEnable()) {
			return AjaxResult.failed("用户被锁定，请联系管理员");
		}
		return AjaxResult.success(new TokenModel(user, JwtUtil.createToken(user, false)));
	}

	@GetMapping("/code")
	@ApiOperation("获取验证码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "手机号", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "purpose", value = "用途，空为登陆/注册", dataType = "String", paramType = "query")
	})
	public AjaxResult valifyCode(@RequestParam String username, @RequestParam(required = false) String purpose) {
		if (StringUtils.isEmpty(username)) {
			return AjaxResult.failed("手机号为空");
		}
		if (!RegexUtil.isMobile(username)) {
			return AjaxResult.failed("手机号格式不正确");
		}
		if (StringUtils.isEmpty(purpose)) {
			purpose = "";
		}
		StringBuilder randomCode = new StringBuilder(redisUtil.hasKey(username + purpose) ? redisUtil.getStr(username + purpose) : "");
		if (StringUtils.isEmpty(randomCode.toString())) {
			Random random = new Random();
			randomCode = new StringBuilder(random.nextInt(10000) + "");
			while (randomCode.length() < 4) {
				randomCode.insert(0, "0");
			}

			String smsResult;
			JSONObject json;
			switch (configMap.get("sms")) {
				case "uc":
					UcpaasClient ucClient = new UcpaasClient();
					smsResult = ucClient.sendSms(randomCode.toString(), username, "");
					LOGGER.info("--------- smsResult ----------" + smsResult);
					json = JSON.parseObject(smsResult);
					if (!"OK".equals(json.get("msg"))) {
						return AjaxResult.failed("验证码发送失败");
					}
					break;
				case "mt":
					MeitangyunClient mtClient = new MeitangyunClient();
					smsResult = mtClient.sendSms(username, randomCode.toString());
					LOGGER.info("--------- smsResult ----------: " + smsResult);
					json = JSON.parseObject(smsResult);
					if (!"0".equals(json.get("res_code"))) {
						return AjaxResult.failed("验证码发送失败");
					}
					break;
				default:
					break;
			}
			redisUtil.setUntil5Minute(username + purpose, randomCode.toString());// purpose为空则为登陆验证码
		}
		return AjaxResult.success(randomCode.toString());
	}

	@GetMapping("/logout")
	@ApiOperation("退出登陆")
	@ApiImplicitParams({
//			@ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header")
			@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "query")
	})
	public AjaxResult logout(@RequestParam Long id) {

		return AjaxResult.success();
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/user/receive")
	@ApiOperation(value = "领取空投", notes = "{\"username\":\"string\"}")
	public AjaxResult receiveAirdrop(@RequestBody Map<String, Object> param) {
		if (param.isEmpty()) {
			return AjaxResult.failed("参数为空");
		}
		if (!param.containsKey("username") || param.get("username").toString().length() == 0) {
			return AjaxResult.failed("手机号为空");
		}
		String username = param.get("username").toString();
		User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
		if (Objects.isNull(user)) {
			return AjaxResult.failed("用户不存在");
		}
		if (1 == user.getAirdrop()) {
			return AjaxResult.failed("空投已领取");
		}
		BigDecimal airdrop = new BigDecimal(configMap.get(BaseConstant.AIRDROP_REWARD));
		user.setAirdrop(1);
		user.setAsset(user.getAsset().add(airdrop));
		user.setCreateDate(null);
		user.setUpdateDate(LocalDateTime.now());
		userService.update(user, new QueryWrapper<User>().eq("id", user.getId()));
		tradeLogService.save(new TradeLog(user.getId(), BaseConstant.ADMIN_ID, airdrop, BaseConstant.TRADE_TYPE_AIRDROP, LocalDateTime.now(), "", BaseConstant.COIN_TYPE_BOSS));
		return AjaxResult.success("领取成功");
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/user/pin")
	@ApiOperation(value = "修改交易密码", notes = "{\"username\":\"string\",\"valifyCode\":\"string\",\"pin\":\"string\"}")
	public AjaxResult editPin(@RequestBody Map<String, Object> param) {
		if (param.isEmpty()) {
			return AjaxResult.failed("参数为空");
		}
		if (StringUtils.isEmpty(param.get("username"))) {
			return AjaxResult.failed("用户名为空");
		}
		if (StringUtils.isEmpty(param.get("valifyCode"))) {
			return AjaxResult.failed("验证码为空");
		}
		String redisValifyCode = redisUtil.getStr(param.get("username").toString() + BaseConstant.EDIT_PWD);
		if (StringUtils.isEmpty(redisValifyCode)) {
			return AjaxResult.failed("验证码已过期");
		}
		if (!redisValifyCode.equals(param.get("valifyCode").toString())) {
			return AjaxResult.failed("验证码有误");
		}
		if (StringUtils.isEmpty(param.get("pin"))) {
			return AjaxResult.failed("交易密码为空");
		}
		User user = new User();
		user.setPin(param.get("pin").toString());
		user.setUpdateDate(LocalDateTime.now());
		return AjaxResult.success(userService.update(user, new QueryWrapper<User>().eq("username", param.get("username"))));
	}

	@GetMapping("/info")
	@ApiOperation("获取用户信息")
	@ApiImplicitParams({
//			@ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header")
			@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "query")
	})
	public AjaxResult userInfo(@RequestParam Long id) {
		return AjaxResult.success(new TokenModel(userService.getById(id)));
	}

	@GetMapping("/bbs/login")
	@ApiOperation("论坛登陆")
	@ApiImplicitParams({
//			@ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "string", paramType = "header")
			@ApiImplicitParam(name = "username", value = "手机号", required = true, dataType = "String", paramType = "query")
	})
	public AjaxResult bbsLogin(@RequestParam String username) {
		JSONObject jsonObj = null;
		try {
			String findRts = HttpClientUtil.get("https://bbs.okboss.io/index.php?app=user&ac=login&ts=find&js=1&email=" + username);
			jsonObj = JSON.parseObject(findRts);
//			findRts = URLDecoder.decode(jsonObj.get("msg").toString(), "UTF-8");
			String registRts = "";
			if (404 == Integer.parseInt(jsonObj.get("status").toString())) {
				registRts = HttpClientUtil.get("https://bbs.okboss.io/index.php?app=user&ac=login&ts=save&js=1&email=" + username + "&pwd=123123&username=" + username);
				jsonObj = JSON.parseObject(registRts);
			}
			jsonObj = JSON.parseObject(jsonObj.get("data").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return AjaxResult.success(jsonObj.get("userid").toString());
	}

	@ApiOperation(value = "获取用户列表", notes = "{\"username\":\"string\",\"origin\":\"string\",\"status\":\"integer\",\"orderCol\":\"string\",\"orderBy\":\"string\",\"current\":\"integer\",\"pageSize\":\"integer\"}")
	@PostMapping("/admin/user/page")
	public AjaxResult page(@RequestBody Map<String, String> param) {
		Integer current = param.containsKey("current") && !StringUtils.isEmpty(param.get("current")) ? Integer.parseInt(param.get("current")) : 1;
		Integer pageSize = param.containsKey("pageSize") && !StringUtils.isEmpty(param.get("pageSize")) ? Integer.parseInt(param.get("pageSize")) : 10;
		Page<UserVO> page = new Page<>();
		page.setCurrent(current);
		page.setSize(pageSize);
		return AjaxResult.success(userService.pageList(page, param));
	}

	@ApiOperation(value = "修改用户锁定状态", notes = "{\"username\":\"string\",\"status\":\"integer\"}")
	@PostMapping("/admin/user/status")
	public AjaxResult updateIsEnable(@RequestBody Map<String, String> param) {
		if (StringUtils.isEmpty(param.get("username"))) {
			return AjaxResult.failed("手机号为空");
		}
		if (StringUtils.isEmpty(param.get("status"))) {
			return AjaxResult.failed("缺少设置参数");
		}
		User user = new User();
		user.setIsEnable(Integer.parseInt(param.get("status")));
		return AjaxResult.success(userService.update(user,
				new UpdateWrapper<User>().eq("username", param.get("username"))));
	}

	@ApiOperation(value = "获取用户列表Excel", notes = "{\"username\":\"string\",\"origin\":\"string\",\"status\":\"integer\"}")
	@PostMapping(value = "/admin/user/excel")
	public void exportExcel(HttpServletResponse response, @RequestBody Map<String, String> param) {
		long t1 = System.currentTimeMillis();
		ExcelUtils.writeExcel(response, userService.list( param), UserVO.class, "airdrop_user");
		long t2 = System.currentTimeMillis();
		System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
	}
}
