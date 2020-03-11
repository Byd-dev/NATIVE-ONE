package com.pro.bityard.entity;

public class TipEntity {

    /**
     * code : 200
     * message :
     * timeout : 600
     */

    private int code;
    private String message;
    private int timeout;
    private boolean check;
    private String account;
    private String email;
    private String phone;
    private boolean verify_email;
    private boolean verify_phone;
    private boolean verify_ga;
    private String token;

    @Override
    public String toString() {
        return "TipEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", timeout=" + timeout +
                ", check=" + check +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", verify_email=" + verify_email +
                ", verify_phone=" + verify_phone +
                ", verify_ga=" + verify_ga +
                ", token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerify_email() {
        return verify_email;
    }

    public void setVerify_email(boolean verify_email) {
        this.verify_email = verify_email;
    }

    public boolean isVerify_phone() {
        return verify_phone;
    }

    public void setVerify_phone(boolean verify_phone) {
        this.verify_phone = verify_phone;
    }

    public boolean isVerify_ga() {
        return verify_ga;
    }

    public void setVerify_ga(boolean verify_ga) {
        this.verify_ga = verify_ga;
    }

}
