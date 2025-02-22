package ru.bot.jokemachine.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.bot.jokemachine.config.properties.DatasourceProperties;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DatasourceProperties.class)
public class FlywayConfig {

	private final DatasourceProperties properties;

	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
				.url(properties.datasource().url())
				.username(properties.datasource().username())
				.password(properties.datasource().password())
				.driverClassName(properties.datasource().driverClassName())
				.build();
	}

	@Bean
	@DependsOn("flyway")
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public Flyway flyway(DataSource dataSource) {
		Flyway flyway = Flyway.configure()
				.dataSource(dataSource)
				.locations("classpath:db/migration")
				.baselineOnMigrate(true)
				.load();
		flyway.migrate();
		return flyway;
	}
}
