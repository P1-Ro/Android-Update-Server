package sk.p1ro.android_update_server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url:#{null}}")
    private String dbUrl;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        if (dbUrl == null) {
            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:~/testdb");
        } else {
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(dbUrl);
        }

        return new HikariDataSource(config);
    }
}
