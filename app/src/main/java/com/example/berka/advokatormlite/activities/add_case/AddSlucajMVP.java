package com.example.berka.advokatormlite.activities.add_case;

import com.example.berka.advokatormlite.activities.add_case.views_fragments.BaseFragment;
import com.example.berka.advokatormlite.activities.add_case.views_fragments.FragmentNavigation;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.example.berka.advokatormlite.model.TabelaBodova;

import java.util.List;

/**
 * Created by berka on 06-Apr-18.
 */

public interface AddSlucajMVP {

    interface View{
    }

    interface Presenter{

        void setView(AddSlucajMVP.View view);
    }

    interface Model{

        List<TabelaBodova> zaprecenjeKazneZaKrivicu(Postupak postupak);

        Slucaj saveSlucaj(String broj_slucaja, Postupak postupak, TabelaBodova selectedItem, int valueOkrivljenOstecen, int broj_stranaka);

        void saveStrankaDetails(StrankaDetail strankaDetail);
    }
}
