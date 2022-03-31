package com.epam.esm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class LogicSpringConfig {
    private static final String DATA_BASE_STRUCTURE_SCRIPT = "sql/db_structure.sql";
    private static final String DATA_BASE_DATA_SCRIPT = "sql/db_data.sql";
    private static final String UTF8_ENCODING = "UTF-8";

    @Bean
    @Profile({"template-test", "hibernate-test"})
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding(UTF8_ENCODING)
                .addScript(DATA_BASE_STRUCTURE_SCRIPT)
                .addScript(DATA_BASE_DATA_SCRIPT)
                .build();
    }

    @Bean
    @Profile({"prod", "template"})
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Profile({"template", "template-test"})
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
