package com.pro.bityard.entity;

import android.graphics.drawable.Drawable;

import com.stx.xhb.xbanner.entity.SimpleBannerInfo;

public class GuideEntity extends SimpleBannerInfo {
    private String textLeft;
    private String textRight;
    private String textBottom;
    private Drawable drawable;

    public GuideEntity(String textLeft, String textRight, String textBottom, Drawable drawable) {
        this.textLeft = textLeft;
        this.textRight = textRight;
        this.textBottom = textBottom;
        this.drawable = drawable;
    }

    public String getTextLeft() {
        return textLeft;
    }

    public void setTextLeft(String textLeft) {
        this.textLeft = textLeft;
    }

    public String getTextRight() {
        return textRight;
    }

    public void setTextRight(String textRight) {
        this.textRight = textRight;
    }

    public String getTextBottom() {
        return textBottom;
    }

    public void setTextBottom(String textBottom) {
        this.textBottom = textBottom;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public Object getXBannerUrl() {
        return drawable;
    }
}
