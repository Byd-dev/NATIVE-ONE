package com.pro.bityard.utils;

import com.pro.bityard.config.AppConfig;
import com.pro.bityard.manger.NetIncomeManger;
import com.pro.bityard.manger.WebSocketManager;
import com.pro.switchlibrary.SPUtils;

public class SocketUtil {

    private SocketUtil() {

    }

    private static SocketUtil socketUtil = null;

    public static SocketUtil getInstance() {
        if (socketUtil == null) {
            synchronized (NetIncomeManger.class) {
                if (socketUtil == null) {
                    socketUtil = new SocketUtil();
                }
            }

        }
        return socketUtil;

    }


    public static void switchQuotesList(String cmidId){
        String quote_code_list = SPUtils.getString(AppConfig.QUOTE_CODE, null);
        switch (cmidId){
            case "3001":
                WebSocketManager.getInstance().sendQuotes("3001", quote_code_list, null);
                break;
            case "3002":
                WebSocketManager.getInstance().cancelQuotes("3002", quote_code_list);
                break;
        }
    }


}
