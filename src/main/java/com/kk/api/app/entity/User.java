package com.kk.api.app.entity;

import com.kk.api.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author kk
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@TableName("t_user")
public class User extends BaseEntity {

	/**
	 * 手机号
	 */
	private String username;

	/**
	 * 交易密码
	 */
	private String pin;

	/**
	 * 资产
	 */
	private BigDecimal asset;

	/**
	 * 钱包地址
	 */
	private String address;

	/**
	 * 推荐码
	 */
	private String invite;

	/**
	 * 是否领取空投(1 是 0 否)
	 */
	private Integer airdrop;

	/**
	 * 推荐人id (默认 0)
	 */
	private Long pid;

	/**
	 * 被推荐人id (,分隔)
	 */
	private String childList;

	/**
	 * 是否启用(默认 1,禁用0)
	 */
	private Integer isEnable;

	/**
	 * 一级奖励
	 */
	private BigDecimal l1Reward;

	/**
	 * 二级奖励
	 */
	private BigDecimal l2Reward;

	public User() {
	}

	public User(Long id) {
		this.setId(id);
	}

	public User(String username, String invite) {
		this.username = username;
		this.invite = invite;
	}

}
