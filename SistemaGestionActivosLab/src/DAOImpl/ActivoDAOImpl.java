/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOImpl;
import DAOInterface.ActivoDAO;
import Conexion.ConexionSQLite;
import Modelo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jotue
 */
public class ActivoDAOImpl implements ActivoDAO {
    @Override
    public boolean guardar(Activo activo) {
        String sqlBase = "INSERT INTO activos (nombreActivo, marca, tipoActivo, costoAdquisicion, estadoActivo, id_custodio) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = ConexionSQLite.conectar();
            if (conn == null) return false;

            // Transacción manual
            conn.setAutoCommit(false);

            // 1. Insertar en la tabla PADRE (activos)
            int idGenerado = -1;
            try (PreparedStatement stmtBase = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS)) {
                stmtBase.setString(1, activo.getNombreActivo());
                stmtBase.setString(2, activo.getMarca());
                stmtBase.setString(3, activo.getTipoActivo());
                stmtBase.setDouble(4, activo.getCostoAdquicicion());
                stmtBase.setString(5, activo.getEstadoActivo());

                if (activo.getCustodio() != null && activo.getCustodio().getIdCustodio() > 0) {
                    stmtBase.setInt(6, activo.getCustodio().getIdCustodio());
                } else {
                    stmtBase.setNull(6, Types.INTEGER);
                }

                int filasAfectadas = stmtBase.executeUpdate();
                if (filasAfectadas == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet rs = stmtBase.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        activo.setIdActivo(idGenerado);
                    }
                }
            }

            // 2. Insertar en la tabla HIJA correspondiente segun el tipo/instancia
            boolean exitoHijo = guardarDetalleHijo(conn, activo, idGenerado);

            if (exitoHijo) {
                conn.commit(); // Confirmar cambios en ambas tablas
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private boolean guardarDetalleHijo(Connection conn, Activo activo, int idActivo) throws SQLException {
        if (activo instanceof Cpu cpu) {
            String sql = "INSERT INTO cpus (idActivo, anniosUso, procesador, memoriaRAM, almacenamiento) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idActivo);
                stmt.setInt(2, cpu.getAnniosUso());
                stmt.setString(3, cpu.getProcesador());
                stmt.setString(4, cpu.getMemoriaRAM());
                stmt.setString(5, cpu.getAlmacenamiento());
                return stmt.executeUpdate() > 0;
            }
        } else if (activo instanceof Monitor mon) {
            String sql = "INSERT INTO monitores (idActivo, anniosUso, tipoConexion, resolucion, tasaRefresco) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idActivo);
                stmt.setInt(2, mon.getAnniosUso());
                stmt.setString(3, mon.getTipoConexion());
                stmt.setString(4, mon.getResolucion());
                stmt.setString(5, mon.getTasaDeRefresco());
                return stmt.executeUpdate() > 0;
            }
        } else if (activo instanceof Mouse mouse) {
            String sql = "INSERT INTO mouses (idActivo, anniosUso, tipoConexion, dpi) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idActivo);
                stmt.setInt(2, mouse.getAnniosUso());
                stmt.setString(3, mouse.getTipoConexion());
                stmt.setString(4, mouse.getDpi());
                return stmt.executeUpdate() > 0;
            }
        } else if (activo instanceof Licencia lic) {
            String sql = "INSERT INTO licencias (idActivo, fechaExpiracion, costoRenovacion) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idActivo);
                stmt.setString(2, lic.getFechaExpiracion() != null ? lic.getFechaExpiracion().toString() : null);
                stmt.setDouble(3, lic.getCostoRenovacion());
                return stmt.executeUpdate() > 0;
            }
        }
        // Si fuera un Activo genérico/Hardware sin tabla especifica adicional
        return true;
    }

    @Override
    public boolean actualizar(Activo activo) {
        String sqlBase = "UPDATE activos SET nombreActivo = ?, marca = ?, tipoActivo = ?, costoAdquisicion = ?, estadoActivo = ?, id_custodio = ? WHERE idActivo = ?";
        
        Connection conn = null;
        try {
            conn = ConexionSQLite.conectar();
            if (conn == null) return false;

            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlBase)) {
                stmt.setString(1, activo.getNombreActivo());
                stmt.setString(2, activo.getMarca());
                stmt.setString(3, activo.getTipoActivo());
                stmt.setDouble(4, activo.getCostoAdquicicion());
                stmt.setString(5, activo.getEstadoActivo());

                if (activo.getCustodio() != null && activo.getCustodio().getIdCustodio() > 0) {
                    stmt.setInt(6, activo.getCustodio().getIdCustodio());
                } else {
                    stmt.setNull(6, Types.INTEGER);
                }
                stmt.setInt(7, activo.getIdActivo());

                stmt.executeUpdate();
            }

            // Actualizar tabla específica si aplica
            actualizarDetalleHijo(conn, activo);

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private void actualizarDetalleHijo(Connection conn, Activo activo) throws SQLException {
        if (activo instanceof Cpu cpu) {
            String sql = "UPDATE cpus SET anniosUso = ?, procesador = ?, memoriaRAM = ?, almacenamiento = ? WHERE idActivo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, cpu.getAnniosUso());
                stmt.setString(2, cpu.getProcesador());
                stmt.setString(3, cpu.getMemoriaRAM());
                stmt.setString(4, cpu.getAlmacenamiento());
                stmt.setInt(5, cpu.getIdActivo());
                stmt.executeUpdate();
            }
        } else if (activo instanceof Monitor mon) {
            String sql = "UPDATE monitores SET anniosUso = ?, tipoConexion = ?, resolucion = ?, tasaRefresco = ? WHERE idActivo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, mon.getAnniosUso());
                stmt.setString(2, mon.getTipoConexion());
                stmt.setString(3, mon.getResolucion());
                stmt.setString(4, mon.getTasaDeRefresco());
                stmt.setInt(5, mon.getIdActivo());
                stmt.executeUpdate();
            }
        } else if (activo instanceof Mouse mouse) {
            String sql = "UPDATE mouses SET anniosUso = ?, tipoConexion = ?, dpi = ? WHERE idActivo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, mouse.getAnniosUso());
                stmt.setString(2, mouse.getTipoConexion());
                stmt.setString(3, mouse.getDpi());
                stmt.setInt(4, mouse.getIdActivo());
                stmt.executeUpdate();
            }
        } else if (activo instanceof Licencia lic) {
            String sql = "UPDATE licencias SET fechaExpiracion = ?, costoRenovacion = ? WHERE idActivo = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, lic.getFechaExpiracion() != null ? lic.getFechaExpiracion().toString() : null);
                stmt.setDouble(2, lic.getCostoRenovacion());
                stmt.setInt(3, lic.getIdActivo());
                stmt.executeUpdate();
            }
        } // Repetir patrón similar para Monitor, Mouse y Licencia...
    }

    @Override
    public boolean eliminar(int idActivo) {
        // Gracias a ON DELETE CASCADE y PRAGMA foreign_keys = ON, solo borramos de la tabla padre
        String sql = "DELETE FROM activos WHERE idActivo = ?";
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idActivo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Activo> obtenerTodos() {
        List<Activo> lista = new ArrayList<>();
        // LEFT JOINs con todas las tablas hijas para obtener la info completa en una sola consulta
        String sql = "SELECT a.*, " +
                 "c.anniosUso AS cpu_uso, c.procesador, c.memoriaRAM, c.almacenamiento, " +
                 "m.anniosUso AS mon_uso, m.tipoConexion AS mon_conexion, m.resolucion, m.tasaRefresco, " +
                 "ms.anniosUso AS mouse_uso, ms.tipoConexion AS mouse_conexion, ms.dpi, " +
                 "l.fechaExpiracion, l.costoRenovacion, " +
                 "cust.nombre AS cust_nombre, cust.apellido AS cust_apellido " +
                 "FROM activos a " +
                 "LEFT JOIN cpus c ON a.idActivo = c.idActivo " +
                 "LEFT JOIN monitores m ON a.idActivo = m.idActivo " +
                 "LEFT JOIN mouses ms ON a.idActivo = ms.idActivo " +
                 "LEFT JOIN licencias l ON a.idActivo = l.idActivo " +
                 "LEFT JOIN custodios cust ON a.id_custodio = cust.idCustodio";

        try (Connection conn = ConexionSQLite.conectar();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("idActivo");
                String nombre = rs.getString("nombreActivo");
                String marca = rs.getString("marca");
                String tipo = rs.getString("tipoActivo");
                double costo = rs.getDouble("costoAdquisicion");
                String estado = rs.getString("estadoActivo");
                
                int idCustodio = rs.getInt("id_custodio");
                Custodio custodio = null;
                if (idCustodio > 0) {
                    custodio = new Custodio();
                    custodio.setIdCustodio(idCustodio);
                    custodio.setNombre(rs.getString("cust_nombre"));
                    custodio.setApellido(rs.getString("cust_apellido"));
                }

                // Reconstruir objeto concreto según el tipo
                Activo activo = null;
                if ("CPU".equalsIgnoreCase(tipo) || rs.getString("procesador") != null) {
                    activo = new Cpu(
                        rs.getString("procesador"),
                        rs.getString("memoriaRAM"),
                        rs.getString("almacenamiento"),
                        rs.getInt("cpu_uso"),
                        id, nombre, marca, tipo, costo, estado, custodio
                    );
                } else if ("MONITOR".equalsIgnoreCase(tipo) || rs.getString("resolucion") != null) {
                    activo = new Monitor(
                        rs.getString("resolucion"),
                        rs.getString("tasaRefresco"),
                        rs.getInt("mon_uso"),
                        rs.getString("mon_conexion"),
                        id, nombre, marca, tipo, 
                        0.0, // costoMantenimiento
                        estado, costo, custodio
                    );
                } else if ("MOUSE".equalsIgnoreCase(tipo) || rs.getString("dpi") != null) {
                    activo = new Mouse(
                        rs.getString("dpi"),
                        rs.getInt("mouse_uso"),
                        rs.getString("mouse_conexion"),
                        id, nombre, marca, tipo, costo, estado, custodio
                    );
                } else if ("LICENCIA".equalsIgnoreCase(tipo) || rs.getString("fechaExpiracion") != null) {
                    activo = new Licencia(
                        null, // Se asigna null o se parsea si existe
                        rs.getDouble("costoRenovacion"),
                        id, nombre, marca, tipo, 
                        0.0, //costoMantenimiento
                        estado, costo, custodio
                    );
                } else if ("PERIFERICO".equalsIgnoreCase(tipo)) {
                activo = new Periferico(0, "USB/Genérico", id, nombre, marca, tipo, costo, estado, custodio);
            } 
            // 6. HARDWARE GENÉRICO (por defecto)
            else {
                activo = new Hardware(0, id, nombre, marca, tipo, costo, estado, custodio);
            }

                if (activo != null) {
                    lista.add(activo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
