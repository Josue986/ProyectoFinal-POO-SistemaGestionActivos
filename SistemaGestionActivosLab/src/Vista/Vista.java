package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Modelo.Activo; 
import java.util.List;

// extendiendo JFrame directamente
public class Vista extends JFrame {
    
    private JTable tablaActivos;
    private DefaultTableModel modeloTabla;
    public JTextField txtId, txtNombre, txtMarca;
    public JButton btnGuardar, btnEliminar;

    public Vista() {
        // Configuramos la ventana básica
        this.setTitle("Gestión de Activos");
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new java.awt.FlowLayout());
        
        configurarTabla();
        
        // Inicializamos los componentes
        txtId = new JTextField(10);
        txtNombre = new JTextField(10);
        txtMarca = new JTextField(10);
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        
        // Los añadimos al JFrame
        this.add(txtId); this.add(txtNombre); this.add(txtMarca);
        this.add(btnGuardar); this.add(btnEliminar);
        this.add(new JScrollPane(tablaActivos));
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Marca", "Tipo"}, 0);
        tablaActivos = new JTable(modeloTabla);
    }

    public void mostrarLista(List<Activo> activos) {
        modeloTabla.setRowCount(0);
        for (Activo a : activos) {
            
            modeloTabla.addRow(new Object[]{a.getIdActivo(), a.getNombreActivo(), a.getMarca(), a.getTipo()});
        }
    }

    public Activo obtenerDatosFormulario() {
        return null; //Implementar la creación del objeto
    }

    public String obtenerIdSeleccionado() {
        int fila = tablaActivos.getSelectedRow();
        return (fila != -1) ? tablaActivos.getValueAt(fila, 0).toString() : null;
    }
}
