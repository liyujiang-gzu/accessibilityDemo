package com.mmo.accessibilitydemo.script;

import android.content.ComponentName;
import android.content.Intent;

import androidx.annotation.WorkerThread;

import com.auto.assist.accessibility.api.UiApi;
import com.auto.assist.accessibility.selector.ActionSelector;
import com.auto.assist.accessibility.selector.ClickTextNode;
import com.auto.assist.accessibility.selector.ConditionNode;
import com.auto.assist.accessibility.selector.NodeSelector;
import com.auto.assist.accessibility.util.LogUtil;
import com.mmo.accessibilitydemo.UiApplication;

import java.util.Arrays;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/23
 *     desc  : 前往 设置->移动网络界面 脚本
 * </pre>
 */
public class ToNetPageScript {

    @WorkerThread
    public static void doWork() {
        startWeChat();
        doAction();
    }

    private static void startWeChat() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UiApplication.context.startActivity(intent);
    }

    private static boolean isConversationPage() {
        NodeSelector nodeSelector = new NodeSelector();
        nodeSelector.setMaxMustMills(5000);
        nodeSelector.setMaxOptionMills(5000);
        ConditionNode must = new ConditionNode();
        must.setText(Arrays.asList("设置"));
        nodeSelector.setMust(must);
        ConditionNode option = new ConditionNode();
        option.setText(Arrays.asList("更多", "双卡和网络", "网络和互联网"));
        nodeSelector.setOption(option);
        return UiApi.isMyNeedPage(nodeSelector);
    }

    private static void doAction() {
        if (isConversationPage()) {
            LogUtil.debug("已在微信回话列表界面");
            if (toNetPage()) {
                LogUtil.debug("前往移动网络页面成功");
            } else {
                LogUtil.error("前往移动网络页面异常,暂停操作");
            }
        } else {
            LogUtil.error("当前不在设置界面,暂停操作");
        }
    }

    private static boolean toNetPage() {
        ActionSelector actionSelector = new ActionSelector();
        actionSelector.setMaxWClickMSec(1000);
        actionSelector.setClick(new ClickTextNode("双卡和网络"));
        NodeSelector nodeSelector = new NodeSelector();
        nodeSelector.setMaxMustMills(5000);
        nodeSelector.setMaxOptionMills(5000);
        ConditionNode must = new ConditionNode();
        must.setText(Arrays.asList("双卡和网络"));
        nodeSelector.setMust(must);
        actionSelector.setPage(nodeSelector);
        return UiApi.jumpToNeedPage(actionSelector);
    }

}
