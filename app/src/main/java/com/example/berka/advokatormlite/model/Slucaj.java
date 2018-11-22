package com.example.berka.advokatormlite.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by berka on 22-Sep-17.
 */
@DatabaseTable(tableName = Slucaj.TABLE_NAME)
public class Slucaj implements Parcelable{

    public static final String TABLE_NAME = "slucaj";
    public static final String ID_SLCJ = "id";
    public static final String BROJ_SLUCAJA = "broj_slucaja";
    public static final String BROJ_STRANAKA = "broj_stranaka";
    public static final String OKRIVLJEN = "okrivljen";
    public static final String VRSTA_ODBRANE = "vrsta_odbrane";
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

    @Nullable
    @DatabaseField(columnName = OKRIVLJEN)
    private int okrivljen;

    @Nullable
    @DatabaseField(columnName = VRSTA_ODBRANE)
    private int vrsta_odbrane;

    @DatabaseField(columnName = ID_POSTUPKA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Postupak postupak;

    @DatabaseField(columnName = ID_TABELE_BODOVA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private TabelaBodova tabelaBodova;

    //lista radnji
    @ForeignCollectionField(columnName = Slucaj.IZRACUNAT_TROSAK_RADNJE, eager = true)
    private ForeignCollection<IzracunatTrosakRadnje> listaIzracunatihTroskovaRadnji;

    @ForeignCollectionField(columnName = Slucaj.LISTA_STRANAKA, eager = true)
    private ForeignCollection<StrankaDetail> lista_stranaka;

    protected Slucaj(Parcel input){
        id = input.readInt();
        broj_slucaja = input.readInt();
        broj_stranaka = input.readInt();
        okrivljen = input.readInt();
        vrsta_odbrane = input.readInt();
        postupak = input.readParcelable(Postupak.class.getClassLoader());
        tabelaBodova = input.readParcelable(TabelaBodova.class.getClassLoader());
    }

    public Slucaj() {
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        //responsible for saving data
        destination.writeInt(id);
        destination.writeInt(broj_slucaja);
        destination.writeInt(broj_stranaka);
        destination.writeInt(okrivljen);
        destination.writeInt(vrsta_odbrane);
        destination.writeParcelable(postupak, 0);
        destination.writeParcelable(tabelaBodova, 1);
    }

    @Override
    public int describeContents() {
        return 0;
    }



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

    public int getVrsta_odbrane() {
        return vrsta_odbrane;
    }

    public void setVrsta_odbrane(int vrsta_odbrane) {
        this.vrsta_odbrane = vrsta_odbrane;
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

    public static final Creator<Slucaj> CREATOR = new Creator<Slucaj>() {
        @Override
        public Slucaj createFromParcel(Parcel source) {
            return new Slucaj(source);
        }

        @Override
        public Slucaj[] newArray(int size) {
            return new Slucaj[size];
        }
    };
}
