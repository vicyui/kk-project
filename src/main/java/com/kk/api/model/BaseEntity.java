package com.kk.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(hidden = true)
	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(hidden = true)
	private LocalDateTime createDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(hidden = true)
	private LocalDateTime updateDate;
}
