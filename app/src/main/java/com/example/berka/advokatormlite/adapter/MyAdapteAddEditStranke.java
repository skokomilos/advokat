package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.StrankaDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by berka on 02-Jan-18.
 */

public class MyAdapteAddEditStranke extends BaseAdapter {

    Context context;
    ArrayList<StrankaDetail> sveStrankeLista;

    public MyAdapteAddEditStranke(Context context, ArrayList<StrankaDetail> sveStrankeLista) {
        this.context = context;
        this.sveStrankeLista = sveStrankeLista;
    }

    @Override
    public int getCount() {
        return sveStrankeLista.size();
    }

    @Override
    public StrankaDetail getItem(int i) {
        return sveStrankeLista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adapter_listastranaka_add_edit, null);

            Log.v("View", "cheep calling is working");
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ref = i;
        Log.v("View", "cheep calling is awesome");
        viewHolder.populateFrom(sveStrankeLista.get(i));

        viewHolder.imeiprezime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {


                sveStrankeLista.get(i).setIme_i_prezime(String.valueOf(viewHolder.imeiprezime.getText()));
                Log.v("Ladnoradi", sveStrankeLista.get(i).getIme_i_prezime());
            }
        });

        viewHolder.adresa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                sveStrankeLista.get(i).setAdresa(String.valueOf(viewHolder.adresa.getText()));
                Log.v("Ladnoradi", sveStrankeLista.get(i).getAdresa());
            }
        });

        viewHolder.mesto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                sveStrankeLista.get(i).setMesto(String.valueOf(viewHolder.mesto.getText()));
                Log.v("Ladnoradi",  sveStrankeLista.get(i).getMesto());
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        EditText imeiprezime;
        EditText adresa;
        EditText mesto;
        int ref;

        ViewHolder(View row) {
            imeiprezime = row.findViewById(R.id.adapter_svestranke_addedit_imeprezime);
            adresa = row.findViewById(R.id.adapter_svestranke_addedit_adresa);
            mesto = row.findViewById(R.id.adapter_svestranke_addedit_mesto);
        }

        void populateFrom(StrankaDetail strankaDetail) {

            imeiprezime.setText(strankaDetail.getIme_i_prezime());
            adresa.setText(strankaDetail.getAdresa());
            mesto.setText(strankaDetail.getMesto());
        }


    }
}


