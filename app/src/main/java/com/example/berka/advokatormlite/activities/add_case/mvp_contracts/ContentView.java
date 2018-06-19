package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

import android.content.Context;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentForDynamicEditText;

/**
 * Created by berka on 12-Jun-18.
 */

public interface ContentView {

    /**
     * Access to application {@link Context}
     * @return  application context
     */
    Context getApplicationContext();

    /**
     * Access to current activity {@link Context}
     * @return  activity context
     */
    FragmentForDynamicEditText getActivityContext();
}
