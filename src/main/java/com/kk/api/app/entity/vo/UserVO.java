package com.kk.api.app.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kk.api.annotation.ExcelColumn;
import com.kk.api.app.entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserVO extends User {

	@ExcelColumn(value = "注册时间", col = 1)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createDate;
	@ExcelColumn(value = "手机号", col = 2)
	private String username;
	@ExcelColumn(value = "BOSS总量", col = 3)
	private BigDecimal asset;
	@ExcelColumn(value = "一级奖励", col = 4)
	private BigDecimal l1Reward;
	@ExcelColumn(value = "二级奖励", col = 5)
	private BigDecimal l2Reward;
	@ExcelColumn(value = "来源", col = 6)
	private String origin;
	private int status;
	@ExcelColumn(value = "状态", col = 7)
	private String statusStr;
}
