package com.example.berka.advokatormlite.interfaces;

import android.content.Context;
import android.widget.GridLayout;

import com.example.berka.advokatormlite.adapter.StrankaDynamicViews;

/**
 * Created by berka on 15-Apr-18.
 */

public interface DynamicalEditTexts extends BaseViewForAddCases{

    void addredniBrojStranke(int redni_broj);

    void addEditTextZaImeStraneke();

    void addEditTextZaAdresu();

    void addEditTextZaMesto();

    String getClientName(int redni_broj_edit_texta_u_grid_layout);

    String getClientAddress(int i);

    String getClientPlace(int i);
}
