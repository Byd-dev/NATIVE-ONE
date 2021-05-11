package com.pro.bityard.entity;

import java.util.List;


public class QuoteCodeEntity {


    /**
     * group : [{"name":"Best","list":["BTCUSDT","BCHUSDT","ETHUSDT"]},{"name":"Recommend","list":["DASHUSDT","LTCUSDT","ETCUSDT","LINKUSDT","EOSUSDT","XRPUSDT","TRXUSDT"]},{"name":"New","list":["ADAUSDT","XLMUSDT","XTZUSDT","ZRXUSDT","BATUSDT","KNCUSDT","LINKUSDT","DASHUSDT","PXGUSDT","UNIUSDT","UNFIUSDT","AVAXUSDT"]},{"name":"Main","list":["BTCUSDT","BCHUSDT","EOSUSDT","ETCUSDT","ETHUSDT","LTCUSDT","XRPUSDT","TRXUSDT","ATOMUSDT","DOTUSDT"]},{"name":"Derivatives","list":["NQ","YM","CN","SI","CL","NG","GC","HG","DAX","NK","HSI"]},{"name":"Spot","list":["BYDUSDT_CC","DASUSDT_CC","XLMUSDT_CC","LINUSDT_CC","MKRUSDT_CC","COMUSDT_CC","BATUSDT_CC","KNCUSDT_CC","ZRXUSDT_CC","BANUSDT_CC","ATOUSDT_CC","XTZUSDT_CC","LENUSDT_CC","OMGUSDT_CC","RENUSDT_CC","ANTUSDT_CC","ENJUSDT_CC","MANUSDT_CC","STOUSDT_CC","LUNUSDT_CC","SANUSDT_CC","ONTUSDT_CC","LRCUSDT_CC","DOTUSDT_CC","YFIUSDT_CC","BTCUSDT_CC","ETHUSDT_CC","TRXUSDT_CC","XRPUSDT_CC","EOSUSDT_CC","BCHUSDT_CC","LTCUSDT_CC","ADAUSDT_CC","UNIUSDT_CC","FILUSDT_CC","ALGUSDT_CC","SNXUSDT_CC","AAVUSDT_CC","ETCUSDT_CC","ZECUSDT_CC","ZENUSDT_CC","CRVUSDT_CC","SUSUSDT_CC","CVCUSDT_CC","ALPUSDT_CC","1INUSDT_CC","UMAUSDT_CC","DOGUSDT_CC","KSMUSDT_CC","MATUSDT_CC","SOLUSDT_CC","NEAUSDT_CC","EGLUSDT_CC","SXPUSDT_CC","OGNUSDT_CC","BNBUSDT_CC","XMRUSDT_CC","KAVUSDT_CC","IRIUSDT_CC","FTTUSDT_CC","FTMUSDT_CC","CKBUSDT_CC","BTTUSDT_CC","GRTUSDT_CC","BALUSDT_CC","WNXUSDT_CC","OCEUSDT_CC","SFPUSDT_CC","CAKUSDT_CC","DODUSDT_CC","LITUSDT_CC","REEUSDT_CC","UNFUSDT_CC","XVSUSDT_CC","AVAUSDT_CC"]},{"name":"Defi","list":["ALPUSDT_CC","UMAUSDT_CC","WNXUSDT_CC","LINUSDT_CC","MKRUSDT_CC","YFIUSDT_CC","UNIUSDT_CC","RENUSDT_CC","COMUSDT_CC","ZRXUSDT_CC","LRCUSDT_CC","KNCUSDT_CC","BANUSDT_CC","SNXUSDT_CC","AAVUSDT_CC","CRVUSDT_CC","SUSUSDT_CC","LUNUSDT_CC","KAVUSDT_CC","BALUSDT_CC","CAKUSDT_CC","XVSUSDT_CC"]},{"name":"POS","list":["DOTUSDT_CC","ADAUSDT_CC","TRXUSDT_CC","EOSUSDT_CC","XTZUSDT_CC","ATOUSDT_CC","ONTUSDT_CC","DASUSDT_CC","OMGUSDT_CC","ALGUSDT_CC","RENUSDT_CC","BANUSDT_CC","ANTUSDT_CC"]},{"name":"Gray","list":["BTCUSDT_CC","ETHUSDT_CC","LTCUSDT_CC","BCHUSDT_CC","ETCUSDT_CC","ZECUSDT_CC","ZENUSDT_CC","XLMUSDT_CC"]},{"name":"BSC","list":["BNBUSDT_CC","SFPUSDT_CC","SXPUSDT_CC","CAKUSDT_CC","XVSUSDT_CC"]}]
     * version : bityard
     * app : h5
     */

    private String version;
    private String app;
    private String all;
    private List<GroupBean> group;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return "QuoteCodeEntity{" +
                "version='" + version + '\'' +
                ", app='" + app + '\'' +
                ", all='" + all + '\'' +
                ", group=" + group +
                '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public List<GroupBean> getGroup() {
        return group;
    }

    public void setGroup(List<GroupBean> group) {
        this.group = group;
    }

    public static class GroupBean {
        /**
         * name : Best
         * list : ["BTCUSDT","BCHUSDT","ETHUSDT"]
         */

        private String name;
        private List<String> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "GroupBean{" +
                    "name='" + name + '\'' +
                    ", list=" + list +
                    '}';
        }
    }
}
