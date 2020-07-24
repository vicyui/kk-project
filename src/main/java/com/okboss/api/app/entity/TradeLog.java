package com.okboss.api.app.entity;

import com.okboss.api.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author kk
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@TableName("t_trade_log")
public class TradeLog extends BaseEntity {

	/**
	 * 收款方
	 */
	private Long toUserId;

	/**
	 * 付款方
	 */
	private Long fromUserId;

	/**
	 * 交易金额
	 */
	private BigDecimal amount;

	/**
	 * 交易类型(1 转帐,2 充值,3 空投,4 一级奖励,5 二级奖励)
	 */
	private Integer tradeType;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 币种
	 */
	private String coinType;

	public TradeLog(Long toUserId, Long fromUserId, BigDecimal amount, Integer tradeType, LocalDateTime createDate, String remark, String coinType) {
		this.toUserId = toUserId;
		this.fromUserId = fromUserId;
		this.amount = amount;
		this.tradeType = tradeType;
		this.setCreateDate(createDate);
		this.remark = remark;
		this.coinType = coinType;
	}
}
