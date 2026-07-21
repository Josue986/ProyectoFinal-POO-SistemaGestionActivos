package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Modelo.*;
import java.awt.*;
import java.util.List;

// Solo una clase, extendiendo JFrame directamente
public class Vista extends JFrame {
    
    //--- Pestaña 1: ACTIVOS ---
    public JTextField txtIdActivo, txtNombreActivo, txtMarcaActivo, txtTipoActivo, txtCostoActivo, txtEstadoActivo, txtIdCustodioActivo;
    public JButton btnGuardarActivo, btnActualizarActivo, btnEliminarActivo;
    private JTable tablaActivos;
    private DefaultTableModel modeloTablaActivos;
    
    //--- Pestaña 2: CUSTODIOS ---
    public JTextField txtIdCustodio, txtCedulaCustodio, txtNombreCustodio, txtApellidoCustodio, txtRolCustodio;
    public JButton btnGuardarCustodio, btnActualizarCustodio, btnEliminarCustodio;
    private JTable tablaCustodios;
    private DefaultTableModel modeloTablaCustodios;

    //--- Pestaña 3: USUARIOS ---
    public JTextField txtIdUsuario, txtIdCustodioUsuario;
    public JButton btnGuardarUsuario, btnActualizarUsuario, btnEliminarUsuario;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;

    //--- Pestaña 4: MANTENIMIENTOS ---
    public JTextField txtDpiMantenimiento, txtDetallesMantenimiento, txtFechaInicioMantenimiento, txtFechaFinMantenimiento, txtIdActivoMantenimiento, txtIdUsuarioMantenimiento;
    public JButton btnGuardarMantenimiento, btnActualizarMantenimiento, btnEliminarMantenimiento, btnFiltrarMantenimientoActivo;
    private JTable tablaMantenimientos;
    private DefaultTableModel modeloTablaMantenimientos;
    
    //--- Botones Generales para compatibilidad ---
    public JButton btnGuardar, btnEliminar;

