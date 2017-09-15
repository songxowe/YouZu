package android.com.changyou.polling;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class PollingUtils {
    /**
     * 开启轮询
     *
     * @param context
     * @param seconds
     * @param cls
     * @param action
     */
    public static void startPollingService(
            Context context, int seconds, Class<?> cls, String action) {
        //获取 AlarmManager 系统服务(定时管理器)
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pi = PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = SystemClock.elapsedRealtime();
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                triggerAtTime, seconds * 1000, pi);
    }

    /**
     * 关闭轮询
     *
     * @param context
     * @param cls
     * @param action
     */
    public static void stopPollingService(
            Context context, Class<?> cls, String action) {
        //获取 AlarmManager 系统服务(定时管理器)
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pi = PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        manager.cancel(pi);
    }
}
