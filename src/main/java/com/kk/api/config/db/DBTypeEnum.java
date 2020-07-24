package com.kk.api.config.db;

public enum DBTypeEnum {

	okboss("okboss"), okbossbbs("okbossbbs");
	private String value;

	DBTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}