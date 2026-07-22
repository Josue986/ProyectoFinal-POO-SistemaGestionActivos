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
        String sql = "INSERT INTO sistema_activos ("
                + "cedulaCustodio, nombreCustodio, apellidoCustodio, rolCustodio, "
                + "nombreActivo, marca, tipoActivo, costoAdquisicion, estadoActivo, "
                + "anniosUso, procesador, memoriaRAM, almacenamiento, "
                + "tipoConexion, resolucion, tasaRefresco, dpi, "
                + "fechaExpiracion, costoRenovacion"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) return false;

            // Datos del Custodio (si tiene)
            if (activo.getCustodio() != null) {
                stmt.setString(1, activo.getCustodio().getCedula());
                stmt.setString(2, activo.getCustodio().getNombre());
                stmt.setString(3, activo.getCustodio().getApellido());
                stmt.setString(4, activo.getCustodio().getRol());
            } else {
                stmt.setNull(1, Types.VARCHAR);
                stmt.setNull(2, Types.VARCHAR);
                stmt.setNull(3, Types.VARCHAR);
                stmt.setNull(4, Types.VARCHAR);
            }

            // Datos comunes del Activo (corregido getCostoAdquisicion)
            stmt.setString(5, activo.getNombreActivo());
            stmt.setString(6, activo.getMarca());
            stmt.setString(7, activo.getTipoActivo());
            stmt.setDouble(8, activo.getCostoAdquisicion()); 
            stmt.setString(9, activo.getEstadoActivo());

            // Setear NULL por defecto a campos específicos
            stmt.setNull(10, Types.INTEGER); // anniosUso
            stmt.setNull(11, Types.VARCHAR); // procesador
            stmt.setNull(12, Types.VARCHAR); // memoriaRAM
            stmt.setNull(13, Types.VARCHAR); // almacenamiento
            stmt.setNull(14, Types.VARCHAR); // tipoConexion
            stmt.setNull(15, Types.VARCHAR); // resolucion
            stmt.setNull(16, Types.VARCHAR); // tasaRefresco
            stmt.setNull(17, Types.VARCHAR); // dpi
            stmt.setNull(18, Types.VARCHAR); // fechaExpiracion
            stmt.setNull(19, Types.DOUBLE);  // costoRenovacion
            
            // Rellenar según la instancia específica
            if (activo instanceof Cpu cpu) {
                stmt.setInt(10, cpu.getAnniosUso());
                stmt.setString(11, cpu.getProcesador());
                stmt.setString(12, cpu.getMemoriaRAM());
                stmt.setString(13, cpu.getAlmacenamiento());
            } else if (activo instanceof Monitor mon) {
                stmt.setInt(10, mon.getAnniosUso());
                stmt.setString(14, mon.getTipoConexion());
                stmt.setString(15, mon.getResolucion());
                stmt.setString(16, mon.getTasaDeRefresco());
            } else if (activo instanceof Mouse mouse) {
                stmt.setInt(10, mouse.getAnniosUso());
                stmt.setString(14, mouse.getTipoConexion());
                stmt.setString(17, mouse.getDpi());
            } else if (activo instanceof Licencia lic) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                stmt.setString(18, lic.getFechaExpiracion() != null ? sdf.format(lic.getFechaExpiracion()) : null);
                stmt.setDouble(19, lic.getCostoRenovacion());
            } else if (activo instanceof Hardware hw) {
                stmt.setInt(10, hw.getAnniosUso());
            } else if (activo instanceof Periferico per) {
                stmt.setInt(10, per.getAnniosUso());
                stmt.setString(14, per.getTipoConexion());
            }
            
            int filas = stmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        activo.setIdActivo(rs.getInt(1));
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
    public boolean actualizar(Activo activo) {
        String sql = "UPDATE sistema_activos SET "
                + "cedulaCustodio = ?, nombreCustodio = ?, apellidoCustodio = ?, rolCustodio = ?, "
                + "nombreActivo = ?, marca = ?, tipoActivo = ?, costoAdquisicion = ?, estadoActivo = ?, "
                + "anniosUso = ?, procesador = ?, memoriaRAM = ?, almacenamiento = ?, "
                + "tipoConexion = ?, resolucion = ?, tasaRefresco = ?, dpi = ?, "
                + "fechaExpiracion = ?, costoRenovacion = ? "
                + "WHERE idRegistro = ?";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            if (activo.getCustodio() != null) {
                stmt.setString(1, activo.getCustodio().getCedula());
                stmt.setString(2, activo.getCustodio().getNombre());
                stmt.setString(3, activo.getCustodio().getApellido());
                stmt.setString(4, activo.getCustodio().getRol());
            } else {
                stmt.setNull(1, Types.VARCHAR);
                stmt.setNull(2, Types.VARCHAR);
                stmt.setNull(3, Types.VARCHAR);
                stmt.setNull(4, Types.VARCHAR);
            }
            
            stmt.setString(5, activo.getNombreActivo());
            stmt.setString(6, activo.getMarca());
            stmt.setString(7, activo.getTipoActivo());
            stmt.setDouble(8, activo.getCostoAdquisicion());
            stmt.setString(9, activo.getEstadoActivo());

            stmt.setNull(10, Types.INTEGER);
            stmt.setNull(11, Types.VARCHAR);
            stmt.setNull(12, Types.VARCHAR);
            stmt.setNull(13, Types.VARCHAR);
            stmt.setNull(14, Types.VARCHAR);
            stmt.setNull(15, Types.VARCHAR);
            stmt.setNull(16, Types.VARCHAR);
            stmt.setNull(17, Types.VARCHAR);
            stmt.setNull(18, Types.VARCHAR);
            stmt.setNull(19, Types.DOUBLE);
            
            if (activo instanceof Cpu cpu) {
                stmt.setInt(10, cpu.getAnniosUso());
                stmt.setString(11, cpu.getProcesador());
                stmt.setString(12, cpu.getMemoriaRAM());
                stmt.setString(13, cpu.getAlmacenamiento());
            } else if (activo instanceof Monitor mon) {
                stmt.setInt(10, mon.getAnniosUso());
                stmt.setString(14, mon.getTipoConexion());
                stmt.setString(15, mon.getResolucion());
                stmt.setString(16, mon.getTasaDeRefresco());
            } else if (activo instanceof Mouse mouse) {
                stmt.setInt(10, mouse.getAnniosUso());
                stmt.setString(14, mouse.getTipoConexion());
                stmt.setString(17, mouse.getDpi());
            } else if (activo instanceof Licencia lic) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                stmt.setString(18, lic.getFechaExpiracion() != null ? sdf.format(lic.getFechaExpiracion()) : null);
                stmt.setDouble(19, lic.getCostoRenovacion());
            }
            
            stmt.setInt(20, activo.getIdActivo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idActivo) {
        String sql = "DELETE FROM sistema_activos WHERE idRegistro = ?";
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
        String sql = "SELECT * FROM sistema_activos";
        
        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("idRegistro");
                String nombre = rs.getString("nombreActivo");
                String marca = rs.getString("marca");
                String tipo = rs.getString("tipoActivo");
                double costo = rs.getDouble("costoAdquisicion");
                String estado = rs.getString("estadoActivo");

                // Reconstruir Custodio
                Custodio custodio = null;
                String cedula = rs.getString("cedulaCustodio");
                if (cedula != null) {
                    custodio = new Custodio();
                    custodio.setCedula(cedula);
                    custodio.setNombre(rs.getString("nombreCustodio"));
                    custodio.setApellido(rs.getString("apellidoCustodio"));
                    custodio.setRol(rs.getString("rolCustodio"));
                }
                
                // Reconstruir Activo según su tipo
                Activo activo = null;
                if ("CPU".equalsIgnoreCase(tipo)) {
                    activo = new Cpu(
                        rs.getString("procesador"),
                        rs.getString("memoriaRAM"),
                        rs.getString("almacenamiento"),
                        rs.getInt("anniosUso"),
                        id, nombre, marca, tipo, costo, estado, custodio
                    );
                } else if ("MONITOR".equalsIgnoreCase(tipo)) {
                    activo = new Monitor(
                        rs.getString("resolucion"),
                        rs.getString("tasaRefresco"),
                        rs.getInt("anniosUso"),
                        rs.getString("tipoConexion"),
                        id, nombre, marca, tipo, estado, costo, custodio
                    );
                } else if ("MOUSE".equalsIgnoreCase(tipo)) {
                    activo = new Mouse(
                        rs.getString("dpi"),
                        rs.getInt("anniosUso"),
                        rs.getString("tipoConexion"),
                        id, nombre, marca, tipo, costo, estado, custodio
                    );
                } else if ("LICENCIA".equalsIgnoreCase(tipo)) {
                    java.util.Date fecha = null;
                    String fechaStr = rs.getString("fechaExpiracion");
                    if (fechaStr != null && !fechaStr.isEmpty()) {
                        try {
                            fecha = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    activo = new Licencia(
                        fecha, // Parsear fecha si aplica
                        rs.getDouble("costoRenovacion"),
                        id, nombre, marca, tipo, estado, costo, custodio
                    );
                } else if ("PERIFERICO".equalsIgnoreCase(tipo)) {
                    activo = new Periferico(rs.getInt("anniosUso"), rs.getString("tipoConexion"), id, nombre, marca, tipo, costo, estado, custodio);
                } else {
                    activo = new Hardware(rs.getInt("anniosUso"), id, nombre, marca, tipo, costo, estado, custodio);
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
