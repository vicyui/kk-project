package com.kk.api.config;

//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
//import com.baomidou.mybatisplus.core.MybatisConfiguration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import com.okboss.api.config.db.DBTypeEnum;
//import com.okboss.api.config.db.DynamicDataSource;
//import org.apache.ibatis.logging.stdout.StdOutImpl;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.type.JdbcType;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;

@Configuration
public class MybatisPlusConfig {

	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		// 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
		paginationInterceptor.setOverflow(true);
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		// paginationInterceptor.setLimit(500);
		// 开启 count 的 join 优化,只针对部分 left join
		paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
		return paginationInterceptor;
	}

//	@Bean(name = "okboss")
//	@ConfigurationProperties(prefix = "spring.datasource.druid.okboss")
//	public DataSource okboss() {
//		return DruidDataSourceBuilder.create().build();
//	}

//	@Bean(name = "okbossbbs")
//	@ConfigurationProperties(prefix = "spring.datasource.druid.okbossbbs")
//	public DataSource okbossbbs() {
//		return DruidDataSourceBuilder.create().build();
//	}


	/**
	 * 动态数据源配置
	 *
	 * @return
	 */
//	@Bean
//	@Primary
//	public DataSource multipleDataSource(@Qualifier("okboss") DataSource okboss,
//										 @Qualifier("okbossbbs") DataSource okbossbbs) {
//		DynamicDataSource dynamicDataSource = new DynamicDataSource();
//		Map<Object, Object> targetDataSources = new HashMap<>();
//		targetDataSources.put(DBTypeEnum.okboss.getValue(), okboss);
//		targetDataSources.put(DBTypeEnum.okbossbbs.getValue(), okbossbbs);
//		dynamicDataSource.setTargetDataSources(targetDataSources);
//		dynamicDataSource.setDefaultTargetDataSource(okboss);
//		return dynamicDataSource;
//	}

//	@Bean("sqlSessionFactory")
//	public SqlSessionFactory sqlSessionFactory() throws Exception {
//		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//		sqlSessionFactory.setDataSource(multipleDataSource(okboss(), okbossbbs()));
//		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/**/*Mapper.xml"));
//		sqlSessionFactory.setTypeAliasesPackage("com.okboss.api.**.mapper");
//		MybatisConfiguration configuration = new MybatisConfiguration();
//		configuration.setJdbcTypeForNull(JdbcType.NULL);
//		configuration.setMapUnderscoreToCamelCase(true);// 驼峰转下划线
//		configuration.setCacheEnabled(false);// 缓存关闭
//		configuration.setLogImpl(StdOutImpl.class);// 打印sql
//		sqlSessionFactory.setConfiguration(configuration);
//		//PerformanceInterceptor(),OptimisticLockerInterceptor()
//		//添加分页功能
//		sqlSessionFactory.setPlugins(new Interceptor[]{
//				paginationInterceptor()
//		});
//		//sqlSessionFactory.setGlobalConfig(globalConfiguration());
//		return sqlSessionFactory.getObject();
//	}
}