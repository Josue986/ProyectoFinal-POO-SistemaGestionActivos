/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOImpl;
import DAOInterface.UsuarioDAO;
import Conexion.ConexionSQLite;
import Modelo.Usuario;
import Modelo.Custodio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jotue
 */
public class UsuarioDAOImpl implements UsuarioDAO{
    @Override
    public boolean guardar(Usuario usuario) {
       // Inserta o vincula el usuario/custodio guardando los datos en sistema_activos
        String sql = "INSERT INTO sistema_activos (cedulaCustodio, nombreCustodio, apellidoCustodio, rolCustodio, nombreActivo, tipoActivo, estadoActivo) "
                   + "VALUES (?, ?, ?, ?, 'Sin Activo Asignado', 'General', 'Activo')";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (conn == null || usuario.getCustodio() == null) return false;

            Custodio c = usuario.getCustodio();
            stmt.setString(1, c.getCedula());
            stmt.setString(2, c.getNombre());
            stmt.setString(3, c.getApellido());
            stmt.setString(4, c.getRol());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setIdUsuario(rs.getInt(1));
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
    public boolean actualizar(Usuario usuario) {
        // Actualiza los datos del custodio/usuario basándose en su ID de registro
        String sql = "UPDATE sistema_activos SET cedulaCustodio = ?, nombreCustodio = ?, apellidoCustodio = ?, rolCustodio = ? WHERE idRegistro = ?";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (conn == null || usuario.getCustodio() == null) return false;

            Custodio c = usuario.getCustodio();
            stmt.setString(1, c.getCedula());
            stmt.setString(2, c.getNombre());
            stmt.setString(3, c.getApellido());
            stmt.setString(4, c.getRol());
            stmt.setInt(5, usuario.getIdUsuario());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idUsuario) {
        // En lugar de borrar la fila entera (lo que eliminaría el activo), desvincula los datos del usuario/custodio
        String sql = "UPDATE sistema_activos SET cedulaCustodio = NULL, nombreCustodio = NULL, apellidoCustodio = NULL, rolCustodio = NULL WHERE idRegistro = ?";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (conn == null) return false;

            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        // Consulta los registros que tienen información de custodio asociada dentro de sistema_activos
        String sql = "SELECT idRegistro, cedulaCustodio, nombreCustodio, apellidoCustodio, rolCustodio " +
                     "FROM sistema_activos " +
                     "WHERE cedulaCustodio IS NOT NULL AND cedulaCustodio != ''";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) return lista;

            while (rs.next()) {
                int idReg = rs.getInt("idRegistro");
                Custodio c = new Custodio(
                    idReg,
                    rs.getString("cedulaCustodio"),
                    rs.getString("nombreCustodio"),
                    rs.getString("apellidoCustodio"),
                    rs.getString("rolCustodio")
                );
                
                Usuario u = new Usuario(idReg, c);
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
