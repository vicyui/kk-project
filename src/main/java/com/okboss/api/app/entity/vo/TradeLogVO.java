package com.okboss.api.app.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
//@TableName("t_trade_log")
public class TradeLogVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	/**
	 * 收款方
	 */
	private String toUsername;

	/**
	 * 付款方
	 */
	private String fromUsername;

	/**
	 * 交易金额
	 */
	private BigDecimal amount;

	/**
	 * 交易类型(1 转帐,2 充值,3 空投,4 一级奖励,5 二级奖励)
	 */
	private String tradeType;

	private int tradeTypeId;

	private String fromAddress;

	private String toAddress;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDate;

	/**
	 * 备注
	 */
	private String remark;

	private String coinType;
}
