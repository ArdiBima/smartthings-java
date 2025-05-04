package com.task1.smartthings.config;

import com.zaxxer.hikari.*;
import javax.sql.DataSource;


public class DBConfig {
    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        String dbUrl = System.getProperty("DB_URL");
        String dbUser = System.getProperty("DB_USER");
        String dbPassword = System.getProperty("DB_PASSWORD");
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        return new HikariDataSource(config);
    }
}