package com.example.berka.advokatormlite.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by berka on 04-Oct-17.
 */
@DatabaseTable(tableName = TabelaBodova.TABLE_NAME)
public class TabelaBodova {


    public static final String TABLE_NAME = "tabela_bodova";
    public static final String ID = "id";
    public static final String TARIFNI_USLOV = "tarifni_uslov";
    public static final String BODOVI = "bodovi";
    public static final String POSTUPAK_ID = "postupak";
    public static final String SLUCAJEVI = "slucajevi";

    @DatabaseField(columnName = ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = TARIFNI_USLOV)
    private String tarifni_uslov;

    @DatabaseField(columnName = BODOVI)
    private int bodovi;

    @DatabaseField(columnName = POSTUPAK_ID, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Postupak postupak;

    @ForeignCollectionField(columnName = SLUCAJEVI, eager = true)
    private ForeignCollection<Slucaj> slucajevi;


    public TabelaBodova() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTarifni_uslov() {
        return tarifni_uslov;
    }

    public void setTarifni_uslov(String tarifni_uslov) {
        this.tarifni_uslov = tarifni_uslov;
    }

    public int getBodovi() {
        return bodovi;
    }

    public void setBodovi(int bodovi) {
        this.bodovi = bodovi;
    }

    public Postupak getPostupak() {
        return postupak;
    }

    public void setPostupak(Postupak postupak) {
        this.postupak = postupak;
    }

    public ForeignCollection<Slucaj> getSlucajevi() {
        return slucajevi;
    }

    public void setSlucajevi(ForeignCollection<Slucaj> slucajevi) {
        this.slucajevi = slucajevi;
    }

    @Override
    public String toString() {
        return tarifni_uslov;
    }
}
