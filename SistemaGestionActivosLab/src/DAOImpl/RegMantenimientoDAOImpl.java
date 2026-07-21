/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOImpl;
import DAOInterface.RegMantenimientoDAO;
import Conexion.ConexionSQLite;
import Modelo.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jotue
 */
public class RegMantenimientoDAOImpl implements RegMantenimientoDAO {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean guardar(RegMantenimiento reg) {
        String sql = "INSERT INTO mantenimientos (detallesMantenimiento, fechaInicio, fechaFin, costoMantenimiento, id_activo, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) return false;

            stmt.setString(1, reg.getDetallesMantenimiento());
            stmt.setString(2, reg.getFechaInicio() != null ? dateFormat.format(reg.getFechaInicio()) : null);
            stmt.setString(3, reg.getFechaFin() != null ? dateFormat.format(reg.getFechaFin()) : null);
            stmt.setDouble(4, reg.getCostoMantenimiento());
            stmt.setInt(5, reg.getActivo() != null ? reg.getActivo().getIdActivo() : 0);
            stmt.setInt(6, reg.getUsuario() != null ? reg.getUsuario().getIdUsuario() : 0);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        reg.setIdMantenimiento(rs.getInt(1));
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
    public boolean actualizar(RegMantenimiento reg) {
        String sql = "UPDATE mantenimientos SET detallesMantenimiento = ?, fechaInicio = ?, fechaFin = ?, costoMantenimiento = ?, id_activo = ?, id_usuario = ? WHERE idMantenimiento = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            stmt.setString(1, reg.getDetallesMantenimiento());
            stmt.setString(2, reg.getFechaInicio() != null ? dateFormat.format(reg.getFechaInicio()) : null);
            stmt.setString(3, reg.getFechaFin() != null ? dateFormat.format(reg.getFechaFin()) : null);
            stmt.setDouble(4, reg.getCostoMantenimiento());
            stmt.setInt(5, reg.getActivo() != null ? reg.getActivo().getIdActivo() : 0);
            stmt.setInt(6, reg.getUsuario() != null ? reg.getUsuario().getIdUsuario() : 0);
            stmt.setInt(7, reg.getIdMantenimiento());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idMantenimiento) {
        String sql = "DELETE FROM mantenimientos WHERE idMantenimiento = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            stmt.setInt(1, idMantenimiento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<RegMantenimiento> obtenerTodos() {
        List<RegMantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.*, a.nombreActivo " +
             "FROM mantenimientos m " +
             "LEFT JOIN activos a ON m.id_activo = a.idActivo";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (conn == null) return lista;

            while (rs.next()) {
                RegMantenimiento reg = mapearMantenimiento(rs);
                if (reg != null) lista.add(reg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<RegMantenimiento> obtenerRegMantenimientosActivo(int idActivo) {
        List<RegMantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.*, a.nombreActivo " +
                     "FROM mantenimientos m " +
                     "LEFT JOIN activos a ON m.id_activo = a.idActivo " +
                     "WHERE m.id_activo = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return lista;

            stmt.setInt(1, idActivo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RegMantenimiento reg = mapearMantenimiento(rs);
                    if (reg != null) lista.add(reg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Auxiliar para parsear las fechas y construir el objeto con su constructor
    private RegMantenimiento mapearMantenimiento(ResultSet rs) {
        try {
        int id = rs.getInt("idMantenimiento");
        String detalles = rs.getString("detallesMantenimiento");
        
        String fInicioStr = rs.getString("fechaInicio");
        String fFinStr = rs.getString("fechaFin");
        java.util.Date fInicio = fInicioStr != null ? dateFormat.parse(fInicioStr) : null;
        java.util.Date fFin = fFinStr != null ? dateFormat.parse(fFinStr) : null;

        double costo = rs.getDouble("costoMantenimiento");

        int idActivo = rs.getInt("id_activo");
        int idUsuario = rs.getInt("id_usuario");
        
        String nombreActivo = null;
        try {
            nombreActivo = rs.getString("nombreActivo");
        } catch (SQLException ignored) {
            // Si la columna no estaba presente en la consulta SQL
        }
        
        if (nombreActivo == null || nombreActivo.trim().isEmpty()) {
            nombreActivo = "Activo #" + idActivo;
        }

        // Instanciación dummy para evitar NullPointerException en la Vista
        Custodio custodioDummy = null;
        Modelo.Activo activoDummy = new Modelo.Hardware(
            0, idActivo, nombreActivo, "", "Hardware", 0.0, "Disponible", custodioDummy //[cite: 20]
        );
        Modelo.Usuario usuarioDummy = new Modelo.Usuario(idUsuario, null);

        RegMantenimiento reg = new RegMantenimiento(id, detalles, fInicio, fFin, activoDummy, usuarioDummy);
        reg.setCostoMantenimiento(costo);
        return reg;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }
}
