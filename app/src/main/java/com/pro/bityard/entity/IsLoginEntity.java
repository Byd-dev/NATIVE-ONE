package com.pro.bityard.entity;

public class IsLoginEntity {


    /**
     * isLogin : true
     * code : 200
     * message :
     * content : {"isLogin":true}
     */

    private boolean isLogin;
    private int code;
    private String message;
    private ContentBean content;

    @Override
    public String toString() {
        return "IsLoginEntity{" +
                "isLogin=" + isLogin +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * isLogin : true
         */

        private boolean isLogin;

        @Override
        public String toString() {
            return "ContentBean{" +
                    "isLogin=" + isLogin +
                    '}';
        }

        public boolean isIsLogin() {
            return isLogin;
        }

        public void setIsLogin(boolean isLogin) {
            this.isLogin = isLogin;
        }
    }
}
