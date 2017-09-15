package android.com.changyou.fragment;

import android.com.changyou.HouseListActivity;
import android.com.changyou.R;
import android.com.changyou.pojo.Citys;
import android.com.changyou.util.VolleyUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CityListFragment extends Fragment {
    private List<Citys> citysList;
    private RecyclerView.Adapter adapter;
    private ImageButton imgCitySearch;
    private EditText searchCity;
    private Citys citys;

    private RequestQueue mQueue;

    public CityListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);
        imgCitySearch = (ImageButton) view.findViewById(R.id.imgSearch);
        searchCity = (EditText) view.findViewById(R.id.searchCity);

        imgCitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = searchCity.getText().toString();
                for(int i=0;i<citysList.size();i++){
                    if(city.trim().equals(citysList.get(i).getCityName())){
                        Intent intent = new Intent(getActivity(), HouseListActivity.class);
                        intent.putExtra("cityName", city);
                        startActivity(intent);
                        return;
                    }
                }
                Toast.makeText(getActivity(),"暂不支持这个城市",Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        setupRecyclerView(recyclerView);

        return view;
    }

    public void getImgAndCity() {
        mQueue = Volley.newRequestQueue(getActivity());
        citysList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                VolleyUtil.getAbsoluteUrl("CityJsonServlet"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray ja) {
                        try {
                            citysList.clear();
                            for (int i = 0; i < ja.length(); i++) {
                                citys = new Citys();
                                JSONObject jo = ja.getJSONObject(i);
                                citys.setCityName(jo.getString("cityname"));
                                citys.setImgPath(jo.getString("citypath"));
                                citysList.add(citys);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       // ChangYouUtil.toast(getActivity(), "网络连接失败");
                    }
                });
        jsonArrayRequest.setTag(VolleyUtil.TAG);
        mQueue.add(jsonArrayRequest);
    }


    private void setupRecyclerView(RecyclerView view) {
        getImgAndCity();
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CityListRecyclerViewAdapter(getActivity(), citysList);
        view.setAdapter(adapter);
    }

    public static class CityListRecyclerViewAdapter
            extends RecyclerView.Adapter<CityListRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypeValue = new TypedValue();
        private int mBackground;
        private List<Citys> citysList;

        public CityListRecyclerViewAdapter(Context context, List<Citys> citysList) {
            this.citysList = citysList;
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground,
                    mTypeValue, true);
            mBackground = mTypeValue.resourceId;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item, parent, false);
            view.setBackgroundResource(mBackground);

            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Citys city = citysList.get(position);

            holder.tvName.setText(city.getCityName());

            ImageLoader imageLoader =
                    VolleyUtil.getInstance(holder.imgPath.getContext()).getImageLoader();
            ImageLoader.ImageListener listener =
                    ImageLoader.getImageListener(
                            holder.imgPath, R.mipmap.ic_launcher, R.drawable.inter_logo);
            imageLoader.get(VolleyUtil.BASE_URL+city.getImgPath(), listener);


            holder.imgPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("MainActivity", "点击" + position);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, HouseListActivity.class);
                    intent.putExtra("cityName", getCityAt(position).getCityName());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return citysList.size();
        }

        public Citys getCityAt(int position) {
            return citysList.get(position);
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView imgPath;
            public final TextView tvName;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                tvName = (TextView) view.findViewById(R.id.tvCity);
                imgPath = (ImageView) view.findViewById(R.id.imgCity);
            }
        }
    }

}
