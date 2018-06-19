package com.example.berka.advokatormlite.activities.add_case.mvp_contracts;

/**
 * Created by berka on 12-Jun-18.
 */

public interface ActivityView extends ContentView{

    /**
     * Shows a toast in the Activity with a short time
     * @param msg   Message to show
     */
    void onShowToast(String msg);

    /**
     * Shows a {@link android.widget.Toast} in the Activity with custom time
     * @param msg       Message to show
     * @param duration  Time Length
     *                      {@link android.widget.Toast#LENGTH_SHORT}
     *                      {@link android.widget.Toast#LENGTH_LONG}
     */
    void onShowToast(String msg, int duration);
}
