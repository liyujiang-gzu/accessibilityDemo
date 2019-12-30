package com.auto.assist.accessibility.selector;

/**
 * Created by liyujiang on 2019/12/30.
 */
public class ClickDescNode extends ClickNode {
    private String desc;

    public ClickDescNode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
