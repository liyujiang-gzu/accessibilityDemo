package com.auto.assist.accessibility.selector;

import com.google.gson.Gson;

public class NodeSelector {

    private long maxMustMills = 0;  //必要节点超时时间,毫秒
    private long maxOptionMills = 0; //可选节点超时时间,毫秒

    private ConditionNode must = null;  //必选节点,key为TEXT_KEY或ID_KEY中一种

    private ConditionNode option = null;  //可选节点


    public long getMaxMustMills() {
        return maxMustMills;
    }

    public void setMaxMustMills(long maxMustMills) {
        this.maxMustMills = maxMustMills;
    }

    public long getMaxOptionMills() {
        return maxOptionMills;
    }

    public void setMaxOptionMills(long maxOptionMills) {
        this.maxOptionMills = maxOptionMills;
    }

    public ConditionNode getMust() {
        return must;
    }

    public void setMust(ConditionNode must) {
        this.must = must;
    }

    public ConditionNode getOption() {
        return option;
    }

    public void setOption(ConditionNode option) {
        this.option = option;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "NodeSelector{" +
                "maxMustMills=" + maxMustMills +
                ", maxOptionMills=" + maxOptionMills +
                ", must=" + toStr(must) +
                ", option=" + toStr(must) +
                '}';
    }

    private String toStr(ConditionNode conditionNode) {
        String rlt = "";
        if (conditionNode != null) {
            rlt = conditionNode.toJson();
        }
        return rlt;

    }

    public static NodeSelector fromJson(String jsonStr) {
        return new Gson().fromJson(jsonStr, NodeSelector.class);
    }

}
