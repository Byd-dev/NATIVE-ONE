package com.pro.bityard.api;

import com.geetest.sdk.GT3ErrorBean;

public interface OnGtUtilResult {


    void onApi1Result(String result);

    void onSuccessResult(String result);

    void onFailedResult(GT3ErrorBean gt3ErrorBean);


}
