package com.mmo.accessibilitydemo;

import android.content.Intent;

import androidx.annotation.WorkerThread;

import com.auto.assist.accessibility.api.AcessibilityApi;
import com.auto.assist.accessibility.api.UiApi;

/**
 * 辅助功能自动化执行脚本
 */
public class TestAutoExecScript {

    @WorkerThread
    public static void doWork() {
        startWeChat();
        doAction();
    }

    private static void startWeChat() {
        Intent intent = UiApplication.context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        if (intent == null) {
            return;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        UiApplication.context.startActivity(intent);
    }

    @WorkerThread
    private static void doAction() {
        //UiApi.clickNodeByIdWithTimeOut(5000, "com.tencent.mm:id/baj");
        if (!UiApi.clickNodeByDescWithTimeOut(5000, "搜索")) {
            return;
        }
        if (UiApi.findNodeByTextWithTimeOut(3000, "搜索指定内容") == null) {
            return;
        }
        AcessibilityApi.closeKeyBoard();
        UiApi.sleepTime(500);
        UiApi.findEditTextByActionAndInput(5000, "穿青人");
    }

}
