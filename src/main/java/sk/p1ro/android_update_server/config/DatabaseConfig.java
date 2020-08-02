package sk.p1ro.android_update_server.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        String databaseUrl = System.getenv("DATABASE_URL");

        if (databaseUrl == null) {
            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:~/testdb");
        } else {
            config.setDriverClassName("org.postgresql.Driver");

            URI dbUri;
            try {
                dbUri = new URI(databaseUrl);
            }
            catch (URISyntaxException e) {
                log.error(String.format("Invalid DATABASE_URL: %s", databaseUrl), e);
                return null;
            }

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            config.setJdbcUrl(dbUrl);
            config.setUsername(username);
            config.setPassword(password);
        }

        return new HikariDataSource(config);
    }
}
