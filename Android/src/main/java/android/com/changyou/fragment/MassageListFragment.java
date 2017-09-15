package android.com.changyou.fragment;

import android.com.changyou.R;
import android.com.changyou.pojo.Apply;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonArrayRequest;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MassageListFragment extends Fragment {
    private View fragment;
    private RequestQueue mQueue;
    private ListView messageListView;
    private Apply apply;
    private List<Apply> applyList;
    private MessageListAdapter adapter;
    private String action = "find";

    public MassageListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        fragment = view.findViewById(R.id.messageFragment);
        messageListView = (ListView) view.findViewById(R.id.messageListView);
        setupView();
        return view;
    }

    private void setupView() {
        mQueue = Volley.newRequestQueue(getActivity());
        applyList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        int userid = ChangYouUtil.getUserId(getActivity());
        params.put("userId", String.valueOf(userid));

        Log.v("MainActivity", params.get("userId"));

        params.put("action", action);

        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("ApplyJsonServlet"),
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            Log.v("MainActivity", jsonArray.toString() + "----------------");
                            applyList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                apply = new Apply();
                                JSONObject jo = jsonArray.getJSONObject(i);
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

                                if (jo.getString("status").equals("U")) {
                                    applyList.add(apply);
                                }

                            }
                            Log.v("MainActivity", applyList.size() + "****size****");
                            if (applyList.size() > 0) {
                                Log.v("MainActivity", "=============");
                                fragment.setVisibility(View.GONE);
                                messageListView.setVisibility(View.VISIBLE);

                                adapter = new MessageListAdapter(getActivity(), applyList);
                                messageListView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ChangYouUtil.toast(getActivity(), R.string.net_error + "...................");
            }
        }
        );
        myJsonArrayRequest.setTag(VolleyUtil.TAG);
        mQueue.add(myJsonArrayRequest);

    }

    public static class MessageListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Apply> applies;
        private Context context;
        private Apply mApply;

        public MessageListAdapter(Context context, List<Apply> applies) {
            this.applies = applies;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return applies.size();
        }

        @Override
        public Object getItem(int position) {
            return applies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.message_item, parent, false);
                holder = new ViewHolder();
                holder.tvTo = (TextView) convertView.findViewById(R.id.tvTo);
                holder.tvFrom = (TextView) convertView.findViewById(R.id.tvFrom);
                holder.tvTel = (TextView) convertView.findViewById(R.id.tvTel);
                holder.tvStartDate = (TextView) convertView.findViewById(R.id.tvStartDate);
                holder.tvEndDate = (TextView) convertView.findViewById(R.id.tvEndDate);
                holder.tvFromName = (TextView) convertView.findViewById(R.id.tvFromName);
                holder.tvFromNum = (TextView) convertView.findViewById(R.id.tvFromNum);
                holder.messageImg = (ImageView) convertView.findViewById(R.id.messageImg);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            mApply = applies.get(position);

            holder.tvTo.append(ChangYouUtil.getRealname(context));
            holder.tvFrom.append(mApply.getUsername());
            holder.tvTel.append(mApply.getTel());
            holder.tvStartDate.append(mApply.getStartdate());
            holder.tvEndDate.append(mApply.getEnddate());
            holder.tvFromName.append(mApply.getRealname());
            holder.tvFromNum.append(mApply.getNumberpeople());

            holder.messageImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ChangYouUtil.toast(context,"信息待处理:.."+position);
                    showApplyDialog(position);

                }
            });
            return convertView;
        }

        private void showApplyDialog(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("消息处理");
            builder.setIcon(R.drawable.about_app);
            builder.setMessage("是否同意该驴友的租房请求");

            final Map<String, String> params = new HashMap<>();
            Apply apply = applies.get(position);
            int applyno = apply.getApplyno();
            final String sApplyno = String.valueOf(applyno);

            builder.setCancelable(false);
            builder.setPositiveButton("同意请求", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ChangYouUtil.toast(context, "请求已同意");
                    String update = "update";
                    params.clear();
                    params.put("action", update);
                    params.put("applyno", sApplyno);
                    MyJsonObjectRequest request = new MyJsonObjectRequest(
                            Request.Method.POST,
                            VolleyUtil.getAbsoluteUrl("ApplyJsonServlet"),
                            params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.i(ChangYouUtil.TAG, jsonObject.toString());
                                    try {
                                        if (jsonObject.getString("flag").equals("success")) {
                                            ChangYouUtil.toast(context, "请求已同意成功");
                                        } else {
                                            ChangYouUtil.toast(context, "请求同意失败");
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    ChangYouUtil.toast(context, R.string.net_error);
                                }
                            });
                    VolleyUtil.getInstance(context).addToRequestQueue(request);
                }
            });

            builder.setNeutralButton("我再想想", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ChangYouUtil.toast(context, "请尽快给出答复");
                }
            });

            builder.setNegativeButton("我要拒绝", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ChangYouUtil.toast(context, "请求已拒绝");
                    String refused = "refused";
                    params.clear();
                    params.put("action", refused);
                    params.put("applyno", sApplyno);
                    MyJsonObjectRequest request = new MyJsonObjectRequest(
                            Request.Method.POST,
                            VolleyUtil.getAbsoluteUrl("ApplyJsonServlet"),
                            params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.i(ChangYouUtil.TAG, jsonObject.toString());
                                    try {
                                        if (jsonObject.getString("flag").equals("success")) {
                                            ChangYouUtil.toast(context, "已删除这条请求");
                                        } else {
                                            ChangYouUtil.toast(context, "请求删除失败");
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    ChangYouUtil.toast(context, R.string.net_error);
                                }
                            });
                    VolleyUtil.getInstance(context).addToRequestQueue(request);
                }
            });


            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private static class ViewHolder {
            private TextView tvTo;
            private TextView tvFrom;
            private TextView tvTel;
            private TextView tvStartDate;
            private TextView tvEndDate;
            private TextView tvFromName;
            private TextView tvFromNum;
            private ImageView messageImg;

        }
    }

}
