package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by berka on 11-Oct-17.
 */

public class SviTroskviAdapter extends BaseAdapter {

    Context context;
    ArrayList<IzracunatTrosakRadnje> listaRadnji;

    public SviTroskviAdapter(Context context, ArrayList<IzracunatTrosakRadnje> listaRadnji) {

        this.context = context;
        this.listaRadnji = listaRadnji;
    }

    public void updateAdapter(ArrayList<IzracunatTrosakRadnje> listaRadnji){
        this.listaRadnji = listaRadnji;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listaRadnji.size();
    }

    @Override
    public Object getItem(int i) {
        return listaRadnji.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adapter_ukupan_trosak, null);
        }

        TextView nazivFilma = convertView.findViewById(R.id.adapter_tv_izvrsena_radnja);
        nazivFilma.setText(String.valueOf(listaRadnji.get(i).getNaziv_radnje()));

        TextView cena = convertView.findViewById(R.id.adapter_tv_izvrsena_cena);
        cena.setText(String.valueOf(listaRadnji.get(i).getCena_izracunate_jedinstvene_radnje()));

        return convertView;
    }
}
