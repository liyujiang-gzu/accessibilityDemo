package com.auto.assist.accessibility.api;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.WorkerThread;

import com.auto.assist.accessibility.selector.ActionSelector;
import com.auto.assist.accessibility.selector.ClickDescNode;
import com.auto.assist.accessibility.selector.ClickIdNode;
import com.auto.assist.accessibility.selector.ClickNode;
import com.auto.assist.accessibility.selector.ClickTextNode;
import com.auto.assist.accessibility.selector.ConditionNode;
import com.auto.assist.accessibility.selector.NodeSelector;
import com.auto.assist.accessibility.util.ApiUtil;
import com.auto.assist.accessibility.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 上层api
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
     * 判断是否是当前页面
     */
    public static boolean isMyNeedPage(String selecor) {
        NodeSelector selector = NodeSelector.fromJson(selecor);
        return isMyNeedPage(selector);
    }

    /**
     * 前往某个界面
     *
     * @param selecors 选择器内容数组
     * @return
     */
    public static boolean jumpToNeedPage(String[] selecors) {
        if (selecors == null) {
            return false;
        }
        List<ActionSelector> lists = new ArrayList<>();
        for (String item : selecors) {
            ActionSelector payPage = ActionSelector.fromJson(item);
            if (payPage == null) {
                LogUtil.error("选择器字符串无法格式成功");
                return false;
            }
            lists.add(payPage);
        }
        return jumpToNeedPage(lists);
    }

    /**
     * 回到home页
     */
    public static void backHome() {
        AcessibilityApi.performAction(AcessibilityApi.ActionType.HOME);
    }

    /**
     * 返回
     */
    public static void back() {
        AcessibilityApi.performAction(AcessibilityApi.ActionType.BACK);
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
                    ApiUtil.sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
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
                    ApiUtil.sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
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
    public static AccessibilityNodeInfo findNodeByDesWithTimeOut(long maxMills, String desc) {
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
                    ApiUtil.sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
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
                    ApiUtil.sleepTime(CHECK_UI_SLEEP_GAP_MSEC);
                } else {
                    break;
                }
            }
        }
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
                    ApiUtil.sleepTime(500);
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
                    ApiUtil.sleepTime(500);
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
        AccessibilityNodeInfo node = findNodeByDesWithTimeOut(maxMills, id);
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
                    ApiUtil.sleepTime(500);
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
                    ApiUtil.sleepTime(500);
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
                    nodeInfo = findNodeByDesWithTimeOut(maxMustMills, vl);
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


    /**
     * 判断是否是当前页面
     */
    public static boolean isMyNeedPage(NodeSelector nodeSelecor) {
        boolean isPage;
        if (nodeSelecor == null) {
            return false;
        }
        long maxMustMills = nodeSelecor.getMaxMustMills();
        long maxOptionMills = nodeSelecor.getMaxOptionMills();
        //---must 只有有一个失败,就失败
        boolean mustTag = true;
        ConditionNode must = nodeSelecor.getOption();
        if (must != null) {
            List<String> desc = must.getDesc();
            if (desc != null && desc.size() > 0) {
                for (String vl : desc) {
                    mustTag = isExists(findNodeByDesWithTimeOut(maxMustMills, vl));
                    if (!mustTag) {
                        LogUtil.debug("must DESC节点[" + vl + "]查找失败");
                        return false;
                    } else {
                        LogUtil.debug("must DESC节点[" + vl + "]查找成功");
                    }
                }
            }
            List<String> text = must.getText();
            if (text != null && text.size() > 0) {
                for (String vl : text) {
                    mustTag = isExists(findNodeByTextWithTimeOut(maxMustMills, vl));
                    if (!mustTag) {
                        LogUtil.debug("must TEXT节点[" + vl + "]查找失败");
                        return false;
                    } else {
                        LogUtil.debug("must TEXT节点[" + vl + "]查找成功");
                    }
                }
            }
            List<String> id = must.getId();
            if (id != null && id.size() > 0) {
                for (String vl : id) {
                    mustTag = isExists(findNodeByIdWithTimeOut(maxMustMills, vl));
                    if (!mustTag) {
                        LogUtil.debug("must ID节点[" + vl + "]查找失败");
                        return false;
                    } else {
                        LogUtil.debug("must ID节点[" + vl + "]查找成功");
                    }
                }
            }
        }
        //---option,只要有一个成功,就成功
        boolean optTag = true;
        ConditionNode option = nodeSelecor.getOption();
        if (option != null) {
            List<String> desc = option.getDesc();
            if (desc != null && desc.size() > 0) {
                for (String vl : desc) {
                    mustTag = isExists(findNodeByDesWithTimeOut(maxMustMills, vl));
                    if (mustTag) {
                        LogUtil.debug("option DESC节点[" + vl + "]查找成功");
                        return true;
                    } else {
                        LogUtil.debug("option DESC节点[" + vl + "]查找失败");

                    }
                }
            }
            List<String> text = option.getText();
            if (text != null && text.size() > 0) {
                for (String vl : text) {
                    optTag = isExists(findNodeByTextWithTimeOut(maxOptionMills, vl));
                    if (optTag) {
                        LogUtil.debug("option TEXT节点[" + vl + "]查找成功");
                        return true;
                    } else {
                        LogUtil.debug("option TEXT节点[" + vl + "]查找失败");
                    }
                }
            }
            List<String> id = option.getId();
            if (id != null && id.size() > 0) {
                for (String vl : id) {
                    optTag = isExists(findNodeByIdWithTimeOut(maxOptionMills, vl));
                    if (optTag) {
                        LogUtil.debug("option ID节点[" + vl + "]查找成功");
                        return true;
                    } else {
                        LogUtil.debug("option ID节点[" + vl + "]查找失败");
                    }
                }
            }
        }
        isPage = (mustTag && optTag);
        return isPage;
    }

    public static boolean jumpToNeedPage(ActionSelector actionSelector) {
        List<ActionSelector> lists = new ArrayList<>();
        lists.add(actionSelector);
        return jumpToNeedPage(lists);
    }

    /**
     * 前往预期页面,可以串联多个页面路径
     *
     * @param lists 条件
     */
    public static boolean jumpToNeedPage(List<ActionSelector> lists) {
        boolean isJump = false;
        if (lists == null || lists.size() == 0) {
            return false;
        }
        for (ActionSelector item : lists) {
            long maxClickMills = item.getMaxWClickMSec();
            NodeSelector page = item.getPage();
            ClickNode clickNode = item.getClick();
            if (page != null && clickNode != null && maxClickMills != 0) {
                if (isMyNeedPage(page)) {
                    if (clickNode instanceof ClickTextNode) {
                        isJump = clickNodeByTextWithTimeOut(maxClickMills, ((ClickTextNode) clickNode).getText());
                    } else if (clickNode instanceof ClickIdNode) {
                        isJump = clickNodeByIdWithTimeOut(maxClickMills, ((ClickIdNode) clickNode).getId());
                    } else if (clickNode instanceof ClickDescNode) {
                        isJump = clickNodeByDesWithTimeOut(maxClickMills, ((ClickDescNode) clickNode).getDesc());
                    }
                    if (!isJump) {
                        LogUtil.error("未找到节点:" + clickNode);
                        return false;
                    }
                } else {
                    LogUtil.error("不在预期页面:" + page);
                    return false;
                }
            }
        }
        return isJump;
    }

    private static boolean isExists(AccessibilityNodeInfo nodeInfo) {
        return nodeInfo != null;
    }

}
