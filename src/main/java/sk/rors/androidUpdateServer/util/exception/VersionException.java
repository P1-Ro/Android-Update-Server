package sk.rors.androidUpdateServer.util.exception;

/**
 * Class for indicating new application has equal or lower version
 */
public class VersionException extends Exception {

    public VersionException(String msg) {
        super(msg);
    }
}
