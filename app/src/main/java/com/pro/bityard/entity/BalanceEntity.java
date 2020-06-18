package com.pro.bityard.entity;

import java.util.List;

public class BalanceEntity {


    /**
     * code : 200
     * data : [{"currency":"USDT","eagle":6.45,"game":99853.19343778,"lucky":0,"money":33100.29985734,"prize":0},{"currency":"BTC","eagle":0,"game":0,"lucky":0,"money":0,"prize":0},{"currency":"ETH","eagle":0,"game":0,"lucky":0,"money":71.996,"prize":0},{"currency":"XRP","eagle":0,"game":0,"lucky":0,"money":10,"prize":0},{"currency":"HT","eagle":0,"game":0,"lucky":0,"money":0.7,"prize":0},{"currency":"TRX","eagle":0,"game":0,"lucky":0,"money":4,"prize":0},{"currency":"EOS","eagle":0,"game":0,"lucky":0,"money":0,"prize":0}]
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
        @Override
        public String toString() {
            return "DataBean{" +
                    "currency='" + currency + '\'' +
                    ", eagle=" + eagle +
                    ", game=" + game +
                    ", lucky=" + lucky +
                    ", money=" + money +
                    ", prize=" + prize +
                    '}';
        }

        /**
         * currency : USDT
         * eagle : 6.45
         * game : 99853.19343778
         * lucky : 0.0
         * money : 33100.29985734
         * prize : 0.0
         */



        private String currency;
        private double eagle;
        private double game;
        private double lucky;
        private double money;
        private double prize;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public double getEagle() {
            return eagle;
        }

        public void setEagle(double eagle) {
            this.eagle = eagle;
        }

        public double getGame() {
            return game;
        }

        public void setGame(double game) {
            this.game = game;
        }

        public double getLucky() {
            return lucky;
        }

        public void setLucky(double lucky) {
            this.lucky = lucky;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getPrize() {
            return prize;
        }

        public void setPrize(double prize) {
            this.prize = prize;
        }
    }
}
