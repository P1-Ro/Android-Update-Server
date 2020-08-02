package sk.p1ro.android_update_server.util.exception;

/**
 * Class for indicating new application has equal or lower version
 */
public class VersionException extends GenericException {

    public VersionException(String msg) {
        super(msg);
    }
}
