package com.example.berka.advokatormlite.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berka on 7/21/2017.
 */

@DatabaseTable(tableName = Tarifa.TABLE_NAME_TARIFA)
public class Tarifa {



    public static final String TABLE_NAME_TARIFA= "tarifa";
    public static final String FIELD_TARIFA_ID = "_id";
    public static final String FIELD_NAZIV_TARIFE = "nazivTarife";
    public static final String FIELD_TARIFA_RADNJE = "radnje";
    public static final String FIELD_TARIFA_POSTUPAK = "idPostupak";
    public static final String FIELD_VRSTA_PARNICA_ID = "idVrstaParnica";

    @DatabaseField(columnName = FIELD_TARIFA_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV_TARIFE)
    private String naslov_tarife;

    @ForeignCollectionField(columnName = Tarifa.FIELD_TARIFA_RADNJE, eager = true)
    private ForeignCollection<Radnja> radnje;

    @DatabaseField(columnName = FIELD_TARIFA_POSTUPAK, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Postupak postupak;

    @DatabaseField(columnName = FIELD_VRSTA_PARNICA_ID, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private VrsteParnica vrsteParnica;

    private List<Radnja> listaRadnja;

    public Tarifa(String naslov_tarife){
        this.naslov_tarife = naslov_tarife;

    }

    public Tarifa(String naslov_tarife, List<Radnja> radnje){
        this.naslov_tarife = naslov_tarife;
        this.listaRadnja = new ArrayList<Radnja>(radnje);
    }

    public Tarifa() {
    }

    public List<Radnja> getListRadnja()
    {
        if (listaRadnja == null) {
            listaRadnja = new ArrayList<Radnja>();
            for (Radnja order : radnje) {
                listaRadnja.add(order);
            }
        }
        return listaRadnja;
       // return new ArrayList<Radnja>(radnje);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaslov_tarife() {
        return naslov_tarife;
    }

    public void setNaslov_tarife(String naslov_tarife) {
        this.naslov_tarife = naslov_tarife;
    }


    public ForeignCollection<Radnja> getRadnje() {
        return radnje;
    }

    public void setRadnje(ForeignCollection<Radnja> radnje) {
        this.radnje = radnje;
    }

    public Postupak getPostupak() {
        return postupak;
    }

    public void setPostupak(Postupak postupak) {
        this.postupak = postupak;
    }

    public VrsteParnica getVrsteParnica() {
        return vrsteParnica;
    }

    public void setVrsteParnica(VrsteParnica vrsteParnica) {
        this.vrsteParnica = vrsteParnica;
    }
}
