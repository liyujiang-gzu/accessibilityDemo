package com.auto.assist.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.auto.assist.accessibility.api.AcessibilityApi;
import com.auto.assist.accessibility.util.LogUtil;

public abstract class AutoCoreService extends AccessibilityService {

    @Override
    public void onCreate() {
        LogUtil.debug("onCreate");
        super.onCreate();
        AcessibilityApi.setAccessibilityService(this);

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtil.debug("onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtil.debug("onAccessibilityEvent: " + event);
        AcessibilityApi.setAccessibilityEvent(event);
        onAccessEvent(event);
    }

    @Override
    public void onInterrupt() {
        LogUtil.debug("onInterrupt");
    }

    public abstract void onAccessEvent(AccessibilityEvent event);

}
