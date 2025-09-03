package com.bharath.springdatajpa.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bharath.springdatajpa.product.entities.Product;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "productEntityManagerFactory", transactionManagerRef = "productTransactionManager", basePackages = {
		"com.bharath.springdatajpa.product" })
public class ProductDataSourceConfig {

	@Bean(name = "productDataSourceProperties")
	@ConfigurationProperties("spring.datasource-product")
	DataSourceProperties productDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "productDataSource")
	DataSource productDataSource() {
		return productDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Bean(name = "productEntityManagerFactory")
	LocalContainerEntityManagerFactoryBean productEntityManagerFactory(EntityManagerFactoryBuilder builder) {

		HashMap<String, String> productJpaProperties = new HashMap<>();
		// productJpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");
		return builder.dataSource(productDataSource()).packages(Product.class).persistenceUnit("productDataSource")
				.properties(productJpaProperties).build();
	}

	@Bean(name = "productTransactionManager")
	PlatformTransactionManager productTransactionManager(
			@Qualifier("productEntityManagerFactory") EntityManagerFactory productEntityManagerFactory) {
		return new JpaTransactionManager(productEntityManagerFactory);
	}

}
