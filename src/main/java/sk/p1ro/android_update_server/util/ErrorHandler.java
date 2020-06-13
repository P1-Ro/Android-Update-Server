package sk.p1ro.android_update_server.util;

import io.sentry.Sentry;
import io.sentry.event.BreadcrumbBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.p1ro.android_update_server.service.FileFactory;


public final class ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(FileFactory.class);

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
            logger.error(msg, t);
            t.printStackTrace();
            Sentry.capture(t);
        } else {
            Sentry.capture(msg);
        }
    }

}
