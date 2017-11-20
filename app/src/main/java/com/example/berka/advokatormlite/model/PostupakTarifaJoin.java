package com.example.berka.advokatormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by berka on 30-Oct-17.
 */
@DatabaseTable(tableName = PostupakTarifaJoin.TABLE_NAME)
public class PostupakTarifaJoin {

    public static final String TABLE_NAME = "postupak_tarifa_join";
    public static final String ID = "id";
    public static final String POSTUPAK_ID = "postupakId";
    public static final String TARIFA_ID = "tarifaId";


    @DatabaseField(columnName = ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = POSTUPAK_ID, foreign = true)
    private Postupak postupakId;

    @DatabaseField(columnName = TARIFA_ID, foreign = true)
    private Tarifa tarifaId;

    public PostupakTarifaJoin() {
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

    public Tarifa getTarifaId() {
        return tarifaId;
    }

    public void setTarifaId(Tarifa tarifaId) {
        this.tarifaId = tarifaId;
    }
}
