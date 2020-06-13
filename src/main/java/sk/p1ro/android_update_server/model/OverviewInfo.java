package sk.p1ro.android_update_server.model;

import sk.p1ro.android_update_server.entity.Apk;

import java.util.ArrayList;
import java.util.List;

public class OverviewInfo {

    private List<Apk> apps;

    public OverviewInfo(Iterable<Apk> apps){
        List<Apk> list = new ArrayList<>();
        apps.iterator().forEachRemaining(list::add);
        this.apps = list;
    }

    public List<Apk> getApps() {
        return apps;
    }

    public void setApps(List<Apk> apps) {
        this.apps = apps;
    }
}
