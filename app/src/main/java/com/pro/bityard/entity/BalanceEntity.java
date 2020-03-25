package com.pro.bityard.entity;

import java.util.List;

public class BalanceEntity {


    /**
     * code : 200
     * data : [{"currency":"USDT","frozen":0,"game":100041.0386908,"money":9989781.26269602},{"currency":"ETH","frozen":0,"game":0,"money":0},{"currency":"BTC","frozen":0,"game":0,"money":2.46881574},{"currency":"TRX","frozen":0,"game":0,"money":0}]
     * message :
     */

    private int code;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "BalanceEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * currency : USDT
         * frozen : 0.0
         * game : 100041.0386908
         * money : 9989781.26269602
         */

        private String currency;
        private double frozen;
        private double game;
        private double money;

        @Override
        public String toString() {
            return "DataBean{" +
                    "currency='" + currency + '\'' +
                    ", frozen=" + frozen +
                    ", game=" + game +
                    ", money=" + money +
                    '}';
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getFrozen() {
            return frozen;
        }

        public void setFrozen(double frozen) {
            this.frozen = frozen;
        }

        public double getGame() {
            return game;
        }

        public void setGame(double game) {
            this.game = game;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }
}
