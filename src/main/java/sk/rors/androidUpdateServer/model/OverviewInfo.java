package sk.rors.androidUpdateServer.model;

import java.util.List;

public class OverviewInfo extends Model {

    private List<Apk> apps;

    public OverviewInfo(List<Apk> apps){
        this.apps = apps;
    }

    public List<Apk> getApps() {
        return apps;
    }

    public void setApps(List<Apk> apps) {
        this.apps = apps;
    }
}
