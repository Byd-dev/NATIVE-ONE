package com.pro.bityard.utils;

import android.util.Log;

import com.pro.bityard.api.TradeResult;
import com.pro.bityard.entity.OpenPositionEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TradeUtil {
    private static String TAG = "TradeUtil";

    public static double scale = 0.01;


    /*保留两位小数*/
    public static String getNumberFormat(double value, int scale) {
        BigDecimal bd = new BigDecimal(value);
        String mon = bd.setScale(scale, RoundingMode.DOWN).toString();//保留两位数字，并且是截断不进行四舍五
        return mon;
    }

    /*long转时间*/
    public static String dateToStamp(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String numberHalfUp(double value, int scale) {
        BigDecimal bd = new BigDecimal(value);
        String mon = bd.setScale(scale, RoundingMode.HALF_UP).toString();//保留两位数字，四舍五入
        return mon;
    }

    /*止损价*/
    public static String StopLossPrice(boolean isBusy, double price, int priceDigit, double lever, double margin, double stopLoss) {
        String stopLossPrice;
        if (isBusy == true) {
            //买多 止损价  =  开仓价 - （开仓价 / 杠杆 * 止损比例） //因为比例是负数   公式是按的正数  所以刚好相反
            stopLossPrice = getNumberFormat(sub(price, mul(div(price, lever, 10), div(stopLoss, margin, 10))), priceDigit);
        } else {
            stopLossPrice = getNumberFormat(add(price, mul(div(price, lever, 10), div(stopLoss, margin, 10))), priceDigit);
            //买空 止损价  =  开仓价 +（开仓价 / 杠杆 * 止损比例）
        }
        return stopLossPrice;
    }

    /*止盈价*/
    public static String StopProfitPrice(boolean isBusy, double price, int priceDigit, double lever, double margin, double stopProfit) {
        String stopLossPrice;
        if (isBusy == true) {
            //买多 止盈价  =  开仓价 + （开仓价 / 杠杆 * 止损比例）
            stopLossPrice = getNumberFormat(add(price, mul(div(price, lever, 10), div(stopProfit, margin, 10))), priceDigit);
        } else {
            stopLossPrice = getNumberFormat(sub(price, mul(div(price, lever, 10), div(stopProfit, margin, 10))), priceDigit);
            //买空 止盈价  =  开仓价 -（开仓价 / 杠杆 * 止损比例）
        }
        return stopLossPrice;
    }


    /*订单盈亏*/
    public static String income(boolean isBuy, double price, double opPrice, double volume) {
        String income;

        if (isBuy) {
            income = getNumberFormat(mul(sub(price, opPrice), volume), 2);
        } else {
            income = getNumberFormat(mul(sub(opPrice, price), volume), 2);

        }

        return income;
    }

    /*净盈亏*/
    public static String netIncome(double income, double service) {
        String netIncome;
        netIncome = getNumberFormat((sub(income, service)), 2);
        return netIncome;
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

    /*价格*/
    public static void price(List<String> quoteList, String contractCode, TradeResult result) {
        if (quoteList != null) {
            for (String value : quoteList) {
                String[] split1 = value.split(",");
                if (contractCode.equals(split1[0])) {
                    result.setResult(split1[2]);
                }
            }
        }


    }


    /*全部盈亏*/
    public static void getIncome(List<String> quoteList, OpenPositionEntity openPositionEntity, TradeResult tradeResult) {
        List<Double> incomeList = new ArrayList<>();
        for (OpenPositionEntity.DataBean dataBean : openPositionEntity.getData()) {
            boolean isBuy = dataBean.isIsBuy();
            TradeUtil.price(quoteList, dataBean.getContractCode(), new TradeResult() {
                @Override
                public void setResult(Object response) {
                    Double income = Double.valueOf(TradeUtil.income(isBuy, Double.parseDouble(response.toString()), dataBean.getOpPrice(), dataBean.getVolume()));
                    incomeList.add(income);
                }
            });
        }
        if (incomeList.size() > 0) {
            double income = 0.0;
            for (int i = 0; i < incomeList.size(); i++) {
                income = TradeUtil.add(income, incomeList.get(i));
            }
            tradeResult.setResult(income);
        }
    }

    /*持仓的列表ID*/
    public static String positionIdList(OpenPositionEntity openPositionEntity) {
        String substring;
        StringBuilder stringBuilder = new StringBuilder();
        if (openPositionEntity.getData().size() > 0) {
            for (OpenPositionEntity.DataBean dataBean : openPositionEntity.getData()) {
                String id = dataBean.getId();
                stringBuilder.append(id + ",");
            }
            substring = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
            Log.d(TAG, "positionIdList: 167: " + substring);
        } else {
            substring = null;
        }

        return substring;
    }
    /*输入只能两位*/
    public static String inputTwoScale(String text) {
        Log.d(TAG, "inputTwoScale:175:  "+text);
        String content = null;
        if (text.contains(".")) {
            int index = text.indexOf(".");
            if (index + 3 < text.length()) {
                text = text.substring(0, index + 3);
                content = text;
            }
        }else {
            content=text;
        }
        Log.d(TAG, "inputTwoScale:185:  "+content);

        return content;

    }

    /*输入只能两位*/
    public static int getScaleLength(String text) {
        Log.d(TAG, "inputTwoScale:175:  "+text);
        int length=0;
        if (text.contains(".")) {
            int index = text.indexOf(".");
             length = text.substring(index, text.length()).length();
        }else {
            length=0;
        }
        Log.d(TAG, "inputTwoScale:185:  "+length);

        return length;

    }



}
