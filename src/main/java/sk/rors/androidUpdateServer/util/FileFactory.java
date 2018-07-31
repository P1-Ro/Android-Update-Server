package sk.rors.androidUpdateServer.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import io.sentry.Sentry;
import org.apache.commons.io.FileUtils;
import sk.rors.androidUpdateServer.model.Apk;
import sk.rors.androidUpdateServer.persistence.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Factory for retrieving uploaded apk files
 */
public class FileFactory {

    private static FileFactory instance;
    private FirebaseApp app;

    private FileFactory() {
        try {
            FileInputStream serviceAccount = new FileInputStream("D:/Desktop/credentials.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://androidupdateserver.firebaseio.com")
                    .build();

            app = FirebaseApp.initializeApp(options, "AndroidUpdateServer");
        } catch (IOException e) {
            e.printStackTrace();
            Sentry.capture(e);
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
            Apk apk = (Apk) Database.getInstance().getDao(Apk.class).queryForId(packageName);
            if (apk.getApk() != null) {
                file = new File(UUID.randomUUID().toString());
                FileUtils.writeByteArrayToFile(file, apk.getApk());
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Method for saving given apk file onto server.
     *
     * @param file apk to be saved
     * @throws FileAlreadyExistsException if apk with specified version code already exists on server
     * @throws CertificateException       if application with same packageName present on server is signed with different certificate
     */
    public void saveApk(File file) throws FileAlreadyExistsException, CertificateException {
        throw new RuntimeException("Not implemented yet");

        //sendNotifications(packageName, "1");
    }

    public void sendNotifications(String packageName, String versionName) {

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("version", versionName)
                .setTopic(packageName)
                .build();

        try {
            FirebaseMessaging.getInstance(app).send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            Sentry.capture(e);
        }
    }

}
