package com.pro.bityard.entity;

import java.util.List;

public class DepositWithdrawEntity {


    /**
     * total : 97
     * code : 200
     * data : [{"address":"100000000098","admin":null,"adminName":null,"bankCard":"OMNI(100000000098)","brand":null,"channel":null,"charge":2,"createTime":1587991905000,"currency":"USDT","detail":"提款取出","device":null,"disposeTime":1587991905000,"explain":"提款取出","exrate":1,"finishTime":1587991905000,"id":"461634765586432000","idStr":"461634765586432000","money":50,"payNumber":"","payOrder":"","payOrderTx":"","realname":null,"remark":"","srcCurrency":"USDT","srcMoney":50,"status":-1,"statusDesc":null,"txid":"","type":200,"typeDesc":"资金取出","userId":"422134795078729728","username":null,"white":false},{"address":"1uwiieuryyqioqwiuyuuwuuwiiq","admin":null,"adminName":null,"bankCard":"OMNI(1uwiieuryyqioqwiuyuuwuuwiiq)","brand":null,"channel":null,"charge":1,"createTime":1587311693000,"currency":"USDT","detail":"提款取出","device":null,"disposeTime":1587311693000,"explain":"提款取出","exrate":1,"finishTime":1587311693000,"id":"458781749984362496","idStr":"458781749984362496","money":50,"payNumber":"","payOrder":"","payOrderTx":"","realname":null,"remark":"","srcCurrency":"USDT","srcMoney":50,"status":0,"statusDesc":null,"txid":"","type":200,"typeDesc":"资金取出","userId":"422134795078729728","username":null,"white":false},{"address":"1uwiieuryyqioqwiuyuuwuuwiiq","admin":null,"adminName":null,"bankCard":"OMNI(1uwiieuryyqioqwiuyuuwuuwiiq)","brand":null,"channel":null,"charge":0,"createTime":1584067485000,"currency":"USDT","detail":"提款取出","device":null,"disposeTime":1584067485000,"explain":"提款取出","exrate":null,"finishTime":1584067485000,"id":"445174557532225536","idStr":"445174557532225536","money":2,"payNumber":"","payOrder":"","payOrderTx":"","realname":null,"remark":"","srcCurrency":"USDT","srcMoney":2,"status":0,"statusDesc":null,"txid":"","type":200,"typeDesc":"资金取出","userId":"422134795078729728","username":null,"white":false},{"address":"","admin":null,"adminName":null,"bankCard":"","brand":null,"channel":null,"charge":null,"createTime":1582859593000,"currency":"USDT","detail":"omniXWALLET测试","device":"wap","disposeTime":1582859593000,"explain":"充值存入","exrate":null,"finishTime":null,"id":"440108288806223872","idStr":"440108288806223872","money":null,"payNumber":"BYD20200228111312HNQE","payOrder":"","payOrderTx":"","realname":null,"remark":"XXPay Error: 请求失败，通道繁忙，请稍后再试","srcCurrency":"USDT","srcMoney":1,"status":0,"statusDesc":null,"txid":"","type":100,"typeDesc":"资金存入","userId":"422134795078729728","username":null,"white":false},{"address":"","admin":null,"adminName":null,"bankCard":"","brand":null,"channel":null,"charge":null,"createTime":1582857005000,"currency":"USDT","detail":"omniXWALLET测试","device":"wap","disposeTime":1582857005000,"explain":"充值存入","exrate":null,"finishTime":null,"id":"440097433016336384","idStr":"440097433016336384","money":null,"payNumber":"BYD20200228103004KYGY","payOrder":"","payOrderTx":"","realname":null,"remark":"XXPay Error: 请求失败，通道繁忙，请稍后再试","srcCurrency":"USDT","srcMoney":1,"status":0,"statusDesc":null,"txid":"","type":100,"typeDesc":"资金存入","userId":"422134795078729728","username":null,"white":false},{"address":"","admin":null,"adminName":null,"bankCard":"","brand":null,"channel":null,"charge":null,"createTime":1582856824000,"currency":"USDT","detail":"omniXWALLET测试","device":"wap","disposeTime":1582856824000,"explain":"充值存入","exrate":null,"finishTime":null,"id":"440096676317757440","idStr":"440096676317757440","money":null,"payNumber":"BYD20200228102704ODNX","payOrder":"","payOrderTx":"","realname":null,"remark":"XXPay Error: 请求失败，通道繁忙，请稍后再试","srcCurrency":"USDT","srcMoney":1,"status":0,"statusDesc":null,"txid":"","type":100,"typeDesc":"资金存入","userId":"422134795078729728","username":null,"white":false},{"address":"","admin":null,"adminName":null,"bankCard":"","brand":null,"channel":null,"charge":null,"createTime":1582856732000,"currency":"USDT","detail":"omniXWALLET测试","device":"wap","disposeTime":1582856732000,"explain":"充值存入","exrate":null,"finishTime":null,"id":"440096289904918528","idStr":"440096289904918528","money":null,"payNumber":"BYD20200228102532UULN","payOrder":"","payOrderTx":"","realname":null,"remark":"XXPay Error: 请求失败，通道繁忙，请稍后再试","srcCurrency":"USDT","srcMoney":1,"status":0,"statusDesc":null,"txid":"","type":100,"typeDesc":"资金存入","userId":"422134795078729728","username":null,"white":false},{"address":"","admin":null,"adminName":null,"bankCard":"","brand":null,"channel":null,"charge":null,"createTime":1582813352000,"currency":"USDT","detail":"omniXWALLET测试","device":"wap","disposeTime":1582813352000,"explain":"充值存入","exrate":null,"finishTime":null,"id":"439914341504909312","idStr":"439914341504909312","money":null,"payNumber":"BYD20200227222232TIIL","payOrder":"","payOrderTx":"","realname":null,"remark":"XXPay Error: 请求失败，通道繁忙，请稍后再试","srcCurrency":"USDT","srcMoney":1,"status":0,"statusDesc":null,"txid":"","type":100,"typeDesc":"资金存入","userId":"422134795078729728","username":null,"white":false},{"address":"","admin":null,"adminName":null,"bankCard":"","brand":null,"channel":null,"charge":null,"createTime":1582813119000,"currency":"USDT","detail":"omniXWALLET测试","device":"wap","disposeTime":1582813119000,"explain":"充值存入","exrate":null,"finishTime":null,"id":"439913362910871552","idStr":"439913362910871552","money":null,"payNumber":"BYD20200227221838ITEP","payOrder":"","payOrderTx":"","realname":null,"remark":"XXPay Error: 请求失败，通道繁忙，请稍后再试","srcCurrency":"USDT","srcMoney":1,"status":0,"statusDesc":null,"txid":"","type":100,"typeDesc":"资金存入","userId":"422134795078729728","username":null,"white":false},{"address":"","admin":null,"adminName":null,"bankCard":"","brand":null,"channel":null,"charge":null,"createTime":1582813114000,"currency":"USDT","detail":"omniXWALLET测试","device":"wap","disposeTime":1582813114000,"explain":"充值存入","exrate":null,"finishTime":null,"id":"439913344191692800","idStr":"439913344191692800","money":null,"payNumber":"BYD20200227221834JQLJ","payOrder":"","payOrderTx":"","realname":null,"remark":"XXPay Error: 请求失败，通道繁忙，请稍后再试","srcCurrency":"USDT","srcMoney":1,"status":0,"statusDesc":null,"txid":"","type":100,"typeDesc":"资金存入","userId":"422134795078729728","username":null,"white":false}]
     * size : 10
     * page : 1
     * message :
     */

