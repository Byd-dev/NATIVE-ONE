package com.pro.bityard.api;

import android.view.View;

public interface PopQuotesResult {

    void  setOptionalResult(Integer optionalPosition);
    void  setClickListenerResult(String clickListener);
    void  setTabSelectResult(Integer tabSelect);
    void  setRefreshResult();
    void  setCancelResult(String type);
    void  setPopClickResult(String data);
    void   setPopView(View popView);

}
