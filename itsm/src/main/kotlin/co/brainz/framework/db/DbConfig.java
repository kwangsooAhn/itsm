package co.brainz.framework.db;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ "classpath:db.properties" })
@EnableJpaRepositories(
	basePackages = { "co.brainz.*" }
	, entityManagerFactoryRef = "postgresqlEntityManager"
	, transactionManagerRef = "postgresqlTransactionManager"
)

public class DbConfig {
	@Value("${db.datasource.driverclassname}")
	private String dbDriverClassName;

	@Value("${db.datasource.url}")
	private String dbURL;

	@Value("${db.datasource.username}")
	private String userName;

	@Value("${db.datasource.password}")
	private String password;

	@Bean(name = "postgresqlEntityManager")
	@Primary
	public LocalContainerEntityManagerFactoryBean postgresqlEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(postgreDataSource());

		em.setPackagesToScan(new String[] { "co.brainz.*" });

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = new HashMap<>();

		properties.put("spring.jpa.database", "POSTGRESQL");
		properties.put("spring.jpa.hibernate.ddl-auto", "create-drop");
		properties.put("spring.jpa.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", "true");
		properties.put("hibernate.implicit_naming_strategy","org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
		properties.put("hibernate.physical_naming_strategy","org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");

		em.setJpaPropertyMap(properties);

		return em;
	}

	@Primary
	@Bean(name = "postgreDataSource")
	public DataSource postgreDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(dbURL);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(dbDriverClassName);
		return dataSource;
	}

	@Primary
	@Bean(name = "postgresqlTransactionManager")
	public PlatformTransactionManager postgresqlTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(postgresqlEntityManager().getObject());
		return transactionManager;
	}

}
