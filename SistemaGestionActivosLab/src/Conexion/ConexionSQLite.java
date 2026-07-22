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
        String tablaUnica = "CREATE TABLE IF NOT EXISTS sistema_activos ("
                + "idRegistro INTEGER PRIMARY KEY AUTOINCREMENT, "
                // Custodio
                + "cedulaCustodio TEXT, "
                + "nombreCustodio TEXT, "
                + "apellidoCustodio TEXT, "
                + "rolCustodio TEXT, "
                // Usuario
                + "idUsuario INTEGER, "
                // Activo Base
                + "nombreActivo TEXT NOT NULL, "
                + "marca TEXT, "
                + "tipoActivo TEXT NOT NULL, "
                + "costoAdquisicion REAL, "
                + "estadoActivo TEXT NOT NULL, "
                // Específicos
                + "anniosUso INTEGER, "
                + "procesador TEXT, "
                + "memoriaRAM TEXT, "
                + "almacenamiento TEXT, "
                + "tipoConexion TEXT, "
                + "resolucion TEXT, "
                + "tasaRefresco TEXT, "
                + "dpi TEXT, "
                + "fechaExpiracion TEXT, "
                + "costoRenovacion REAL, "
                // Mantenimiento
                + "detallesMantenimiento TEXT, "
                + "fechaInicioMantenimiento TEXT, "
                + "fechaFinMantenimiento TEXT, "
                + "costoMantenimiento REAL"
                + ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(tablaUnica);
                System.out.println("[BD] Tabla única 'sistema_activos' creada/verificada con éxito.");
            }
        } catch (SQLException e) {
            System.out.println("[BD Error] Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
