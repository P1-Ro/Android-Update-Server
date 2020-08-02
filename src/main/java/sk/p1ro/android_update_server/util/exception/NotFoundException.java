package sk.p1ro.android_update_server.util.exception;

public class NotFoundException extends GenericException {

    public NotFoundException() {
        super("Resource not found");
    }

}