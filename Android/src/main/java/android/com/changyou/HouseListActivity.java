package android.com.changyou;

import android.com.changyou.pojo.House;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonArrayRequest;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HouseListRecyclerViewAdapter adapter;
    private static String cityNames;
    private Button btnSearchMore;
    private Button btnSearch;
    private List<House> houseList;
    private LinearLayout linearLayout;
    private House house;

    private CheckBox cbDJ;
    private CheckBox cbYY;
    private CheckBox cbLY;
    private CheckBox cbSY;
    private CheckBox cbSL;
    private Spinner priceSpinner;
    private String price;

    private RequestQueue mQueue;
    private Map<String, String> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_house);

        btnSearchMore = (Button) findViewById(R.id.btnSearchMore);
        linearLayout = (LinearLayout)findViewById(R.id.checkLinear);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        cbDJ = (CheckBox)findViewById(R.id.cbDJ);
        cbYY = (CheckBox) findViewById(R.id.cbYY);
        cbLY = (CheckBox) findViewById(R.id.cbLY);
        cbSY = (CheckBox) findViewById(R.id.cbSY);
        cbSL = (CheckBox) findViewById(R.id.cbSL);
        priceSpinner = (Spinner)findViewById(R.id.priceSpinner);


        btnSearchMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayout.getVisibility()==View.GONE){
                    linearLayout.setVisibility(View.VISIBLE);
                }else {
                    linearLayout.setVisibility(View.GONE);
                }

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchCondition();
                linearLayout.setVisibility(View.GONE);
            }
        });

        Intent intent = getIntent();
        String cityName = intent.getStringExtra("cityName");
        if (cityName != null) {
            cityNames = cityName;
        }
        // 工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 应用栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(cityNames);
        actionBar.setHomeAsUpIndicator(R.drawable.left);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        params = new HashMap<>();

        setupRecyclerView(recyclerView);

    }

    private void getSearchCondition() {
        params.clear();
        if (cbDJ.isChecked()){
            params.put("a",cbDJ.getText().toString());
        }
        if (cbYY.isChecked()){
            params.put("b",cbYY.getText().toString());
        }
        if (cbLY.isChecked()){
            params.put("c", cbLY.getText().toString());
        }
        if (cbSY.isChecked()){
            params.put("d",cbSY.getText().toString());
        }
        if (cbSL.isChecked()){
            params.put("f",cbSL.getText().toString());
        }

        priceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        price = "500";
                        break;
                    case 2:
                        price = "1000";
                        break;
                    case 3:
                        price = "1500";
                        break;
                    case 4:
                        price = "2500";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        params.put("price", (price != null) ? price : "");
        getHouseMessage();
        adapter = new HouseListRecyclerViewAdapter(this, houseList);
        recyclerView.setAdapter(adapter);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        getHouseMessage();

        Log.v("MainActivity", houseList.toString() + "houseList");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HouseListRecyclerViewAdapter(this, houseList);
        recyclerView.setAdapter(adapter);
    }

    private void getHouseMessage() {
        mQueue = Volley.newRequestQueue(this);
        houseList = new ArrayList<>();
        params.put("address",cityNames);
        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("PropertyJsonServlet"),
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray ja) {
                        try {
                            houseList.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                house = new House();

                                JSONObject jo = ja.getJSONObject(i);

                                house.setPropertyNo(jo.getString("propertyno"));
                                house.setUserId(jo.getString("userid"));
                                house.setPrice(jo.getDouble("price"));
                                house.setStatus(jo.getString("status"));
                                house.setHomeSize(jo.getString("homesize"));
                                house.setPhoto(jo.getString("photo"));
                                house.setLeaseTrem(jo.getString("leaseTrem"));
                                house.setStartDate(jo.getString("startDate"));
                                house.setExpireDate(jo.getString("expireDate"));
                                house.setTotalRent(jo.getString("totalRent"));
                                house.setDeposit(jo.getString("deposit"));
                                house.setPropertyName(jo.getString("propertyname"));
                                house.setAddress(jo.getString("address"));
                                house.setScore(jo.getDouble("score"));
                                house.setUserImgPath(jo.getString("userimgpath"));
                                house.setTel(jo.getString("tel"));
                                house.setUserName(jo.getString("username"));

                                Log.v("MainActivity", house.getUserImgPath());
                                houseList.add(house);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ChangYouUtil.toast(getApplicationContext(), R.string.net_error);
            }
        }
        );
        jsonArrayRequest.setTag(VolleyUtil.TAG);
        mQueue.add(jsonArrayRequest);
    }

    public static class HouseListRecyclerViewAdapter
            extends RecyclerView.Adapter<HouseListRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypeValue = new TypedValue();
        private int mBackground;
        private List<House> houseList;

        public HouseListRecyclerViewAdapter(Context context, List<House> houseList) {
            this.houseList = houseList;
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground,
                    mTypeValue, true);
            mBackground = mTypeValue.resourceId;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_list_item, parent, false);
            view.setBackgroundResource(mBackground);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final House house = houseList.get(position);

            holder.tvName.setText(house.getPropertyName());
            holder.score.setRating((float) house.getScore());
            holder.price.setText(String.valueOf(house.getPrice()));

            ImageLoader imageLoaderFd =
                    VolleyUtil.getInstance(holder.imgFd.getContext()).getImageLoader();
            ImageLoader.ImageListener listenerFd =
                    ImageLoader.getImageListener(
                            holder.imgFd, R.mipmap.ic_launcher, R.drawable.inter_logo);
            imageLoaderFd.get(VolleyUtil.BASE_URL + house.getUserImgPath(), listenerFd);



            ImageLoader imageLoader =
                    VolleyUtil.getInstance(holder.imgPath.getContext()).getImageLoader();
            ImageLoader.ImageListener listener =
                    ImageLoader.getImageListener(
                            holder.imgPath, R.mipmap.ic_launcher, R.drawable.inter_logo);
            imageLoader.get(VolleyUtil.BASE_URL + house.getPhoto(), listener);


            holder.imgPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("MainActivity", "点击" + position);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, HouseDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("house", getHouseAt(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });


            holder.btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    String userid = String.valueOf(ChangYouUtil.getUserId(context));
                    String username = ChangYouUtil.getUsername(context);

                    if (userid.length() > 0 && username.length() > 0) {
                        ChangYouUtil.toast(v.getContext(), "已收藏房屋:" + getHouseAt(position).getPropertyName());

                        String housephoto = getHouseAt(position).getPhoto();
                        String houseno = getHouseAt(position).getPropertyNo();
                        String userimg = getHouseAt(position).getUserImgPath();
                        String price = String.valueOf(getHouseAt(position).getPrice());
                        String housename = getHouseAt(position).getPropertyName();
                        String score = String.valueOf(getHouseAt(position).getScore());

                        Map<String, String> collect = new HashMap<>();

                        collect.put("housephoto", housephoto);
                        collect.put("userimg", userimg);
                        collect.put("price", price);
                        collect.put("housename", housename);
                        collect.put("score", score);
                        collect.put("userid", userid);
                        collect.put("username", username);
                        collect.put("houseno", houseno);

                        Log.v("MainActivity", collect.get("username"));
                        MyJsonObjectRequest request = new MyJsonObjectRequest(
                                Request.Method.POST,
                                VolleyUtil.getAbsoluteUrl("CollectJsonServlet"),
                                collect,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        try {
                                            if (jsonObject.getString("flag").equals("success")) {
                                                ChangYouUtil.toast(context, "收藏成功.");
                                            } else if (jsonObject.getString("flag").equals("failed")) {
                                                ChangYouUtil.toast(context, "收藏失败.");
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
                                }
                        );
                        // 把 Json请求对象 加入到 Volley 的请求队列 (默认的TAG)
                        VolleyUtil.getInstance(context).addToRequestQueue(request);
                    } else {
                        ChangYouUtil.toast(context, "你还没有登录用户，无法进行收藏!");
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return houseList.size();
        }

        public House getHouseAt(int position) {
            return houseList.get(position);
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView imgPath;
            public final TextView tvName;
            public final TextView price;
            public final RatingBar score;
            public final ImageView imgFd;
            public final ImageButton btnCollect;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                tvName = (TextView) view.findViewById(R.id.tvHouseName);
                imgPath = (ImageView) view.findViewById(R.id.imgHouse);
                price = (TextView)view.findViewById(R.id.tvHousePrice);
                score = (RatingBar)view.findViewById(R.id.ratingBar);
                imgFd = (ImageView)view.findViewById(R.id.imgFd);
                btnCollect = (ImageButton)view.findViewById(R.id.btnCollect);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

}
