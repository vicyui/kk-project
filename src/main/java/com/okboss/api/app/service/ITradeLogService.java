package com.okboss.api.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.okboss.api.app.entity.TradeLog;
import com.okboss.api.app.entity.vo.TradeLogVO;
import com.okboss.api.model.AjaxResult;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author kk
 * @since 2020-07-01
 */
public interface ITradeLogService extends IService<TradeLog> {
	Map<String, Object> airdropData();

	IPage<TradeLogVO> pageByUserId(Page<TradeLog> page, Long userId);

	AjaxResult trade(Long userId, String pin, String address, BigDecimal amount, String remark);

	AjaxResult selectVOById(Long id, Long userId);

	IPage<TradeLogVO> pageList(Page<TradeLogVO> page, Map<String, String> param);
}