    private String total;
    private int code;
    private int size;
    private int page;
    private String message;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "DepositWithdrawEntity{" +
                "total='" + total + '\'' +
                ", code=" + code +
                ", size=" + size +
                ", page=" + page +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
                    "address='" + address + '\'' +
                    ", admin=" + admin +
                    ", adminName=" + adminName +
                    ", bankCard='" + bankCard + '\'' +
                    ", brand=" + brand +
                    ", channel=" + channel +
                    ", charge=" + charge +
                    ", createTime=" + createTime +
                    ", currency='" + currency + '\'' +
                    ", detail='" + detail + '\'' +
                    ", device=" + device +
                    ", disposeTime=" + disposeTime +
                    ", explain='" + explain + '\'' +
                    ", exrate=" + exrate +
                    ", finishTime=" + finishTime +
                    ", id='" + id + '\'' +
                    ", idStr='" + idStr + '\'' +
                    ", money=" + money +
                    ", payNumber='" + payNumber + '\'' +
                    ", payOrder='" + payOrder + '\'' +
                    ", payOrderTx='" + payOrderTx + '\'' +
                    ", realname=" + realname +
                    ", remark='" + remark + '\'' +
                    ", srcCurrency='" + srcCurrency + '\'' +
                    ", srcMoney=" + srcMoney +
                    ", status=" + status +
                    ", statusDesc=" + statusDesc +
                    ", txid='" + txid + '\'' +
                    ", type=" + type +
                    ", typeDesc='" + typeDesc + '\'' +
                    ", userId='" + userId + '\'' +
                    ", username=" + username +
                    ", white=" + white +
                    '}';
        }

        /**
         * address : 100000000098
         * admin : null
         * adminName : null
         * bankCard : OMNI(100000000098)
         * brand : null
         * channel : null
         * charge : 2
         * createTime : 1587991905000
         * currency : USDT
         * detail : 提款取出
         * device : null
         * disposeTime : 1587991905000
         * explain : 提款取出
         * exrate : 1
         * finishTime : 1587991905000
         * id : 461634765586432000
         * idStr : 461634765586432000
         * money : 50
         * payNumber :
         * payOrder :
         * payOrderTx :
         * realname : null
         * remark :
         * srcCurrency : USDT
         * srcMoney : 50
         * status : -1
         * statusDesc : null
         * txid :
         * type : 200
         * typeDesc : 资金取出
         * userId : 422134795078729728
         * username : null
         * white : false
         */



        private String address;
        private Object admin;
        private Object adminName;
        private String bankCard;
        private Object brand;
        private Object channel;
        private int charge;
        private long createTime;
        private String currency;
        private String detail;
        private Object device;
        private long disposeTime;
        private String explain;
        private int exrate;
        private long finishTime;
        private String id;
        private String idStr;
        private int money;
        private String payNumber;
        private String payOrder;
        private String payOrderTx;
        private Object realname;
        private String remark;
        private String srcCurrency;
        private int srcMoney;
        private int status;
        private Object statusDesc;
        private String txid;
        private int type;
        private String typeDesc;
        private String userId;
        private Object username;
        private boolean white;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getAdmin() {
            return admin;
        }

        public void setAdmin(Object admin) {
            this.admin = admin;
        }

        public Object getAdminName() {
            return adminName;
        }

        public void setAdminName(Object adminName) {
            this.adminName = adminName;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public Object getBrand() {
            return brand;
        }

        public void setBrand(Object brand) {
            this.brand = brand;
        }

        public Object getChannel() {
            return channel;
        }

        public void setChannel(Object channel) {
            this.channel = channel;
        }

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public Object getDevice() {
            return device;
        }

        public void setDevice(Object device) {
            this.device = device;
        }

        public long getDisposeTime() {
            return disposeTime;
        }

        public void setDisposeTime(long disposeTime) {
            this.disposeTime = disposeTime;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public int getExrate() {
            return exrate;
        }

        public void setExrate(int exrate) {
            this.exrate = exrate;
        }

        public long getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(long finishTime) {
            this.finishTime = finishTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdStr() {
            return idStr;
        }

        public void setIdStr(String idStr) {
            this.idStr = idStr;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getPayNumber() {
            return payNumber;
        }

        public void setPayNumber(String payNumber) {
            this.payNumber = payNumber;
        }

        public String getPayOrder() {
            return payOrder;
        }

        public void setPayOrder(String payOrder) {
            this.payOrder = payOrder;
        }

        public String getPayOrderTx() {
            return payOrderTx;
        }

        public void setPayOrderTx(String payOrderTx) {
            this.payOrderTx = payOrderTx;
        }

        public Object getRealname() {
            return realname;
        }

        public void setRealname(Object realname) {
            this.realname = realname;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSrcCurrency() {
            return srcCurrency;
        }

        public void setSrcCurrency(String srcCurrency) {
            this.srcCurrency = srcCurrency;
        }

        public int getSrcMoney() {
            return srcMoney;
        }

        public void setSrcMoney(int srcMoney) {
            this.srcMoney = srcMoney;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(Object statusDesc) {
            this.statusDesc = statusDesc;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeDesc() {
            return typeDesc;
        }

        public void setTypeDesc(String typeDesc) {
            this.typeDesc = typeDesc;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public boolean isWhite() {
            return white;
        }

        public void setWhite(boolean white) {
            this.white = white;
        }
    }
}
