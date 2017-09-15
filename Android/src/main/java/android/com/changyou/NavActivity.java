package android.com.changyou;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.com.changyou.fragment.HousePublishFragment;
import android.com.changyou.fragment.MomentsFragment;
import android.com.changyou.fragment.PersonalMessageFragment;
import android.com.changyou.fragment.UseHelpFragment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

public class NavActivity extends AppCompatActivity {
    private LinearLayout contentLayout;
    private String num;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        contentLayout = (LinearLayout)findViewById(R.id.contentLayout);

        Intent intent = getIntent();
        num = intent.getStringExtra("itemNum");
        String name = num.equals("a")?"个人信息":(num.equals("b")?"发布房源":"帮助");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 应用栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(name);
        actionBar.setHomeAsUpIndicator(R.drawable.left);

        fm = this.getFragmentManager();
        ft = fm.beginTransaction();



        loadFragment();

    }

    private void loadFragment() {
        ft = fm.beginTransaction();
        switch (num){
            case "a":
                ft.replace(R.id.contentLayout, new PersonalMessageFragment());
                break;
            case "b":
                ft.replace(R.id.contentLayout,new HousePublishFragment());

                break;
            case "c":
                ft.replace(R.id.contentLayout,new MomentsFragment());
                break;
            case "d":
                ft.replace(R.id.contentLayout,new UseHelpFragment());
                break;
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
