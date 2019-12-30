package com.auto.assist.accessibility.selector;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class ActionSelector {

    private NodeSelector page;

    private long maxWClickMSec = 0; //最大点击超时

    private ClickNode click;  //点击的内容

    public NodeSelector getPage() {
        return page;
    }

    public void setPage(NodeSelector page) {
        this.page = page;
    }

    public long getMaxWClickMSec() {
        return maxWClickMSec;
    }

    public void setMaxWClickMSec(long maxWClickMSec) {
        this.maxWClickMSec = maxWClickMSec;
    }

    public ClickNode getClick() {
        return click;
    }

    public void setClick(ClickNode click) {
        this.click = click;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @NonNull
    @Override
    public String toString() {
        return "ActionSelector{" +
                "page=" + page.toString() +
                ", maxWClickMSec=" + maxWClickMSec +
                ", click=" + click +
                '}';
    }

    public static ActionSelector fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, ActionSelector.class);
    }
}
