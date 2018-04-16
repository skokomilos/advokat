package com.example.berka.advokatormlite.interfaces;

import android.support.v7.app.AppCompatActivity;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.sql.SQLException;

/**
 * Created by berka on 24-Feb-18.
 */

public interface IAddCasePresenter{

    Postupak loadPostupak(int postupakId);
    void loadZapreceneKazne(int postupakId);
    void saveCasetoDB(String sifraSlucaja, Postupak postupak, TabelaBodova cenaBodova, int brojStranaka);
    void saveCaseToDB(String sifraSlucaja, Postupak postupak, TabelaBodova cenaBodova, int okrivljen_ostecen, int brojStranaka);
}
