package com.example.berka.advokatormlite.activities.add_case.views_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.berka.advokatormlite.R;

/**
 * Created by berka on 17-Apr-18.
 */

public class UpperFragmentUstavniSud  extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.upper_fragment_ustavni_sud, container, false);

        return rootView;
    }

    @Override
    protected int getLayout() {
        return 0;
    }
}