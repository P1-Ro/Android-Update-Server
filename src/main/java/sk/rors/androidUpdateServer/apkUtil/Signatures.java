package sk.rors.androidUpdateServer.apkUtil;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.File;

/**
 * Utility class for extracting and manipulation of signatures of apk files
 */
public final class Signatures {

    private Signatures(){
    }

    /**
     * Extracts signature string from given apkFile
     * @param apkFile apk file of application you want to extract signature
     * @throws InvalidArgumentException if given file is not apk at all
     * @return returns signature of apk
     */
    public static String getSignature(File apkFile) throws InvalidArgumentException {
        throw new RuntimeException("Not yet implemented");
    }

}
