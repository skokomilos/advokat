package com.example.berka.advokatormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by berka on 30-Oct-17.
 */
@DatabaseTable(tableName = PostupakVrstaParniceJoin.TABLE_NAME)
public class PostupakVrstaParniceJoin {

    public static final String TABLE_NAME = "postupak_vrstapostupka_join";
    public static final String ID = "id";
    public static final String POSTUPAK_ID = "postupakId";
    public static final String VRSTA_PARNICE_ID = "vrstaParniceId";

    @DatabaseField(columnName = ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = POSTUPAK_ID, foreign = true)
    private Postupak postupakId;

    @DatabaseField(columnName = VRSTA_PARNICE_ID, foreign = true)
    private VrsteParnica vrsteParnicaId;

    public PostupakVrstaParniceJoin() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Postupak getPostupakId() {
        return postupakId;
    }

    public void setPostupakId(Postupak postupakId) {
        this.postupakId = postupakId;
    }

    public VrsteParnica getVrsteParnicaId() {
        return vrsteParnicaId;
    }

    public void setVrsteParnicaId(VrsteParnica vrsteParnicaId) {
        this.vrsteParnicaId = vrsteParnicaId;
    }
}
