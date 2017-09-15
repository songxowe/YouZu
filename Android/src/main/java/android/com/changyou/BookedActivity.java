package android.com.changyou;

import android.app.DatePickerDialog;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookedActivity extends AppCompatActivity {
    private TextView tvStartDate;
    private TextView tvEndDate;
    private EditText edRealName;
    private EditText edTel;
    private EditText edNum;



    private int propertyNo;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        edRealName=(EditText)findViewById(R.id.txtRealName);
        edTel = (EditText)findViewById(R.id.txtTel);
        edNum = (EditText)findViewById(R.id.txtNum);


        Intent intent = getIntent();
        propertyNo = Integer.parseInt(intent.getStringExtra("houseNo"));
        userId = Integer.parseInt(intent.getStringExtra("userId"));



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 应用栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("申请预定");
        actionBar.setHomeAsUpIndicator(R.drawable.left);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartDate:
                showStartDate();
                break;
            case R.id.btnEndDate:
                if (tvStartDate.getText().toString().length() > 0) {
                    showEndDate();
                } else {
                    ChangYouUtil.toast(getApplicationContext(), "请先选择起租日期");
                }

                break;
            case R.id.btnBooking:
                    btnBookingMessage();
                break;
        }

    }

    private void btnBookingMessage() {
        String realName = edRealName.getText().toString();
        String tel = edTel.getText().toString();
        String number = edNum.getText().toString();
        String startDate = tvStartDate.getText().toString();
        String endDate = tvEndDate.getText().toString();
        String username = ChangYouUtil.getUsername(getApplicationContext());

        if (realName.length()>0&&tel.length()>0&&number.length()>0&&startDate.length()>0&&endDate.length()>0){

            Map<String,String> bookMap = new HashMap<>();
            bookMap.put("realName",realName);
            bookMap.put("tel",tel);
            bookMap.put("number",number);
            bookMap.put("startDate",startDate);
            bookMap.put("endDate",endDate);
            bookMap.put("username",username);
            bookMap.put("propertyNo",String.valueOf(propertyNo));
            bookMap.put("userId",String.valueOf(userId));

            MyJsonObjectRequest request = new MyJsonObjectRequest(
                    Request.Method.POST,
                    VolleyUtil.getAbsoluteUrl("ApplyJsonServlet"),
                    bookMap,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (jsonObject.getString("flag").equals("success")) {
                                    ChangYouUtil.toast(getApplicationContext(), "申请成功，请静候佳音！");
                                    startActivity(new Intent(getApplicationContext(), HouseDetailActivity.class));
                                    finish();
                                } else if (jsonObject.getString("flag").equals("error")) {
                                    ChangYouUtil.toast(getApplicationContext(), "申请失败.");
                                    //清空数据
                                    clear();
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
            VolleyUtil.getInstance(this).addToRequestQueue(request);
        }else {
            ChangYouUtil.toast(getApplicationContext(),"请先完善预定资料!");
        }
    }

    public void clear() {
        edRealName.setText("");
        edTel.setText("");
        edNum.setText("");
        tvStartDate.setText("");
        tvEndDate.setText("");
    }

    private void showStartDate() {

        Calendar now = Calendar.getInstance();

        final int yearnow = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH);
        final int day = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                // Log.v("MainActivity", year + "年" + (monthOfYear + 1) + "月" + dayOfMonth);
                if (yearnow > year) {
                    ChangYouUtil.toast(getApplicationContext(), "无效日期,请重新选择");
                } else if (yearnow == year) {
                    if (month > monthOfYear) {
                        ChangYouUtil.toast(getApplicationContext(), "无效日期,请重新选择");
                    } else if (month == monthOfYear) {
                        if (day < dayOfMonth) {
                            String text = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                            tvStartDate.setText(text);
                        } else {
                            ChangYouUtil.toast(getApplicationContext(), "无效日期,请重新选择");
                        }
                    } else {
                        String text = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        tvStartDate.setText(text);
                    }
                } else {
                    String text = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                    tvStartDate.setText(text);
                }

            }

        };

        DatePickerDialog dialog = new DatePickerDialog(
                BookedActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                startDateListener,
                yearnow, month, day
        );
        dialog.show();
    }

    private void showEndDate() {
        String startDate = tvStartDate.getText().toString();
        String[] dates = startDate.split("-");
        // Log.v("MainActivity",dates[0]+dates[1]+dates[2]);
        final int yearStart = Integer.parseInt(dates[0]);
        final int monthStart = Integer.parseInt(dates[1]);
        final int dayStart = Integer.parseInt(dates[2]);

        DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //   Log.v("MainActivity", year + "年" + monthOfYear + "月" + dayOfMonth);

                if (yearStart > year) {
                    ChangYouUtil.toast(getApplicationContext(), "无效日期,请重新选择");
                } else if (yearStart == year) {
                    if ((monthStart - 1) > monthOfYear) {
                        ChangYouUtil.toast(getApplicationContext(), "无效日期,请重新选择");
                    } else if ((monthStart - 1) == monthOfYear) {
                        if (dayStart < dayOfMonth) {
                            String text = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                            tvEndDate.setText(text);
                        } else {
                            ChangYouUtil.toast(getApplicationContext(), "无效日期,请重新选择");
                        }
                    } else {
                        String text = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        tvEndDate.setText(text);
                    }
                } else {
                    String text = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                    tvEndDate.setText(text);
                }
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                BookedActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                startDateListener,
                yearStart, monthStart - 1, dayStart
        );
        dialog.show();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
