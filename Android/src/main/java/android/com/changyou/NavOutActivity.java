package android.com.changyou;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.com.changyou.fragment.HelpNewFragment;
import android.com.changyou.fragment.HousePublishFragment;
import android.com.changyou.fragment.MomentsFragment;
import android.com.changyou.fragment.PersonalMessageFragment;
import android.com.changyou.fragment.UseHelpFragment;
import android.com.changyou.fragment.WhyFragment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavOutActivity extends AppCompatActivity {
    private LinearLayout contentLayout;
    private String num;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_out);

        contentLayout = (LinearLayout)findViewById(R.id.contentLayout);

        Intent intent = getIntent();
        num = intent.getStringExtra("itemNum");

        fm = this.getFragmentManager();
        ft = fm.beginTransaction();



        loadFragment();

    }

    public void backClick(View view){
        onBackPressed();
    }

    private void loadFragment() {
        ft = fm.beginTransaction();
        switch (num){
            case "a":
                ft.replace(R.id.contentLayout, new HelpNewFragment());
                break;
            case "b":
                ft.replace(R.id.contentLayout,new WhyFragment());
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
