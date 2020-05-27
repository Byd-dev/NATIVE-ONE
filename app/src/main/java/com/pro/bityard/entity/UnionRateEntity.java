package com.pro.bityard.entity;

import java.util.List;

public class UnionRateEntity {


    @Override
    public String toString() {
        return "UnionRateEntity{" +
                "code=" + code +
                ", unionTotal='" + unionTotal + '\'' +
                ", union=" + union +
                ", asset=" + asset +
                ", unionVolume=" + unionVolume +
                ", message='" + message + '\'' +
                '}';
    }

    /**
     * code : 200
     * unionTotal : 39.24225000
     * union : {"brand":"BYD","commRatio":0.06,"commRatioExtra":"","commTime":183,"userConsume":1,"userCount":3,"userId":"422134795078729728","visitCount":0,"visitIp":"","visitTime":0}
     * asset : {"brand":"BYD","comm":0,"commApply":0,"commExchange":0,"currency":"USDT","dataMD5":"2f1c8e79bfd1b7f1ebd5c355b2945895","discount":"","eagle":0,"frozen":0,"fundIn":100100,"fundOut":248,"game":87074.93,"modify":false,"modifyFields":[],"money":101.93851677,"signString":"422134795078729728`101.9385`0.0000`0.0000","userId":"422134795078729728"}
     * unionVolume : 184804
     * message :
     */



    private int code;
    private String unionTotal;
    private UnionBean union;
    private AssetBean asset;
    private int unionVolume;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUnionTotal() {
        return unionTotal;
    }

    public void setUnionTotal(String unionTotal) {
        this.unionTotal = unionTotal;
    }

    public UnionBean getUnion() {
        return union;
    }

    public void setUnion(UnionBean union) {
        this.union = union;
    }

    public AssetBean getAsset() {
        return asset;
    }

    public void setAsset(AssetBean asset) {
        this.asset = asset;
    }

    public int getUnionVolume() {
        return unionVolume;
    }

    public void setUnionVolume(int unionVolume) {
        this.unionVolume = unionVolume;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class UnionBean {
        @Override
        public String toString() {
            return "UnionBean{" +
                    "brand='" + brand + '\'' +
                    ", commRatio=" + commRatio +
                    ", commRatioExtra='" + commRatioExtra + '\'' +
                    ", commTime=" + commTime +
                    ", userConsume=" + userConsume +
                    ", userCount=" + userCount +
                    ", userId='" + userId + '\'' +
                    ", visitCount=" + visitCount +
                    ", visitIp='" + visitIp + '\'' +
                    ", visitTime=" + visitTime +
                    '}';
        }

        /**
         * brand : BYD
         * commRatio : 0.06
         * commRatioExtra :
         * commTime : 183
         * userConsume : 1
         * userCount : 3
         * userId : 422134795078729728
         * visitCount : 0
         * visitIp :
         * visitTime : 0
         */



        private String brand;
        private double commRatio;
        private String commRatioExtra;
        private int commTime;
        private int userConsume;
        private int userCount;
        private String userId;
        private int visitCount;
        private String visitIp;
        private int visitTime;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public double getCommRatio() {
            return commRatio;
        }

        public void setCommRatio(double commRatio) {
            this.commRatio = commRatio;
        }

        public String getCommRatioExtra() {
            return commRatioExtra;
        }

        public void setCommRatioExtra(String commRatioExtra) {
            this.commRatioExtra = commRatioExtra;
        }

        public int getCommTime() {
            return commTime;
        }

        public void setCommTime(int commTime) {
            this.commTime = commTime;
        }

        public int getUserConsume() {
            return userConsume;
        }

        public void setUserConsume(int userConsume) {
            this.userConsume = userConsume;
        }

        public int getUserCount() {
            return userCount;
        }

        public void setUserCount(int userCount) {
            this.userCount = userCount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getVisitCount() {
            return visitCount;
        }

        public void setVisitCount(int visitCount) {
            this.visitCount = visitCount;
        }

        public String getVisitIp() {
            return visitIp;
        }

        public void setVisitIp(String visitIp) {
            this.visitIp = visitIp;
        }

        public int getVisitTime() {
            return visitTime;
        }

        public void setVisitTime(int visitTime) {
            this.visitTime = visitTime;
        }
    }

    public static class AssetBean {
        @Override
        public String toString() {
            return "AssetBean{" +
                    "brand='" + brand + '\'' +
                    ", comm=" + comm +
                    ", commApply=" + commApply +
                    ", commExchange=" + commExchange +
                    ", currency='" + currency + '\'' +
                    ", dataMD5='" + dataMD5 + '\'' +
                    ", discount='" + discount + '\'' +
                    ", eagle=" + eagle +
                    ", frozen=" + frozen +
                    ", fundIn=" + fundIn +
                    ", fundOut=" + fundOut +
                    ", game=" + game +
                    ", modify=" + modify +
                    ", money=" + money +
                    ", signString='" + signString + '\'' +
                    ", userId='" + userId + '\'' +
                    ", modifyFields=" + modifyFields +
                    '}';
        }

        /**
         * brand : BYD
         * comm : 0
         * commApply : 0
         * commExchange : 0
         * currency : USDT
         * dataMD5 : 2f1c8e79bfd1b7f1ebd5c355b2945895
         * discount :
         * eagle : 0
         * frozen : 0
         * fundIn : 100100
         * fundOut : 248
         * game : 87074.93
         * modify : false
         * modifyFields : []
         * money : 101.93851677
         * signString : 422134795078729728`101.9385`0.0000`0.0000
         * userId : 422134795078729728
         */



        private String brand;
        private int comm;
        private int commApply;
        private int commExchange;
        private String currency;
        private String dataMD5;
        private String discount;
        private double eagle;
        private double frozen;
        private double fundIn;
        private double fundOut;
        private double game;
        private boolean modify;
        private double money;
        private String signString;
        private String userId;
        private List<?> modifyFields;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public int getComm() {
            return comm;
        }

        public void setComm(int comm) {
            this.comm = comm;
        }

        public int getCommApply() {
            return commApply;
        }

        public void setCommApply(int commApply) {
            this.commApply = commApply;
        }

        public int getCommExchange() {
            return commExchange;
        }

        public void setCommExchange(int commExchange) {
            this.commExchange = commExchange;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDataMD5() {
            return dataMD5;
        }

        public void setDataMD5(String dataMD5) {
            this.dataMD5 = dataMD5;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public double getEagle() {
            return eagle;
        }

        public void setEagle(double eagle) {
            this.eagle = eagle;
        }

        public double getFrozen() {
            return frozen;
        }

        public void setFrozen(double frozen) {
            this.frozen = frozen;
        }

        public double getFundIn() {
            return fundIn;
        }

        public void setFundIn(double fundIn) {
            this.fundIn = fundIn;
        }

        public double getFundOut() {
            return fundOut;
        }

        public void setFundOut(double fundOut) {
            this.fundOut = fundOut;
        }

        public double getGame() {
            return game;
        }

        public void setGame(double game) {
            this.game = game;
        }

        public boolean isModify() {
            return modify;
        }

        public void setModify(boolean modify) {
            this.modify = modify;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getSignString() {
            return signString;
        }

        public void setSignString(String signString) {
            this.signString = signString;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public List<?> getModifyFields() {
            return modifyFields;
        }

        public void setModifyFields(List<?> modifyFields) {
            this.modifyFields = modifyFields;
        }
    }
}
