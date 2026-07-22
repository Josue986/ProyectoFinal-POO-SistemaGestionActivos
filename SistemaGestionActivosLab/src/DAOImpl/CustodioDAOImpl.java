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
    @Override public boolean guardar(Custodio custodio) {
        // Asigna o crea un registro en sistema_activos con los datos del custodio
        String sql = "INSERT INTO sistema_activos (cedulaCustodio, nombreCustodio, apellidoCustodio, rolCustodio, nombreActivo, tipoActivo, estadoActivo) "
                   + "VALUES (?, ?, ?, ?, 'Sin Activo Asignado', 'General', 'Activo')";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null || custodio == null) return false;

            stmt.setString(1, custodio.getCedula());
            stmt.setString(2, custodio.getNombre());
            stmt.setString(3, custodio.getApellido());
            stmt.setString(4, custodio.getRol());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }}

    @Override public boolean actualizar(Custodio custodio) {// Actualiza la información del custodio basada en su cédula o id
        String sql = "UPDATE sistema_activos SET nombreCustodio = ?, apellidoCustodio = ?, rolCustodio = ? WHERE cedulaCustodio = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null || custodio == null) return false;

            stmt.setString(1, custodio.getNombre());
            stmt.setString(2, custodio.getApellido());
            stmt.setString(3, custodio.getRol());
            stmt.setString(4, custodio.getCedula());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }}

    @Override public boolean eliminar(int idCustodio) {
        // Desvincula los datos del custodio dejando los campos en NULL
        String sql = "UPDATE sistema_activos SET cedulaCustodio = NULL, nombreCustodio = NULL, apellidoCustodio = NULL, rolCustodio = NULL WHERE idRegistro = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            stmt.setInt(1, idCustodio);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }}

    @Override
    public List<Custodio> obtenerTodos() {
        List<Custodio> lista = new ArrayList<>();
        // Selecciona registros donde los datos de custodio no sean nulos (evita duplicados si varias filas coinciden)
        String sql = "SELECT DISTINCT cedulaCustodio, nombreCustodio, apellidoCustodio, rolCustodio, idRegistro "
                   + "FROM sistema_activos WHERE cedulaCustodio IS NOT NULL AND cedulaCustodio != ''";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) return lista;

            while (rs.next()) {
                Custodio c = new Custodio(
                    rs.getInt("idRegistro"),
                    rs.getString("cedulaCustodio"),
                    rs.getString("nombreCustodio"),
                    rs.getString("apellidoCustodio"),
                    rs.getString("rolCustodio")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    @Override
    public Custodio obtenerPorId(int idCustodio) {
        Custodio custodio = null;
        String sql = "SELECT idRegistro, cedulaCustodio, nombreCustodio, apellidoCustodio, rolCustodio "
                   + "FROM sistema_activos WHERE idRegistro = ? AND cedulaCustodio IS NOT NULL";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            if (conn == null) return null;
            
            stmt.setInt(1, idCustodio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    custodio = new Custodio(
                        rs.getInt("idRegistro"),
                        rs.getString("cedulaCustodio"),
                        rs.getString("nombreCustodio"),
                        rs.getString("apellidoCustodio"),
                        rs.getString("rolCustodio")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return custodio;
    }
}
