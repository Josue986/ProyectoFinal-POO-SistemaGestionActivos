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
        String sql = "UPDATE sistema_activos SET detallesMantenimiento = ?, fechaInicioMantenimiento = ?, fechaFinMantenimiento = ?, costoMantenimiento = ? WHERE idRegistro = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null || reg.getActivo() == null) return false;

            stmt.setString(1, reg.getDetallesMantenimiento());
            stmt.setString(2, reg.getFechaInicio() != null ? dateFormat.format(reg.getFechaInicio()) : null);
            stmt.setString(3, reg.getFechaFin() != null ? dateFormat.format(reg.getFechaFin()) : null);
            stmt.setDouble(4, reg.getCostoMantenimiento());
            stmt.setInt(5, reg.getActivo().getIdActivo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(RegMantenimiento reg) {
        String sql = "UPDATE sistema_activos SET detallesMantenimiento = ?, fechaInicioMantenimiento = ?, fechaFinMantenimiento = ?, costoMantenimiento = ? WHERE idRegistro = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null || reg.getActivo() == null) return false;

            stmt.setString(1, reg.getDetallesMantenimiento());
            stmt.setString(2, reg.getFechaInicio() != null ? dateFormat.format(reg.getFechaInicio()) : null);
            stmt.setString(3, reg.getFechaFin() != null ? dateFormat.format(reg.getFechaFin()) : null);
            stmt.setDouble(4, reg.getCostoMantenimiento());
            stmt.setInt(5, reg.getActivo().getIdActivo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idMantenimiento) {
        String sql = "UPDATE sistema_activos SET detallesMantenimiento = NULL, fechaInicioMantenimiento = NULL, fechaFinMantenimiento = NULL, costoMantenimiento = 0.0 WHERE idRegistro = ?";

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
        // Consulta todos los registros de sistema_activos que tengan detalles de mantenimiento registrados
        String sql = "SELECT * FROM sistema_activos WHERE detallesMantenimiento IS NOT NULL AND detallesMantenimiento != ''";

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
        String sql = "SELECT * FROM sistema_activos WHERE idRegistro = ? AND detallesMantenimiento IS NOT NULL";

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
    
    @Override
    public RegMantenimiento obtenerPorId(int idMantenimiento) {
        RegMantenimiento reg = null;
        String sql = "SELECT * FROM sistema_activos WHERE idRegistro = ? AND detallesMantenimiento IS NOT NULL AND detallesMantenimiento != ''";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            stmt.setInt(1, idMantenimiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reg = mapearMantenimiento(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reg;
    }

    // Auxiliar para parsear las fechas y construir el objeto con su constructor
    private RegMantenimiento mapearMantenimiento(ResultSet rs) {
        try {
            int idActivo = rs.getInt("idRegistro");
            String detalles = rs.getString("detallesMantenimiento");

            String fInicioStr = rs.getString("fechaInicioMantenimiento");
            String fFinStr = rs.getString("fechaFinMantenimiento");
            java.util.Date fInicio = (fInicioStr != null && !fInicioStr.trim().isEmpty()) ? dateFormat.parse(fInicioStr) : null;
            java.util.Date fFin = (fFinStr != null && !fFinStr.trim().isEmpty()) ? dateFormat.parse(fFinStr) : null;

            double costo = rs.getDouble("costoMantenimiento");

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

            Usuario usuarioDummy = new Usuario(idActivo, null);
            
            // Retorna el registro usando el idActivo como identificador del mantenimiento
            return new RegMantenimiento(idActivo, detalles, fInicio, fFin, costo, activo, usuarioDummy);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
