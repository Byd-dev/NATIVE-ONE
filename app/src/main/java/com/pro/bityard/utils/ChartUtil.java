package com.pro.bityard.utils;


import android.util.ArrayMap;
import android.util.Log;

import com.pro.bityard.chart.KData;
import com.pro.bityard.entity.KlineEntity;
import com.pro.bityard.entity.QuoteChartEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.pro.bityard.utils.Util.parseServerTime;

public class ChartUtil {
    private static String TAG = "ChartUtil";
    private static double volume;


    /*获取当前行情的ContCode*/
    public static List<KData> klineList(QuoteChartEntity data) {
        if (data == null) {
            return null;
        } else {


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

            return agoToNow(dataList);
        }
    }


    /*5分 获取当时时间戳的分钟 除5求余 比如7/5 余2 那就是把当前最新的数据设置成2分钟前的时间戳*/
    public static long setMinTime(List<KData> refreshList, int times) {
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

    /*5分 获取当时时间戳的分钟 除5求余 比如7/5 余2 那就是把当前最新的数据设置成2分钟前的时间戳*/
    public static long setWeekTime(List<KData> refreshList) {
        long time;
        long nowTime = refreshList.get(refreshList.size() - 1).getTime();
        Date date = parseServerTime(Util.stampToDate(nowTime));
        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(date);
        todayCal.setFirstDayOfWeek(Calendar.MONDAY);

        Integer i = getWeekCount(nowTime);
        int a = i % 7;
        if (a == 0) {
            time = nowTime;
        } else {
            todayCal.add(Calendar.DAY_OF_WEEK, -(a - 1));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = Util.dateToStampLong(sdf.format(todayCal.getTimeInMillis()));
        }
        return time;
    }

    public static long setMonthTime(List<KData> refreshList) {
        long time;
        long nowTime = refreshList.get(refreshList.size() - 1).getTime();

        Date date = parseServerTime(Util.stampToDate(nowTime));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String format2 = sdf2.format(calendar.getTimeInMillis());
        String[] split = format2.split("-");

        int dayOfMonth = getDayOfMonth(Integer.parseInt(split[0]), Integer.parseInt(split[1]));

        int a = Integer.parseInt(split[2]) % dayOfMonth;

        if (a == 0) {
            time = nowTime;
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, -(a - 1));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = Util.dateToStampLong(sdf.format(calendar.getTimeInMillis()));
        }
        return time;
    }


    public static long getTimeNow() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.add(Calendar.DAY_OF_WEEK, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis();
        return today;
    }

    public static String getWeekDay(Long time) {
        //获取当前的毫秒值
        Date date = new Date(time);
        String Week = "";
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
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

    public static long getTodayZero() {
        Date date = new Date();
        long l = 24*60*60*1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
    }

    public static String getDate(Long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        return format.format(d1);

    }


    public static Integer getMonthDay(String time) {
        //获取当前的毫秒值
        Date date = new Date(Long.parseLong(time));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayInMonth = c.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "getMonthDay:239:  " + dayInMonth);
        return dayInMonth;
    }


