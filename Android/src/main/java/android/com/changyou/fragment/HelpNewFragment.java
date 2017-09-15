package android.com.changyou.fragment;


import android.com.changyou.LoginInActivity;
import android.com.changyou.LoginUpActivity;
import android.com.changyou.R;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HelpNewFragment extends Fragment {
    private Button loginIn;
    private Button loginUp;

    public HelpNewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_new, container, false);
        loginIn = (Button) view.findViewById(R.id.LoginIn);
        loginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginInActivity.class));
            }
        });
        loginUp = (Button) view.findViewById(R.id.LoginUp);
        loginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginUpActivity.class));
            }
        });
        return view;
    }

}


