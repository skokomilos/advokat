package com.example.berka.advokatormlite.activities.add_case.repository_interfaces;

import android.support.design.widget.TabLayout;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 22-Jun-18.
 */

public interface UstavniSudRepositoryInterface {
    TabelaBodova loadZaprecenaKazna(Postupak postupak);
}
