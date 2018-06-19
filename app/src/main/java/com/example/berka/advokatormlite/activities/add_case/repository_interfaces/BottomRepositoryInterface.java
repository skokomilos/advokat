package com.example.berka.advokatormlite.activities.add_case.repository_interfaces;

import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;

import java.sql.SQLException;

/**
 * Created by berka on 25-Apr-18.
 */

public interface BottomRepositoryInterface {

    boolean insertSlucajToDatabase(Slucaj slucaj) throws SQLException;

    void insertStrankaDetailsToDatabase(StrankaDetail strankaDetail);
}
