package com.example.berka.advokatormlite.activities.add_case.presenters;

/**
 * Created by berka on 12-Jun-18.
 */

public interface BasePresenter<T> {

    /**
     * Binds presenter with a view when resumed. The Presenter will perform initialization here.
     *
     * @param view the view associated with this presenter
     */
    void setView(T view);

    /**
     * Drops the reference to the view when destroyed
     */
    void detachView();
}
