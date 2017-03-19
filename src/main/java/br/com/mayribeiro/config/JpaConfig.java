package br.com.mayribeiro.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.mayribeiro.repository", 
						entityManagerFactoryRef = "entityManagerFactory", 
						transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class JpaConfig {

	@Autowired
	private Environment environment;
	
	@Value("${datasource.abapai.maxPoolSize:10}")
	private int maxPoolSize;
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "datasource.abapai")
	public DataSourceProperties getDataSourceProperties() {
		return new DataSourceProperties();
	}
	
	/***
	 * HikariCP parecido com C3PO
	 */
	@Bean
	public DataSource getDataSource() {
		
		DataSourceProperties dataSourceProperties = getDataSourceProperties();
		
		HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create(dataSourceProperties.getClassLoader())
				.driverClassName(dataSourceProperties.getDriverClassName())
				.url(dataSourceProperties.getUrl())
				.username(dataSourceProperties.getUsername())
				.password(dataSourceProperties.getPassword())
				.type(HikariDataSource.class)
				.build();
		
		dataSource.setMaximumPoolSize(maxPoolSize);
		
		return dataSource;
	}
	
	/**
	 * Entity Manager Factory setup
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(getDataSource());
		factoryBean.setPackagesToScan(new String[] {"br.com.mayribeiro.model"});
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.setJpaProperties(getJpaProperties());
		
		return factoryBean;
	}

	/**
	 * Provider specific adapter
	 * @return
	 */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		return hibernateJpaVendorAdapter;
	}
	
	/**
	 * Properties specifics
	 * @return
	 */
	private Properties getJpaProperties() {
		
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("datasource.abapai.hibernate.dialect"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("datasource.abapai.hibernate.hbm2ddl.method"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("datasource.abapai.hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("datasource.abapai.hibernate.format_sql"));
    
	    if(StringUtils.isNotEmpty(environment.getRequiredProperty("datasource.abapai.defaultSchema"))){
            properties.put("hibernate.default_schema", environment.getRequiredProperty("datasource.abapai.defaultSchema"));
        }
	        
	   return properties;
	}
	
	/**
	 * Renomear para transactionManager se nao, pode gerar uma excecao 
	 * ao criar o bean
	 * @param emf
	 * @return
	 */
	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(emf);
		return txManager;
	}

	
}
