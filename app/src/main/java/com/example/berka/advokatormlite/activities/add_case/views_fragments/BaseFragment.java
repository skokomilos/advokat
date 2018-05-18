package com.example.berka.advokatormlite.activities.add_case.views_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by berka on 19-Apr-18.
 */

public abstract class BaseFragment extends Fragment{

    protected View rootView;

    protected FragmentNavigation.Presenter navigationPresenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        return rootView;
    }


    protected abstract int getLayout();
//
//    @Override
//    public void atachPresenter(FragmentNavigation.Presenter presenter) {
//
//        navigationPresenter = presenter;
//    }
}
