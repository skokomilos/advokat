package com.example.berka.advokatormlite.activities.add_case;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 06-Apr-18.
 */

public interface AddKrivicaRepository {

    List<TabelaBodova> queryForAllZapreceneKazneForKrivica(Postupak postupak);

    Slucaj insertObjectSlucaj(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka);
}
