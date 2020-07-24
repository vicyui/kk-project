package com.kk.api.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.api.app.mapper.TradeLogMapper;
import com.kk.api.app.mapper.UserMapper;
import com.kk.api.constant.BaseConstant;
import com.kk.api.model.AjaxResult;
import com.kk.api.app.entity.TradeLog;
import com.kk.api.app.entity.User;
import com.kk.api.app.entity.vo.TradeLogVO;
import com.kk.api.app.service.ITradeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Wrapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-07-01
 */
@Service
public class TradeLogServiceImpl extends ServiceImpl<TradeLogMapper, TradeLog> implements ITradeLogService {

	@Resource
	private UserMapper userMapper;
	@Resource
	private TradeLogMapper tradeLogMapper;

	@Override
	public Map<String, Object> airdropData() {
		QueryWrapper<User> wrapper = new QueryWrapper();
		List<Object> totalAirdrop = userMapper.selectObjs(wrapper.select("sum(asset)").eq("is_enable", 1));
		wrapper.clear();
		List<Object> yesterdayAirdrop = userMapper.selectObjs(wrapper.select("sum(asset)")
				.eq("is_enable", 1)
				.eq("Date_format(create_date,'%Y-%m-%d')", LocalDate.now().minusDays(1)));
		wrapper.clear();
		List<Object> airdropDays = userMapper.selectObjs(wrapper.select("Datediff('" + LocalDate.now() + "',create_date)").orderByAsc("create_date").last("limit 1"));
		wrapper.clear();
		Integer yesterdayAirdropCount = userMapper.selectCount(wrapper.eq("is_enable", 1)
				.eq("Date_format(create_date,'%Y-%m-%d')", LocalDate.now().minusDays(1)));
		wrapper.clear();
		Integer totalAirdropCount = userMapper.selectCount(wrapper.eq("is_enable", 1));
		wrapper.clear();
		Integer hasChlidListCount = userMapper.selectCount(wrapper.eq("is_enable", 1).isNotNull("child_list").ne("child_list", ""));
		wrapper.clear();
		Integer hasPidCount = userMapper.selectCount(wrapper.eq("is_enable", 1).ne("pid", 0));
		wrapper.clear();
		Integer nonPidCount = userMapper.selectCount(wrapper.eq("is_enable", 1).eq("pid", 0));
		wrapper.clear();
		List<Map<String, Object>> countOfDayMap = tradeLogMapper.selectAirdropLine();
		List<String> countOfDay = new ArrayList<>();
		for (Map<String, Object> map : countOfDayMap) {
			countOfDay.add(map.get("date") + "," + map.get("count") + "," + map.get("amount"));
		}

		Map<String, Object> result = new HashMap<>();
		result.put("totalAirdrop", Objects.isNull(totalAirdrop.get(0)) ? 0 : totalAirdrop.get(0));
		result.put("yesterdayAirdrop", Objects.isNull(yesterdayAirdrop.get(0)) ? 0 : yesterdayAirdrop.get(0));
		result.put("airdropDays", airdropDays.isEmpty() ? 0 : airdropDays.get(0));
		result.put("yesterdayAirdropCount", yesterdayAirdropCount);
		result.put("totalAirdropCount", totalAirdropCount);
		result.put("hasChlidListCount", hasChlidListCount);
		result.put("hasPidCount", hasPidCount);
		result.put("nonPidCount", nonPidCount);
		result.put("countOfDay", countOfDay);
		return result;
	}

	@Override
	public IPage<TradeLogVO> pageByUserId(Page<TradeLog> page, Long userId) {
		return tradeLogMapper.selectPageByUserid(page, userId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public AjaxResult trade(Long userId, String pin, String address, BigDecimal amount, String remark) {
		User userIm = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
		if (Objects.isNull(userIm.getPin())) {
			return AjaxResult.failed("交易密码未设置");
		}
		if (!userIm.getPin().equals(pin)) {
			return AjaxResult.failed("交易密码有误");
		}
		User userTo = userMapper.selectOne(new QueryWrapper<User>().eq("address", address));
		if (Objects.isNull(userTo)) {
			return AjaxResult.failed("转出用户不存在");
		}
		if (userIm.getAddress().equals(address)) {
			return AjaxResult.failed("转出用户不能为自己");
		}
		if (amount.compareTo(BigDecimal.ZERO) != 1) {
			return AjaxResult.failed("转出金额不能小于0");
		}
		amount = amount.setScale(4, BigDecimal.ROUND_DOWN);// 截取小数点后4位
		BigDecimal tax = BigDecimal.ZERO; // 手续费
		if (userIm.getAsset().compareTo(amount.add(tax)) < 0) {
			return AjaxResult.failed("账户余额不足");
		}
		userIm.setAsset(userIm.getAsset().subtract(amount.add(tax)));
		userIm.setCreateDate(null);
		userIm.setUpdateDate(LocalDateTime.now());
		userMapper.updateById(userIm);

		userTo.setAsset(userTo.getAsset().add(amount));
		userTo.setCreateDate(null);
		userTo.setUpdateDate(LocalDateTime.now());
		userMapper.updateById(userTo);

		tradeLogMapper.insert(new TradeLog(userTo.getId(), userIm.getId(), amount.add(tax), BaseConstant.TRADE_TYPE_TRANSFER, LocalDateTime.now(), remark, BaseConstant.COIN_TYPE_BOSS));
		return AjaxResult.success();
	}

	@Override
	public AjaxResult selectVOById(Long id, Long userId) {
		return AjaxResult.success(tradeLogMapper.selectVOById(id, userId));
	}

	@Override
	public IPage<TradeLogVO> pageList(Page<TradeLogVO> page, Map<String, String> param) {
		return tradeLogMapper.pageList(page, param);
	}
}
