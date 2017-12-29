package com.example.berka.advokatormlite.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by berka on 22-Sep-17.
 */
@DatabaseTable(tableName = Slucaj.TABLE_NAME)
public class Slucaj {

    public static final String TABLE_NAME = "slucaj";
    public static final String ID_SLCJ = "id";
    public static final String BROJ_SLUCAJA = "broj_slucaja";
    public static final String BROJ_STRANAKA = "broj_stranaka";
    public static final String OKRIVLJEN = "okrivljen";
    public static final String ID_POSTUPKA = "id_postupak";
    public static final String ID_TABELE_BODOVA = "id_tabela_bodova";
    public static final String IZRACUNAT_TROSAK_RADNJE = "lista_izracunatih_radnji";
    public static final String LISTA_STRANAKA = "lista_stranaka";


    @DatabaseField(columnName = ID_SLCJ, generatedId = true)
    private int id;

    @DatabaseField(columnName = BROJ_SLUCAJA)
    private int broj_slucaja;

    @DatabaseField(columnName = BROJ_STRANAKA)
    private int broj_stranaka;

    @DatabaseField(columnName = OKRIVLJEN)
    private int okrivljen;

    @DatabaseField(columnName = ID_POSTUPKA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Postupak postupak;

    @DatabaseField(columnName = ID_TABELE_BODOVA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private TabelaBodova tabelaBodova;

    //lista radnji
    @ForeignCollectionField(columnName = Slucaj.IZRACUNAT_TROSAK_RADNJE, eager = true)
    private ForeignCollection<IzracunatTrosakRadnje> listaIzracunatihTroskovaRadnji;

    @ForeignCollectionField(columnName = Slucaj.LISTA_STRANAKA, eager = true)
    private ForeignCollection<StrankaDetail> lista_stranaka;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBroj_slucaja() {
        return broj_slucaja;
    }

    public void setBroj_slucaja(int broj_slucaja) {
        this.broj_slucaja = broj_slucaja;
    }

    public int getBroj_stranaka() {
        return broj_stranaka;
    }

    public void setBroj_stranaka(int broj_stranaka) {
        this.broj_stranaka = broj_stranaka;
    }

    public int getOkrivljen() {
        return okrivljen;
    }

    public void setOkrivljen(int okrivljen) {
        this.okrivljen = okrivljen;
    }

    public Postupak getPostupak() {
        return postupak;
    }

    public void setPostupak(Postupak postupak) {
        this.postupak = postupak;
    }

    public TabelaBodova getTabelaBodova() {
        return tabelaBodova;
    }

    public void setTabelaBodova(TabelaBodova tabelaBodova) {
        this.tabelaBodova = tabelaBodova;
    }

    public ForeignCollection<IzracunatTrosakRadnje> getListaIzracunatihTroskovaRadnji() {
        return listaIzracunatihTroskovaRadnji;
    }

    public void setListaIzracunatihTroskovaRadnji(ForeignCollection<IzracunatTrosakRadnje> listaIzracunatihTroskovaRadnji) {
        this.listaIzracunatihTroskovaRadnji = listaIzracunatihTroskovaRadnji;
    }

    public ForeignCollection<StrankaDetail> getLista_stranaka() {
        return lista_stranaka;
    }

    public void setLista_stranaka(ForeignCollection<StrankaDetail> lista_stranaka) {
        this.lista_stranaka = lista_stranaka;
    }

    @Override
    public String toString() {
        return String.valueOf(broj_slucaja);
    }
}
