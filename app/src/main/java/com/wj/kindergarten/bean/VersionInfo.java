package com.wj.kindergarten.bean;

/**
 * Created by Administrator on 2015/10/28.
 */
public class VersionInfo extends BaseModel {
        String type;
        String mobileVersion;
        String appVersion;
        String city;

        public VersionInfo(String type, String mobileVersion, String appVersion, String city) {
            this.type = type;
            this.mobileVersion = mobileVersion;
            this.appVersion = appVersion;
            this.city = city;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMobileVersion() {
            return mobileVersion;
        }

        public void setMobileVersion(String mobileVersion) {
            this.mobileVersion = mobileVersion;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    @Override
    public boolean equals(Object o) {
        VersionInfo ver = (VersionInfo) o;
        if(o == null) return false;
        return type.equals(ver.getType()) && mobileVersion.equals(ver.getMobileVersion())
                && appVersion.equals(ver.getAppVersion()) && city.equals(ver.getCity());
    }
}
