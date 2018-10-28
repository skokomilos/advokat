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
import android.view.autofill.AutofillValue;
import android.view.inputmethod.InputMethodManager;
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

    //todo prva stvar sutra sveStranke prebaciti iz array u listu

    Context context;
    List<StrankaDetail> sveStrankeLista;
    private ViewHolder viewHolder;

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

        //1
        final StrankaDetail stranka = sveStrankeLista.get(i);

        //checking if convetView is null, meaning we have to inflate a new row
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adapter_listastranaka_add_edit, null);

            //Log.v("View", "cheep calling is working");
            Log.d("View", "creating for the first time");
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            //well, we have our row as convertView, so just set viewHolder as that view
            Log.d("View", "view recycled");
            viewHolder = (ViewHolder) convertView.getTag();
        }
       // viewHolder.ref = i;
        //Log.v("View", "cheep calling is awesome");
        viewHolder.populateFrom(sveStrankeLista.get(i));
        View.OnFocusChangeListener listener;
        listener = new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                EditText txtUser = (EditText) v;
                String string = txtUser.getText().toString();
                switch(v.getId()) {
                    case R.id.adapter_svestranke_addedit_imeprezime:
                        stranka.setIme_i_prezime(string);
                        break;
                    case R.id.adapter_svestranke_addedit_adresa:
                        stranka.setAdresa(string);
                        break;
                    case R.id.adapter_svestranke_addedit_mesto:
                        stranka.setMesto(string);
                        break;
                    }
                }
            }
        };
        viewHolder.imeiprezime.setOnFocusChangeListener(listener);
        viewHolder.adresa.setOnFocusChangeListener(listener);
        viewHolder.mesto.setOnFocusChangeListener(listener);

        return convertView;
    }
}

    /**
     * I am using ViewHolder patern
     * Reduce findIVewById calls over and over again
     * Because they are expensive
     */

    class ViewHolder{
        EditText imeiprezime;
        EditText adresa;
        EditText mesto;

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


