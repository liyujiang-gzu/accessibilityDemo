package com.mmo.accessibilitydemo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.WorkerThread;

import com.auto.assist.accessibility.api.AcessibilityApi;
import com.auto.assist.accessibility.api.UiApi;
import com.auto.assist.accessibility.selector.ConditionNode;
import com.auto.assist.accessibility.selector.NodeSelector;
import com.auto.assist.accessibility.util.GestureUtil;
import com.auto.assist.accessibility.util.LogUtil;

import java.util.Arrays;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/23
 *     desc  : 前往 设置->移动网络界面 脚本
 * </pre>
 */
public class TestAutoExecScript {

    @WorkerThread
    public static void doWork() {
        startWeChat();
        doAction();
    }

    private static void startWeChat() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UiApplication.context.startActivity(intent);
    }

    private static boolean isExcerptPage() {
        NodeSelector nodeSelector = new NodeSelector();
        nodeSelector.setMaxMustMills(5000);
        nodeSelector.setMaxOptionMills(5000);
        ConditionNode must = new ConditionNode();
        must.setText(Arrays.asList("微信"));
        nodeSelector.setMust(must);
        return UiApi.isMyNeedPage(nodeSelector);
    }

    private static void doAction() {
        if (isExcerptPage()) {
            LogUtil.debug("已在微信主界面");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                GestureUtil.dispatchGestureClick(AcessibilityApi.getAccessibilityService(), 100, 200, 100);
            }
        } else {
            LogUtil.error("当前不在微信主界面,暂停操作");
        }
    }

}
