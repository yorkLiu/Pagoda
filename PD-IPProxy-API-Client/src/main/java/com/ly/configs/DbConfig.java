package com.ly.configs;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@Configuration
@EnableJpaRepositories("com.ly.repository")
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(value = "classpath:application.properties")
public class DbConfig {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String databaseDriverClassName;
  private String databasePassword;
  private String databaseUrl;
  private String databaseUsername;

  /** DOCUMENT ME! */
  private Environment env;

  private String       hibernateDialect;
  private String       hibernateFormatSql;
  private String       hibernateShowSql;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Bean
  public DataSource dataSource() {
    final HikariDataSource ds = new HikariDataSource();
// ds.setMaximumPoolSize(100);
    ds.setDataSourceClassName(this.databaseDriverClassName);
    ds.addDataSourceProperty("url", this.databaseUrl);
    ds.addDataSourceProperty("user", this.databaseUsername);
    ds.addDataSourceProperty("password", this.databasePassword);
    ds.addDataSourceProperty("cachePrepStmts", true);
    ds.addDataSourceProperty("prepStmtCacheSize", 250);
    ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    ds.addDataSourceProperty("useServerPrepStmts", true);
    ds.setMinimumIdle(5);
    ds.setLeakDetectionThreshold(60000);
    ds.setConnectionTimeout(30000);
    ds.setIdleTimeout(600000);
    ds.setMaxLifetime(1200000);

    return ds;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * entityManager.
   *
   * @param   entityManagerFactory  EntityManagerFactory
   *
   * @return  EntityManager
   */
  @Bean
  @Qualifier(value = "entityManager")
  public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
    return entityManagerFactory.createEntityManager();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * entityManagerFactory.
   *
   * @return  LocalContainerEntityManagerFactoryBean
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(Boolean.FALSE);
    vendorAdapter.setShowSql(Boolean.FALSE);

    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.ly.domains");

    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.dialect", hibernateDialect);
    jpaProperties.put("hibernate.hbm2ddl.auto", "none");
    jpaProperties.put("hibernate.show_sql", this.hibernateShowSql);
    jpaProperties.put("hibernate.format_sql", this.hibernateFormatSql);
    jpaProperties.put("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");
    jpaProperties.put("hibernate.hikari.dataSourceClassName", this.databaseDriverClassName);
    jpaProperties.put("hibernate.hikari.dataSource.url", this.databaseUrl);
    jpaProperties.put("hibernate.hikari.dataSource.user", this.databaseUsername);
    jpaProperties.put("hibernate.hikari.dataSource.password", this.databasePassword);
    jpaProperties.put("hibernate.hikari.dataSource.cachePrepStmts", "true");
    jpaProperties.put("hibernate.hikari.dataSource.prepStmtCacheSize", "250");
    jpaProperties.put("hibernate.hikari.dataSource.prepStmtCacheSqlLimit", "2048");
    jpaProperties.put("hibernate.hikari.dataSource.useServerPrepStmts", "true");

    factory.setJpaProperties(jpaProperties);
// factory.setDataSource(dataSource());

    factory.afterPropertiesSet();
    factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

    return factory;
  } // end method entityManagerFactory

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Bean
  public HibernateExceptionTranslator hibernateExceptionTranslator() {
    return new HibernateExceptionTranslator();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for env.
   *
   * @param  env  Environment
   */
  @Autowired
  public void setEnv(Environment env) {
    this.env = env;

    this.databaseDriverClassName = env.getProperty("JDBC_DRIVER");
    logger.info("Using :" + this.databaseDriverClassName);
    this.databaseUrl = env.getProperty("JDBC_URL");
    logger.info("For URL:" + this.databaseUrl);
    this.databaseUsername = env.getProperty("JDBC_USERNAME");
    this.databasePassword = env.getProperty("JDBC_PASSWORD");

    this.hibernateDialect   = env.getProperty("hibernate.dialect");
    this.hibernateFormatSql = env.getProperty("hibernate.format_sql");
    this.hibernateShowSql   = env.getProperty("hibernate.show_sql");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * transactionManager.
   *
   * @return  PlatformTransactionManager
   */
  @Bean
  public PlatformTransactionManager transactionManager() {
    EntityManagerFactory factory = entityManagerFactory().getObject();

    return new JpaTransactionManager(factory);
  }
} // end class DbConfig
