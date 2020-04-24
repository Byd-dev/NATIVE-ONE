package com.pro.bityard.entity;

import com.pro.bityard.config.AppConfig;
import com.pro.switchlibrary.SPUtils;

import java.io.Serializable;

public class LoginEntity implements Serializable {


    private static LoginEntity instance;

    public static LoginEntity getInstance() {
        if (instance == null) {
            instance = new LoginEntity();
        }
        return instance;
    }

    public static LoginEntity getUserIsLogin() {
        LoginEntity data = SPUtils.getData(AppConfig.LOGIN, LoginEntity.class);
        return data;
    }

    public boolean isLogin() {
        return LoginEntity.getUserIsLogin() != null;
    }

    /**
     * code : 200
     * message : 登录成功
     * user : {"principal":"wilde.tomoya@gmail.com","pw_l":1,"phone":"639155106373","refer":"051264","mobile":"639155106373","pw_w":1,"userId":"424288963751051264","email":"wilde.tomoya@gmail.com","tradeQuick":true,"account":"wilde.tomoya@gmail.com"}
     */

    private int code;
    private String message;
    private UserBean user;

    @Override
    public String toString() {
        return "LoginEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Serializable {
        /**
         * principal : wilde.tomoya@gmail.com
         * pw_l : 1
         * phone : 639155106373
         * refer : 051264
         * mobile : 639155106373
         * pw_w : 1
         * userId : 424288963751051264
         * email : wilde.tomoya@gmail.com
         * tradeQuick : true
         * account : wilde.tomoya@gmail.com
         */

        private String principal;
        private int pw_l;
        private String phone;
        private String refer;
        private String mobile;
        private int pw_w;
        private String userId;
        private String email;
        private boolean tradeQuick;
        private String account;

        @Override
        public String toString() {
            return "UserBean{" +
                    "principal='" + principal + '\'' +
                    ", pw_l=" + pw_l +
                    ", phone='" + phone + '\'' +
                    ", refer='" + refer + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", pw_w=" + pw_w +
                    ", userId='" + userId + '\'' +
                    ", email='" + email + '\'' +
                    ", tradeQuick=" + tradeQuick +
                    ", account='" + account + '\'' +
                    '}';
        }

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public int getPw_l() {
            return pw_l;
        }

        public void setPw_l(int pw_l) {
            this.pw_l = pw_l;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRefer() {
            return refer;
        }

        public void setRefer(String refer) {
            this.refer = refer;
        }

        public String getMobile() {

            return mobile;

        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getPw_w() {
            return pw_w;
        }

        public void setPw_w(int pw_w) {
            this.pw_w = pw_w;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {

            return email;

        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isTradeQuick() {
            return tradeQuick;
        }

        public void setTradeQuick(boolean tradeQuick) {
            this.tradeQuick = tradeQuick;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
