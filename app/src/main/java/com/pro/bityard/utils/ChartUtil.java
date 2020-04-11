package com.pro.bityard.utils;


import com.pro.bityard.chart.KData;
import com.pro.bityard.entity.KlineEntity;
import com.pro.bityard.entity.QuoteChartEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.pro.bityard.utils.Util.parseServerTime;

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

    /*判断当前时间是在同一周*/
    public static boolean isSameWeekWithToday(Long time, Long time2) {

        String s = Util.stampToDate(time);
        Date date = parseServerTime(s);

        String s1 = Util.stampToDate(time2);
        Date date1 = parseServerTime(s1);
        if (date == null) {
            return false;
        }
        // 0.先把Date类型的对象转换Calendar类型的对象
        Calendar todayCal = Calendar.getInstance();
        Calendar dateCal = Calendar.getInstance();

        todayCal.setTime(date1);
        dateCal.setTime(date);

        if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)
                && todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)) {
            return true;
        } else {
            return false;
        }
    }

}
