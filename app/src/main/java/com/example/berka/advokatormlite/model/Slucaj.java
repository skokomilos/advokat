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
    public static final String SL_IME = "ime";
    public static final String SL_PREZIME = "prezime";
    public static final String BROJ_SLUCAJA = "broj_slucaja";
    public static final String ID_POSTUPKA = "id_postupak";
    //todo resiti tabelu_bodova
    public static final String ID_TABELE_BODOVA = "id_tabela_bodova";
    public static final String IZRACUNAT_TROSAK_RADNJE = "lista_izracunatih_radnji";


    @DatabaseField(columnName = ID_SLCJ, generatedId = true)
    private int id;

    @DatabaseField(columnName = SL_IME)
    private String ime;

    @DatabaseField(columnName = SL_PREZIME)
    private String prezime;

    @DatabaseField(columnName = BROJ_SLUCAJA)
    private int broj_slucaja;

    @DatabaseField(columnName = ID_POSTUPKA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Postupak postupak;

    @DatabaseField(columnName = ID_TABELE_BODOVA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private TabelaBodova tabelaBodova;

    //lista radnji
    @ForeignCollectionField(columnName = Slucaj.IZRACUNAT_TROSAK_RADNJE, eager = true)
    private ForeignCollection<IzracunatTrosakRadnje> listaIzracunatihTroskovaRadnji;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public int getBroj_slucaja() {
        return broj_slucaja;
    }

    public void setBroj_slucaja(int broj_slucaja) {
        this.broj_slucaja = broj_slucaja;
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
}
