package sk.rors.androidUpdateServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "latest_apk")
public class Apk {

    @DatabaseField(id = true)
    private String packageName;
    @DatabaseField
    private String versionName;
    @DatabaseField
    private String signature;
    @DatabaseField
    private Integer versionCode;
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] apk;

    public Apk() {
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @JsonIgnore
    public byte[] getApk() {
        return apk;
    }

    public void setApk(byte[] apk) {
        this.apk = apk;
    }

    @JsonIgnore
    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String serialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }


}
