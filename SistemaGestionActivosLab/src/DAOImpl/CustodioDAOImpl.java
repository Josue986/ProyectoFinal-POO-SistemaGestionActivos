/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOImpl;
import DAOInterface.CustodioDAO;
import Conexion.ConexionSQLite;
import Modelo.Custodio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jotue
 */
public class CustodioDAOImpl implements CustodioDAO {
    @Override public boolean guardar(Custodio c) { return true; }

    @Override public boolean actualizar(Custodio c) { return true; }

    @Override public boolean eliminar(int id) { return true; }

    @Override
    public List<Custodio> obtenerTodos() {
        List<Custodio> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT cedulaCustodio, nombreCustodio, apellidoCustodio, rolCustodio " +
                     "FROM sistema_activos WHERE cedulaCustodio IS NOT NULL";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Custodio c = new Custodio();
                c.setCedula(rs.getString("cedulaCustodio"));
                c.setNombre(rs.getString("nombreCustodio"));
                c.setApellido(rs.getString("apellidoCustodio"));
                c.setRol(rs.getString("rolCustodio"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
