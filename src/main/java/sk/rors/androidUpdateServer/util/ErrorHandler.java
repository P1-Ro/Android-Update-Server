package sk.rors.androidUpdateServer.util;

import io.sentry.Sentry;
import io.sentry.event.BreadcrumbBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ErrorHandler {

    private static final Logger logger = Logger.getGlobal();

    static {
        String sentryDsn = System.getenv().get("SENTRY_DSN");
        if (sentryDsn != null && sentryDsn.length() != 0) {
            Sentry.init(sentryDsn);
        }
    }

    private ErrorHandler() {
    }

    public static void handle(String msg) {
        handle(msg, null);
    }

    public static void handle(Throwable t) {
        handle(null, t);
    }

    public static void handle(String msg, Throwable t) {

        if (t != null) {
            if (msg != null) {
                Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage(msg).build());
            }
            logger.log(Level.SEVERE, msg, t);
            t.printStackTrace();
            Sentry.capture(t);
        } else {
            Sentry.capture(msg);
        }
    }

}
