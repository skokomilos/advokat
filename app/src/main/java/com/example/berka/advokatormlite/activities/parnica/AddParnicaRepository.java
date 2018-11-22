package com.example.berka.advokatormlite.activities.parnica;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 16-Apr-18.
 */

interface AddParnicaRepository {
    List<Postupak> lookUpPostupciForVrstaParnica(VrsteParnica vrsteParnica) throws SQLException;

    PreparedQuery<Postupak> makePostupciForVrsteParnica() throws SQLException;

    List<VrsteParnica> lookUpPostupciForVrstaParnica(Postupak postupak) throws SQLException;

    PreparedQuery<VrsteParnica> makeVrsteparnicaForPostupak() throws SQLException;
}