    public static Integer getWeekCount(Long time) {
        //获取当前的毫秒值
        Date date = new Date(time);
        int Week = 0;
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);

        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week = 7;
        }
        if (wek == 2) {
            Week = 1;
        }
        if (wek == 3) {
            Week = 2;
        }
        if (wek == 4) {
            Week = 3;
        }
        if (wek == 5) {
            Week = 4;
        }
        if (wek == 6) {
            Week = 5;
        }
        if (wek == 7) {
            Week = 6;
        }
        return Week;

    }

    /*获取时间范围的列表*/
    public static List<String> getWeekRange(List<KData> kDataList) {
        List<String> longList;
        if (kDataList == null) {
            return null;
        } else {
            longList = new ArrayList<>();
            for (KData data : kDataList) {
                if (getWeekDay(data.getTime()).equals("一")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(data.getTime());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    calendar.add(Calendar.DAY_OF_WEEK, 0);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    String format = sdf.format(calendar.getTimeInMillis());
                    Long aLong = Util.dateToStampLong(format);
                    Long aLong2 = aLong + 7 * 24 * 60 * 60 * 1000;
                    longList.add(aLong + "-" + aLong2);
                }
            }
            return longList;
        }


    }

    /*获取月时间范围的列表*/
    public static List<String> getMonthRange(List<KData> kDataList) {
        List<String> longList;
        if (kDataList == null) {
            return null;
        } else {
            longList = new ArrayList<>();
            for (KData data : kDataList) {
                if (getMonthDay(String.valueOf(data.getTime())) == 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(data.getTime());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DAY_OF_WEEK, 0);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    String format = sdf.format(calendar.getTimeInMillis());
                    Long aLong = Util.dateToStampLong(format);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
                    String format2 = sdf2.format(calendar.getTimeInMillis());
                    String[] split = format2.split("-");

                    calendar.add(Calendar.DAY_OF_MONTH, getDayOfMonth(Integer.parseInt(split[0]), Integer.parseInt(split[1])));

                    String format1 = sdf.format(calendar.getTimeInMillis());
                    Long aLong1 = Util.dateToStampLong(format1);

                    longList.add(aLong + "-" + aLong1);
                }
            }
            return longList;
        }


    }


    private static int getDayOfMonth(int year, int month) {
        int day;
        if (isLeapYear(year)) {
            day = 29;
        } else {
            day = 28;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return day;

        }

        return 0;
    }

    private static boolean isLeapYear(int year) {

        if (year > 0) {
            if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static ArrayMap<Object, KData> getWeekKDataList(List<KData> kDataList) {
        List<KData> kDataList1;
        ArrayMap<Object, KData> arrayMap;
        if (kDataList == null) {
            return null;
        } else {
            List<String> timeRange = getWeekRange(kDataList);
            arrayMap = new ArrayMap<>();
            for (String dataStr : timeRange) {
                String[] split = dataStr.split("-");
                kDataList1 = new ArrayList<>();
                for (KData data : kDataList) {
                    if (data.getTime() >= Long.parseLong(split[0]) && data.getTime() < Long.parseLong(split[1])) {
                        kDataList1.add(data);
                        KData weekOneData = getOneData(kDataList1);
                        arrayMap.put(Long.parseLong(split[0]), weekOneData);
                    }
                }
            }
            return arrayMap;
        }
    }


    public static ArrayMap<Object, KData> getMonthKDataList(List<KData> kDataList) {
        List<KData> kDataList1;
        ArrayMap<Object, KData> arrayMap;
        if (kDataList == null) {
            return null;
        } else {
            List<String> timeRange = getMonthRange(kDataList);
            arrayMap = new ArrayMap<>();
            for (String dataStr : timeRange) {
                String[] split = dataStr.split("-");
                kDataList1 = new ArrayList<>();
                for (KData data : kDataList) {
                    if (data.getTime() >= Long.parseLong(split[0]) && data.getTime() < Long.parseLong(split[1])) {
                        kDataList1.add(data);
                        KData weekOneData = getOneData(kDataList1);
                        arrayMap.put(Long.parseLong(split[0]), weekOneData);
                    }
                }
            }
            return arrayMap;
        }
    }

    /*获得列表*/
    public static List<KData> KlineWeekData(ArrayMap<Object, KData> arrayMap) {
        List<KData> list;
        Set<Map.Entry<Object, KData>> entries = arrayMap.entrySet();
        Iterator<Map.Entry<Object, KData>> iterator = entries.iterator();
        list = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Object, KData> next = iterator.next();
            KData value = next.getValue();
            list.add(value);
        }
        List<KData> list1 = ChartUtil.agoToNow(list);
        return list1;

    }

    /*获得列表*/
    public static List<KData> KlineMonthData(ArrayMap<Object, KData> arrayMap) {
        List<KData> list;
        Set<Map.Entry<Object, KData>> entries = arrayMap.entrySet();
        Iterator<Map.Entry<Object, KData>> iterator = entries.iterator();
        list = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<Object, KData> next = iterator.next();
            KData value = next.getValue();
            list.add(value);
        }
        List<KData> list1 = ChartUtil.agoToNow(list);
        return list1;

    }

    /*排序时间从故去到现在*/
    public static List<KData> agoToNow(List<KData> kDataList) {
        List<KData> kDataList1 = new ArrayList<>();
        Collections.sort(kDataList, (o1, o2) -> {
            long time = o1.getTime();
            long time1 = o2.getTime();
            double sub = time - time1;
            if (sub == 0) {
                return (int) (time1 - time);
            }
            return (int) sub;
        });
        for (KData kData : kDataList) {
            kDataList1.add(kData);
        }

        return kDataList1;
    }


    public static KData getOneData(List<KData> kData1WeekHistory) {

        if (kData1WeekHistory == null) {
            return null;
        } else {

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
            return kData;
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
