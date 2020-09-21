package com.pro.bityard.entity;

import java.io.Serializable;

public class TagEntity implements Serializable {
    public  boolean isChecked;

    public String content;

    public String code;


    public String type;


    @Override
    public String toString() {
        return "TagEntity{" +
                "isChecked=" + isChecked +
                ", content='" + content + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public TagEntity(boolean isChecked, String content, String code, String type) {
        this.isChecked = isChecked;
        this.content = content;
        this.code = code;
        this.type = type;
    }




    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
