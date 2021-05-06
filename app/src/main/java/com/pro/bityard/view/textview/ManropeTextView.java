package com.pro.bityard.view.textview;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import skin.support.widget.SkinCompatTextView;

public class ManropeTextView extends SkinCompatTextView {


    public ManropeTextView(@NonNull Context context) {
        super(context);
    }

    public ManropeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ManropeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "Manrope_medium.otf");
        super.setTypeface(tf, style);

    }
}
