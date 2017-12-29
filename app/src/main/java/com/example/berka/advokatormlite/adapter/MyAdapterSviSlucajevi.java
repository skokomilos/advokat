package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.Slucaj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 23-Nov-17.
 */

public class MyAdapterSviSlucajevi extends BaseAdapter {

    Context context;
    ArrayList<Slucaj> slucajevi;
    List<Double> izracunateTarife;


    public MyAdapterSviSlucajevi(Context context, ArrayList<Slucaj> slucajevi, List<Double> izracunateTarife) {
        this.context = context;
        this.slucajevi = slucajevi;
        this.izracunateTarife = izracunateTarife;
    }

    @Override
    public int getCount() {
        return slucajevi.size();
    }

    @Override
    public Object getItem(int position) {
        return slucajevi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RecyclerView.ViewHolder holder;
        SurfaceHolder surfaceHolder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adapter_lista_slucajeva, null);
        }

        TextView sif_slucaja = convertView.findViewById(R.id.adr_sifra_slucaja);
        sif_slucaja.setText(String.valueOf(slucajevi.get(position).getBroj_slucaja()));

        TextView trenutva_vrednost = convertView.findViewById(R.id.adr_trenutna_vrednost);
        trenutva_vrednost.setText(String.valueOf(izracunateTarife.get(position)));

        return convertView;
    }
}
