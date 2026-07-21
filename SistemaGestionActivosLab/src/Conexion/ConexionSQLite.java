/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author jotue
 */
public class ConexionSQLite {
    private static final String URL = "jdbc:sqlite:GestionActivos.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }

    public static void inicializarBaseDeDatos() {
        String tablaCustodios = "CREATE TABLE IF NOT EXISTS custodios ("
                + "idCustodio INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "cedula TEXT NOT NULL UNIQUE, "
                + "nombre TEXT NOT NULL, "
                + "apellido TEXT NOT NULL, "
                + "rol TEXT NOT NULL"
                + ");";
        
        String tablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_custodio INTEGER NOT NULL, "
                + "FOREIGN KEY(id_custodio) REFERENCES custodios(idCustodio)"
                + ");";
        
        // TABLA BASE: ACTIVOS (Campos comunes de la clase abstracta Activo)
        String tablaActivos = "CREATE TABLE IF NOT EXISTS activos ("
                + "idActivo INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombreActivo TEXT NOT NULL, "
                + "marca TEXT, "
                + "tipoActivo TEXT NOT NULL, "
                + "costoAdquisicion REAL, "
                + "estadoActivo TEXT NOT NULL, "
                + "id_custodio INTEGER, "
                + "FOREIGN KEY(id_custodio) REFERENCES custodios(idCustodio) "
                + ");";
        
        // --- TABLAS HIJAS (Especializaciones del modelo) ---
        String tablaCpus = "CREATE TABLE IF NOT EXISTS cpus ("
                + "idActivo INTEGER PRIMARY KEY, "
                + "anniosUso INTEGER, "
                + "procesador TEXT, "
                + "memoriaRAM TEXT, "
                + "almacenamiento TEXT, "
                + "FOREIGN KEY(idActivo) REFERENCES activos(idActivo) ON DELETE CASCADE"
                + ");";
        
        String tablaMonitores = "CREATE TABLE IF NOT EXISTS monitores ("
                + "idActivo INTEGER PRIMARY KEY, "
                + "anniosUso INTEGER, "
                + "tipoConexion TEXT, "
                + "resolucion TEXT, "
                + "tasaRefresco TEXT, "
                + "FOREIGN KEY(idActivo) REFERENCES activos(idActivo) ON DELETE CASCADE"
                + ");";
        
        String tablaMouses = "CREATE TABLE IF NOT EXISTS mouses ("
                + "idActivo INTEGER PRIMARY KEY, "
                + "anniosUso INTEGER, "
                + "tipoConexion TEXT, "
                + "dpi TEXT, "
                + "FOREIGN KEY(idActivo) REFERENCES activos(idActivo) ON DELETE CASCADE"
                + ");";
        
        String tablaLicencias = "CREATE TABLE IF NOT EXISTS licencias ("
                + "idActivo INTEGER PRIMARY KEY, "
                + "fechaExpiracion TEXT, "
                + "costoRenovacion REAL, "
                + "FOREIGN KEY(idActivo) REFERENCES activos(idActivo) ON DELETE CASCADE"
                + ");";
        
        
        String tablaMantenimientos = "CREATE TABLE IF NOT EXISTS mantenimientos ("
                + "idMantenimiento INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "detallesMantenimiento TEXT NOT NULL, "
                + "fechaInicio TEXT NOT NULL, "
                + "fechaFin TEXT, "
                + "costoMantenimiento REAL, "
                + "id_activo INTEGER NOT NULL, "
                + "id_usuario INTEGER NOT NULL, "
                + "FOREIGN KEY(id_activo) REFERENCES activos(idActivo), "
                + "FOREIGN KEY(id_usuario) REFERENCES usuarios (idUsuario)"
                + ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            // Habilitar el soporte de Claves Foráneas en SQLite para ON DELETE CASCADE
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            stmt.execute(tablaCustodios);
            stmt.execute(tablaUsuarios);
            stmt.execute(tablaActivos);
            stmt.execute(tablaCpus);
            stmt.execute(tablaMonitores);
            stmt.execute(tablaMouses);
            stmt.execute(tablaLicencias);
            stmt.execute(tablaMantenimientos);
            System.out.println("[BD] Estructura de tablas verificada/creada con éxito.");

        } catch (SQLException e) {
            System.out.println("Error al inicializar las tablas: " + e.getMessage());
        }
    }
}
