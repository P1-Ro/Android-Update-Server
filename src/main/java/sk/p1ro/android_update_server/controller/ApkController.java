package sk.p1ro.android_update_server.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.p1ro.android_update_server.entity.Apk;
import sk.p1ro.android_update_server.repository.ApkRepository;
import sk.p1ro.android_update_server.service.FileFactory;
import sk.p1ro.android_update_server.util.exception.NotFoundException;
import sk.p1ro.android_update_server.util.exception.VersionException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.cert.CertificateException;
import java.util.Optional;

@RestController
public class ApkController {

    final ApkRepository apkRepository;
    final FileFactory fileFactory;

    public ApkController(ApkRepository apkRepository, FileFactory fileFactory) {
        this.apkRepository = apkRepository;
        this.fileFactory = fileFactory;
    }

    @GetMapping(value = "download/{packageName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> download(@PathVariable String packageName) {

        Optional<Apk> optionalApk = apkRepository.findById(packageName);
        if (optionalApk.isPresent()) {
            Apk apk = optionalApk.get();
            String fileName = apk.getPackageName() + "_" + apk.getVersionName() + ".apk";
            InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(apk.getApk()));

            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setContentType(MediaType.asMediaType(new MimeType("application", "vnd.android.package-archive")));
            respHeaders.setContentLength(apk.getApk().length);
            respHeaders.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);
        } else {
            throw new NotFoundException();
        }
    }

    @GetMapping(value = "latest/{packageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Apk latest(@PathVariable String packageName) {

        Optional<Apk> apk = apkRepository.findById(packageName);
        if (apk.isPresent()) {
            return apk.get();
        } else {
            throw new NotFoundException();
        }
    }

    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public void upload(@RequestParam("file") MultipartFile file) throws IOException, CertificateException, VersionException {

        File result = Files.createTempFile(file.getName(), ".apk").toFile();
        FileUtils.copyInputStreamToFile(file.getInputStream(), result);
        fileFactory.saveApk(result);
    }
}
