package com.okboss.api.system.service.impl;

import com.okboss.api.system.entity.SysConfig;
import com.okboss.api.system.mapper.SysConfigMapper;
import com.okboss.api.system.service.ISysConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kk
 * @since 2020-07-03
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

	@Override
	public Map<String, String> map() {
		Map<String, String> configMap = new HashMap<>();
		List<SysConfig> list = list();
		list.forEach(e -> configMap.put(e.getName(), e.getValue()));
		return configMap;
	}
}
