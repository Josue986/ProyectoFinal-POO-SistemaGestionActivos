package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Modelo.*;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

// Solo una clase, extendiendo JFrame directamente
public class Vista extends JFrame {
    
    //--- Pestaña 1: ACTIVOS ---
    public JTextField txtIdActivo, txtNombreActivo, txtMarcaActivo, txtCostoActivo, txtEstadoActivo, txtIdCustodioActivo, txtAnniosUsoActivo;
    public JComboBox<String> cbTipoActivo;
    public JButton btnGuardarActivo, btnActualizarActivo, btnEliminarActivo;
    public JTable tablaActivos;
    private DefaultTableModel modeloTablaActivos;
    
    //--- Pestaña 2: CUSTODIOS ---
    public JTextField txtIdCustodio, txtCedulaCustodio, txtNombreCustodio, txtApellidoCustodio, txtRolCustodio;
    public JButton btnGuardarCustodio, btnActualizarCustodio, btnEliminarCustodio;
    public JTable tablaCustodios;
    private DefaultTableModel modeloTablaCustodios;

    //--- Pestaña 3: USUARIOS ---
    public JTextField txtIdUsuario, txtIdCustodioUsuario;
    public JButton btnGuardarUsuario, btnActualizarUsuario, btnEliminarUsuario;
    public JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;

    //--- Pestaña 4: MANTENIMIENTOS ---
    public JTextField txtIdMantenimiento, txtDetallesMantenimiento, txtFechaInicioMantenimiento, txtFechaFinMantenimiento, txtCostoMantenimiento, txtIdActivoMantenimiento, txtIdUsuarioMantenimiento;
    public JButton btnGuardarMantenimiento, btnActualizarMantenimiento, btnEliminarMantenimiento, btnFiltrarMantenimientoActivo;
    public JTable tablaMantenimientos;
    private DefaultTableModel modeloTablaMantenimientos;

