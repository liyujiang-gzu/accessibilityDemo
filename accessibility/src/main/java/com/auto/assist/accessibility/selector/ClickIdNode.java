package com.auto.assist.accessibility.selector;

/**
 * Created by liyujiang on 2019/12/30.
 */
public class ClickIdNode extends ClickNode {
    private String id;

    public ClickIdNode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
