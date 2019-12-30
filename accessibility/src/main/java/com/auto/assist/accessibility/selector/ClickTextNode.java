package com.auto.assist.accessibility.selector;

/**
 * Created by liyujiang on 2019/12/30.
 */
public class ClickTextNode extends ClickNode {
    private String text;

    public ClickTextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
