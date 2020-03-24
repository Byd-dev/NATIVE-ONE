package com.pro.bityard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;

import com.pro.bityard.R;

import androidx.appcompat.widget.AppCompatEditText;

public class DecimalEditText extends AppCompatEditText {


    /**
     * 保留小数点前多少位，默认三位，既到千位
     */
    private int mDecimalStarNumber = 20;

    private double max=75.0;

    private double min=0.75;

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    /**
     * 保留小数点后多少位，默认两位
     */
    private int mDecimalEndNumber = 2 ;

    public DecimalEditText(Context context) {
        this(context, null);
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public DecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DecimalEditText);

        mDecimalStarNumber = typedArray.getInt(R.styleable.DecimalEditText_decimalStarNumber, mDecimalStarNumber);
        mDecimalEndNumber = typedArray.getInt(R.styleable.DecimalEditText_decimalEndNumber, mDecimalEndNumber);
        max=typedArray.getFloat(R.styleable.DecimalEditText_max,0);
        min=typedArray.getFloat(R.styleable.DecimalEditText_min,0);
        typedArray.recycle();
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String lastInputContent = dest.toString();

                Log.d("EditUtil", "source->" + source + "--start->" + start + " " +
                        "--lastInputContent->" + lastInputContent + "--dstart->" + dstart + "--dend->" + dend);


                if (source.equals(".") && lastInputContent.length() == 0) {
                    return "0.";
                }

                if (!source.equals(".") && !source.equals("") && lastInputContent.equals("0")) {
                    return "";
                }

                if (source.equals(".") && lastInputContent.contains(".")) {
                    return "";
                }



                if (lastInputContent.contains(".")) {
                    int index = lastInputContent.indexOf(".");
                    if (dend - index >= mDecimalEndNumber + 1) {
                        return "";
                    }
                } else {
                    if (!source.equals(".") && lastInputContent.length() >= mDecimalStarNumber) {
                        return "";
                    }
                }






                return null;
            }
        }});
    }


    public int getDecimalStarNumber() {
        return mDecimalStarNumber;
    }

    public void setDecimalStarNumber(int decimalStarNumber) {
        mDecimalStarNumber = decimalStarNumber;
    }

    public int getDecimalEndNumber() {
        return mDecimalEndNumber;
    }

    public void setDecimalEndNumber(int decimalEndNumber) {
        mDecimalEndNumber = decimalEndNumber;
    }
}

