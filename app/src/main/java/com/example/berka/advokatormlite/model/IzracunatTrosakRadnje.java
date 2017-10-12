package com.example.berka.advokatormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by berka on 09-Oct-17.
 */
@DatabaseTable(tableName = IzracunatTrosakRadnje.TABLE_NAME)
public class IzracunatTrosakRadnje {

    public static final String TABLE_NAME = "izracunat_trosak_radnje";
    public static final String ID = "id";
    public static final String NAZIV = "naziv";
    public static final String CENA = "cena_radnje";
    public static final String SLUCAJ_ID = "slucaj_id";

    @DatabaseField(columnName = ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = NAZIV)
    private String naziv_radnje;

    @DatabaseField(columnName = CENA)
    private double cena_izracunate_jedinstvene_radnje;

    @DatabaseField(columnName = SLUCAJ_ID, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Slucaj slucaj;

    public IzracunatTrosakRadnje() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv_radnje() {
        return naziv_radnje;
    }

    public void setNaziv_radnje(String naziv_radnje) {
        this.naziv_radnje = naziv_radnje;
    }

    public double getCena_izracunate_jedinstvene_radnje() {
        return cena_izracunate_jedinstvene_radnje;
    }

    public void setCena_izracunate_jedinstvene_radnje(double cena_izracunate_jedinstvene_radnje) {
        this.cena_izracunate_jedinstvene_radnje = cena_izracunate_jedinstvene_radnje;
    }

    public Slucaj getSlucaj() {
        return slucaj;
    }

    public void setSlucaj(Slucaj slucaj) {
        this.slucaj = slucaj;
    }

    @Override
    public String toString() {
        return String.valueOf(cena_izracunate_jedinstvene_radnje);
    }
}
