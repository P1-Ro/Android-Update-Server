package sk.p1ro.android_update_server.service;


import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.*;
import org.springframework.stereotype.Service;
import sk.p1ro.android_update_server.entity.Apk;
import sk.p1ro.android_update_server.util.exception.CertificateException;
import sk.p1ro.android_update_server.util.exception.GenericException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public final class ParseApkService {

    /**
     * Extracts signature string from given apk file
     *
     * @param apkFile apk file of application you want to extract signature
     * @return returns signature of apk
     * @throws IOException          if given file is not apk at all
     * @throws CertificateException if apk has no certificate at all
     */
    public String getSignature(ApkFile apkFile) throws IOException, CertificateException {

        CertificateMeta meta;

        try {
            List<ApkV2Signer> v2signers = apkFile.getApkV2Singers(); // apk v2 signers
            meta = v2signers.get(0).getCertificateMetas().get(0);
        } catch (Exception e) {
            try {
                List<ApkSigner> signers = apkFile.getApkSingers();
                if (signers.isEmpty()) {
                    throw new GenericException("Your app is not signed");
                }

                meta = signers.get(0).getCertificateMetas().get(0);
            } catch (java.security.cert.CertificateException e1) {
                throw new CertificateException(e1.getMessage());
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
    public long getVersion(File file) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        ApkMeta apkMeta = apkFile.getApkMeta();
        return apkMeta.getVersionCode();
    }

    public String getPackageName(File file) throws IOException {
        ApkFile apkFile = new ApkFile(file);
        ApkMeta apkMeta = apkFile.getApkMeta();
        return apkMeta.getPackageName();
    }

    public byte[] getIcon(ApkFile apkFile) throws IOException {
        List<IconFace> icons = apkFile.getAllIcons();
        if (!icons.isEmpty()) {
            return icons.get(0).getData();
        }

        return new byte[]{};
    }

    public void fillMeta(Apk newFile, File file) throws IOException, CertificateException {
        ApkFile apkFile = new ApkFile(file);
        ApkMeta apkMeta = apkFile.getApkMeta();

        newFile.setVersionCode(apkMeta.getVersionCode());
        newFile.setSignature(getSignature(apkFile));
        newFile.setPackageName(apkMeta.getPackageName());
        newFile.setVersionName(apkMeta.getVersionName());
        newFile.setIcon(getIcon(apkFile));
        newFile.setName(apkMeta.getName());
    }
}
