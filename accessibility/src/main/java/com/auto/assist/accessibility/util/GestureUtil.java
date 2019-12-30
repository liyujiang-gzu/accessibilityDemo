package com.auto.assist.accessibility.util;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * Adapted from https://github.com/amber281/WeChatLuckyMoney/.../GestureUtil.java
 * Created by liyujiang on 2019/12/30.
 */
public class GestureUtil {

    /**
     * 模拟手指触摸点击事件
     *
     * @param x           横坐标
     * @param y           纵坐标
     * @param millisecond 点击时间间隔
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void dispatchGestureClick(AccessibilityService service, float x, float y, int millisecond) {
        Path p = new Path();
        p.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0, millisecond));
        GestureDescription gesture = builder.build();
        service.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }
        }, null);
    }


    /**
     * 模拟点击返回
     *
     * @param service
     * @param resourceId 返回按钮的resoucesid
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void performWxBack(AccessibilityService service, String resourceId) {
        AccessibilityNodeInfo rootNode = service.getRootInActiveWindow();
        if (rootNode == null) {
            return;
        }
        List<AccessibilityNodeInfo> list = rootNode.findAccessibilityNodeInfosByViewId(resourceId);
        if (list != null && list.size() > 0) {//返回按钮存在，模拟点击返回按钮
            performClick(list.get(0));
        } else {//返回按钮不存在，模拟点击系统返回
            Rect rect = new Rect(22, 54, 86, 184);
            dispatchGestureClick(service, rect.centerX(), rect.centerY(), 100);
        }
    }

    /**
     * 模拟点击微信首页的“搜索”按钮
     *
     * @param service
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void performWxSearchClick(AccessibilityService service, Rect outBounds) {
        dispatchGestureClick(service, outBounds.centerX(), outBounds.centerY(), 100);
    }


    /**
     * 模拟点击事件
     *
     * @param nodeInfo
     * @return
     */
    public static boolean performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        if (nodeInfo.isClickable()) {
            return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            return performClick(nodeInfo.getParent());
        }
    }

}
