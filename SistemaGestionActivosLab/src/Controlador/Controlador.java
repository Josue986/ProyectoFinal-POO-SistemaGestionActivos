package Controlador;
//import vista.Vista ;

import DAOInterface.ActivoDAO; // Importa tu interfaz de DAO
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Controlador {

    public class InventarioControlador implements ActionListener {

        private Vista vista;
        private ActivoDAO activoDAO;

        // El constructor une la Vista y el DAO
        public InventarioControlador(Vista vista, ActivoDAO activoDAO) {
            this.vista = vista;
            this.activoDAO = activoDAO;

            // Aquí le decimos a los botones que, al ser presionados, 
            // avisen a esta clase (el controlador)
            this.vista.btnGuardar.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // ¿Qué botón se presionó?
            if (e.getSource() == vista.btnGuardar) {
                guardarNuevoActivo();
            }
        }

        private void guardarNuevoActivo() {
            // 1. Obtener datos de la vista
            var activo = vista.obtenerDatosFormulario();

            // 2. Guardar en BD usando el DAO
            if (activoDAO.guardar(activo)) {
                JOptionPane.showMessageDialog(vista, "¡Éxito al guardar!");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar");
            }
        }
    }
}
