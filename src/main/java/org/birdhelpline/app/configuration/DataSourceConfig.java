package org.birdhelpline.app.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@Repository
@EnableTransactionManagement
public class DataSourceConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    HikariConfig config = new HikariConfig();
    HikariDataSource dataSource;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/dev");
        config.setUsername("dev");
        config.setPassword("dev123");
        config.setMaximumPoolSize(2);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
        return dataSource;
    }

//    /**
//     * DataSource PostConstruct call-back
//     * @throws SQLException
//     */
//    @PostConstruct
//    public void dataSourceInitialization() {
//        // h2 admin via hsql Database Manager
////        DatabaseManagerSwing.main(new String[] { "--url", "jdbc:h2:mem:dataSource", "--user", "sa", "--password", "" });
//    }
//
//    @PreDestroy()
//    public void dataSourceDestroy() throws SQLException {
//    }

}