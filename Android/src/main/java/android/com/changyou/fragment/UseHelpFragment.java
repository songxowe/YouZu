package android.com.changyou.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.com.changyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UseHelpFragment extends Fragment {


    public UseHelpFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_use_help, container, false);
    }


}
