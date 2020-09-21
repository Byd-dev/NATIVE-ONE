package com.pro.bityard.entity;

import java.io.Serializable;

public class TagEntity implements Serializable {
    public  boolean isChecked;

    public String content;

    public String type;

    @Override
    public String toString() {
        return "TagEntity{" +
                "isChecked=" + isChecked +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public TagEntity(boolean isChecked, String content, String type) {
        this.isChecked = isChecked;
        this.content = content;
        this.type = type;
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
