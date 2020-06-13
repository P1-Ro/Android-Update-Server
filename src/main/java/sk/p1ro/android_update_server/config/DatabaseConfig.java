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

    @Value("${spring.datasource.username:#{null}}")
    private String dbUserName;

    @Value("${spring.datasource.password:#{null}}")
    private String dbPass;

    @Value("${spring.datasource.driverClassName:#{null}}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        if (driverClassName == null) {
            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:mem:testdb");
        } else {
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl(dbUrl);
            config.setPassword(dbPass);
            config.setUsername(dbUserName);
        }

        return new HikariDataSource(config);
    }
}
