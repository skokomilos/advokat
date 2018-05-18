package com.example.berka.advokatormlite.activities.add_case.repository_interfaces;

import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.TabelaBodova;
import com.example.berka.advokatormlite.model.VrsteParnica;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by berka on 18-Apr-18.
 */

public interface ParnicaRepositoryInterface {

    List<Postupak> lookUpPostupciForVrstaParnica(VrsteParnica vrsteParnica) throws SQLException;

    PreparedQuery<Postupak> makePostupciForVrsteParnica() throws SQLException;

    List<VrsteParnica> lookUpPostupciForVrstaParnica(Postupak postupak) throws SQLException;

    PreparedQuery<VrsteParnica> makeVrsteparnicaForPostupak() throws SQLException;

    List<TabelaBodova> queryForNeprocenjenuUnikatnuListu(VrsteParnica vrsteParnica, Postupak postupak);

    List<TabelaBodova> queryForNeprocenjenu_Ne_UnikatnuListu(VrsteParnica vrsteParnica);

    List<TabelaBodova> get_Procenjenu_Listu(VrsteParnica vrsteParnica);
}
