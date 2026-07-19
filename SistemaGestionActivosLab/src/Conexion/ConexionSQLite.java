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
        
        String tablaActivos = "CREATE TABLE IF NOT EXISTS activos ("
                + "idActivo INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombreActivo TEXT NOT NULL, "
                + "tipoActivo TEXT NOT NULL, "
                + "costoAdquisicion DECIMAL(8,2), "
                + "estadoActivo TEXT NOT NULL, "
                + "id_custodio INTEGER, "
                + "FOREIGN KEY(id_custodio) REFERENCES custodios(idCustodio) "
                + ");";

        String tablaMantenimientos = "CREATE TABLE IF NOT EXISTS mantenimientos ("
                + "idMantenimiento INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "detallesMantenimiento TEXT NOT NULL UNIQUE, "
                + "fechaInicio TEXT NOT NULL, "
                + "fechaFin TEXT, "
                + "costoMantenimiento DECIMAL(8,2), "
                + "id_activo INTEGER NOT NULL, "
                + "id_usuario INTEGER NOT NULL, "
                + "FOREIGN KEY(id_activo) REFERENCES activos(idActivo), "
                + "FOREIGN KEY(id_usuario) REFERENCES usuarios (idUsuario)"
                + ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {

            stmt.execute(tablaCustodios);
            stmt.execute(tablaUsuarios);
            stmt.execute(tablaActivos);
            stmt.execute(tablaMantenimientos);
            System.out.println("[BD] Estructura de tablas verificada/creada con éxito.");

        } catch (SQLException e) {
            System.out.println("Error al inicializar las tablas: " + e.getMessage());
        }
    }
}
