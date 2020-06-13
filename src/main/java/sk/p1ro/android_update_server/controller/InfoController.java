package sk.p1ro.android_update_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.p1ro.android_update_server.model.OverviewInfo;
import sk.p1ro.android_update_server.repository.ApkRepository;


@RestController
public class InfoController {

    private final ApkRepository apkRepository;

    public InfoController(ApkRepository apkRepository) {
        this.apkRepository = apkRepository;
    }

    @GetMapping("info")
    public OverviewInfo getOverview() {

        return new OverviewInfo(apkRepository.findAll());
    }
}
