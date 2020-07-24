package com.kk.api.system.service;

import com.kk.api.system.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author kk
 * @since 2020-07-03
 */
public interface ISysConfigService extends IService<SysConfig> {
	Map<String, String> map();
}
