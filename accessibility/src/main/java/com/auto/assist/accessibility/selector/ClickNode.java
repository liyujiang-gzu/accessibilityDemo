package com.auto.assist.accessibility.selector;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by liyujiang on 2019/12/30.
 */
public class ClickNode implements Serializable {

    @NonNull
    public String toJson() {
        return new Gson().toJson(this);
    }

    @NonNull
    @Override
    public String toString() {
        return toJson();
    }

}
