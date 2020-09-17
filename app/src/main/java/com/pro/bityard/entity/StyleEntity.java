package com.pro.bityard.entity;

import java.util.List;

public class StyleEntity {


    /**
     * code : 200
     * data : [{"code":"term_short","content":"短线","id":null,"language":"zh_CN","name":null,"type":2},{"code":"term_mid","content":"中线","id":null,"language":"zh_CN","name":null,"type":2},{"code":"term_long","content":"长线","id":null,"language":"zh_CN","name":null,"type":2},{"code":"pos_light","content":"轻仓","id":null,"language":"zh_CN","name":null,"type":2},{"code":"pos_heavy","content":"重仓","id":null,"language":"zh_CN","name":null,"type":2},{"code":"freq_low","content":"低频","id":null,"language":"zh_CN","name":null,"type":2},{"code":"freq_high","content":"高频","id":null,"language":"zh_CN","name":null,"type":2},{"code":"risk_low","content":"稳健","id":null,"language":"zh_CN","name":null,"type":2},{"code":"risk_high","content":"高风险","id":null,"language":"zh_CN","name":null,"type":2},{"code":"mainstream","content":"主流","id":null,"language":"zh_CN","name":null,"type":2},{"code":"radical","content":"激进","id":null,"language":"zh_CN","name":null,"type":2}]
     * message :
     */

    private int code;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "StyleEntity{" +
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
         * code : term_short
         * content : 短线
         * id : null
         * language : zh_CN
         * name : null
         * type : 2
         */

        private String code;
        private String content;
        private String id;
        private String language;
        private String name;
        private int type;

        @Override
        public String toString() {
            return "DataBean{" +
                    "code='" + code + '\'' +
                    ", content='" + content + '\'' +
                    ", id=" + id +
                    ", language='" + language + '\'' +
                    ", name=" + name +
                    ", type=" + type +
                    '}';
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
