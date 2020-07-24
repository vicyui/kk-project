package com.kk.api.model;

import lombok.Data;

import java.util.Objects;

/**
 * 返回结果类
 */
@Data
public class AjaxResult {
	private Boolean success;
	private String msg;
	private Object data;

	private AjaxResult(Boolean success, String msg, Object data) {
		this.success = success;
		this.msg = msg;
		this.data = data;
	}

	public static AjaxResult success() {
		return new AjaxResult(true, "Success", null);
	}

	public static AjaxResult success(Object object) {
		if (object instanceof String)
			new AjaxResult(true, object.toString(), null);
		return new AjaxResult(true, "Success", object);
	}

	public static AjaxResult failed() {
		return new AjaxResult(false, "Failed", null);
	}

	public static AjaxResult failed(String msg) {
		return new AjaxResult(false, msg, null);
	}
}
