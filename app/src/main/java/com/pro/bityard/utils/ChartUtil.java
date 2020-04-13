package com.pro.bityard.utils;


import com.pro.bityard.chart.KData;
import com.pro.bityard.entity.KlineEntity;
import com.pro.bityard.entity.QuoteChartEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.pro.bityard.utils.Util.parseServerTime;

public class ChartUtil {
    private static String TAG = "ChartUtil";
    private static double volume;


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

    /*判断当前时间是在同一区间*/
    public static boolean isSameTime(List<KData> historyList, List<KData> refreshList, int times) {


        long oldTime = historyList.get(historyList.size() - 1).getTime();
        long nowTime = refreshList.get(refreshList.size() - 1).getTime();
        String s = Util.stampToDate(oldTime);
        Date date = parseServerTime(s);
        String s1 = Util.stampToDate(nowTime);
        Date date1 = parseServerTime(s1);
        if (date == null) {
            return false;
        }
        // 0.先把Date类型的对象转换Calendar类型的对象
        Calendar todayCal = Calendar.getInstance();
        todayCal.setFirstDayOfWeek(Calendar.MONDAY);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setFirstDayOfWeek(Calendar.MONDAY);
        todayCal.setTime(date1);
        dateCal.setTime(date);
        // nowTime-oldTime<=times*60*1000;
        if (todayCal.get(Calendar.MINUTE) == dateCal.get(Calendar.MINUTE)
                && todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)) {
            return true;
        } else {
            return false;
        }

    }

    /*5分 获取当时时间戳的分钟 除5求余 比如7/5 余2 那就是把当前最新的数据设置成2分钟前的时间戳*/
    public static long setTime(List<KData> refreshList, int times) {
        long time;
        long nowTime = refreshList.get(refreshList.size() - 1).getTime();
        Date date = parseServerTime(Util.stampToDate(nowTime));
        Calendar todayCal = Calendar.getInstance();
        todayCal.setFirstDayOfWeek(Calendar.MONDAY);
        todayCal.setTime(date);
        int a = todayCal.get(Calendar.MINUTE) % times;
        if (a == 0) {
            time = nowTime;

        } else {
            todayCal.add(Calendar.MINUTE, -a);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = Util.dateToStampLong(sdf.format(todayCal.getTimeInMillis()));
        }
        return time;
    }

    /*判断当前时间是在同一周*/
    public static boolean isSameWeek(Long time, Long time2) {
        String s = Util.stampToDate(time);
        Date date = parseServerTime(s);
        String s1 = Util.stampToDate(time2);
        Date date1 = parseServerTime(s1);
        if (date == null) {
            return false;
        }
        // 0.先把Date类型的对象转换Calendar类型的对象
        Calendar todayCal = Calendar.getInstance();
        todayCal.setFirstDayOfWeek(Calendar.MONDAY);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setFirstDayOfWeek(Calendar.MONDAY);
        todayCal.setTime(date1);
        dateCal.setTime(date);

        if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)
                && todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)) {
            return true;
        } else {
            return false;
        }
    }

    public static long getTimeNow() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis();
        return today;
    }

    public static String getWeekDay() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        String Week = "";
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "日";
        }
        if (wek == 2) {
            Week += "一";
        }
        if (wek == 3) {
            Week += "二";
        }
        if (wek == 4) {
            Week += "三";
        }
        if (wek == 5) {
            Week += "四";
        }
        if (wek == 6) {
            Week += "五";
        }
        if (wek == 7) {
            Week += "六";
        }
        return Week;

    }

    /*先获取当前时间 判断是星期几 然后 获取当前一周的数据 */
    /*同一周的数据 放在一起*/
    public static List<KData> getWeekData(List<KData> kData1WeekHistory) {
        List<KData> kDataList;
        if (kData1WeekHistory == null) {
            return null;
        }
        kDataList = new ArrayList<>();
        double maxPrice = kData1WeekHistory.get(kData1WeekHistory.size() - 1).getMaxPrice();
        double minPrice = kData1WeekHistory.get(kData1WeekHistory.size() - 1).getMinPrice();

        KData kData;
        for (int i = 0; i < kData1WeekHistory.size(); i++) {
            if (isSameWeek(getTimeNow(), kData1WeekHistory.get(i).getTime())) {
                kDataList.add(kData1WeekHistory.get(i));
            }
        }
        for (int i = 0; i < kDataList.size(); i++) {
            if (kDataList.get(i).getMaxPrice() > maxPrice) {
                maxPrice = kDataList.get(i).getMaxPrice();
            }
            if (kDataList.get(i).getMinPrice() < minPrice) {
                minPrice = kDataList.get(i).getMinPrice();
            }
            volume = +kDataList.get(i).getVolume();
        }

        kData = new KData(kDataList.get(0).getTime(), kDataList.get(0).getOpenPrice(),
                kDataList.get(kDataList.size() - 1).getClosePrice(), maxPrice, minPrice, volume);
        kDataList.add(kData);
        return kDataList;

    }


    public static List<KData> getWeekOneData(List<KData> kData1WeekHistory) {
        List<KData> kDataList;
        if (kData1WeekHistory == null) {
            return null;
        } else {
            kDataList = new ArrayList<>();
            double maxPrice = kData1WeekHistory.get(kData1WeekHistory.size() - 1).getMaxPrice();
            double minPrice = kData1WeekHistory.get(kData1WeekHistory.size() - 1).getMinPrice();

            KData kData;

            for (int i = 0; i < kData1WeekHistory.size(); i++) {
                if (kData1WeekHistory.get(i).getMaxPrice() > maxPrice) {
                    maxPrice = kData1WeekHistory.get(i).getMaxPrice();
                }
                if (kData1WeekHistory.get(i).getMinPrice() < minPrice) {
                    minPrice = kData1WeekHistory.get(i).getMinPrice();
                }
                volume = +kData1WeekHistory.get(i).getVolume();
            }

            kData = new KData(kData1WeekHistory.get(0).getTime(), kData1WeekHistory.get(0).getOpenPrice(),
                    kData1WeekHistory.get(kData1WeekHistory.size() - 1).getClosePrice(), maxPrice, minPrice, volume);
            kDataList.add(kData);
            return kDataList;
        }
    }

    /*加法*/
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /*减法*/
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /*乘法*/
    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.multiply(b2).doubleValue();
    }

    /*除法*/
    public static double div(double d1, double d2, int len) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