    public Vista() {
        // Configuramos la ventana básica
        this.setTitle("Sistema de Gestión de Activos e Inventarios - UTPL");
        this.setSize(950, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane panelPestañas = new JTabbedPane();

        panelPestañas.addTab("Activos", crearPanelActivos());
        panelPestañas.addTab("Custodios", crearPanelCustodios());
        panelPestañas.addTab("Usuarios", crearPanelUsuarios());
        panelPestañas.addTab("Mantenimientos", crearPanelMantenimientos());

        this.add(panelPestañas);
    }
    
    //  PANEL ACTIVOS | CONSTRUCCIÓN DE PANELES (UI)
    private JPanel crearPanelActivos() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Activo"));

        txtIdActivo = new JTextField();
        txtIdActivo.setEditable(false);
        txtNombreActivo = new JTextField();
        txtMarcaActivo = new JTextField();
        txtTipoActivo = new JTextField();
        txtCostoActivo = new JTextField();
        txtEstadoActivo = new JTextField();
        txtIdCustodioActivo = new JTextField();

        panelForm.add(new JLabel("ID Activo:"));
        panelForm.add(txtIdActivo);
        panelForm.add(new JLabel("Nombre Activo:"));
        panelForm.add(txtNombreActivo);
        panelForm.add(new JLabel("Marca:"));
        panelForm.add(txtMarcaActivo);
        panelForm.add(new JLabel("Tipo Activo:"));
        panelForm.add(txtTipoActivo);
        panelForm.add(new JLabel("Costo Adquisición ($):"));
        panelForm.add(txtCostoActivo);
        panelForm.add(new JLabel("Estado Activo:"));
        panelForm.add(txtEstadoActivo);
        panelForm.add(new JLabel("ID Custodio:"));
        panelForm.add(txtIdCustodioActivo);

        JPanel panelBotones = new JPanel();
        btnGuardarActivo = new JButton("Guardar");
        btnActualizarActivo = new JButton("Actualizar");
        btnEliminarActivo = new JButton("Eliminar");

        // Alias para compatibilidad con el controlador
        btnGuardar = btnGuardarActivo;
        btnEliminar = btnEliminarActivo;

        panelBotones.add(btnGuardarActivo);
        panelBotones.add(btnActualizarActivo);
        panelBotones.add(btnEliminarActivo);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaActivos = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Marca", "Tipo", "Costo Adquisición", "Estado", "Custodio"}, 0
        );
        tablaActivos = new JTable(modeloTablaActivos);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaActivos), BorderLayout.CENTER);

        return panel;
    }  
    
    //  PANEL CUSTODIOS
    private JPanel crearPanelCustodios() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Custodio"));

        txtIdCustodio = new JTextField();
        txtIdCustodio.setEditable(false);
        txtCedulaCustodio = new JTextField();
        txtNombreCustodio = new JTextField();
        txtApellidoCustodio = new JTextField();
        txtRolCustodio = new JTextField();

        panelForm.add(new JLabel("ID Custodio:"));
        panelForm.add(txtIdCustodio);
        panelForm.add(new JLabel("Cédula:"));
        panelForm.add(txtCedulaCustodio);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombreCustodio);
        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtApellidoCustodio);
        panelForm.add(new JLabel("Rol:"));
        panelForm.add(txtRolCustodio);

        JPanel panelBotones = new JPanel();
        btnGuardarCustodio = new JButton("Guardar");
        btnActualizarCustodio = new JButton("Actualizar");
        btnEliminarCustodio = new JButton("Eliminar");

        panelBotones.add(btnGuardarCustodio);
        panelBotones.add(btnActualizarCustodio);
        panelBotones.add(btnEliminarCustodio);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaCustodios = new DefaultTableModel(
            new String[]{"ID", "Cédula", "Nombre", "Apellido", "Rol"}, 0
        );
        tablaCustodios = new JTable(modeloTablaCustodios);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCustodios), BorderLayout.CENTER);

        return panel;
    }
    
    //  PANEL USUARIOS
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));

        txtIdUsuario = new JTextField();
        txtIdUsuario.setEditable(false);
        txtIdCustodioUsuario = new JTextField();

        panelForm.add(new JLabel("ID Usuario:"));
        panelForm.add(txtIdUsuario);
        panelForm.add(new JLabel("ID Custodio Asociado:"));
        panelForm.add(txtIdCustodioUsuario);

        JPanel panelBotones = new JPanel();
        btnGuardarUsuario = new JButton("Guardar");
        btnActualizarUsuario = new JButton("Actualizar");
        btnEliminarUsuario = new JButton("Eliminar");

        panelBotones.add(btnGuardarUsuario);
        panelBotones.add(btnActualizarUsuario);
        panelBotones.add(btnEliminarUsuario);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaUsuarios = new DefaultTableModel(
            new String[]{"ID Usuario", "ID Custodio", "Nombre Custodio"}, 0
        );
        tablaUsuarios = new JTable(modeloTablaUsuarios);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        return panel;
    }
    
    //  PANEL MANTENIMIENTOS
    private JPanel crearPanelMantenimientos() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registro de Mantenimiento"));

        txtDpiMantenimiento = new JTextField();
        txtDpiMantenimiento.setEditable(false);
        txtDetallesMantenimiento = new JTextField();
        txtFechaInicioMantenimiento = new JTextField();
        txtFechaFinMantenimiento = new JTextField();
        txtIdActivoMantenimiento = new JTextField();
        txtIdUsuarioMantenimiento = new JTextField();

        panelForm.add(new JLabel("DPI / Código:"));
        panelForm.add(txtDpiMantenimiento);
        panelForm.add(new JLabel("Detalles:"));
        panelForm.add(txtDetallesMantenimiento);
        panelForm.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        panelForm.add(txtFechaInicioMantenimiento);
        panelForm.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        panelForm.add(txtFechaFinMantenimiento);
        panelForm.add(new JLabel("ID Activo:"));
        panelForm.add(txtIdActivoMantenimiento);
        panelForm.add(new JLabel("ID Usuario:"));
        panelForm.add(txtIdUsuarioMantenimiento);

        JPanel panelBotones = new JPanel();
        btnGuardarMantenimiento = new JButton("Guardar");
        btnActualizarMantenimiento = new JButton("Actualizar");
        btnEliminarMantenimiento = new JButton("Eliminar");
        btnFiltrarMantenimientoActivo = new JButton("Filtrar por Activo");

        panelBotones.add(btnGuardarMantenimiento);
        panelBotones.add(btnActualizarMantenimiento);
        panelBotones.add(btnEliminarMantenimiento);
        panelBotones.add(btnFiltrarMantenimientoActivo);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        modeloTablaMantenimientos = new DefaultTableModel(
            new String[]{"DPI", "Detalles", "Fecha Inicio", "Fecha Fin", "Costo", "Activo", "Usuario"}, 0
        );
        tablaMantenimientos = new JTable(modeloTablaMantenimientos);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMantenimientos), BorderLayout.CENTER);

        return panel;
    }
    
    //  MÉTODOS HELPER DE INTEGRACIÓN CON MODELO
    
    // --- ACTIVOS ---
    public void mostrarLista(List<Activo> activos) {
        modeloTablaActivos.setRowCount(0);
        if (activos != null) {
            for (Activo a : activos) {
                String nombreCustodio = (a.getCustodio() != null) ? a.getCustodio().getNombre() + " " + a.getCustodio().getApellido() : "Sin Custodio";
                modeloTablaActivos.addRow(new Object[]{
                    a.getIdActivo(),
                    a.getNombreActivo(),
                    a.getMarca(),
                    a.getTipoActivo(),
                    a.getCostoAdquicicion(), // Atributo exacto de tu modelo Activo
                    a.getEstadoActivo(),
                    nombreCustodio
                });
            }
        }
    }
    
    public Activo obtenerDatosFormulario() {
        try {
            String nombre = txtNombreActivo.getText().trim();
            String marca = txtMarcaActivo.getText().trim();
            String tipo = txtTipoActivo.getText().trim();
            double costo = Double.parseDouble(txtCostoActivo.getText().trim());
            String estado = txtEstadoActivo.getText().trim();

            int idCustodio = Integer.parseInt(txtIdCustodioActivo.getText().trim());
            Custodio custodioAux = new Custodio();
            custodioAux.setIdCustodio(idCustodio);

            if (nombre.isEmpty() || tipo.isEmpty() || estado.isEmpty()) {
                return null;
            }

            // Usamos Hardware como instancia concreta de la clase abstracta Activo
            return new Hardware(0, 0, nombre, marca, tipo, costo, estado, custodioAux);
        } catch (Exception e) {
            return null;
        }
    }
    
    public String obtenerIdSeleccionado() {
        int fila = tablaActivos.getSelectedRow();
        return (fila != -1) ? tablaActivos.getValueAt(fila, 0).toString() : null;
    }
    
    // --- CUSTODIOS ---
    public void mostrarListaCustodios(List<Custodio> custodios) {
        modeloTablaCustodios.setRowCount(0);
        if (custodios != null) {
            for (Custodio c : custodios) {
                modeloTablaCustodios.addRow(new Object[]{
                    c.getIdCustodio(),
                    c.getCedula(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getRol()
                });
            }
        }
    }
    
    public Custodio obtenerDatosCustodio() {
        try {
            String cedula = txtCedulaCustodio.getText().trim();
            String nombre = txtNombreCustodio.getText().trim();
            String apellido = txtApellidoCustodio.getText().trim();
            String rol = txtRolCustodio.getText().trim();

            if (cedula.isEmpty() || nombre.isEmpty()) return null;

            return new Custodio(0, cedula, nombre, apellido, rol);
        } catch (Exception e) {
            return null;
        }
    }
    
    // --- USUARIOS ---
    public void mostrarListaUsuarios(List<Usuario> usuarios) {
        modeloTablaUsuarios.setRowCount(0);
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                String nombreCustodio = (u.getCustodio() != null) ? u.getCustodio().getNombre() + " " + u.getCustodio().getApellido() : "N/A";
                int idCustodio = (u.getCustodio() != null) ? u.getCustodio().getIdCustodio() : 0;
                
                modeloTablaUsuarios.addRow(new Object[]{
                    u.getIdUsuario(),
                    idCustodio,
                    nombreCustodio
                });
            }
        }
    }

    public Usuario obtenerDatosUsuario() {
        try {
            int idCustodio = Integer.parseInt(txtIdCustodioUsuario.getText().trim());
            Custodio c = new Custodio();
            c.setIdCustodio(idCustodio);

            return new Usuario(0, c);
        } catch (Exception e) {
            return null;
        }
    }
    
    // --- MANTENIMIENTOS ---
    public void mostrarListaMantenimientos(List<RegMantenimiento> mantenimientos) {
        modeloTablaMantenimientos.setRowCount(0);
        if (mantenimientos != null) {
            for (RegMantenimiento rm : mantenimientos) {
                String nombreActivo = (rm.getActivo() != null) ? rm.getActivo().getNombreActivo() : "N/A";
                String idUsuario = (rm.getUsuario() != null) ? String.valueOf(rm.getUsuario().getIdUsuario()) : "N/A";

                modeloTablaMantenimientos.addRow(new Object[]{
                    rm.getDpi(), // Clave primaria según tu clase RegMantenimiento
                    rm.getDetallesMantenimiento(),
                    rm.getFechaInicio(),
                    rm.getFechaFin(),
                    rm.getCostoMantenimiento(),
                    nombreActivo,
                    idUsuario
                });
            }
        }
    }
}
