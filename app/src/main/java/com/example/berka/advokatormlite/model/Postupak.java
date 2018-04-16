package com.example.berka.advokatormlite.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import static com.example.berka.advokatormlite.model.Postupak.POSTUPAK_VRSTEPARNICA;

/**
 * Created by berka on 7/21/2017.
 */

@DatabaseTable(tableName = Postupak.TABLE_NAME_POSTUPAK)
public class Postupak implements Parcelable {

    public static final String TABLE_NAME_POSTUPAK = "postupak";
    public static final String POSTUPAK_ID = "_id";
    public static final String POSTUPAK_NAZIV = "naziv";
    public static final String POSTUPAK_TARIFE = "tarife";
    public static final String POSTUPAK_TABELABODOVA = "bodovi";
    public static final String POSTUPAK_VRSTEPARNICA = "vrste_parnica";

    @DatabaseField(columnName = POSTUPAK_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = POSTUPAK_NAZIV)
    private String nazivpostupka;

    @ForeignCollectionField(columnName = Postupak.POSTUPAK_TARIFE, eager = true)
    private ForeignCollection<Tarifa> tarife;

    @ForeignCollectionField(columnName = POSTUPAK_TABELABODOVA, eager = true)
    private ForeignCollection<TabelaBodova> tabelaBodovas;

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

    public ForeignCollection<TabelaBodova> getTabelaBodovas() {
        return tabelaBodovas;
    }

    public void setTabelaBodovas(ForeignCollection<TabelaBodova> tabelaBodovas) {
        this.tabelaBodovas = tabelaBodovas;
    }

    @Override
    public String toString() {
        return  nazivpostupka;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nazivpostupka);
    }

    protected Postupak(Parcel in) {
        this.id = in.readInt();
        this.nazivpostupka = in.readString();
    }

    public static final Parcelable.Creator<Postupak> CREATOR = new Parcelable.Creator<Postupak>() {
        @Override
        public Postupak createFromParcel(Parcel source) {
            return new Postupak(source);
        }

        @Override
        public Postupak[] newArray(int size) {
            return new Postupak[size];
        }
    };
}
