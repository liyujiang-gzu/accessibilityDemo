package com.auto.assist.accessibility.selector;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyujiang on 2019/12/30.
 */
public class ConditionNode implements Serializable {
    private List<String> text;
    private List<String> id;
    private List<String> desc;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return toJson();
    }
}
