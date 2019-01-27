package org.birdhelpline.app.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@Repository
@EnableTransactionManagement
public class DataSourceConfig {
    private final static Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);
    @Value("${database.jdbc.url}")
    private String jdbcURL;
    @Value("${database.jdbc.user}")
    private String userName;
    @Value("${database.jdbc.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        logger.info("VKJ DB : " + jdbcURL + "\t" + userName + "\t" + password);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcURL);
        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
}