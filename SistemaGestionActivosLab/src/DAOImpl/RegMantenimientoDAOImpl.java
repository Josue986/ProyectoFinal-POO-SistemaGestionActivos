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
        // Un solo LEFT JOIN a sistema_activos
        String sql = "SELECT m.*, a.* FROM mantenimientos m " +
                     "LEFT JOIN sistema_activos a ON m.id_activo = a.idRegistro";

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
        String sql = "SELECT m.*, a.* FROM mantenimientos m " +
                     "LEFT JOIN sistema_activos a ON m.id_activo = a.idRegistro " +
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
            java.util.Date fInicio = (fInicioStr != null && !fInicioStr.trim().isEmpty()) ? dateFormat.parse(fInicioStr) : null;
            java.util.Date fFin = (fFinStr != null && !fFinStr.trim().isEmpty()) ? dateFormat.parse(fFinStr) : null;

            double costo = rs.getDouble("costoMantenimiento");
            int idActivo = rs.getInt("id_activo");
            int idUsuario = rs.getInt("id_usuario");

            // Reconstrucción del Custodio
            Custodio custodio = null;
            String cedula = rs.getString("cedulaCustodio");
            if (cedula != null) {
                custodio = new Custodio();
                custodio.setCedula(cedula);
                custodio.setNombre(rs.getString("nombreCustodio"));
                custodio.setApellido(rs.getString("apellidoCustodio"));
                custodio.setRol(rs.getString("rolCustodio"));
            }

            // Datos base del Activo
            String nombreActivo = rs.getString("nombreActivo");
            if (nombreActivo == null) nombreActivo = "Activo #" + idActivo;

            String marca = rs.getString("marca") != null ? rs.getString("marca") : "";
            String tipoActivo = rs.getString("tipoActivo") != null ? rs.getString("tipoActivo") : "Hardware";
            double costoAdq = rs.getDouble("costoAdquisicion");
            String estado = rs.getString("estadoActivo") != null ? rs.getString("estadoActivo") : "Disponible";

            Activo activo = null;

            if ("CPU".equalsIgnoreCase(tipoActivo)) {
                activo = new Cpu(
                    rs.getString("procesador"),
                    rs.getString("memoriaRAM"),
                    rs.getString("almacenamiento"),
                    rs.getInt("anniosUso"),
                    idActivo, nombreActivo, marca, tipoActivo, costoAdq, estado, custodio
                );
            } else if ("MONITOR".equalsIgnoreCase(tipoActivo)) {
                // Firma real de Monitor: (resolucion, tasaDeRefresco, anniosUso, tipoConexion, idActivo, nombreActivo, marca, tipoActivo, estadoActivo, costoAdquisicion, custodio)
                activo = new Monitor(
                    rs.getString("resolucion"),
                    rs.getString("tasaRefresco"),
                    rs.getInt("anniosUso"),
                    rs.getString("tipoConexion"),
                    idActivo, nombreActivo, marca, tipoActivo, estado, costoAdq, custodio
                );
            } else if ("MOUSE".equalsIgnoreCase(tipoActivo)) {
                activo = new Mouse(
                    rs.getString("dpi"),
                    rs.getInt("anniosUso"),
                    rs.getString("tipoConexion"),
                    idActivo, nombreActivo, marca, tipoActivo, costoAdq, estado, custodio
                );
            } else if ("LICENCIA".equalsIgnoreCase(tipoActivo)) {
                java.util.Date fExp = null;
                String fExpStr = rs.getString("fechaExpiracion");
                if (fExpStr != null && !fExpStr.trim().isEmpty()) {
                    try {
                        fExp = new SimpleDateFormat("yyyy-MM-dd").parse(fExpStr);
                    } catch (Exception e) {
                        // Ignorar si la fecha no parsea
                    }
                }
                // Firma real de Licencia: (fechaExpiracion, costoRenovacion, idActivo, nombreActivo, marca, tipoActivo, estadoActivo, costoAdquisicion, custodio)
                activo = new Licencia(
                    fExp,
                    rs.getDouble("costoRenovacion"),
                    idActivo, nombreActivo, marca, tipoActivo, estado, costoAdq, custodio
                );
            } else if ("PERIFERICO".equalsIgnoreCase(tipoActivo)) {
                activo = new Periferico(
                    rs.getInt("anniosUso"),
                    rs.getString("tipoConexion"),
                    idActivo, nombreActivo, marca, tipoActivo, costoAdq, estado, custodio
                );
            } else {
                activo = new Hardware(
                    rs.getInt("anniosUso"),
                    idActivo, nombreActivo, marca, tipoActivo, costoAdq, estado, custodio
                );
            }

            Usuario usuarioDummy = new Usuario(idUsuario, null);
            
            // Firma real de RegMantenimiento: (idMantenimiento, detallesMantenimiento, fechaInicio, fechaFin, costoMantenimiento, activo, usuario)
            return new RegMantenimiento(id, detalles, fInicio, fFin, costo, activo, usuarioDummy);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
