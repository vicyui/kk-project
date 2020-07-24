package com.kk.api.system.entity;

import com.kk.api.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kk
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
//@TableName("t_sys_config")
public class SysConfig extends BaseEntity {

    private String name;

    private String value;

    private String description;
}
