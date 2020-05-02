package com.pro.bityard.entity;

public class UserDetailEntity {


    /**
     * code : 200
     * message :
     * user : {"account":"ppmwok@gmail.com","bankCardCount":0,"countryCode":"60","currency":"USDT","eagle":0,"eagleRatio":10,"email":"ppmwok@gmail.com","game":86857.19620973,"googleEnable":false,"googleValid":true,"hello":"下午好","identityNumber":"","identityNumberValid":false,"level":3,"localCurrency":"USD","mobile":"601133751865","money":17609.71342714,"name":"","phone":"601133751865","pw_l":1,"pw_w":1,"refer":"729728","registerTime":1578574378000,"tradeQuick":true,"unread":0,"userId":"422134795078729728","username":"小花花","usernameNo":1}
     */

    private int code;
    private String message;
    private UserBean user;

    @Override
    public String toString() {
        return "UserDetailEntity{" +
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

    public static class UserBean {
        @Override
        public String toString() {
            return "UserBean{" +
                    "account='" + account + '\'' +
                    ", bankCardCount=" + bankCardCount +
                    ", countryCode='" + countryCode + '\'' +
                    ", currency='" + currency + '\'' +
                    ", eagle=" + eagle +
                    ", eagleRatio=" + eagleRatio +
                    ", email='" + email + '\'' +
                    ", game=" + game +
                    ", googleEnable=" + googleEnable +
                    ", googleValid=" + googleValid +
                    ", hello='" + hello + '\'' +
                    ", identityNumber='" + identityNumber + '\'' +
                    ", identityNumberValid=" + identityNumberValid +
                    ", level=" + level +
                    ", localCurrency='" + localCurrency + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", money=" + money +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", pw_l=" + pw_l +
                    ", pw_w=" + pw_w +
                    ", refer='" + refer + '\'' +
                    ", registerTime=" + registerTime +
                    ", tradeQuick=" + tradeQuick +
                    ", unread=" + unread +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    ", usernameNo=" + usernameNo +
                    '}';
        }

        /**
         * account : ppmwok@gmail.com
         * bankCardCount : 0
         * countryCode : 60
         * currency : USDT
         * eagle : 0
         * eagleRatio : 10
         * email : ppmwok@gmail.com
         * game : 86857.19620973
         * googleEnable : false
         * googleValid : true
         * hello : 下午好
         * identityNumber :
         * identityNumberValid : false
         * level : 3
         * localCurrency : USD
         * mobile : 601133751865
         * money : 17609.71342714
         * name :
         * phone : 601133751865
         * pw_l : 1
         * pw_w : 1
         * refer : 729728
         * registerTime : 1578574378000
         * tradeQuick : true
         * unread : 0
         * userId : 422134795078729728
         * username : 小花花
         * usernameNo : 1
         */



        private String account;
        private int bankCardCount;
        private String countryCode;
        private String currency;
        private int eagle;
        private int eagleRatio;
        private String email;
        private double game;
        private boolean googleEnable;
        private boolean googleValid;
        private String hello;
        private String identityNumber;
        private boolean identityNumberValid;
        private int level;
        private String localCurrency;
        private String mobile;
        private double money;
        private String name;
        private String phone;
        private int pw_l;
        private int pw_w;
        private String refer;
        private long registerTime;
        private boolean tradeQuick;
        private int unread;
        private String userId;
        private String username;
        private int usernameNo;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getBankCardCount() {
            return bankCardCount;
        }

        public void setBankCardCount(int bankCardCount) {
            this.bankCardCount = bankCardCount;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getEagle() {
            return eagle;
        }

        public void setEagle(int eagle) {
            this.eagle = eagle;
        }

        public int getEagleRatio() {
            return eagleRatio;
        }

        public void setEagleRatio(int eagleRatio) {
            this.eagleRatio = eagleRatio;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public double getGame() {
            return game;
        }

        public void setGame(double game) {
            this.game = game;
        }

        public boolean isGoogleEnable() {
            return googleEnable;
        }

        public void setGoogleEnable(boolean googleEnable) {
            this.googleEnable = googleEnable;
        }

        public boolean isGoogleValid() {
            return googleValid;
        }

        public void setGoogleValid(boolean googleValid) {
            this.googleValid = googleValid;
        }

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        public String getIdentityNumber() {
            return identityNumber;
        }

        public void setIdentityNumber(String identityNumber) {
            this.identityNumber = identityNumber;
        }

        public boolean isIdentityNumberValid() {
            return identityNumberValid;
        }

        public void setIdentityNumberValid(boolean identityNumberValid) {
            this.identityNumberValid = identityNumberValid;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLocalCurrency() {
            return localCurrency;
        }

        public void setLocalCurrency(String localCurrency) {
            this.localCurrency = localCurrency;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getPw_l() {
            return pw_l;
        }

        public void setPw_l(int pw_l) {
            this.pw_l = pw_l;
        }

        public int getPw_w() {
            return pw_w;
        }

        public void setPw_w(int pw_w) {
            this.pw_w = pw_w;
        }

        public String getRefer() {
            return refer;
        }

        public void setRefer(String refer) {
            this.refer = refer;
        }

        public long getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(long registerTime) {
            this.registerTime = registerTime;
        }

        public boolean isTradeQuick() {
            return tradeQuick;
        }

        public void setTradeQuick(boolean tradeQuick) {
            this.tradeQuick = tradeQuick;
        }

        public int getUnread() {
            return unread;
        }

        public void setUnread(int unread) {
            this.unread = unread;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getUsernameNo() {
            return usernameNo;
        }

        public void setUsernameNo(int usernameNo) {
            this.usernameNo = usernameNo;
        }
    }
}
