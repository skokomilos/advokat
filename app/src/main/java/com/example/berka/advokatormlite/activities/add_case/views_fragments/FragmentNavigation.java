package com.example.berka.advokatormlite.activities.add_case.views_fragments;

/**
 * Created by berka on 19-Apr-18.
 */

public interface FragmentNavigation {

    interface View {
        void atachPresenter(Presenter presenter);
    }

    interface Presenter {

        void addFragment(BaseFragment fragment);
    }
}
