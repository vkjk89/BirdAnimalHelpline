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

    @Value("${database.jdbc.url}")
    private String jdbcURL;
    @Value("${database.jdbc.user}")
    private String userName;
    @Value("${database.jdbc.password}")
    private String password;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        logger.info("VKJ DB : "+jdbcURL+"\t"+userName+"\t"+password);
        config = new HikariConfig();
        config.setJdbcUrl(jdbcURL);
        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
        return dataSource;
    }

}