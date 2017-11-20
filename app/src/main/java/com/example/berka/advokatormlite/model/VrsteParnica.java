package com.example.berka.advokatormlite.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by berka on 18-Oct-17.
 */

@DatabaseTable(tableName = VrsteParnica.TABLE_NAME_VRSTA_PARNICA)
public class VrsteParnica {

    public static final String TABLE_NAME_VRSTA_PARNICA = "vrste_parnica";
    public static final String ID = "_id";
    public static final String NAZIV_VRSTE_PARNICE = "naziv_vrste_parnice";
    public static final String POSTUPAK_ID = "postupak_id";
    public static final String LISTA_TABELA_BODOVA = "tabele_bodova";
    public static final String LISTA_TARIFA = "tarife";

    @DatabaseField(columnName = ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = NAZIV_VRSTE_PARNICE)
    private String vrstaParnice;

    @ForeignCollectionField(columnName = LISTA_TABELA_BODOVA, eager = true)
    private ForeignCollection<TabelaBodova> tabelaBodovaLista;

    @ForeignCollectionField(columnName = LISTA_TARIFA, eager = true)
    private ForeignCollection<Tarifa> tarifaLista;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVrstaParnice() {
        return vrstaParnice;
    }

    public void setVrstaParnice(String vrstaParnice) {
        this.vrstaParnice = vrstaParnice;
    }

    public ForeignCollection<TabelaBodova> getTabelaBodovaLista() {
        return tabelaBodovaLista;
    }

    public void setTabelaBodovaLista(ForeignCollection<TabelaBodova> tabelaBodovaLista) {
        this.tabelaBodovaLista = tabelaBodovaLista;
    }

    public ForeignCollection<Tarifa> getTarifaLista() {
        return tarifaLista;
    }

    public void setTarifaLista(ForeignCollection<Tarifa> tarifaLista) {
        this.tarifaLista = tarifaLista;
    }

    @Override
    public String toString() {
        return vrstaParnice;
    }
}
