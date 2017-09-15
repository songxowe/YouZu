package android.com.changyou.polling;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.com.changyou.ApplyActivity;
import android.com.changyou.MainActivity;
import android.com.changyou.R;
import android.com.changyou.pojo.Apply;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonArrayRequest;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollingService extends Service {
    private static final String TAG = "MainActivity";
    public static final String ACTION = "android.intent.action.PollingService";
    private Apply apply;
    private RequestQueue mQueue;
    private Map<String,String> params;

    public PollingService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new PollingThread().start();
        return START_STICKY;
    }


    class PollingThread extends Thread {
        @Override
        public void run() {
            Log.v(TAG, "轮询:访问服务器的数据!");

            getPollingApply();
        }

        private void getPollingApply() {
            mQueue = Volley.newRequestQueue(getApplicationContext());
            String username = ChangYouUtil.getUsername(getApplicationContext());

            String url = VolleyUtil.getAbsoluteUrl("ChangeApplyJsonServlet?puser="+username);
            params = new HashMap<>();

            params.put("puser", username);

            Log.v("MainActivity", url+".......");
            Log.v("MainActivity", params.get("puser") + "---user-----");


            MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(
                    url,
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jo) {
                            Log.v(TAG, "loop........" + jo.toString());

                            try {
                                apply = new Apply();
                                apply.setApplyno(jo.getInt("applyno"));
                                apply.setTel(jo.getString("tel"));
                                apply.setHostno(jo.getInt("hostno"));
                                apply.setHouseno(jo.getInt("houseno"));
                                apply.setRealname(jo.getString("realname"));
                                apply.setUsername(jo.getString("username"));
                                apply.setStartdate(jo.getString("startdate"));
                                apply.setEnddate(jo.getString("enddate"));
                                apply.setNumberpeople(jo.getString("numberpeople"));
                                apply.setStatus(jo.getString("status"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.v("MainActivity", apply.getStatus() + "=========apply==========");
                            if (apply.getStatus() != null) {
                                if (!apply.getStatus().equals("U")) {
                                    int host = apply.getHostno();
                                    int houseno = apply.getHouseno();

                                    showNotification();
                                } else {
                                    return;
                                }
                            } else {
                                Log.v(TAG, "没数据");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //ChangYouUtil.toast(getApplicationContext(), "轮询失败***********");
                }
            });
            mQueue.add(jsonObjectRequest);
        }
    }

    private void showNotification() {
        //Log.v(TAG, String.format("%d: %s %s %s", i,content, date, number));
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;

        Intent intent = new Intent();
        intent.setAction("android.intent.action.APPLY");
        intent.setClass(this,ApplyActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this).
                setSmallIcon(R.drawable.about_app).
                setContentTitle("消息提醒").
                setContentText("关于您对租房申请的答复").
                setContentIntent(pi);

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;

        manager.notify(0, notification);
        //多条通知
        //manager.notify(i,notification);

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
