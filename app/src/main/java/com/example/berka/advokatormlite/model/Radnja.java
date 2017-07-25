package com.example.berka.advokatormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by berka on 7/21/2017.
 */

@DatabaseTable(tableName = Radnja.TABLE_NAME_RADNJA)
public class Radnja {

    public static final String TABLE_NAME_RADNJA = "radnja";
    public static final String FIELD_RADNJA_ID = "_id";
    public static final String FIELD_RADNJA_NAZIV = "naziv_radnje";
    public static final String FIELD_RADNJA_TARIFA = "idTarifa";


    @DatabaseField(columnName = FIELD_RADNJA_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_RADNJA_NAZIV)
    private String naziv_radnje;

    @DatabaseField(columnName = FIELD_RADNJA_TARIFA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Tarifa tarifa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public String getNaziv_radnje() {
        return naziv_radnje;
    }

    public void setNaziv_radnje(String naziv_radnje) {
        this.naziv_radnje = naziv_radnje;
    }
}
