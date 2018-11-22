package com.example.berka.advokatormlite.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.model.Radnja;
import com.example.berka.advokatormlite.model.Tarifa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableAdapterRadnja1 extends BaseExpandableListAdapter {


    private Context context;

    private List<Tarifa> listDataHeader;
    private List<Tarifa> originalListHeader;

    private HashMap<Tarifa, List<Radnja>> listHashMap;
    private HashMap<Tarifa, List<Radnja>> originalHashMap;

    public ExpandableAdapterRadnja1(Context context, List<Tarifa> listDataHeader, HashMap<Tarifa, List<Radnja>> listHashMap) {
        this.context = context;

        this.listDataHeader = new ArrayList<>();
        this.listDataHeader.addAll(listDataHeader);
        this.originalListHeader = new ArrayList<>();
        this.originalListHeader.addAll(listDataHeader);

        this.listHashMap = new HashMap<>();
        this.listHashMap.putAll(listHashMap);
        this.originalHashMap = new HashMap<>();
        this.originalHashMap.putAll(listHashMap);



//
//        this.listDataHeader = listDataHeader;
//        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i).getNaslov_tarife();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String) getGroup(i);
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_headlines, null);
        }
        TextView tv_header = (TextView)view.findViewById(R.id.expandable_list_header_item);
        tv_header.setTypeface(null, Typeface.BOLD);
        tv_header.setText(headerTitle);
        return view;

    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        final Radnja radnja = (Radnja) getChild(i, i1);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        TextView textListChild = (TextView)view.findViewById(R.id.lblListItem);
        textListChild.setText(radnja.getNaziv_radnje());


        return view;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    public void filterData(String query){

        query = query.toLowerCase();
        listDataHeader.clear();
        listHashMap.clear();

        if(query.isEmpty()){
            listDataHeader.addAll(originalListHeader);
            listHashMap.putAll(originalHashMap);
        }else {
            for(Map.Entry<Tarifa, List<Radnja>> entry : originalHashMap.entrySet()) {
                HashMap<Tarifa, List<Radnja>> hm = new HashMap<Tarifa, List<Radnja>>();
                HashMap<Tarifa, List<Radnja>> newHM = new HashMap<>();
                List<Radnja> novaListaRadnji = new ArrayList<>();
                Tarifa tarifa = entry.getKey();
                List<Radnja> radnje = entry.getValue();

                for (Radnja r : radnje) {
                    if(r.getNaziv_radnje().toLowerCase().contains(query)){

                        novaListaRadnji.add(r);
                    }
                }
                if(novaListaRadnji.size() > 0){
                    Tarifa t = new Tarifa(tarifa.getNaslov_tarife());
                    listDataHeader.add(t);
                    listHashMap.put(t, novaListaRadnji);
                }


                // do what you have to do here
                // In your case, another loop.
            }
        }
        notifyDataSetChanged();
    }
}
