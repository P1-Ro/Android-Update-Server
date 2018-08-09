package sk.rors.androidUpdateServer.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.io.Files;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import sk.rors.androidUpdateServer.model.Apk;
import sk.rors.androidUpdateServer.persistence.Database;
import sk.rors.androidUpdateServer.util.exception.VersionException;

import java.io.*;
import java.nio.charset.Charset;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Factory for retrieving uploaded apk files
 */
public class FileFactory {

    private static FileFactory instance;
    private FirebaseApp app = null;

    private FileFactory() {
        try {
            String credentials = System.getenv().get("FIREBASE_CREDENTIALS");
            if (credentials != null && credentials.length() != 0) {
                InputStream serviceAccount = IOUtils.toInputStream(credentials, Charset.forName("UTF-8"));
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://androidupdateserver.firebaseio.com")
                        .build();

                app = FirebaseApp.initializeApp(options, "AndroidUpdateServer");
            }
        } catch (IOException e) {
            ErrorHandler.handle(e);
        }
    }

    public static FileFactory getInstance() {
        if (instance == null) {
            instance = new FileFactory();
        }

        return instance;
    }

    /**
     * Returns latest valid apk file with given package name
     *
     * @param packageName package name of application to return
     * @return latest valid apk
     */
    public File getLatestApk(String packageName) {
        File file = null;
        try {
            Apk apk = Database.getInstance().getDao(Apk.class).queryForId(packageName);
            if (apk != null && apk.getApk() != null) {
                file = new File(UUID.randomUUID().toString());
                FileUtils.writeByteArrayToFile(file, apk.getApk());
            }
        } catch (SQLException | IOException e) {
            ErrorHandler.handle(e);
        }
        return file;
    }

    /**
     * Method for saving given apk file onto server.
     *
     * @param file apk to be saved
     * @throws VersionException     if apk with specified version code already exists on server
     * @throws CertificateException if application with same packageName present on server is signed with different certificate
     */
    public void saveApk(File file) throws VersionException, CertificateException {
        try {
            String packageName = ApkUtil.getPackageName(file);

            Apk newFile = new Apk();
            newFile.setPackageName(packageName);
            newFile.setVersionCode(ApkUtil.getVersion(file));
            newFile.setSignature(ApkUtil.getSignature(file));
            newFile.setVersionName(ApkUtil.getVersionName(file));
            newFile.setApk(Files.toByteArray(file));
            Apk dbFile = Database.getInstance().getDao(Apk.class).queryForId(packageName);

            if (dbFile == null) {
                Database.getInstance().getDao(Apk.class).create(newFile);
            } else {
                if (newFile.getVersionCode() > dbFile.getVersionCode()) {
                    if(newFile.getSignature().equals(dbFile.getSignature())) {
                        Database.getInstance().getDao(Apk.class).update(newFile);
                    } else {
                        throw new CertificateException("Certificates do not match");
                    }
                } else {
                    throw new VersionException("Version code is lower");
                }
            }

            sendNotifications(packageName, newFile.getVersionName());

        } catch (IOException | SQLException e) {
            ErrorHandler.handle(e);
        }
    }

    private void sendNotifications(String packageName, String versionName) {
        if (app != null) {

            Message message = Message.builder()
                    .putData("version", versionName)
                    .setTopic(packageName)
                    .build();

            try {
                FirebaseMessaging.getInstance(app).send(message);
            } catch (FirebaseMessagingException e) {
                ErrorHandler.handle(e);
            }
        }
    }

}
