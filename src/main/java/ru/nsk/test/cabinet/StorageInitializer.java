package ru.nsk.test.cabinet;

import java.util.Properties;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Class configure persistence layer for MySQL database. Configuration loaded
 * from application.properties property file.
 * @author me
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("ru.nsk.test.cabinet")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("ru.nsk.test.cabinet.repository")
public class StorageInitializer {

    private static final String DATABASE_DRIVER = "db.driver";
    private static final String DATABASE_PASSWORD = "db.password";
    private static final String DATABASE_URL = "db.url";
    private static final String DATABASE_USERNAME = "db.username";
    private static final String HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String HIBERNATE_VALIDATOR_LISTENERS = "hibernate.validator.autoregister_listeners";
    private static final String HIBERNATE_VALIDATOR_DDL = "hibernate.validator.apply_to_ddl";
    private static final String HIBERNATE_HBM2DDL ="hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_USE_UNICODE ="hibernate.connection.useUnicode";
    private static final String HIBERNATE_ENCODING ="hibernate.connection.characterEncoding";
    private static final String HIBERNATE_CHARSET ="hibernate.connection.charSet";
    private static final String JAVAX_VALIDATOR_MODE = "javax.persistence.validation.mode";
    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(DATABASE_PASSWORD));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPersistenceProviderClass(HibernatePersistence.class);
        factory.setPackagesToScan(getClass().getPackage().getName());

        factory.setJpaProperties(hibProperties());

        return factory;
    }

    private Properties hibProperties() {
        Properties properties = new Properties();
        setProperty(properties, HIBERNATE_DIALECT);
        setProperty(properties, HIBERNATE_SHOW_SQL);
        setProperty(properties, HIBERNATE_VALIDATOR_LISTENERS);
        setProperty(properties, HIBERNATE_VALIDATOR_DDL);
        setProperty(properties, JAVAX_VALIDATOR_MODE);
        setProperty(properties, HIBERNATE_HBM2DDL);
        
        setProperty(properties, HIBERNATE_USE_UNICODE);
        setProperty(properties, HIBERNATE_ENCODING);
        setProperty(properties, HIBERNATE_CHARSET);
        
        return properties;
    }

    private void setProperty(Properties properties, String propertyName) {
        properties.put(propertyName, env.getRequiredProperty(propertyName));
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
