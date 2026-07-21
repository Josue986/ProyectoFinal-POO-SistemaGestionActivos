/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistemagestionactivoslab;
import Conexion.ConexionSQLite;
import DAOImpl.*;
import DAOInterface.*;
import Controlador.InventarioControlador;
import Vista.Vista;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author jotue
 */
public class SistemaGestionActivosLab {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Inicializar/verificar la base de datos SQLite antes de lanzar la GUI
        ConexionSQLite.inicializarBaseDeDatos();

        // Ejecutar la interfaz gráfica en el hilo de eventos de Swing (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                // Aplicar Look and Feel del sistema para que Swing se vea moderno
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si falla el Look and Feel, continúa con el estándar por defecto
            }

            // Instanciar la Vista
            Vista vista = new Vista();

            // Instanciar las implementaciones DAO
            ActivoDAO activoDAO = new ActivoDAOImpl();
            RegMantenimientoDAO regMantenimientoDAO = new RegMantenimientoDAOImpl();
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
            CustodioDAO custodioDAO = new CustodioDAOImpl();

            // Instanciar el Controlador (esto conecta los listeners y carga las listas iniciales)
            InventarioControlador controlador = new InventarioControlador(
                vista, 
                activoDAO, 
                regMantenimientoDAO, 
                usuarioDAO, 
                custodioDAO
            );

            // Hacer visible la ventana principal
            vista.setLocationRelativeTo(null); // Centrar en pantalla
            vista.setVisible(true);
        });
    }  
}
