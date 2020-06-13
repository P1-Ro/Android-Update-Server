package sk.p1ro.android_update_server;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AndroidUpdateServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AndroidUpdateServerApplication.class, args);

		String sentryDsn = System.getenv().get("SENTRY_DSN");
		if (sentryDsn != null && sentryDsn.length() != 0) {
			Sentry.init(sentryDsn);
		}
	}

}
