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
        String sql = "INSERT INTO usuarios (id_custodio) VALUES (?)";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (conn == null || usuario.getCustodio() == null) return false;

            stmt.setInt(1, usuario.getCustodio().getIdCustodio());

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
        String sql = "UPDATE usuarios SET id_custodio = ? WHERE idUsuario = ?";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (conn == null || usuario.getCustodio() == null) return false;

            stmt.setInt(1, usuario.getCustodio().getIdCustodio());
            stmt.setInt(2, usuario.getIdUsuario());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE idUsuario = ?";
        
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
        String sql = "SELECT u.idUsuario, c.idCustodio, c.cedula, c.nombre, c.apellido, c.rol " +
                     "FROM usuarios u " +
                     "INNER JOIN custodios c ON u.id_custodio = c.idCustodio";

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
                
                Usuario u = new Usuario(rs.getInt("idUsuario"), c);
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
