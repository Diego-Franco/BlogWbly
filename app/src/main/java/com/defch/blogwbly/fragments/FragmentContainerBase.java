package com.defch.blogwbly.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.View;

import com.defch.blogwbly.MyApplication;
import com.defch.blogwbly.ui.WeeblyThemes;

import butterknife.ButterKnife;

/**
 * Created by DiegoFranco on 4/18/15.
 */
public class FragmentContainerBase extends Fragment {

    public MyApplication app;
    public WeeblyThemes theme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = MyApplication.getInstance(getActivity().getApplicationContext());
        theme = app.getWTheme();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroyView();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
