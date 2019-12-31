package com.auto.assist.accessibility.api;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;

import com.auto.assist.accessibility.selector.ConditionNode;
import com.auto.assist.accessibility.util.LogUtil;

import java.util.List;

/**
 * 上层API，配合 SDK/tools/bin/uiautomatorviewer.bat 进行节点分析
 */
public class UiApi {

    private static final int WAIT_UI_APPEAR_MSEC = 500;  //控件默认的超时时间,毫秒
    private static final int CHECK_UI_SLEEP_GAP_MSEC = 200;  //控件默认的超时时间,毫秒

    /**
     * 前往开启辅助服务界面
     */
    public static void goAccessibilitySettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断指定的应用的辅助功能是否开启,
     *
     * @param context 上下文
     * @param
     * @return 是否开启
     */
    public static boolean isAccessibilityServiceOn(@NonNull Context context, Class cls) {
        int ok = 0;
        String serName = context.getPackageName() + "/" + cls.getCanonicalName();

        try {
            ok = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }

        TextUtils.SimpleStringSplitter ms = new TextUtils.SimpleStringSplitter(':');
        if (ok == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                ms.setString(settingValue);
                while (ms.hasNext()) {
                    String accessibilityService = ms.next();
                    if (accessibilityService.equalsIgnoreCase(serName)) {
                        return true;
                    }

                }
            }
        }

