package wzp.libs.utils;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * 应用程序Activity管理类：用于Activity管理
 */
public final class ActivityManagerUtils {
    /** 日志TAG */
    private static final String TAG = ActivityManagerUtils.class.getSimpleName();
    /** ActivityManager 实例 */
    private static ActivityManagerUtils instance;
    /** 存放Activity的集合 */
    private List<Activity> list;
    /** 禁止构造对象,保证只有一个实例 */
    private ActivityManagerUtils() {
    }

    /** 单一实例 */
    public static ActivityManagerUtils getInstance() {
        if (instance == null) {
            instance = new ActivityManagerUtils();
        }
        return instance;
    }

    //================================================//

    /**
     * 通过 Context 获取 Activity
     * @param context
     * @return context 所属的 Activity
     */
    public static Activity getActivity(Context context) {
        try {
            Activity activity = (Activity) context;
            return activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // == 页面判断处理 ==

    /**
     * 判断页面是否关闭
     * @param activity
     * @return true: 关闭, false: 未关闭
     */
    public static boolean isFinishing(Activity activity){
        if (activity != null){
            return activity.isFinishing();
        }
        return false;
    }

    /**
     * 判断页面是否关闭
     * @param context
     * @return true: 关闭, false: 未关闭
     */
    public static boolean isFinishing(Context context){
        if (context != null){
            try {
                return ((Activity) context).isFinishing();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    // ==============

    /** 添加Activity到集合 */
    public void addActivityToList(Activity activity) {
        if (list == null) {
            list = new ArrayList<Activity>();
        }
        list.add(activity);
    }


    /** 遍历所有集合中Activity finish */
    public void finishAllListActivity() {
        if (list == null) {
            return;
        }
        for (Activity activity : list) {
            activity.finish();
        }
        if (list.size() == 0)
            list.clear();
    }

    /**
     * 刪除所有activity,除了指定的activity之外
     * @param classz
     */
    public void finishAllListActivity(Class classz) {
        if (list == null) {
            return;
        }
        for (Activity activity : list) {
            if (activity.getClass() != classz) {
                activity.finish();
            }
        }
    }

    /**
     * 刪除所有activity,除了指定的多个activity之外
     * @param classz
     */
    public void finishAllListActivity(Class... classz) {
        if (list == null) {
            return;
        }
        for (Activity activity : list){
            //是否移除的标志,默认移除
            boolean isRemove = true;
            for (Class cla : classz) {
                if (activity.getClass()==cla){
                    isRemove = false; //list集合中有包含传递的类的话，就不移除
                }
            }
            if (isRemove){
                activity.finish();
            }
        }
    }

    /**
     * 删除指定的多个activity，其它没指定的都不删除
     * @param classz
     */
    public void finishSpecifiedActivity(Class... classz){
        if (list == null) {
            return;
        }
        for (Activity activity : list) {
            for (Class cla : classz) {
                if (activity.getClass()==cla){
                    activity.finish();
                }
            }
        }
    }
}