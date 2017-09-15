package android.com.changyou;

import android.app.NotificationManager;
import android.com.changyou.fragment.CityListFragment;
import android.com.changyou.fragment.CollectListFragment;
import android.com.changyou.fragment.MassageListFragment;
import android.com.changyou.fragment.JourneyListFragment;
import android.com.changyou.polling.PollingService;
import android.com.changyou.polling.PollingUtils;
import android.com.changyou.util.ChangYouUtil;
import android.com.changyou.util.VolleyUtil;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private String username;
    private String imgPath;
    private long exitTime;
    private static NavigationView navView;


    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        PollingUtils.startPollingService(
                this, 30, PollingService.class, PollingService.ACTION);

        // 抽屉布局
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 应用栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.user_png_48px);

        // 导航视图
        navView = (NavigationView) findViewById(R.id.nav_view);
        if (navView != null) {

            setDrawerContent(navView);
        }

        // 滑动分页
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setViewPage(viewPager);
        }

        // 选项卡布局 关联 滑动分页
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    private void setViewPage(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        adapter.addFragment(new CityListFragment(), getString(R.string.city_name));
        adapter.addFragment(new CollectListFragment(), getString(R.string._collection));
        adapter.addFragment(new MassageListFragment(), getString(R.string._massage));
        adapter.addFragment(new JourneyListFragment(), getString(R.string._journey));

        viewPager.setAdapter(adapter);
    }

    private void setDrawerContent(NavigationView navView) {
        View view = View.inflate(this,R.layout.nav_header,navView);
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        username = ChangYouUtil.getUsername(this);
        imgPath = ChangYouUtil.getImgPath(this);



        Log.v(ChangYouUtil.TAG, imgPath+"......");
        if (username.length()>0){
            tvUsername.setText(username);
        }

        // 设置当前登录的用户图片
        ImageView imgUsername = (ImageView)navView.findViewById(R.id.imgUsername);
        imgUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,LoadImgActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("imgPath", imgPath);

                startActivity(intent);
            }
        });
        //来自选项存储的图片路径
        final String imgPath = ChangYouUtil.getImgPath(this);

        if (imgPath.length()>0){
            ImageLoader imageLoader =
                    VolleyUtil.getInstance(this).getImageLoader();
            ImageLoader.ImageListener listener =
                    ImageLoader.getImageListener(imgUsername,
                            R.drawable.logo2,
                            R.drawable.logo2);
            imageLoader.get(VolleyUtil.BASE_URL+imgPath, listener);
        }

        // -- 左侧抽屉导航视图 菜单 ------------------------------
        // 导航视图中的菜单选中事件
        final boolean flag = ChangYouUtil.isLogined(getApplicationContext());
        if (!flag){
            navView.getMenu().findItem(R.id.action_login).setTitle(R.string.action_qr_code);
            navView.getMenu().findItem(R.id.nav_home).setTitle(R.string.nav_home);
            navView.getMenu().findItem(R.id.nav_messages).setTitle(R.string.nav_messages);
        }else {
            navView.getMenu().findItem(R.id.action_login).setTitle(R.string.action_logout);
            navView.getMenu().findItem(R.id.action_login).setIcon(R.drawable.out_48px);
            navView.getMenu().findItem(R.id.nav_home).setTitle(R.string.nav_user);
            navView.getMenu().findItem(R.id.nav_messages).setTitle(R.string.nav_lord);
        }
        // -- 左侧抽屉导航视图 菜单 ------------------------------
        // 导航视图中的菜单选中事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent = new Intent();
                if(!flag){
                    intent.setClass(getApplicationContext(),NavOutActivity.class);
                }else {
                    intent.setClass(getApplicationContext(),NavActivity.class);
                }
                switch (menuItem.getItemId()) {
                    // 匹配菜单的选项 (其它选项的处理 省略)
                    case R.id.action_about:
                        aboutDialog();
                        break;
                    case R.id.action_login:
                        if (!flag) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            showAlertDialog();
                        }
                        break;
                    case R.id.nav_messages:
                        intent.putExtra("itemNum","b");
                        startActivity(intent);
                        break;
                    case R.id.nav_home:
                        intent.putExtra("itemNum","a");
                        startActivity(intent);
                        break;
                    case R.id.nav_friends:
                        intent.putExtra("itemNum","c");
                        startActivity(intent);
                        break;
                    case R.id.nav_discussion:
                        intent.putExtra("itemNum","d");
                        startActivity(intent);
                        break;

                }
                // 选择后自动关闭左侧抽屉
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void aboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("关于这个APP");
        builder.setIcon(R.drawable.about_app);
        builder.setMessage(R.string.about_app);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("不再看会了嘛？");

        //不可取消(默认true)
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChangYouUtil.savePreferences(getApplicationContext(), 0, "", "", "", "");
                finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //创建对话框并显示
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 点击左上角图标,打开抽屉
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        // Fragment 集合
        private final List<Fragment> fragments = new ArrayList<>();
        // Fragment Title 集合
        private final List<String> fragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        // 添加 Fragment 和 Fragment Title
        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PollingUtils.stopPollingService(
                this, PollingService.class, PollingService.ACTION);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
