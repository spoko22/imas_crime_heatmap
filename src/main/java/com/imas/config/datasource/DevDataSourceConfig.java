package com.imas.config.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * Created by spoko on 22.06.16.
 */

@Configuration
@Profile("dev")
public class DevDataSourceConfig {
    String datasourceUrl = "jdbc:mysql://localhost/imas";
    String datasourceUsername = "root";
    String datasourcePassword = "root";
    String datasourceDriverClassName = "com.mysql.jdbc.Driver";

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setMaxPoolSize(10);
        dataSource.setMinPoolSize(5);
        dataSource.setAcquireIncrement(1);
        dataSource.setIdleConnectionTestPeriod(0);
        dataSource.setMaxStatements(50);
        dataSource.setMaxIdleTime(0);
        dataSource.setJdbcUrl(datasourceUrl);
        dataSource.setPassword(datasourcePassword);
        dataSource.setUser(datasourceUsername);
        dataSource.setDriverClass(datasourceDriverClassName);
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.imas.model");

        em.setJpaVendorAdapter(jpaVendorAdapter());

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.jdbc.batch_size", "1000");
        properties.setProperty("hibernate.connection.useUnicode", "true");
        properties.setProperty("hibernate.connection.characterEncoding", "UTF-8");
        em.setJpaProperties(properties);

        return em;
    }
}