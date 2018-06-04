package com.example.berka.advokatormlite.activities.add_case;

import com.example.berka.advokatormlite.model.StrankaDetail;

/**
 * Created by berka on 29-Apr-18.
 */

public interface AddSlucajRepositoryInterface {

    void insertStrankaDetailToDatabase(StrankaDetail strankaDetail);
}