    public Vista() {
        // Configuramos la ventana básica
        this.setTitle("Sistema de Gestión de Activos e Inventarios - UTPL");
        this.setSize(1000, 700);
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

        JPanel panelForm = new JPanel(new GridLayout(8, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Activo"));

        txtIdActivo = new JTextField();
        txtIdActivo.setEditable(false);
        txtNombreActivo = new JTextField();
        txtMarcaActivo = new JTextField();
        
        // JComboBox con las opciones concretas
        cbTipoActivo = new JComboBox<>(new String[]{"Hardware", "Periferico", "Licencia"});
        
        txtCostoActivo = new JTextField();
        txtEstadoActivo = new JTextField();
        txtIdCustodioActivo = new JTextField();
        txtAnniosUsoActivo = new JTextField("0");

        panelForm.add(new JLabel("ID Activo:"));
        panelForm.add(txtIdActivo);
        panelForm.add(new JLabel("Nombre Activo:"));
        panelForm.add(txtNombreActivo);
        panelForm.add(new JLabel("Marca:"));
        panelForm.add(txtMarcaActivo);
        panelForm.add(new JLabel("Tipo Activo:"));
        panelForm.add(cbTipoActivo);
        panelForm.add(new JLabel("Costo Adquisición ($):"));
        panelForm.add(txtCostoActivo);
        panelForm.add(new JLabel("Años de Uso:"));
        panelForm.add(txtAnniosUsoActivo);
        panelForm.add(new JLabel("Estado Activo:"));
        panelForm.add(txtEstadoActivo);
        panelForm.add(new JLabel("ID Custodio:"));
        panelForm.add(txtIdCustodioActivo);

        JPanel panelBotones = new JPanel();
        btnGuardarActivo = new JButton("Guardar");
        btnActualizarActivo = new JButton("Actualizar");
        btnEliminarActivo = new JButton("Eliminar");

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

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registro de Mantenimiento"));

        txtIdMantenimiento = new JTextField();
        txtIdMantenimiento.setEditable(false);
        txtDetallesMantenimiento = new JTextField();
        txtFechaInicioMantenimiento = new JTextField();
        txtFechaFinMantenimiento = new JTextField();
        txtCostoMantenimiento = new JTextField();
        txtIdActivoMantenimiento = new JTextField();
        txtIdUsuarioMantenimiento = new JTextField();

        panelForm.add(new JLabel("ID Mantenimiento:"));
        panelForm.add(txtIdMantenimiento);
        panelForm.add(new JLabel("Detalles:"));
        panelForm.add(txtDetallesMantenimiento);
        panelForm.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        panelForm.add(txtFechaInicioMantenimiento);
        panelForm.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        panelForm.add(txtFechaFinMantenimiento);
        panelForm.add(new JLabel("Costo ($):"));
        panelForm.add(txtCostoMantenimiento);
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
            new String[]{"ID", "Detalles", "Fecha Inicio", "Fecha Fin", "Costo", "Activo", "Usuario"}, 0
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
                    a.getIdActivo(), // Clave primaria
                    a.getNombreActivo(),
                    a.getMarca(),
                    a.getTipoActivo(),
                    a.getCostoAdquicicion(),
                    a.getEstadoActivo(),
                    nombreCustodio
                });
            }
        }
    }
    
    public Activo obtenerDatosFormulario() {
        try {
        int idActivo = 0;
        if (!txtIdActivo.getText().trim().isEmpty()) {
            idActivo = Integer.parseInt(txtIdActivo.getText().trim());
        }

        String nombre = txtNombreActivo.getText().trim();
        String marca = txtMarcaActivo.getText().trim();
        String tipo = cbTipoActivo.getSelectedItem().toString();
        String estado = txtEstadoActivo.getText().trim();

        if (nombre.isEmpty() || estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor llena los campos obligatorios: Nombre y Estado del Activo.", 
                "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // Correcion de formato numérico por estabilidad (remueve comas y espacios extra)
        String costoStr = txtCostoActivo.getText().trim().replace(",", ".");
        double costo = costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr);

        int anniosUso = txtAnniosUsoActivo.getText().trim().isEmpty() 
            ? 0 
            : Integer.parseInt(txtAnniosUsoActivo.getText().trim());

        Custodio custodioAux = null;
        if (!txtIdCustodioActivo.getText().trim().isEmpty()) {
            int idCustodio = Integer.parseInt(txtIdCustodioActivo.getText().trim());
            custodioAux = new Custodio();
            custodioAux.setIdCustodio(idCustodio);
        }

        switch (tipo) {
            case "Hardware":
                return new Hardware(anniosUso, idActivo, nombre, marca, tipo, costo, estado, custodioAux);

            case "Periferico":
                String tipoConexion = "USB / Generico"; 
                return new Periferico(anniosUso, tipoConexion, idActivo, nombre, marca, tipo, costo, estado, custodioAux);

            case "Licencia":
                Date fechaExpiracionPorDefecto = new Date();
                double costoRenovacion = 0.0;
                double costoMantenimiento = 0.0;

                return new Licencia(
                    fechaExpiracionPorDefecto, 
                    costoRenovacion, 
                    idActivo, 
                    nombre, 
                    marca, 
                    tipo, 
                    costoMantenimiento, 
                    estado, 
                    costo, 
                    custodioAux
                );

            default:
                return new Hardware(anniosUso, idActivo, nombre, marca, tipo, costo, estado, custodioAux);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El costo o los años de uso e ID de Custodio deben ser valores numéricos válidos.", 
        "Error de Formato", JOptionPane.ERROR_MESSAGE);
        return null;
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error insospechado al construir el activo: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        return null;
    }
    }
    
    public void cargarFormularioActivoDesdeTabla() {
    int fila = tablaActivos.getSelectedRow();
    if (fila != -1) {
        txtIdActivo.setText(tablaActivos.getValueAt(fila, 0).toString());
        txtNombreActivo.setText(tablaActivos.getValueAt(fila, 1).toString());
        txtMarcaActivo.setText(tablaActivos.getValueAt(fila, 2).toString());
        cbTipoActivo.setSelectedItem(tablaActivos.getValueAt(fila, 3).toString());
        txtCostoActivo.setText(tablaActivos.getValueAt(fila, 4).toString());
        txtEstadoActivo.setText(tablaActivos.getValueAt(fila, 5).toString());
        
        // Carga auxiliar para prevenir campos vacíos al actualizar
        txtAnniosUsoActivo.setText("0");
        txtIdCustodioActivo.setText("");
    }
}
    
    // --- ACTIVOS ---
    public String obtenerIdActivoSeleccionado() {
        int fila = tablaActivos.getSelectedRow();
        return (fila != -1) ? tablaActivos.getValueAt(fila, 0).toString() : null;
    }
    
    // --- CUSTODIOS ---
    public String obtenerIdCustodioSeleccionado() {
        int fila = tablaCustodios.getSelectedRow();
        return (fila != -1) ? tablaCustodios.getValueAt(fila, 0).toString() : null;
    }
    public void mostrarListaCustodios(List<Custodio> custodios) {
        modeloTablaCustodios.setRowCount(0);
        if (custodios != null) {
            for (Custodio c : custodios) {
                modeloTablaCustodios.addRow(new Object[]{
                    c.getIdCustodio(), // Clave primaria
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
            String idCustodio = txtIdCustodio.getText().trim(); // Clave primaria
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
    public String obtenerIdUsuarioSeleccionado() {
        int fila = tablaUsuarios.getSelectedRow();
        return (fila != -1) ? tablaUsuarios.getValueAt(fila, 0).toString() : null;
    }
    
    public void mostrarListaUsuarios(List<Usuario> usuarios) {
        modeloTablaUsuarios.setRowCount(0);
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                String nombreCustodio = (u.getCustodio() != null) ? u.getCustodio().getNombre() + " " + u.getCustodio().getApellido() : "N/A";
                int idCustodio = (u.getCustodio() != null) ? u.getCustodio().getIdCustodio() : 0;
                
                modeloTablaUsuarios.addRow(new Object[]{
                    u.getIdUsuario(), // Clave primaria
                    idCustodio, // Clave foranea
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
    public String obtenerIdMantenimientoSeleccionado() {
        int fila = tablaMantenimientos.getSelectedRow();
        return (fila != -1) ? tablaMantenimientos.getValueAt(fila, 0).toString() : null;
    }
    
    public void mostrarListaMantenimientos(List<RegMantenimiento> mantenimientos) {
        modeloTablaMantenimientos.setRowCount(0);
        if (mantenimientos != null) {
            for (RegMantenimiento rm : mantenimientos) {
                String nombreActivo = (rm.getActivo() != null) ? rm.getActivo().getNombreActivo() : "N/A";
                String idUsuario = (rm.getUsuario() != null) ? String.valueOf(rm.getUsuario().getIdUsuario()) : "N/A";

                modeloTablaMantenimientos.addRow(new Object[]{
                    rm.getIdMantenimiento(), // Clave primaria
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
    
    public RegMantenimiento obtenerDatosMantenimiento() {
        try {
        String detalles = txtDetallesMantenimiento.getText().trim();
        String fInicioStr = txtFechaInicioMantenimiento.getText().trim();
        String fFinStr = txtFechaFinMantenimiento.getText().trim();      
        
        if (detalles.isEmpty() || fInicioStr.isEmpty() || 
            txtIdActivoMantenimiento.getText().trim().isEmpty() || 
            txtIdUsuarioMantenimiento.getText().trim().isEmpty()) {
            return null;
        }

        String costoStr = txtCostoMantenimiento.getText().trim().replace(",", ".");
        double costo = costoStr.isEmpty() ? 0.0 : Double.parseDouble(costoStr);
        
        int idActivo = Integer.parseInt(txtIdActivoMantenimiento.getText().trim());
        // Firma exacta: (anniosUso = 0, idActivo = idActivo, nombre, marca, tipo, costo, estado, custodio)
        Activo activoAux = new Hardware(0, idActivo, "", "", "Hardware", 0.0, "", null);

        int idUsuario = Integer.parseInt(txtIdUsuarioMantenimiento.getText().trim());
        Usuario usuarioAux = new Usuario(idUsuario, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fInicio = sdf.parse(fInicioStr);
        Date fFin = fFinStr.isEmpty() ? null : sdf.parse(fFinStr);
        
        RegMantenimiento reg = new RegMantenimiento(0, detalles, fInicio, fFin, activoAux, usuarioAux);
        reg.setCostoMantenimiento(costo);
        return reg;
    } catch (Exception e) {
        System.err.println("Error en obtenerDatosMantenimiento: " + e.getMessage());
        return null;
    }
    }
}
