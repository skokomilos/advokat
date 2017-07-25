package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.Tarifa;

import java.util.ArrayList;

/**
 * Created by berka on 7/21/2017.
 */

public class MyAdapterTarifa extends BaseAdapter{

    ArrayList<Tarifa> tarife;
    Context context;

    public MyAdapterTarifa(Context context, ArrayList<Tarifa> tarife) {
        this.tarife = tarife;
        this.context = context;
    }

    public MyAdapterTarifa() {

    }

    public void updateAdapter(ArrayList<Tarifa> listaTarifa){
        this.tarife = listaTarifa;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tarife.size();
    }

    @Override
    public Object getItem(int i) {
        return tarife.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adapter_tarifa, null);
        }
        TextView naziv = convertView.findViewById(R.id.imeTarife);
        naziv.setText(tarife.get(position).getNaslov_tarife());

        return convertView;
    }
}
