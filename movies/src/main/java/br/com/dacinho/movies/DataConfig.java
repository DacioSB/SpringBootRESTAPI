package br.com.dacinho.movies;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

//@Configuration
public class DataConfig {
//	@Bean
//	public DataSource dataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUsername("root");
//        dataSource.setPassword("3volu0202");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/merdaflix");
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        return dataSource;
//	}
//	@Bean
//	public JpaVendorAdapter jpaVendorAdapter() {
//		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		vendorAdapter.setDatabase(Database.MYSQL);
//		vendorAdapter.setShowSql(true);
//		vendorAdapter.setGenerateDdl(true);
//		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
//		vendorAdapter.setPrepareConnection(true);
//		return vendorAdapter;
//	}
}
