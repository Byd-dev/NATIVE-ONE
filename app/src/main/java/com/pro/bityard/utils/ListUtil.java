package com.pro.bityard.utils;

import java.util.List;

public class ListUtil {

    public static boolean isNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isEmpty(List list) {
        return !isNotEmpty(list);
    }

    public static int count(List list) {
        return list == null ? 0 : list.size();
    }

    public static boolean hasValidVal(List list) {
        if(isEmpty(list)) return false;
        for (Object obj : list) {
            if(obj != null) {
                return true;
            }
        }
        return false;
    }
}
