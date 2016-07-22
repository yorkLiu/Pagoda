package com.ly.test.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


/**
 * Created by yongliu on 6/1/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/21/2016 15:41
 */
//@Configuration
//@EnableTransactionManagement(proxyTargetClass = true)
////@EnableAutoConfiguration
//@ComponentScan(basePackages = {"com.ly.model", "com.ly.service", "com.ly.dao"})
//@PropertySource(value = "classpath:jdbc.properties")
public class DBConfig {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  @Value("${JDBC_DRIVER}")
  private String       driverClassName;
  @Value("${JDBC_URL}")
  private String       jdbcUrl;
  private final Logger logger   = LoggerFactory.getLogger(getClass());
  @Value("${JDBC_PASSWORD}")
  private String       password;

  @Value("${JDBC_USERNAME}")
  private String username;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * dataSource.
   *
   * @return  DataSource
   */
  @Bean public DataSource dataSource() {
    logger.info("driverClassName:" + this.driverClassName);
    logger.info("JDBC URL: " + this.jdbcUrl);

    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName(this.driverClassName);
    dataSource.setJdbcUrl(this.jdbcUrl);
    dataSource.setUsername(this.username);
    dataSource.setPassword(this.password);
    dataSource.addDataSourceProperty("cachePrepStmts", true);
    dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
    dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    dataSource.addDataSourceProperty("useServerPrepStmts", true);
    dataSource.setMinimumIdle(5);
    dataSource.setLeakDetectionThreshold(60000);
    dataSource.setConnectionTimeout(30000);
    dataSource.setIdleTimeout(600000);
    dataSource.setMaxLifetime(1200000);
    dataSource.setConnectionTestQuery("show tables");

    return dataSource;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * sessionFactory.
   *
   * @return  LocalSessionFactoryBean
   */
  @Bean public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
    factoryBean.setDataSource(dataSource());
    factoryBean.setPackagesToScan("com.ly.model", "com.ly.service", "com.ly.dao");


    org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();


//    StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//    configuration.setProperty("hibernate.dialect ", "org.hibernate.dialect.MySQLInnoDBDialect");
//    configuration.setProperty("hibernate.hbm2ddl.auto ", "create");
//    configuration.setProperty("hibernate.show_sql ", "true");
//    configuration.setProperty("hibernate.connection.url ", "jdbc:sqlite:TailorDB.db");
//    configuration.setProperty("hibernate.connection.driver_class ", "org.sqlite.JDBC");
//    configuration.setProperty("hibernate.connection.username ", "");
//    configuration.setProperty("hibernate.connection.password ", "");
//    sessionFactory = configuration.buildSessionFactory(serviceRegistry);





    try {
      Properties hibernateProperties = new Properties();
      hibernateProperties.put(" hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
      hibernateProperties.put("hibernate.show_sql", "true");
      hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
      factoryBean.setHibernateProperties(hibernateProperties);
      factoryBean.afterPropertiesSet();
    } catch (Exception e) {
      e.printStackTrace();
    }


    return factoryBean;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * transactionManager.
   *
   * @return  PlatformTransactionManager
   */
//  @Bean public PlatformTransactionManager transactionManager() {
//    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//    transactionManager.setSessionFactory(sessionFactory());
//
//    return transactionManager;
//  }

  @Bean
  @Autowired
  public HibernateTransactionManager transactionManager() {
    HibernateTransactionManager txManager = new HibernateTransactionManager();
    txManager.setSessionFactory(sessionFactory().getObject());
    return txManager;
  }


// Using JPA
// @Bean
// public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
// LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//
// factoryBean.setDataSource(dataSource());
////        factoryBean.setPackagesToScan(new String[] { "com.ly.model", "com.ly.repository", "com.ly.service" });
// factoryBean.setPackagesToScan("com.ly.model");
//
// HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
// vendorAdapter.setShowSql(true);
// //vendorAdapter.setGenerateDdl(generateDdl)
// factoryBean.setJpaVendorAdapter(vendorAdapter);
//
// Properties additionalProperties = new Properties();
////        additionalProperties.put("hibernate.hbm2ddl.auto", "create");
// additionalProperties.put("hibernate.hbm2ddl.auto", "update");
//
// factoryBean.setJpaProperties(additionalProperties);
// factoryBean.afterPropertiesSet();
// factoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
//
// return factoryBean;
// }
//
// @Bean
// @Qualifier(value = "entityManager")
// public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
// return entityManagerFactory.createEntityManager();
// }

// @Bean
// public PlatformTransactionManager transactionManager() {
// JpaTransactionManager transactionManager = new JpaTransactionManager();
// transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
//
// return transactionManager;
// }
//
// @Bean
// public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
// return new PersistenceExceptionTranslationPostProcessor();
// }
//
// @Bean
// public PlatformTransactionManager transactionManager() {
// EntityManagerFactory factory = entityManagerFactory().getObject();
//
// return new JpaTransactionManager(factory);
// }
} // end class DBConfig
