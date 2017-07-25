package com.example.berka.advokatormlite.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by berka on 7/21/2017.
 */

@DatabaseTable(tableName = Postupak.TABLE_NAME_POSTUPAK)
public class Postupak {

    public static final String TABLE_NAME_POSTUPAK = "postupak";
    public static final String POSTUPAK_ID = "_id";
    public static final String POSTUPAK_NAZIV = "naziv";
    public static final String POSTUPAK_TARIFE = "tarife";

    @DatabaseField(columnName = POSTUPAK_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = POSTUPAK_NAZIV)
    private String nazivpostupka;

    @ForeignCollectionField(columnName = Postupak.POSTUPAK_TARIFE, eager = true)
    private ForeignCollection<Tarifa> tarife;

    public Postupak(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazivpostupka() {
        return nazivpostupka;
    }

    public void setNazivpostupka(String nazivpostupka) {
        this.nazivpostupka = nazivpostupka;
    }

    public ForeignCollection<Tarifa> getTarife() {
        return tarife;
    }

    public void setTarife(ForeignCollection<Tarifa> tarife) {
        this.tarife = tarife;
    }

    @Override
    public String toString() {
        return  nazivpostupka;
    }
}
