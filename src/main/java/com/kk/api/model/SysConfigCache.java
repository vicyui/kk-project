package com.kk.api.model;

import com.alibaba.fastjson.JSON;
import com.kk.api.system.service.ISysConfigService;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class SysConfigCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigCache.class);
	@Resource
	private ISysConfigService sysConfigService;
	/**
	 * 自动刷新时间（单位为秒）
	 */
	private static final Integer EXPIRE_SENCOND = 60 * 60 * 24;
	/**
	 * 存储数据的容器
	 */
	private static Map<String, String> CONFIG_MAP = new HashMap<>();

	/**
	 * 缓存数据key
	 */
	public static final String KEY = "config";


	public static Map<String, String> getConfigMap() {
		return CONFIG_MAP;
	}

	public static Map<String, String> updateConfigMap(Map<String,String> map) {
		CONFIG_MAP.clear();
		CONFIG_MAP.putAll(map);
		return CONFIG_MAP;
	}

	/**
	 * Spring Bean创建之后执行的方法
	 */
	@PostConstruct
	public void initMethod() {
		ExecutorService pool = new ThreadPoolExecutor(1, 1, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1),
				new BasicThreadFactory.Builder().namingPattern("config-map-pool-%d").daemon(true).build(),
				new ThreadPoolExecutor.AbortPolicy());
		pool.execute(new Task());
		pool.shutdown();
	}

	/**
	 * 初始化方法，查询数据库数据储存到map中
	 */
	private void initDataMap() {
		// 测试表数据
//		setDataMap(SysConfigCache.KEY, sysConfigService.map());
		CONFIG_MAP.putAll(sysConfigService.map());
	}

	/**
	 * 获取map缓存的对象
	 */
	public static <T> T getObject(String key, Class<T> clazz) {
		return json2Object(CONFIG_MAP.get(key), clazz);
	}

	/**
	 * 获取map缓存的集合数据
	 */
	public static <T> List<T> getList(String key, Class<T> clazz) {
		return json2List(CONFIG_MAP.get(key), clazz);
	}

	/**
	 * 缓存对象数据到map中
	 */
	private <T> void setDataMap(String key, T value) {
		CONFIG_MAP.put(key, object2Json(value));
	}

	/**
	 * 缓存集合数据到map中
	 */
	private <T> void setDataMap(String key, List<T> value) {
		CONFIG_MAP.put(key, list2Json(value));
	}

	/**
	 * json转换为Object
	 */
	public static <T> T json2Object(String json, Class<T> clazz) {
		try {
			return JSON.parseObject(json, clazz);
		} catch (Exception e) {
			LOGGER.error("{} 转 JSON 失败", json);
		}
		return null;
	}

	/**
	 * 对象转json
	 */
	public static <T> String object2Json(T object) {
		String json = JSON.toJSONString(object);
		return json;
	}

	/**
	 * list转json
	 */
	public static <T> String list2Json(List<T> list) {
		String jsons = JSON.toJSONString(list);
		return jsons;
	}

	/**
	 * 获取key对应的List集合
	 */
	public static <T> List<T> json2List(String json, Class<T> clazz) {
		List<T> list = JSON.parseArray(json, clazz);
		return list;
	}

	/**
	 * 初始化及定时刷新
	 */
	class Task implements Runnable {
		@Override
		public void run() {
			while (true) {
				LOGGER.info("=================初始化系统配置数据================= start");
				initDataMap();
				LOGGER.info("=================初始化系统配置数据=================  end");
				try {
					Thread.sleep(EXPIRE_SENCOND * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
