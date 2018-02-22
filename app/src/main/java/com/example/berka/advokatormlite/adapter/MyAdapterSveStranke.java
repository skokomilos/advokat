package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 01-Jan-18.
 */

public class MyAdapterSveStranke extends BaseAdapter {

    Context context;
    ArrayList<StrankaDetail> sveStrankeLista;

    public MyAdapterSveStranke(Context context, ArrayList<StrankaDetail> sveStrankeLista) {
        this.context = context;
        this.sveStrankeLista = sveStrankeLista;
    }

    @Override
    public int getCount() {
        return sveStrankeLista.size();
    }

    @Override
    public Object getItem(int i) {
        return sveStrankeLista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adapter_lista_stranaka, null);
        }

        TextView br = convertView.findViewById(R.id.adapter_svestranke_brojredni);
        br.setText(String.valueOf(i + 1));

        TextView imeprezime = convertView.findViewById(R.id.adapter_svestranke_imeprezime);
        imeprezime.setText(String.valueOf(sveStrankeLista.get(i).getIme_i_prezime()));

        TextView adresa = convertView.findViewById(R.id.adapter_svestranke_adresa);
        adresa.setText(String.valueOf(sveStrankeLista.get(i).getAdresa()));

        TextView mesto = convertView.findViewById(R.id.adapter_svestranke_mesto);
        mesto.setText(String.valueOf(sveStrankeLista.get(i).getMesto()));

        return convertView;
    }
}
