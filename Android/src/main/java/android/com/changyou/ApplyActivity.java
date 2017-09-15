package android.com.changyou;

import android.app.NotificationManager;
import android.com.changyou.pojo.Apply;
import android.com.changyou.pojo.User;
import android.com.changyou.polling.PollingService;
import android.com.changyou.polling.PollingUtils;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApplyActivity extends AppCompatActivity {
    private Apply apply;
    private User user;
    private RequestQueue mQueue;
    private Map<String,String> params;

    private TextView tvHostName;
    private TextView tvPoint;
    private TextView num;
    private String telnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        tvHostName = (TextView)findViewById(R.id.tvHostName);
        tvPoint = (TextView)findViewById(R.id.tvPoint);
        num = (TextView)findViewById(R.id.num);

        getApplyMessage();

    }

    private void getPropertyMessage(int houseNo) {
        mQueue = Volley.newRequestQueue(getApplicationContext());
        String sHostno = String.valueOf(houseNo);
        String url = VolleyUtil.getAbsoluteUrl("FindPropertyJsonServlet?hostno="+sHostno);

        params = new HashMap<>();
        params.put("hostno", sHostno);

        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jo) {
                        try {

                            user = new User();
                            user.setUserName(jo.getString("username"));
                            user.setTel(jo.getString("tel"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (user.getUserName()!=null){
                            tvHostName.setText(user.getUserName());
                            telnum = user.getTel();
                            num.setText(telnum);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ChangYouUtil.toast(getApplicationContext(), "名字这。。。。。");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void getApplyMessage() {
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

                        if(apply.getStatus()!=null){
                            if (apply.getStatus().equals("Y")){
                                tvPoint.setText("同意");
                            }else {
                                tvPoint.setText("拒绝");
                            }
                            getPropertyMessage(apply.getHostno());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ChangYouUtil.toast(getApplicationContext(), "轮询失败***********");
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PollingUtils.stopPollingService(
                this, PollingService.class, PollingService.ACTION);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(0);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btCall:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + telnum));
                startActivity(intent);
                break;
            case R.id.btRemove:
                removeApply();
                onBackPressed();
                finish();
                break;
        }
    }

    private void removeApply() {
        params = new HashMap<>();
        params.clear();
        String remove = "remove";
        int applyno = apply.getApplyno();
        String sApplyno = String.valueOf(applyno);
        params.put("action", remove);
        params.put("applyno",sApplyno);
        MyJsonObjectRequest request = new MyJsonObjectRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("ApplyJsonServlet?applyno="+sApplyno+"&action=remove"),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(ChangYouUtil.TAG, jsonObject.toString());
                        try {
                            if (jsonObject.getString("flag").equals("success")) {
                               // ChangYouUtil.toast(getApplicationContext(), "已删除这条请求");
                            } else {
                               // ChangYouUtil.toast(getApplicationContext(), "请求删除失败");
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ChangYouUtil.toast(getApplicationContext(), R.string.net_error);
                    }
                });
        VolleyUtil.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
