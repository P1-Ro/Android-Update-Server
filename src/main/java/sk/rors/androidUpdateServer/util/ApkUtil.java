package sk.rors.androidUpdateServer.util;


import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.ApkSigner;
import net.dongliu.apk.parser.bean.ApkV2Signer;
import net.dongliu.apk.parser.bean.CertificateMeta;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * Utility class for extracting and manipulation of information of apk files
 */
public final class ApkUtil {

    private ApkUtil() {
    }

    /**
     * Extracts signature string from given apk file
     *
     * @param file apk file of application you want to extract signature
     * @return returns signature of apk
     * @throws IOException          if given file is not apk at all
     * @throws CertificateException if apk has no certificate at all
     */
    public static String getSignature(File file) throws IOException, CertificateException {
        ApkFile apkFile = new ApkFile(file);

        CertificateMeta meta;

        try {
            List<ApkV2Signer> v2signers = apkFile.getApkV2Singers(); // apk v2 signers
            meta = v2signers.get(0).getCertificateMetas().get(0);
        } catch (Exception e) {
            try {
                List<ApkSigner> signers = apkFile.getApkSingers();
                meta = signers.get(0).getCertificateMetas().get(0);
            } catch (CertificateException e1) {
                throw new CertificateException(e1);
            }

        }

        return meta.getCertMd5() + "_" + meta.getSignAlgorithmOID();
    }

    /**
     * Extracts versionCode from given apk file
     *
     * @param file apk file of application you want to extract versionCode
     * @return versionCode of apk
     * @throws IOException if given file is not apk at all
     */
    public static long getVersion(File file) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        ApkMeta apkMeta = apkFile.getApkMeta();
        return apkMeta.getVersionCode();
    }

    public static String getPackageName(File file) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        ApkMeta apkMeta = apkFile.getApkMeta();
        return apkMeta.getPackageName();
    }

}
