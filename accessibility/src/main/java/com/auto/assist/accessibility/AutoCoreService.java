package com.auto.assist.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.debug("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtil.debug("onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtil.debug("onAccessibilityEvent: " + AccessibilityEvent.eventTypeToString(event.getEventType()) + "," + event.getPackageName());
        onAccessEvent(event);
    }

    @Override
    public void onInterrupt() {
        LogUtil.debug("onInterrupt");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.debug("onUnbind");
        return super.onUnbind(intent);
    }

    public abstract void onAccessEvent(AccessibilityEvent event);

}
