package com.example.berka.advokatormlite.activities.add_case.repository_interfaces;

import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;

/**
 * Created by berka on 25-Apr-18.
 */

public interface BottomRepositoryInterface {

    void insertSlucajToDatabase(Slucaj slucaj);

    void insertStrankaDetailsToDatabase(StrankaDetail strankaDetail);
}
