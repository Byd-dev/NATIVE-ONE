package com.pro.bityard.entity;

public class QuoteEntity {


    /**
     * success : true
     * data : NG1907,-1,2.581,2.592;CL1907,1,58.60,57.91;GC1906,-1,1283.2,1285.4;SI1907,-1,14.560,14.613;HG1907,1,2.6920,2.6805
     */

    private boolean success;
    private String cmid;
    private String data;


    @Override
    public String toString() {
        return "QuoteEntity{" +
                "success=" + success +
                ", cmid='" + cmid + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
