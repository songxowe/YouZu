package android.com.changyou;

import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginInActivity extends AppCompatActivity {
    private EditText txtUsername;
    private EditText txtPassword;
    private ImageButton imgPassword;

    private boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        imgPassword = (ImageButton)findViewById(R.id.imgPassword);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                finish();
                break;
            case R.id.btnLoginUp:
                startActivity(new Intent(this, LoginUpActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                finish();
                break;
            case R.id.btn_login:
                loginIn();
                break;
            case R.id.imgPassword:
                isChecked();
                break;
            case R.id.btnForget:
                startActivity(new Intent(this,ChangePasswordActivity.class));
                finish();
                break;
        }
    }

    private void isChecked() {
        if (isChecked) {
            imgPassword.setImageResource(R.drawable.agu);
            txtPassword.setTransformationMethod(
                    HideReturnsTransformationMethod.getInstance());
            isChecked = false;
        } else {
            imgPassword.setImageResource(R.drawable.amg);
            txtPassword.setTransformationMethod(
                    PasswordTransformationMethod.getInstance());
            isChecked = true;
        }
    }

    private void loginIn() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        // 自定义Json对象请求类(请求方式,URL,表单参数,响应成功后的处理类,响应错误后的处理类)
        MyJsonObjectRequest request = new MyJsonObjectRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("LoginJsonServlet"),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(ChangYouUtil.TAG, jsonObject.toString());
                        try {
                            if (jsonObject.getInt("userid") == 0) {
                                ChangYouUtil.toast(getApplicationContext(), R.string.user_invalid);
                            } else {
                                // 1.保存已登录成功的用户到选项存储
                                ChangYouUtil.savePreferences(getApplicationContext(),
                                        jsonObject.getInt("userid"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("model"),
                                        jsonObject.getString("realname"),
                                        jsonObject.getString("userimgpath"));
                                // 2.跳转至 MainActivity
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                // 3.销毁当前 Activity
                                finish();
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
        // 把 Json请求对象 加入到 Volley 的请求队列 (默认的TAG)
        VolleyUtil.getInstance(this).addToRequestQueue(request);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //从 Volley 的请求队列中移除默认的 TAG
        VolleyUtil.getInstance(this).cancelRequests(VolleyUtil.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
