package com.example.berka.advokatormlite.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by berka on 04-Oct-17.
 */
@DatabaseTable(tableName = TabelaBodova.TABLE_NAME)
public class TabelaBodova implements Parcelable {


    public static final String TABLE_NAME = "tabela_bodova";
    public static final String ID = "id";
    public static final String TARIFNI_USLOV = "tarifni_uslov";
    public static final String BODOVI = "bodovi";
    public static final String POSTUPAK_ID = "postupak";
    public static final String SLUCAJEVI = "slucajevi";
    public static final String VRSTE_PARNICA_ID = "vrste_parnica_id";

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

    @DatabaseField(columnName = VRSTE_PARNICA_ID, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private VrsteParnica vrsteParnicaId;



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

    public VrsteParnica getVrsteParnicaId() {
        return vrsteParnicaId;
    }

    public void setVrsteParnicaId(VrsteParnica vrsteParnicaId) {
        this.vrsteParnicaId = vrsteParnicaId;
    }

    @Override
    public String toString() {
        return tarifni_uslov;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.tarifni_uslov);
        dest.writeInt(this.bodovi);
        dest.writeParcelable(this.postupak, flags);
    }

    protected TabelaBodova(Parcel in) {
        this.id = in.readInt();
        this.tarifni_uslov = in.readString();
        this.bodovi = in.readInt();
        this.postupak = in.readParcelable(Postupak.class.getClassLoader());
    }

    public static final Parcelable.Creator<TabelaBodova> CREATOR = new Parcelable.Creator<TabelaBodova>() {
        @Override
        public TabelaBodova createFromParcel(Parcel source) {
            return new TabelaBodova(source);
        }

        @Override
        public TabelaBodova[] newArray(int size) {
            return new TabelaBodova[size];
        }
    };
}