        return false;
    }


    /**
     * 触发系统rebind通知监听服务
     *
     * @param context      上下文
     * @param serviceClass 辅助功能服务的类
     */
    public static void rebindAccessibilityService(Context context, Class serviceClass) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(context, serviceClass),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
        );
        pm.setComponentEnabledSetting(
                new ComponentName(context, serviceClass),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        );
    }

    /**
     * 回到home页
     */
    public static void home() {
        AcessibilityApi.performAction(AcessibilityApi.ActionType.HOME);
    }

    /**
     * 返回
     */
    public static void back() {
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
    }

    public static void notifications() {
        AcessibilityApi.performAction(AcessibilityApi.ActionType.NOTIFICATIONS);
    }

    public static void scrollBackward() {
        AcessibilityApi.performAction(AcessibilityApi.ActionType.SCROLL_BACKWARD);
    }

    public static void scrollForward() {
        AcessibilityApi.performAction(AcessibilityApi.ActionType.SCROLL_FORWARD);
    }

    /**
     * 模拟手指触摸点击事件
     *
     * @param x           横坐标
     * @param y           纵坐标
     * @param millisecond 点击时间间隔
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void performGestureClick(AccessibilityService service, float x, float y, int millisecond) {
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
     * 通过text获取节点,直到超时
     *
     * @param maxMills 最大查找时间
     * @param text     查找文本
     * @return 节点对象
     */
    @WorkerThread
    public static AccessibilityNodeInfo findNodeByTextWithTimeOut(long maxMills, String text) {
        AccessibilityNodeInfo mNode = null;
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            mNode = AcessibilityApi.findViewByText(text);
            if (isExists(mNode)) {
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
        LogUtil.debug("按 TEXT(" + text + ") 查找节点： " + mNode);
        return mNode;
    }


    /**
     * 通过Id获取节点,直到超时
     *
     * @param maxMills 最大查找时间
     * @param id       查找的资源id
     * @return 节点对象
     */
    @WorkerThread
    public static AccessibilityNodeInfo findNodeByIdWithTimeOut(long maxMills, String id) {
        AccessibilityNodeInfo mNode;
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            mNode = AcessibilityApi.findViewByID(id);
            if (isExists(mNode)) {
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
        LogUtil.debug("按 ID(" + id + ") 查找节点： " + mNode);
        return mNode;
    }

    /**
     * 通过desc获取节点,直到超时
     *
     * @param maxMills 最大查找时间
     * @param desc     查找的节点的描述内容
     * @return 节点对象
     */
    @WorkerThread
    public static AccessibilityNodeInfo findNodeByDescWithTimeOut(long maxMills, String desc) {
        AccessibilityNodeInfo mNode;
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            mNode = AcessibilityApi.findViewByDesc(desc);
            if (isExists(mNode)) {
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
        LogUtil.debug("按 DESC(" + desc + ") 查找节点： " + mNode);
        return mNode;
    }


    /**
     * 根据类名查找控件,直到超时时间
     *
     * @param maxMills 最大查找时间
     * @param cls      节点的类名
     * @return 节点对象
     */
    @WorkerThread
    public static AccessibilityNodeInfo findNodeByClsWithTimeOut(long maxMills, String cls) {
        AccessibilityNodeInfo mNode = null;
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            List<AccessibilityNodeInfo> lists = AcessibilityApi.findViewByCls(cls);
            if (lists != null && lists.size() != 0) {
                mNode = lists.get(0);
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
        LogUtil.debug("按 CLASS(" + cls + ") 查找节点： " + mNode);
        return mNode;
    }

    @WorkerThread
    public static AccessibilityNodeInfo findNodeByActionWithTimeOut(long maxMills, AccessibilityNodeInfo.AccessibilityAction action) {
        AccessibilityNodeInfo mNode = null;
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            List<AccessibilityNodeInfo> lists = AcessibilityApi.findViewByAction(action);
            if (lists != null && lists.size() != 0) {
                mNode = lists.get(0);
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
        LogUtil.debug("按 ACTION(" + action + ") 查找节点： " + mNode);
        return mNode;
    }

    /**
     * 按条件查找节点,并返回text中的字符串
     */
    public static String getTextByNode(long maxMustMills, ConditionNode conditionNode) {
        AccessibilityNodeInfo nodeInfo = findOptionNodeWithTimeOut(maxMustMills, conditionNode);
        if (nodeInfo != null && nodeInfo.getText() != null) {
            return nodeInfo.getText().toString();
        }
        return "";
    }

    /**
     * 通过条件精确查找,获取节点上的描述信息
     *
     * @param maxMustMills  超时时间
     * @param conditionNode 查找条件
     */
    public static String getDescByNode(long maxMustMills, ConditionNode conditionNode) {
        AccessibilityNodeInfo nodeInfo = findOptionNodeWithTimeOut(maxMustMills, conditionNode);
        if (nodeInfo != null && nodeInfo.getContentDescription() != null) {
            return nodeInfo.getContentDescription().toString();
        }
        return "";
    }


    /**
     * 点击text节点,直到超时
     *
     * @param maxMills
     * @param text
     * @return
     */
    @WorkerThread
    public static boolean clickNodeByTextWithTimeOut(long maxMills, String text) {
        boolean isClick;
        AccessibilityNodeInfo node = findNodeByTextWithTimeOut(maxMills, text);
        if (node == null) {
            return false;
        }
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            isClick = AcessibilityApi.performViewClick(node);
            if (isClick) {
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(500);
                    LogUtil.debug("尝试失败,再次尝试点击");
                } else {
                    break;
                }
            }
        }
        return isClick;
    }

    /**
     * 点击id节点直到超时
     *
     * @param maxMills 超时时间
     * @param id       id查找
     * @return
     */
    @WorkerThread
    public static boolean clickNodeByIdWithTimeOut(long maxMills, String id) {
        boolean isClick = false;
        AccessibilityNodeInfo node = findNodeByIdWithTimeOut(maxMills, id);
        if (node == null) {
            return false;
        }
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            isClick = AcessibilityApi.performViewClick(node);
            if (isClick) {
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(500);
                    LogUtil.debug("尝试失败,再次尝试点击");
                } else {
                    break;
                }
            }
        }
        return isClick;
    }

    /**
     * 通过des查找并点击控件,含超时
     *
     * @return
     */
    @WorkerThread
    public static boolean clickNodeByDesWithTimeOut(long maxMills, String id) {
        boolean isClick;
        AccessibilityNodeInfo node = findNodeByDescWithTimeOut(maxMills, id);
        if (node == null) {
            return false;
        }
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            isClick = AcessibilityApi.performViewClick(node);
            if (isClick) {
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(500);
                    LogUtil.debug("尝试失败,再次尝试点击");
                } else {
                    break;
                }
            }
        }
        return isClick;
    }

    /**
     * 点击某个节点,直到超时
     *
     * @param maxMills 超时时间
     */
    @WorkerThread
    public static boolean clickNodeWithTimeOut(long maxMills, AccessibilityNodeInfo node) {
        boolean isClick = false;
        if (node == null) {
            return false;
        }
        long beginUtcMsc = System.currentTimeMillis(); //记录当前开始时间
        long curUtcMsc; //当前时间
        if (maxMills == 0 || maxMills < 0) {
            maxMills = WAIT_UI_APPEAR_MSEC;
        }
        while (true) {
            isClick = AcessibilityApi.performViewClick(node);
            if (isClick) {
                break;
            } else {
                curUtcMsc = System.currentTimeMillis();
                if ((curUtcMsc - beginUtcMsc) < maxMills) {
                    sleepTime(500);
                    LogUtil.debug("尝试失败,再次尝试点击");
                } else {
                    break;
                }
            }
        }
        return isClick;
    }


    /**
     * 模糊查找,只要找到一个就返回
     *
     * @param maxMustMills  超时时间
     * @param conditionNode 查找的条件
     */
    public static AccessibilityNodeInfo findOptionNodeWithTimeOut(long maxMustMills, ConditionNode conditionNode) {
        AccessibilityNodeInfo nodeInfo;
        if (conditionNode != null) {
            List<String> desc = conditionNode.getDesc();
            if (desc != null && desc.size() > 0) {
                for (String vl : desc) {
                    nodeInfo = findNodeByDescWithTimeOut(maxMustMills, vl);
                    if (!isExists(nodeInfo)) {
                        LogUtil.debug("模糊查找 DESC节点[" + vl + "]查找失败");
                    } else {
                        LogUtil.debug("模糊查找 DESC节点[" + vl + "]查找成功");
                        return nodeInfo;
                    }
                }
            }
            List<String> text = conditionNode.getText();
            if (text != null && text.size() > 0) {
                for (String vl : text) {
                    nodeInfo = findNodeByTextWithTimeOut(maxMustMills, vl);
                    if (!isExists(nodeInfo)) {
                        LogUtil.debug("模糊查找 TEXT节点[" + vl + "]查找失败");
                    } else {
                        LogUtil.debug("模糊查找 TEXT节点[" + vl + "]查找成功");
                        return nodeInfo;
                    }
                }
            }
            List<String> id = conditionNode.getId();
            if (id != null && id.size() > 0) {
                for (String vl : id) {
                    nodeInfo = findNodeByIdWithTimeOut(maxMustMills, vl);
                    if (!isExists(nodeInfo)) {
                        LogUtil.debug("模糊查找 ID节点[" + vl + "]查找失败");
                    } else {
                        LogUtil.debug("模糊查找 ID节点[" + vl + "]查找成功");
                        return nodeInfo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过text查找输入控件,然后输入
     *
     * @param maxMils  最大超时时间
     * @param text     text节点查找
     * @param inputStr 输入的内容
     * @return 是否输入成功
     */
    public static boolean findNodeByTextAndInput(long maxMils, String text, String inputStr) {
        AccessibilityNodeInfo node = findNodeByTextWithTimeOut(maxMils, text);
        if (node != null) {
            return AcessibilityApi.inputTextByNode(node, inputStr);
        }
        return false;
    }

    public static boolean findNodeByDescAndInput(long maxMils, String text, String inputStr) {
        AccessibilityNodeInfo node = findNodeByDescWithTimeOut(maxMils, text);
        if (node != null) {
            return AcessibilityApi.inputTextByNode(node, inputStr);
        }
        return false;
    }

    public static boolean findEditTextByClsAndInput(long maxMils, String inputStr) {
        AccessibilityNodeInfo node = findNodeByClsWithTimeOut(maxMils, "android.widget.EditText");
        if (node != null) {
            return AcessibilityApi.inputTextByNode(node, inputStr);
        }
        return false;
    }

    public static boolean findEditTextByActionAndInput(long maxMils, String inputStr) {
        AccessibilityNodeInfo node = findNodeByActionWithTimeOut(maxMils, AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT);
        if (node != null) {
            return AcessibilityApi.inputTextByNode(node, inputStr);
        }
        return false;
    }

    /**
     * 通过text查找输入控件,然后输入
     *
     * @param maxMils  最大超时时间
     * @param id       id节点查找
     * @param inputStr 输入的内容
     * @return 是否输入成功
     */
    public static boolean findNodeByIdAndInput(long maxMils, String id, String inputStr) {
        AccessibilityNodeInfo node = findNodeByIdWithTimeOut(maxMils, id);
        if (node != null) {
            return AcessibilityApi.inputTextByNode(node, inputStr);
        }
        return false;
    }

    private static boolean isExists(AccessibilityNodeInfo nodeInfo) {
        return nodeInfo != null;
    }

    public static void sleepTime(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
