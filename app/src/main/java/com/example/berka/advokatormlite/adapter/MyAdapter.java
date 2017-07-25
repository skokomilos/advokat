package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.Postupak;

import java.util.ArrayList;

/**
 * Created by berka on 7/21/2017.
 */

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Postupak> postupci;


    public MyAdapter(Context context, ArrayList<Postupak> postupci) {
        this.context = context;
        this.postupci = postupci;
    }


    public void updateAdapter(ArrayList<Postupak> listaPostupaka){
        this.postupci = listaPostupaka;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return postupci.size();
    }

    @Override
    public Object getItem(int i) {
        return postupci.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adapter, null);
        }

        TextView naziv = convertView.findViewById(R.id.imePostupka);
        naziv.setText(postupci.get(position).getNazivpostupka());

        return convertView;

    }
}
