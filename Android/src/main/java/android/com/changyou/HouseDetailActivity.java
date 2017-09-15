package android.com.changyou;

import android.com.changyou.pojo.House;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.MyJsonArrayRequest;
import android.com.changyou.util.MyJsonObjectRequest;
import android.com.changyou.util.VolleyUtil;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseDetailActivity extends AppCompatActivity {
    private List<String> imgResIDs = new ArrayList<>();
    private ViewPager pager;
    private ArrayList<View> items = new ArrayList<>();
    private int mCurrentItem = 0;
    private int mItem;
    private Runnable mPagerAction;
    private boolean isFrist = true;
    private static House house;
    private static String houseName;


    private TextView tvHousePrice;
    private TextView tvHouseName;
    private TextView starScore;
    private TextView tvLordName;
    private TextView tvHouseType;
    private TextView tvHomeSize;
    private TextView tvHomeAddress;
    private ImageView imgUsername;
    private RatingBar ratingBar;
    private Button btnCallLord;
    private ListView likeListView;
    private House likeHouse;
    private LikeListAdapter adapter;

    private RequestQueue mQueue;
    private Map<String, String> params;
    private List<House> houseList;


    private TextView tvHouseTV;
    private TextView tvHouseKT;
    private TextView tvHouseXYJ;
    private TextView tvHouseWifi;
    private TextView tvHouseInternet;
    private TextView tvHouseBX;
    private String checked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        Intent intent = getIntent();
        final House house = (House) intent.getSerializableExtra("house");
        if (house != null) {
            this.house = house;
            this.houseName = house.getPropertyName();
        }

        tvHousePrice = (TextView) findViewById(R.id.tvHousePrice);
        tvHouseName = (TextView) findViewById(R.id.tvHouseName);
        starScore = (TextView) findViewById(R.id.starScore);
        tvLordName = (TextView) findViewById(R.id.tvLordName);
        tvHouseType = (TextView) findViewById(R.id.tvHouseType);
        tvHomeSize = (TextView) findViewById(R.id.tvHomeSize);
        tvHomeAddress = (TextView)findViewById(R.id.tvHomeAddress);
        btnCallLord = (Button) findViewById(R.id.btnCallLord);
        imgUsername = (ImageView) findViewById(R.id.imgUsername);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        likeListView = (ListView)findViewById(R.id.likeListView);

        tvHouseTV = (TextView)findViewById(R.id.tvHouseTV);
        tvHouseKT = (TextView)findViewById(R.id.tvHouseKT);
        tvHouseXYJ = (TextView)findViewById(R.id.tvHouseXYJ);
        tvHouseWifi = (TextView)findViewById(R.id.tvHouseWifi);
        tvHouseInternet = (TextView)findViewById(R.id.tvHouseInternet);
        tvHouseBX = (TextView)findViewById(R.id.tvHouseBX);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 应用栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(houseName);
        actionBar.setHomeAsUpIndicator(R.drawable.left);

        setHouseMessage();

        pager = (ViewPager) findViewById(R.id.house_img_pager);
        imgResIDs.add("a.jpg");
        imgResIDs.add("b.jpg");
        imgResIDs.add("c.jpg");
        imgResIDs.add("d.jpg");


        initAllItems();
        getLikeHouse();
        getFacility();

        adapter = new LikeListAdapter(getApplicationContext(),houseList);
        likeListView.setAdapter(adapter);

        pager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(View container, int position) {
                View layout = items.get(position % items.size());
                pager.addView(layout);
                return layout;
            }


            @Override
            public void destroyItem(View container, int position, Object object) {
                View layout = items.get(position % items.size());
                pager.removeView(layout);
            }


            @Override
            public int getCount() {
                return imgResIDs.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        mPagerAction = new Runnable() {
            @Override
            public void run() {

                if (mItem != 0) {
                    if (isFrist == true) {
                        mCurrentItem = 0;
                        isFrist = false;
                    } else {
                        if (mCurrentItem == items.size() - 1) {
                            mCurrentItem = 0;
                        } else {
                            mCurrentItem++;
                        }
                    }

                    pager.setCurrentItem(mCurrentItem);

                }
                pager.postDelayed(mPagerAction, 3000);
            }
        };
        pager.postDelayed(mPagerAction, 100);
}

    private void getFacility() {
        mQueue = Volley.newRequestQueue(this);
        String propertyno = house.getPropertyNo();
        MyJsonObjectRequest request = new MyJsonObjectRequest(
                VolleyUtil.getAbsoluteUrl("FacilityJsonServlet?propertyno="+propertyno),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                                String tv = (jsonObject.getString("tv"))!=null?(jsonObject.getString("tv")):"Y";
                                String aircondition = (jsonObject.getString("aircondition"))!=null?(jsonObject.getString("aircondition")):"Y";
                                String washer = (jsonObject.getString("washer")!=null)?(jsonObject.getString("washer")):"Y";
                                String network = (jsonObject.getString("network")!=null)?(jsonObject.getString("network")):"Y";
                                String computer = (jsonObject.getString("computer")!=null)?(jsonObject.getString("computer")):"Y";
                                String dryer = (jsonObject.getString("dryer")!=null)?(jsonObject.getString("dryer")):"Y";

                                check(aircondition,tvHouseKT);
                                check(tv,tvHouseTV);
                                check(washer,tvHouseXYJ);
                                check(network,tvHouseWifi);
                                check(computer,tvHouseInternet);
                                check(dryer,tvHouseBX);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ChangYouUtil.toast(getApplicationContext(),R.string.net_error);
            }
        });
        mQueue.add(request);
    }

    private void check(String facility,TextView tvfacility) {
        if (facility.equals("Y")){
            tvfacility.setVisibility(View.VISIBLE);
        }else {
            tvfacility.setVisibility(View.GONE);
        }
    }

    private void setHouseMessage() {
        tvHousePrice.setText(String.valueOf(house.getPrice()));
        tvHomeSize.setText(house.getHomeSize());
        tvHomeAddress.setText(house.getAddress());
        tvHouseName.setText(house.getPropertyName());
        starScore.setText(String.valueOf(house.getScore()));
        ratingBar.setRating((float) house.getScore());
        tvHouseType.append(house.getStatus());
        tvLordName.append(house.getUserName());


        ImageLoader imageLoader =
                VolleyUtil.getInstance(this).getImageLoader();
        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(imgUsername,
                        R.drawable.logo2,
                        R.drawable.logo2);
        imageLoader.get(VolleyUtil.BASE_URL + house.getUserImgPath(), listener);


        btnCallLord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + house.getTel()));
                startActivity(intent);
            }
        });


    }

    private void getLikeHouse() {
        mQueue = Volley.newRequestQueue(this);
        params = new HashMap<>();
        houseList = new ArrayList<>();
        String price = String.valueOf(house.getPrice());
        String address = house.getAddress().substring(0,2);
        params.put("price", price);
        params.put("address", address);
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
                                likeHouse = new House();

                                JSONObject jo = ja.getJSONObject(i);

                                likeHouse.setPropertyNo(jo.getString("propertyno"));
                                likeHouse.setUserId(jo.getString("userid"));
                                likeHouse.setPrice(jo.getDouble("price"));
                                likeHouse.setStatus(jo.getString("status"));
                                likeHouse.setHomeSize(jo.getString("homesize"));
                                likeHouse.setPhoto(jo.getString("photo"));
                                likeHouse.setLeaseTrem(jo.getString("leaseTrem"));
                                likeHouse.setStartDate(jo.getString("startDate"));
                                likeHouse.setExpireDate(jo.getString("expireDate"));
                                likeHouse.setTotalRent(jo.getString("totalRent"));
                                likeHouse.setDeposit(jo.getString("deposit"));
                                likeHouse.setPropertyName(jo.getString("propertyname"));
                                likeHouse.setAddress(jo.getString("address"));
                                likeHouse.setScore(jo.getDouble("score"));
                                likeHouse.setUserImgPath(jo.getString("userimgpath"));
                                likeHouse.setTel(jo.getString("tel"));
                                likeHouse.setUserName(jo.getString("username"));

                                Log.v("MainActivity", likeHouse.getUserImgPath());
                                houseList.add(likeHouse);
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


    private void initAllItems() {
        for (int i = 0; i < imgResIDs.size(); i++) {
            items.add(initPagerItem(imgResIDs.get(i)));
        }
        mItem = items.size();
    }

    private View initPagerItem(String resID) {
        View layout1 = getLayoutInflater().inflate(R.layout.house_header, null);
        ImageView imageView1 = (ImageView) layout1.findViewById(R.id.house_header_img);

        ImageLoader imageLoader =
                VolleyUtil.getInstance(this).getImageLoader();
        ImageLoader.ImageListener listener =
                ImageLoader.getImageListener(imageView1,
                        R.drawable.logo2,
                        R.drawable.logo2);
        imageLoader.get(VolleyUtil.BASE_URL + "images/house/" + resID, listener);
        return layout1;
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnBooked:
                if (ChangYouUtil.isLogined(getApplicationContext())) {

                        Intent intent = new Intent(this, BookedActivity.class);
                        intent.putExtra("houseNo", house.getPropertyNo());
                        intent.putExtra("userId", house.getUserId());
                        intent.putExtra("userName", house.getUserName());
                        startActivity(intent);
                } else {
                    ChangYouUtil.toast(getApplicationContext(), "请先登录！");
                    startActivity(new Intent(this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                }

                break;
        }
    }

    private void checkBooking() {
        Map<String,String> checkMap = new HashMap<>();
        String username = ChangYouUtil.getUsername(this);
        String action = "check";
        checkMap.put("username", username);
        checkMap.put("action",action);
        checked = "";
        MyJsonObjectRequest request = new MyJsonObjectRequest(
                Request.Method.POST,
                VolleyUtil.getAbsoluteUrl("ApplyJsonServlet"),
                checkMap,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                           checked = jsonObject.getString("flag");
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


    }




    public static class LikeListAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private Context context;
        private List<House> houseList;
        private House house;

        public LikeListAdapter(Context context, List<House> houseList) {
            this.context = context;
            this.houseList = houseList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return houseList.size();
        }

        @Override
        public Object getItem(int position) {
            return houseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView = inflater.inflate(R.layout.house_like_item, parent, false);
                holder = new ViewHolder();
                holder.likeHouseImg = (ImageView)convertView.findViewById(R.id.likeHouseImg);
                holder.likeHouseName = (TextView)convertView.findViewById(R.id.likeHouseName);
                holder.likeHouseLord = (TextView)convertView.findViewById(R.id.likeHouseLord);
                holder.likeHousePrice=(TextView)convertView.findViewById(R.id.likeHousePrice);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            house = houseList.get(position);

            holder.likeHouseName.setText(house.getPropertyName().trim());
            holder.likeHouseLord.setText(house.getUserName().trim());
            holder.likeHousePrice.setText(String.valueOf(house.getPrice()));

            ImageLoader imageLoaderLike =
                    VolleyUtil.getInstance(holder.likeHouseImg.getContext()).getImageLoader();
            ImageLoader.ImageListener listenerFd =
                    ImageLoader.getImageListener(
                            holder.likeHouseImg, R.mipmap.ic_launcher, R.drawable.inter_logo);
            imageLoaderLike.get(VolleyUtil.BASE_URL + house.getPhoto(), listenerFd);


            return convertView;
        }

        private static class ViewHolder {
           private ImageView likeHouseImg;
           private TextView likeHouseName;
           private TextView likeHouseLord;
           private TextView likeHousePrice;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
