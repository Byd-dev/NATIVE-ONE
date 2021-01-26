package com.pro.bityard.entity;

import java.io.Serializable;
import java.util.List;

public class FollowEntity implements Serializable{


    /**
     * total : 21
     * code : 200
     * data : [{"avatar":"","betDays":62,"brand":"BYD","currency":"USDT","follower":4,"idearTag":"idear_3","idearTags":["团队在做事,不要看价格,拿住!"],"registerTime":1594265436000,"selectedRegion":null,"sex":0,"styleTag":"risk_low,risk_high,pos_light","styleTags":["稳健","高风险","轻仓"],"trader30DaysCost":385,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0.258,"trader30DaysIncome":39.182153,"trader30DaysRate":0.10177183,"type":2,"userId":"487947864136630272","username":"带单人02"},{"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200828154005_432.jpg?Expires=1913960405&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=tAE0%2FeJ1wgO1lpltq1%2FQxP4qCV8%3D","betDays":161,"brand":"BYD","currency":"USDT","follower":3,"idearTag":"idear_5","idearTags":["一币一嫩模,一币一别墅"],"registerTime":1585717772000,"selectedRegion":null,"sex":0,"styleTag":"term_short","styleTags":["短线"],"trader30DaysCost":0,"trader30DaysCount":2,"trader30DaysDefeat":12,"trader30DaysDraw":38.7795,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"452096359483310080","username":"Apple"},{"avatar":"http://sfdev.oss-cn-hangzhou.aliyuncs.com/BYD20200814191510_177.png?Expires=1912763710&OSSAccessKeyId=LTAIbLR0iiwt3DiP&Signature=BceF%2FPW%2BsBcbO6V3p6nWaCBUdgc%3D","betDays":244,"brand":"BYD","currency":"USDT","follower":3,"idearTag":"idear_1","idearTags":["一切有为法,如梦亦如露"],"registerTime":1578574378000,"selectedRegion":null,"sex":1,"styleTag":"term_long,pos_heavy,term_short","styleTags":["长线","重仓","短线"],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"422134795078729728","username":"投资鬼才"},{"avatar":"","betDays":120,"brand":"BYD","currency":"USDT","follower":1,"idearTag":null,"idearTags":[],"registerTime":1589263461000,"selectedRegion":null,"sex":1,"styleTag":null,"styleTags":[],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"466968059912683520","username":"wasim测试11"},{"avatar":"","betDays":118,"brand":"BYD","currency":"USDT","follower":0,"idearTag":"idear_7","idearTags":["耐得住寂寞,才能守得住芳华!"],"registerTime":1589423012000,"selectedRegion":null,"sex":0,"styleTag":"risk_high","styleTags":["高风险"],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"467637263133343744","username":"跟单人A01废弃"},{"avatar":"","betDays":117,"brand":"BYD","currency":"USDT","follower":0,"idearTag":null,"idearTags":[],"registerTime":1589543214000,"selectedRegion":null,"sex":0,"styleTag":null,"styleTags":[],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"468141429110489088","username":"prayer测试头头"},{"avatar":"","betDays":113,"brand":"BYD","currency":"USDT","follower":0,"idearTag":null,"idearTags":[],"registerTime":1589854479000,"selectedRegion":null,"sex":0,"styleTag":null,"styleTags":[],"trader30DaysCost":0,"trader30DaysCount":0,"trader30DaysDefeat":0,"trader30DaysDraw":0,"trader30DaysIncome":0,"trader30DaysRate":0,"type":2,"userId":"469446966125559808","username":"日工资1号下级8"}]
     */

    private int total;
    private int code;
    private int size;
    private int page;
    private String message;


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

    @Override
    public String toString() {
        return "FollowEntity{" +
                "total=" + total +
                ", code=" + code +
                ", size=" + size +
                ", page=" + page +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    private List<FollowerDetailEntity.DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<FollowerDetailEntity.DataBean> getData() {
        return data;
    }

    public void setData(List<FollowerDetailEntity.DataBean> data) {
        this.data = data;
    }


}
