package com.pro.bityard.entity;

public class UpdateEntity {


    /**
     * update : {"versionCode":"1","versionUrl":"https://static.bityard.com/APP-apk/bityard_test_1.0.0.apk","versionMessage":"淇浜哹ug"}
     */

    private UpdateBean update;

    @Override
    public String toString() {
        return "UpdateEntity{" +
                "update=" + update +
                '}';
    }

    public UpdateBean getUpdate() {
        return update;
    }

    public void setUpdate(UpdateBean update) {
        this.update = update;
    }

    public static class UpdateBean {
        /**
         * versionCode : 1
         * versionUrl : https://static.bityard.com/APP-apk/bityard_test_1.0.0.apk
         * versionMessage : 淇浜哹ug
         */

        private String versionCode;
        private String versionUrl;
        private String versionMessage;

        @Override
        public String toString() {
            return "UpdateBean{" +
                    "versionCode='" + versionCode + '\'' +
                    ", versionUrl='" + versionUrl + '\'' +
                    ", versionMessage='" + versionMessage + '\'' +
                    '}';
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionUrl() {
            return versionUrl;
        }

        public void setVersionUrl(String versionUrl) {
            this.versionUrl = versionUrl;
        }

        public String getVersionMessage() {
            return versionMessage;
        }

        public void setVersionMessage(String versionMessage) {
            this.versionMessage = versionMessage;
        }
    }
}
