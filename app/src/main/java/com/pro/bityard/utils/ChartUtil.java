package com.pro.bityard.utils;


import com.pro.bityard.chart.KData;
import com.pro.bityard.entity.KlineEntity;
import com.pro.bityard.entity.QuoteChartEntity;

import java.util.ArrayList;
import java.util.List;

public class ChartUtil {
    private static String TAG = "ChartUtil";


    /*获取当前行情的ContCode*/
    public static List<KData> klineList(QuoteChartEntity data) {
        if (data == null) {
            return null;
        }
        List<KlineEntity> klineEntityList;
        List<Long> t = data.getT();
        List<Double> c = data.getC();
        List<Double> op = data.getO();
        List<Double> h = data.getH();
        List<Double> l = data.getL();
        List<Double> v = data.getV();
        klineEntityList = new ArrayList<>();
        for (int i = 0; i < t.size(); i++) {
            klineEntityList.add(new KlineEntity(t.get(i), op.get(i), c.get(i), h.get(i), l.get(i), v.get(i)));
        }
        List<KData> dataList = new ArrayList<>();
        long start;
        double openPrice;
        double closePrice;
        double maxPrice;
        double minPrice;
        double volume;

        for (int i = 0; i < klineEntityList.size(); i++) {
            start = klineEntityList.get(i).getTime() * 1000;
            openPrice = klineEntityList.get(i).getOpenPrice();
            closePrice = klineEntityList.get(i).getClosePrice();
            maxPrice = klineEntityList.get(i).getHighPrice();
            minPrice = klineEntityList.get(i).getLowPrice();
            volume = klineEntityList.get(i).getVolume();
            dataList.add(new KData(start, openPrice, closePrice, maxPrice, minPrice, volume));

        }


        return dataList;

    }


}
