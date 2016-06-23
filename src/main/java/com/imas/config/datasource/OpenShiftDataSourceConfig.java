package com.imas.config.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by piotrek on 29.12.15.
 */
@Configuration
@Profile("openshift")
public class OpenShiftDataSourceConfig {
    String datasourceDriverClassName = "com.mysql.jdbc.Driver";

    @Bean
    public DataSource dataSource() {
        String OPENSHIFT_MYSQL_DB_HOST = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
        String OPENSHIFT_MYSQL_DB_PORT = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
        String OPENSHIFT_APP_NAME = System.getenv("OPENSHIFT_APP_NAME");
        String DATA_SOURCE_URL = String.format("jdbc:mysql://%s:%s/%s?useUnicode=yes&characterEncoding=UTF-8", OPENSHIFT_MYSQL_DB_HOST, OPENSHIFT_MYSQL_DB_PORT, OPENSHIFT_APP_NAME);
        String OPENSHIFT_MYSQL_DB_USERNAME = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
        String OPENSHIFT_MYSQL_DB_PASSWORD = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setMaxPoolSize(5);
        dataSource.setMinPoolSize(1);
        dataSource.setAcquireIncrement(1);
        dataSource.setIdleConnectionTestPeriod(0);
        dataSource.setMaxStatements(50);
        dataSource.setMaxIdleTime(0);
        dataSource.setJdbcUrl(DATA_SOURCE_URL);
        dataSource.setUser(OPENSHIFT_MYSQL_DB_USERNAME);
        dataSource.setPassword(OPENSHIFT_MYSQL_DB_PASSWORD);
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
        properties.setProperty("hibernate.jdbc.batch_size", "500");
        properties.setProperty("hibernate.connection.useUnicode", "true");
        properties.setProperty("hibernate.connection.characterEncoding", "UTF-8");
        em.setJpaProperties(properties);

        return em;
    }
}
