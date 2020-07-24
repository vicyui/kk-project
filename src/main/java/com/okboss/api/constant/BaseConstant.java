package com.okboss.api.constant;

public class BaseConstant {
	public static final String MAINNET_DEFAULT_ADDRESS_PREFIX = "BOSS";
	//public static final String TESTNET_DEFAULT_ADDRESS_PREFIX = "tBOSS";
	/**
	 * hash length
	 */
	public static final int ADDRESS_LENGTH = 24;

	/**
	 * 存储当前登录用户id的字段名
	 */
	public static final String CURRENT_USER_ID = "id";

	/**
	 * token有效期（小时）
	 */
	public static final int TOKEN_EXPIRES_HOUR = 24;

	/**
	 * 存放Authorization的header字段
	 */
	public static final String AUTHORIZATION = "authorization";

	/**
	 * 空投奖励
	 */
	public static final String AIRDROP_REWARD = "airdrop_reward";

	/**
	 * 一级推荐奖励
	 */
	public static final String LEVEL_ONE_REWARD = "lv1_reward";

	/**
	 * 二级推荐奖励
	 */
	public static final String LEVEL_TWO_REWARD = "lv2_reward";

	/**
	 * 交易类型 转帐
	 */
	public static final int TRADE_TYPE_TRANSFER = 1;

	/**
	 * 交易类型 充值
	 */
	public static final int TRADE_TYPE_RECHARGE = 2;

	/**
	 * 交易类型 空投
	 */
	public static final int TRADE_TYPE_AIRDROP = 3;

	/**
	 * 交易类型 一级奖励
	 */
	public static final int TRADE_TYPE_REWARD_L1 = 4;

	/**
	 * 交易类型 二级奖励
	 */
	public static final int TRADE_TYPE_REWARD_L2 = 5;

	/**
	 * 管理员ID
	 */
	public static final long ADMIN_ID = 0l;

	/**
	 * 短信类型 交易
	 */
	public static final String TRADE_SMS = "TRADE";

	/**
	 * 短信类型 修改密码
	 */
	public static final String EDIT_PWD = "PWD";

	/**
	 * 币种 boss
	 */
	public static final String COIN_TYPE_BOSS = "BOSS";
}
