package com.mmo.accessibilitydemo;

import android.view.accessibility.AccessibilityEvent;

import com.auto.assist.accessibility.AutoCoreService;

/**
 * <pre>
 *     author: momoxiaoming
 *     time  : 2019/4/13
 *     desc  : 继承module 中的AutoCoreService
 * </pre>
 */
public class MainAccessService extends AutoCoreService {

    @Override
    public void onAccessEvent(AccessibilityEvent event) {
        if (event == null || event.getPackageName() == null) {
            return;
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Rect rect = AcessibilityApi.getRectByNodeInfo(AcessibilityApi.getRootNodeInfo());
//            if (rect != null) {
//                float x = rect.centerX();
//                float y = rect.centerY();
//                LogUtil.debug("get rect: " + rect + ", center: x=" + x + ",y=" + y);
//                GestureUtil.dispatchGestureClick(AcessibilityApi.getAccessibilityService(), x, y, 1000);
//            }
//        }
    }
}
