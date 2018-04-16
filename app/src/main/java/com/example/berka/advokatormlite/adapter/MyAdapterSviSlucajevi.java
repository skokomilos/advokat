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

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by berka on 23-Nov-17.
 */

public class MyAdapterSviSlucajevi extends BaseAdapter {

    Context context;
    List<Slucaj> mSlucajevi;
    List<Double> izracunateTarife;


    public MyAdapterSviSlucajevi(Context context, List<Slucaj> slucajevi, List<Double> izracunateTarife) {
        this.context = context;
        this.mSlucajevi = slucajevi;
        this.izracunateTarife = izracunateTarife;
    }


    public void replaceData(List<Slucaj> slucajevi) {
        setList(slucajevi);
        notifyDataSetChanged();
    }

    private void setList(List<Slucaj> slucajevi) {
        mSlucajevi = (ArrayList<Slucaj>) slucajevi;
    }

    @Override
    public int getCount() {
        return mSlucajevi.size();
    }

    @Override
    public Object getItem(int position) {
        return mSlucajevi.get(position);
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
        sif_slucaja.setText(String.valueOf("Sifra slucaja: " + mSlucajevi.get(position).getBroj_slucaja()));

        TextView vrsta_postupka = convertView.findViewById(R.id.adr_vrsta_postupka);
        vrsta_postupka.setText(String.valueOf("Vrsta postupka: " + mSlucajevi.get(position).getPostupak()));

        TextView trenutva_vrednost = convertView.findViewById(R.id.adr_trenutna_vrednost);
        trenutva_vrednost.setText(String.valueOf("Trenutna vrednost: " + izracunateTarife.get(position)));

        return convertView;
    }
}
