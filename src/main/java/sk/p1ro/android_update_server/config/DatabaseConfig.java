package sk.p1ro.android_update_server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:#{null}}")
    private String dbUrl;

    @Value("${DB_USERNAME:#{null}}")
    private String dbUserName;

    @Value("${DB_PASSWORD:#{null}}")
    private String dbPass;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        if (dbUrl == null) {
            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:~/testdb");
        } else {
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(dbUrl);
            config.setPassword(dbPass);
            config.setUsername(dbUserName);
        }

        return new HikariDataSource(config);
    }
}
