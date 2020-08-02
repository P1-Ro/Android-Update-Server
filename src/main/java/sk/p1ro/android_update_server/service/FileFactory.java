package sk.p1ro.android_update_server.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import sk.p1ro.android_update_server.entity.Apk;
import sk.p1ro.android_update_server.repository.ApkRepository;
import sk.p1ro.android_update_server.util.ErrorHandler;
import sk.p1ro.android_update_server.util.exception.CertificateException;
import sk.p1ro.android_update_server.util.exception.VersionException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

/**
 * Factory for retrieving uploaded apk files
 */
@Service
public class FileFactory {

    private FirebaseApp app = null;
    final ApkRepository apkRepository;
    final ParseApkService parseApkService;

    public FileFactory(ApkRepository apkRepository, ParseApkService parseApkService) {
        try {
            String credentials = System.getenv().get("FIREBASE_CREDENTIALS");
            if (credentials != null && credentials.length() != 0) {
                InputStream serviceAccount = IOUtils.toInputStream(credentials, StandardCharsets.UTF_8);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://androidupdateserver.firebaseio.com")
                        .build();

                app = FirebaseApp.initializeApp(options, "AndroidUpdateServer");
            }
        } catch (IOException e) {
            ErrorHandler.handle(e);
        }
        this.apkRepository = apkRepository;
        this.parseApkService = parseApkService;
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
            Optional<Apk> apk = apkRepository.findById(packageName);
            if (apk.isPresent() && apk.get().getApk() != null) {
                file = new File(UUID.randomUUID().toString());
                FileUtils.writeByteArrayToFile(file, apk.get().getApk());
            }
        } catch (IOException e) {
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
            String packageName = parseApkService.getPackageName(file);

            Apk newFile = new Apk();
            parseApkService.fillMeta(newFile, file);
            newFile.setApk(FileUtils.readFileToByteArray(file));
            Optional<Apk> apk = apkRepository.findById(packageName);

            if (!apk.isPresent()) {
                apkRepository.save(newFile);
            } else {
                Apk dbFile = apk.get();
                if (newFile.getVersionCode() > dbFile.getVersionCode()) {
                    if(newFile.getSignature().equals(dbFile.getSignature())) {
                        apkRepository.save(newFile);
                    } else {
                        throw new CertificateException("Certificates do not match");
                    }
                } else {
                    throw new VersionException("Version code is lower");
                }
            }

            sendNotifications(packageName, newFile.getVersionName());

        } catch (IOException e) {
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
