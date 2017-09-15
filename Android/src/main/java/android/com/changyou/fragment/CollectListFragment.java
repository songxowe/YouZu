package android.com.changyou.fragment;

import android.com.changyou.R;
import android.com.changyou.pojo.Apply;
import android.com.changyou.pojo.Collect;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectListFragment extends Fragment {
    private List<Collect> collects;
    private Collect collect;
    private View fragment;
    private RequestQueue mQueue;
    private ListView collectListView;
    private String action = "select";
    private CollectAdapter adapter;


    public CollectListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_list, container, false);
        fragment = view.findViewById(R.id.collectFragment);
        collectListView = (ListView) view.findViewById(R.id.collectListView);
        setupView();

        return view;
    }

    private void setupView() {
        mQueue = Volley.newRequestQueue(getActivity());
        collects = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        int userid = ChangYouUtil.getUserId(getActivity());
        params.put("userid", String.valueOf(userid));
        params.put("action", action);

        Log.v("MainActivity","我到这里来了");
        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("CollectJsonServlet"),
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.v("MainActivity",jsonArray.toString()+"ooooooooooo");
                        collects.clear();
                        for (int i=0;i<jsonArray.length();i++){
                            collect = new Collect();
                            try {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                collect.setUsername(jo.getString("username"));
                                collect.setUserid(jo.getInt("userid"));
                                collect.setUserimg(jo.getString("userimg"));
                                collect.setHousename(jo.getString("housename"));
                                collect.setHouseno(jo.getInt("houseno"));
                                collect.setHousephoto(jo.getString("housephoto"));
                                collect.setScore(jo.getString("score"));
                                collect.setPrice(Double.parseDouble(jo.getString("price")));

                                collects.add(collect);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Log.v("MainActivity",collectListView.toString());
                            if (collects.size() > 0) {
                                Log.v("MainActivity", "=============");
                                fragment.setVisibility(View.GONE);
                                collectListView.setVisibility(View.VISIBLE);

                                adapter = new CollectAdapter(getActivity(),collects);
                                collectListView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        jsonArrayRequest.setTag(VolleyUtil.TAG);
        mQueue.add(jsonArrayRequest);

    }

    public static class CollectAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<Collect> collects;
        private Context context;
        private Collect collect;

        public CollectAdapter( Context context,List<Collect> collects) {
            this.collects = collects;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return collects.size();
        }

        @Override
        public Object getItem(int position) {
            return collects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView = inflater.inflate(R.layout.collect_house_item, parent, false);
                holder = new ViewHolder();
                holder.housename = (TextView)convertView.findViewById(R.id.housename);
                holder.price = (TextView)convertView.findViewById(R.id.price);
                holder.housephoto = (ImageView)convertView.findViewById(R.id.collectHouseImg);
                holder.userimg = (ImageView)convertView.findViewById(R.id.collectUserImg);
                holder.rating = (RatingBar)convertView.findViewById(R.id.collectRating);
                holder.btnCollectLess = (ImageButton)convertView.findViewById(R.id.btnCollectLess);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            collect = collects.get(position);
            ImageLoader imageLoader =
                    VolleyUtil.getInstance(holder.housephoto.getContext()).getImageLoader();
            ImageLoader.ImageListener listener =
                    ImageLoader.getImageListener(
                            holder.housephoto, R.mipmap.ic_launcher, R.drawable.inter_logo);
            imageLoader.get(VolleyUtil.BASE_URL + collect.getHousephoto(), listener);

            ImageLoader imageLoaderFd =
                    VolleyUtil.getInstance(holder.userimg.getContext()).getImageLoader();
            ImageLoader.ImageListener listenerFd =
                    ImageLoader.getImageListener(
                            holder.userimg, R.mipmap.ic_launcher, R.drawable.inter_logo);
            imageLoaderFd.get(VolleyUtil.BASE_URL + collect.getUserimg(), listenerFd);

            holder.housename.setText(collect.getHousename());
            holder.price.setText(String.valueOf(collect.getPrice()));
            holder.rating.setRating(Float.parseFloat(collect.getScore()));

            holder.btnCollectLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(position);
                }
            });

            return convertView;
        }

        private void showDialog(int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("删除收藏");
            builder.setIcon(R.drawable.about_app);
            builder.setMessage("是否删除这间房子的收藏");

            Collect collect = collects.get(position);
            int houseno = collect.getHouseno();
            final String shouseno = String.valueOf(houseno);
            final String delete = "delete";
            final Map<String,String> params = new HashMap<>();
            params.put("shouseno",shouseno);
            params.put("action",delete);

            builder.setCancelable(false);
            builder.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(
                            Request.Method.POST,
                            VolleyUtil.getAbsoluteUrl("CollectJsonServlet"),
                            params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    try {
                                        if (jsonObject.getString("flag").equals("success")) {
                                            ChangYouUtil.toast(context, "请求已同意成功");
                                        } else {
                                            ChangYouUtil.toast(context, "请求同意失败");
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ChangYouUtil.toast(context, R.string.net_error);
                        }
                    });
                    VolleyUtil.getInstance(context).addToRequestQueue(jsonObjectRequest);
                }
            });

            builder.setNegativeButton("继续收藏",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        private static class ViewHolder{
            private TextView housename;
            private ImageView housephoto;
            private ImageView userimg;
            private TextView price;
            private RatingBar rating;
            private ImageButton btnCollectLess;
        }
    }

}
