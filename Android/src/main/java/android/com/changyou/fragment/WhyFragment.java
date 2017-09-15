package android.com.changyou.fragment;


import android.com.changyou.LoginActivity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.com.changyou.R;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhyFragment extends Fragment {
    private Button btnPublish;


    public WhyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_why, container, false);
        btnPublish = (Button)view.findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return view;
    }


}
