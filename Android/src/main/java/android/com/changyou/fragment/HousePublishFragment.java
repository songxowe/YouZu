package android.com.changyou.fragment;


import android.com.changyou.MainActivity;
import android.com.changyou.NavActivity;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.com.changyou.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HousePublishFragment extends Fragment {
    private Spinner spCity;
    private Spinner spSize;
    private EditText txtUsername;
    private EditText txtRealName;
    private EditText txtTel;
    private EditText txtPrice;
    private EditText edAddress;

    private CheckBox cbTV;
    private CheckBox cbKT;
    private CheckBox cbXYJ;
    private CheckBox cbWF;
    private CheckBox cbInternet;
    private CheckBox cbBX;

    private Button btnPush;

    private String city;
    private String homesize;
    private String tv = "N";
    private String air = "N";
    private String washer = "N";
    private String network = "N";
    private String dryer = "N";
    private String computer = "N";

    private ArrayAdapter adapter;


    public HousePublishFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_publish, container, false);
        spCity = (Spinner) view.findViewById(R.id.spCity);
        spSize = (Spinner) view.findViewById(R.id.spSize);
        txtUsername = (EditText) view.findViewById(R.id.txtUsername);
        txtRealName = (EditText) view.findViewById(R.id.txtRealName);
        txtTel = (EditText) view.findViewById(R.id.txtTel);
        txtPrice = (EditText) view.findViewById(R.id.txtPrice);
        edAddress = (EditText) view.findViewById(R.id.edAddress);
        cbTV = (CheckBox) view.findViewById(R.id.cbTV);
        cbKT = (CheckBox) view.findViewById(R.id.cbKT);
        cbXYJ = (CheckBox) view.findViewById(R.id.cbXYJ);
        cbWF = (CheckBox) view.findViewById(R.id.cbWF);
        cbInternet = (CheckBox) view.findViewById(R.id.cbInternet);
        cbBX = (CheckBox) view.findViewById(R.id.cbBX);
        btnPush = (Button) view.findViewById(R.id.btnPush);

        final String[] cities = getActivity().getResources().getStringArray(R.array.province);
        final String[] homeSizes = getActivity().getResources().getStringArray(R.array.homeSizes);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, cities);
        spCity.setAdapter(adapter);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cities[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                homesize = homeSizes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }

    private void getDate() {
        String propertyname = txtUsername.getText().toString();
        String realname = txtRealName.getText().toString();
        String tel = txtTel.getText().toString();
        String price = txtPrice.getText().toString();
        String address = edAddress.getText().toString();

        if (cbTV.isChecked()) {
            tv = "Y";
        } else {
            tv = "N";
        }
        if (cbKT.isChecked()) {
            air = "Y";
        } else {
            air = "N";
        }
        if (cbXYJ.isChecked()) {
            washer = "Y";
        } else {
            washer = "N";
        }
        if (cbWF.isChecked()) {
            network = "Y";
        } else {
            network = "N";
        }
        if (cbInternet.isChecked()) {
            computer = "Y";
        } else {
            computer = "N";
        }
        if (cbBX.isChecked()) {
            dryer = "Y";
        } else {
            dryer = "N";
        }
        if (propertyname.trim().length() > 0 & realname.trim().length() > 0 &
                tel.trim().length() > 0 & price.trim().length() > 0 & address.trim().length() > 0) {
            String userid = String.valueOf(ChangYouUtil.getUserId(getActivity()));
            String model = ChangYouUtil.getModel(getActivity());
            String action = "add";
            Map<String, String> params = new HashMap<>();
            params.put("propertyname", propertyname);
            params.put("realname", realname);
            params.put("tel", tel);
            params.put("price", price);
            params.put("address", address);
            params.put("tv", tv);
            params.put("air", air);
            params.put("washer", washer);
            params.put("network", network);
            params.put("computer", computer);
            params.put("dryer", dryer);
            params.put("userid", userid);
            params.put("action", action);
            params.put("homesize", homesize);
            params.put("city", city);
            params.put("model", model);

            MyJsonObjectRequest objectRequest = new MyJsonObjectRequest(
                    Request.Method.POST,
                    VolleyUtil.getAbsoluteUrl("PropertyJsonServlet"),
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                String out = jsonObject.getString("out");
                                if (out.equals("success")) {
                                    ChangYouUtil.toast(getActivity(), "发布成功.");
                                } else if (out.equals("error")) {
                                    ChangYouUtil.toast(getActivity(), "发布失败,房子有误.");
                                } else if (out.equals("error1")) {
                                    ChangYouUtil.toast(getActivity(), "发布失败,设备有误.");
                                } else if (out.equals("exist")) {
                                    ChangYouUtil.toast(getActivity(), "房子已经存在.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ChangYouUtil.toast(getActivity(), R.string.net_error);
                }
            }
            );

            // 把 Json请求对象 加入到 Volley 的请求队列 (默认的TAG)
            VolleyUtil.getInstance(getActivity()).addToRequestQueue(objectRequest);

        } else {
            ChangYouUtil.toast(getActivity(), "选项不能为空.");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        VolleyUtil.getInstance(getActivity()).cancelRequests(VolleyUtil.TAG);
    }
}