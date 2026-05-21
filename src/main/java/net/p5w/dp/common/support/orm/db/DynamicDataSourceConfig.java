package net.p5w.dp.common.support.orm.db;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * 多数据源配置
 * <p>
 * 注册 dpo（主库）和 ecology（从库）两个数据源，
 * 并将其组合为动态数据源 {@link DynamicDataSource}，
 * 由 {@link net.p5w.dp.common.aspect.DataSourceAspect} 在运行时切换。
 * </p>
 */
@Configuration
public class DynamicDataSourceConfig {

    @Primary
    @Bean(name = "dpoDataSource")
    @ConfigurationProperties("spring.datasource.dpo-data-source")
    public DataSource dpoDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "ecologyDataSource")
    @ConfigurationProperties("spring.datasource.ecology-data-source")
    public DataSource ecologyDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource(@Qualifier("dpoDataSource") DataSource dpoDataSource,
                                        @Qualifier("ecologyDataSource") DataSource ecologyDataSource) {
        Map<String, DataSource> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DataSourceEnum.DPO.getName(), dpoDataSource);
        targetDataSources.put(DataSourceEnum.ECOLOGY.getName(), ecologyDataSource);
        DynamicDataSource dynamicDataSource = new DynamicDataSource(dpoDataSource, targetDataSources);
        return dynamicDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        // 此处设置为了解决找不到mapper文件的问题
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:net/p5w/dp/mapper/**/*.xml"));
        // 手动创建 SqlSessionFactory 时，Spring Boot 的 mybatis.configuration 不会自动生效，需在此处显式配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 事务管理
     *
     * @return 事务管理实例
     */
    @Bean
    public PlatformTransactionManager platformTransactionManager(@Qualifier("dynamicDataSource") DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}
