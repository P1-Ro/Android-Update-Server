package sk.rors.androidUpdateServer.util.exception;

/**
 * Class for indicating new application has equal or lower version
 */
public class VersionException extends RuntimeException {

    public VersionException(String msg) {
        super(msg);
    }
}
