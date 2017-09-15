package android.com.changyou;

import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    private LinearLayout userLinear;
    private LinearLayout questionLinear;
    private LinearLayout newPassLinear;

    private EditText edUserName;
    private EditText edAnswer;
    private EditText newPassword;
    private EditText newPassword2;
    private TextView txtQue;

    private String action = "modify";
    private String question = "";
    private String answer = "";
    private String username="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        userLinear = (LinearLayout)findViewById(R.id.userLinear);
        questionLinear = (LinearLayout)findViewById(R.id.questionLinear);
        newPassLinear = (LinearLayout)findViewById(R.id.newPassLinear);

        edUserName = (EditText)findViewById(R.id.edUserName);
        edAnswer = (EditText)findViewById(R.id.edAnswer);
        newPassword = (EditText)findViewById(R.id.newPassword);
        newPassword2 = (EditText)findViewById(R.id.newPassword2);
        txtQue = (TextView)findViewById(R.id.txtQue);

    }

    public void onClick(View view){
        username = edUserName.getText().toString();
        switch (view.getId()){
            case R.id.btnNext1:
                checkUserName();
                break;
            case R.id.btnNext2:
                checkQuestion();
                break;
            case R.id.btnPut:
                changePassword();
                break;
        }
    }

    private void changePassword() {
        String newPass = newPassword.getText().toString();
        String newPass2 = newPassword2.getText().toString();

        if(newPass.equals(newPass2)&&newPass.trim().length()>0){
            Map<String,String> params = new HashMap<>();
            params.put("action",action);
            params.put("password",newPass);
            params.put("username",username);

            MyJsonObjectRequest request = new MyJsonObjectRequest(
                    Request.Method.POST,
                    VolleyUtil.getAbsoluteUrl("UserJsonServlet"),
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (jsonObject.getString("flag").equals("ok")){
                                    ChangYouUtil.toast(getApplicationContext(),"修改密码成功");
                                    startActivity(new Intent(getApplicationContext(),LoginInActivity.class));
                                    finish();
                                }else {
                                    ChangYouUtil.toast(getApplicationContext(),"修改密码失败");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ChangYouUtil.toast(getApplicationContext(), R.string.net_error);
                        }
                    }
            );
            // 把 Json请求对象 加入到 Volley 的请求队列 (默认的TAG)
            VolleyUtil.getInstance(this).addToRequestQueue(request);

        }else {
            ChangYouUtil.toast(this, "两次输入的密码不一致");
            newPassword.setText("");
            newPassword2.setText("");
        }
    }

    private void checkQuestion() {
        String txtAnswer = edAnswer.getText().toString();
        if(txtAnswer.trim().equals(answer)){
            questionLinear.setVisibility(View.GONE);
            newPassLinear.setVisibility(View.VISIBLE);
        }else {
            ChangYouUtil.toast(getApplicationContext(),"密保答案有误!");
            edAnswer.setText("");
        }
    }

    private void checkUserName() {
        Map<String,String> params = new HashMap<>();
        params.put("action",action);
        params.put("username",username);

        MyJsonObjectRequest request = new MyJsonObjectRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("UserJsonServlet"),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.length()>0){
                                question = jsonObject.getString("question");
                                answer = jsonObject.getString("answer");
                                userLinear.setVisibility(View.GONE);
                                txtQue.append(question);
                                questionLinear.setVisibility(View.VISIBLE);
                            }else {
                                ChangYouUtil.toast(getApplicationContext(),"该用户不存在!");
                                edUserName.setText("");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ChangYouUtil.toast(getApplicationContext(), R.string.net_error);
                    }
                }
        );
        // 把 Json请求对象 加入到 Volley 的请求队列 (默认的TAG)
        VolleyUtil.getInstance(this).addToRequestQueue(request);
    }
}
