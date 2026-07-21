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
    @Override
    public boolean guardar(Custodio custodio) {
        String sql = "INSERT INTO custodios (cedula, nombre, apellido, rol) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (conn == null) return false;

            stmt.setString(1, custodio.getCedula());
            stmt.setString(2, custodio.getNombre());
            stmt.setString(3, custodio.getApellido());
            stmt.setString(4, custodio.getRol());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        custodio.setIdCustodio(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(Custodio custodio) {
        String sql = "UPDATE custodios SET cedula = ?, nombre = ?, apellido = ?, rol = ? WHERE idCustodio = ?";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;

            stmt.setString(1, custodio.getCedula());
            stmt.setString(2, custodio.getNombre());
            stmt.setString(3, custodio.getApellido());
            stmt.setString(4, custodio.getRol());
            stmt.setInt(5, custodio.getIdCustodio());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idCustodio) {
        String sql = "DELETE FROM custodios WHERE idCustodio = ?";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;

            stmt.setInt(1, idCustodio);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Custodio> obtenerTodos() {
        List<Custodio> lista = new ArrayList<>();
        String sql = "SELECT * FROM custodios";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) return lista;

            while (rs.next()) {
                Custodio c = new Custodio(
                    rs.getInt("idCustodio"),
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("rol")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
