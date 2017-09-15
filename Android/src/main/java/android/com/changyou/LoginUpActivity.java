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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginUpActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private EditText txtUsername;
    private EditText txtPassword;
    private EditText txtRealName;
    private EditText txtTel;
    private EditText txtQuestion;
    private LinearLayout queLinear;
    private Spinner spinnerQue;
    private EditText txtAnswer;
    private RadioGroup rgSex;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private ImageButton imgPassword;

    private String sex = "";
    private String question = "";

    private boolean isChecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_up);

        final String[] questions = this.getResources().getStringArray(R.array.questions);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRealName = (EditText) findViewById(R.id.txtRealName);
        txtTel = (EditText) findViewById(R.id.txtTel);
        queLinear = (LinearLayout) findViewById(R.id.queLinear);
        txtQuestion = (EditText) findViewById(R.id.txtQuestion);
        spinnerQue = (Spinner) findViewById(R.id.spinnerQue);
        txtAnswer = (EditText) findViewById(R.id.txtAnswer);
        rgSex = (RadioGroup) findViewById(R.id.rg_sex);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        imgPassword = (ImageButton)findViewById(R.id.imgPassword);

        spinnerQue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question = questions[position];
                if (position == 3) {
                    queLinear.setVisibility(View.VISIBLE);
                    question = "";
                } else {
                    queLinear.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                break;
            case R.id.btnLoginIn:
                startActivity(new Intent(this, LoginInActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                finish();
                break;
            case R.id.btn_login_up:
                loginUp();
                break;
            case R.id.imgPassword:
                isChecked();
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbMale:
                sex = "男";
                break;
            case R.id.rbFemale:
                sex = "女";
                break;
        }
    }

    private void loginUp() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String realname = txtRealName.getText().toString();
        String tel = txtTel.getText().toString();

        if (question.trim().length() == 0) {
            question = txtQuestion.getText().toString();
        }

        String answer = txtAnswer.getText().toString();

        if (rbMale.isChecked()) {
            onCheckedChanged(rgSex, R.id.rbMale);
        }
        if (rbFemale.isChecked()) {
            onCheckedChanged(rgSex, R.id.rbFemale);
        }

        Log.v(ChangYouUtil.TAG, sex);

        if (username.trim().length() == 0 || password.trim().length() == 0 ||
                realname.trim().length() == 0 || tel.trim().length() == 0 ||
                question.trim().length() == 0 || answer.trim().length() == 0) {
            ChangYouUtil.toast(getApplicationContext(), "选项不能为空.");
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("realname", realname);
        params.put("tel", tel);
        params.put("sex", sex);
        params.put("question", question);
        params.put("answer", answer);

        // 自定义Json对象请求类(请求方式,URL,表单参数,响应成功后的处理类,响应错误后的处理类)
        MyJsonObjectRequest request = new MyJsonObjectRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("UserJsonServlet"),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("flag").equals("success")) {
                                startActivity(new Intent(getApplicationContext(), LoginInActivity.class));
                                finish();
                            } else if (jsonObject.getString("flag").equals("error")) {
                                ChangYouUtil.toast(getApplicationContext(), "注册失败.");
                                //清空数据
                                clear();
                            } else if (jsonObject.getString("flag").equals("exist")) {
                                ChangYouUtil.toast(getApplicationContext(), "用户已经存在.");
                                clear();
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
                }
        );
        // 把 Json请求对象 加入到 Volley 的请求队列 (默认的TAG)
        VolleyUtil.getInstance(this).addToRequestQueue(request);
    }

    public void clear() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtRealName.setText("");
        txtTel.setText("");
        txtQuestion.setText("");
        txtAnswer.setText("");

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
