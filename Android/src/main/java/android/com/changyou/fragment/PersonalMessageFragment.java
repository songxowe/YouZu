package android.com.changyou.fragment;


import android.com.changyou.LoginInActivity;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.com.changyou.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PersonalMessageFragment extends Fragment {
    private ImageView userImg;
    private TextView txtUserName;
    private TextView txtModel;
    private TextView txtRealName;
    private Button changePassword;
    private LinearLayout passLinear;
    private Button change;

    private EditText newPass;
    private EditText newPass2;

    private String action = "modify";


    public PersonalMessageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_message, container, false);
        userImg = (ImageView) view.findViewById(R.id.userImg);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtModel = (TextView) view.findViewById(R.id.txtModel);
        txtRealName = (TextView) view.findViewById(R.id.txtRealName);
        changePassword = (Button)view.findViewById(R.id.changePass);
        passLinear = (LinearLayout)view.findViewById(R.id.passLinear);
        change = (Button)view.findViewById(R.id.change);

        newPass = (EditText)view.findViewById(R.id.newPass);
        newPass2 = (EditText)view.findViewById(R.id.newPass2);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passLinear.getVisibility() == View.GONE){
                    passLinear.setVisibility(View.VISIBLE);
                }else {
                    passLinear.setVisibility(View.GONE);
                }

            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushNewPassword();
            }
        });

        setPersonalMessage();
        return view;
    }

    private void pushNewPassword() {
        String snewPass = newPass2.getText().toString();
        String snewPass2 = newPass.getText().toString();
        String name = ChangYouUtil.getUsername(getActivity());

        if(snewPass.equals(snewPass2)&&snewPass.trim().length()>0){
            Map<String,String> params = new HashMap<>();
            params.put("action",action);
            params.put("password",snewPass);
            params.put("username",name);

            MyJsonObjectRequest request = new MyJsonObjectRequest(
                    Request.Method.POST,
                    VolleyUtil.getAbsoluteUrl("UserJsonServlet"),
                    params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (jsonObject.getString("flag").equals("ok")){
                                    ChangYouUtil.toast(getActivity(),"修改密码成功");
                                    startActivity(new Intent(getActivity(), LoginInActivity.class));
                                    getActivity().finish();
                                }else {
                                    ChangYouUtil.toast(getActivity(),"修改密码失败");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ChangYouUtil.toast(getActivity(), R.string.net_error);
                        }
                    }
            );
            // 把 Json请求对象 加入到 Volley 的请求队列 (默认的TAG)
            VolleyUtil.getInstance(getActivity()).addToRequestQueue(request);

        }else {
            ChangYouUtil.toast(getActivity(), "两次输入的密码不一致");
            newPass.setText("");
            newPass2.setText("");
        }
    }


    private void setPersonalMessage() {


        String userimg = ChangYouUtil.getImgPath(getActivity());
        String model = "";
        txtUserName.setText(ChangYouUtil.getUsername(getActivity()));
        if (ChangYouUtil.getModel(getActivity()).equals("B")){
            model = "房东";
        }else {
            model = "普通用户";
        }
        txtModel.setText(model);
        txtRealName.setText(ChangYouUtil.getRealname(getActivity()));

        ImageLoader imageLoader =
                VolleyUtil.getInstance(getActivity()).getImageLoader();
        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(userImg,
                        R.drawable.logo2,
                        R.drawable.logo2);
        imageLoader.get(VolleyUtil.BASE_URL + userimg, listener);


    }


}
