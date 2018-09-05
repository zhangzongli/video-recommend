package com.video.recommend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 描述:
 *    jdbc 配置类
 * @author zhangzl
 * @create 2018-08-25 下午3:04
 */
@Configuration
public class JdbcConfig {

    @Bean(name = "DataSource")
    @Qualifier("DataSource")
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource fromDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate fromJdbcTemplate(
            @Qualifier("DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
