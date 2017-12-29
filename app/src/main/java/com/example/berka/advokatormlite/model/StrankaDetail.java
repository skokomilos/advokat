package com.example.berka.advokatormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by berka on 27-Dec-17.
 */
@DatabaseTable(tableName = StrankaDetail.TABLE_NAME)
public class StrankaDetail {

    public static final String TABLE_NAME = "stranka_detail";
    public static final String ID = "id";
    public static final String IME_I_PREZIME = "ime_i_prezime";
    public static final String ADRESA = "adresa";
    public static final String MESTO = "mesto";
    public static final String ID_SLUCAJA = "id_slucaja";

    @DatabaseField(columnName = ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = IME_I_PREZIME)
    private String ime_i_prezime;

    @DatabaseField(columnName = ADRESA)
    private String adresa;

    @DatabaseField(columnName = MESTO)
    private String mesto;

    @DatabaseField(columnName = ID_SLUCAJA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Slucaj slucaj;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme_i_prezime() {
        return ime_i_prezime;
    }

    public void setIme_i_prezime(String ime_i_prezime) {
        this.ime_i_prezime = ime_i_prezime;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public Slucaj getSlucaj() {
        return slucaj;
    }

    public void setSlucaj(Slucaj slucaj) {
        this.slucaj = slucaj;
    }
}
