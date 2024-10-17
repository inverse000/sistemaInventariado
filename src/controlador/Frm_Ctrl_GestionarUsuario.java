package controlador;

import conexion.Conexion;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;
import vista.FromMenu;
import vista.InterGestionarUsuario;

public class Frm_Ctrl_GestionarUsuario {
    
    InterGestionarUsuario vista;
    
    
    private int idUsuario = 0;
    

    public Frm_Ctrl_GestionarUsuario() {
    }
    
    
    
    public Frm_Ctrl_GestionarUsuario(InterGestionarUsuario vista) {
        this.vista = vista;
        init();
    }
    
    
    
    
    private void init() {
        
        
        vista.setSize(new Dimension(1420, 580));
        vista.setTitle("Gestionar Usuarios");
        vista.setVisible(true);
        vista.setLocation(20, 45);
        FromMenu.desktopPane.add(vista);
        
        vista.toFront();
        
        
        CargarTablaUsuarios();
        
        
        
        ImageIcon wallpaper = new ImageIcon("src/img/fondo2.jpg");
        Icon icono = new ImageIcon(wallpaper.getImage().getScaledInstance(900, 500, WIDTH));
        
        vista.repaint();
        
        
        vista.jButton_Guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton_GuardarActionPerformed(e);
            }

            
        });
        vista.jButton_eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton_eliminarActionPerformed(e);
            }

            
        });
        vista.jButton_editar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton_editarActionPerformed(e);
            }

            
        });
        
        vista.jButton_Cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton_CancelarActionPerformed(e);
            }

            
        });
        
        vista.jtbUsuario.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && vista.jtbUsuario.getSelectedRow() != -1) {
                idUsuario = (int) vista.jtbUsuario.getValueAt(vista.jtbUsuario.getSelectedRow(), 0);
                System.out.println("Proveedor seleccionado con ID: " + idUsuario);
                
            }
        });
         
    }
    private void jButton_GuardarActionPerformed(ActionEvent e) {
        
          Usuario usuario = new Usuario();
  

    // Verifica si es un nuevo registro o una actualización
    boolean isNewRecord = (idUsuario == 0); // true si es nuevo, false si es actualización

    if (vista.jtxtNombre.getText().isEmpty() || vista.jtxtApellido.getText().isEmpty() 
            || vista.jtxtDni.getText().isEmpty() || vista.jtxtDireccion.getText().isEmpty() 
            || vista.jtxtTelefono.getText().isEmpty() || vista.jtxtCorreo.getText().isEmpty()
            || vista.jtxtUsuario.getText().isEmpty() || vista.jtxtPassword.getText().isEmpty() 
            || vista.jComboBox_Rol.getSelectedItem().equals("Seleccione Rol:")) {
        
        JOptionPane.showMessageDialog(null, "¡Completa todos los campos!");

    } else {
        // Rellenar objeto usuario con los datos del formulario
        usuario.setNombre(vista.jtxtNombre.getText().trim());
        usuario.setApellido(vista.jtxtApellido.getText().trim());
        usuario.setIdentificacion(vista.jtxtDni.getText().trim());
        usuario.setDireccion(vista.jtxtDireccion.getText().trim());
        usuario.setTelefono(vista.jtxtTelefono.getText().trim());
        usuario.setCorreo(vista.jtxtCorreo.getText().trim());
        usuario.setUsuario(vista.jtxtUsuario.getText().trim());
        usuario.setPassword(vista.jtxtPassword.getText().trim());
        usuario.setRol(vista.jComboBox_Rol.getSelectedItem().toString());
        usuario.setEstado(obtenerEstado());

        if (isNewRecord) {
            // Nuevo registro
            if (!existeUsuario(usuario.getUsuario())) {
                if (guardar(usuario)) {
                    JOptionPane.showMessageDialog(null, "¡Usuario Registrado!");
                    CargarTablaUsuarios();
                    Limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "¡Error al registrar usuario!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El usuario ya está registrado, ingrese otro.");
            }
        } else {
            // Actualización de registro existente
            if (actualizar(usuario, idUsuario)) {
                JOptionPane.showMessageDialog(null, "¡Datos del usuario actualizados!");
                CargarTablaUsuarios();
                idUsuario = 0; // Resetear el valor de idUsuario después de actualizar
                Limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "Error al actualizar los datos del usuario");
            }
        } 
   }}
    
    
    
    private void jButton_eliminarActionPerformed(java.awt.event.ActionEvent e) {
        
      
        
        if (idUsuario == 0) {
            JOptionPane.showMessageDialog(null, "¡Seleccione un usuario!");
        } else {
            if (!eliminar(idUsuario)) {
                JOptionPane.showMessageDialog(null, "¡Usuario Eliminado!");
                CargarTablaUsuarios();
                Limpiar();
                idUsuario = 0;
            } else {
                JOptionPane.showMessageDialog(null, "¡Error al eliminar usuario!");
                Limpiar();
                
            }
        }
    }
    
    
    private void jButton_editarActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = vista.jtbUsuario.getSelectedRow();
        if (selectedRow != -1) {
            idUsuario = (int) vista.jtbUsuario.getValueAt(selectedRow, 0);
            EnviarDatosUsuarioSeleccionado(idUsuario);
        } else {
            System.out.println("No se ha seleccionado ningún proveedor.");
        }
    }
    
    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {                                                 
       
         
         Limpiar();
         
         vista.jtxtNombre.requestFocus();
         
         
    }    
    
    public boolean guardar(Usuario objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_usuario values(?,?,?,?,?,?,?,?,?,?,?)");
            consulta.setInt(1, 0);//id
            consulta.setString(2, objeto.getNombre());
            consulta.setString(3, objeto.getApellido());
            consulta.setString(4, objeto.getIdentificacion());
            consulta.setString(5, objeto.getDireccion());
            consulta.setString(6, objeto.getTelefono());
            consulta.setString(7, objeto.getCorreo());
            consulta.setString(8, objeto.getUsuario());
            consulta.setString(9, objeto.getPassword());
            consulta.setString(10, objeto.getRol());
            consulta.setInt(11, objeto.getEstado());
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar usuario: " + e);
        }
        return respuesta;
    }
    
    public boolean actualizar(Usuario objeto, int idUsuario) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {

            PreparedStatement consulta = cn.prepareStatement("update tb_usuario set nombre=?, apellido = ?, identificacion = ?, direccion = ?, telefono = ?, correo = ?, usuario = ?, password= ?, rol = ?, estado = ? where idUsuario ='" + idUsuario + "'");
            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getApellido());
            consulta.setString(3, objeto.getIdentificacion());
            consulta.setString(4, objeto.getDireccion());
            consulta.setString(5, objeto.getTelefono());
            consulta.setString(6, objeto.getCorreo());
            consulta.setString(7, objeto.getUsuario());
            consulta.setString(8, objeto.getPassword());
            consulta.setString(9, objeto.getRol());
            consulta.setInt(10, objeto.getEstado());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e);
        }
        return respuesta;
    }

        public boolean eliminar(int idUsuario) {
                boolean respuesta = false;
                Connection cn = Conexion.conectar();
                try {
                    PreparedStatement consulta = cn.prepareStatement(
                            "delete from tb_usuario where idUsuario ='" + idUsuario + "'");
                    consulta.executeUpdate();

                    if (consulta.executeUpdate() > 0) {
                        respuesta = true;
                    }
                    cn.close();
                } catch (SQLException e) {
                    System.out.println("Error al eliminar usuario: " + e);
                }
                return respuesta;
            }
    /**
     * ********************************************************************
     * metodo para consultar si el producto ya esta registrado en la BBDD
     * ********************************************************************
     */
    public boolean existeUsuario(String usuario) {
        boolean respuesta = false;
        String sql = "select usuario from tb_usuario where usuario = '" + usuario + "';";
        Statement st;
        try {
            Connection cn = Conexion.conectar();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar usuario: " + e);
        }
        return respuesta;
    }
     private int obtenerEstado() {
        String estado = (String) vista.jComboBox_estado.getSelectedItem();
        if ("Activo".equals(estado)) {
            return 1;
        } else if ("Inactivo".equals(estado)) {
            return 0;
        }
        return -1; // Valor por defecto en caso de error
    }
    
    private void Limpiar() {
        
        vista.jtxtNombre.setText("");
        vista.jtxtApellido.setText("");
        vista.jtxtDni.setText("");
        vista.jtxtDireccion.setText("");
        vista.jtxtTelefono.setText("");
        vista.jtxtCorreo.setText("");
        vista.jtxtUsuario.setText("");
        vista.jtxtPassword.setText("");
        vista.jComboBox_Rol.setSelectedIndex(0);
        vista.jComboBox_estado.setSelectedIndex(0);
        
        idUsuario=0;
    }

    public void CargarTablaUsuarios() {
        Connection con = Conexion.conectar();
        DefaultTableModel model = new DefaultTableModel();
        String sql = "select * from tb_usuario";
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            vista.jtbUsuario = new JTable(model);
            vista.jscllUsuario.setViewportView(vista.jtbUsuario);

            model.addColumn("N°");
            model.addColumn("Nombre");
            model.addColumn("Apellido");
            model.addColumn("DNI");
            model.addColumn("Dirección");
            model.addColumn("Teléfono");
            model.addColumn("Correo");
            model.addColumn("Usuario");
            model.addColumn("Password");
            model.addColumn("Rol");
            model.addColumn("Estado");

            while (rs.next()) {
                Object fila[] = new Object[11];
                for (int i = 0; i < 11; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                model.addRow(fila);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla usuarios: " + e);
        }
       
    }
    
    
    private void EnviarDatosUsuarioSeleccionado(int idUsuario) {
    try {
        Connection con = Conexion.conectar();
        // Usar parámetros preparados correctamente para evitar inyección SQL
        PreparedStatement pst = con.prepareStatement("SELECT * FROM tb_usuario WHERE idUsuario = ?");
        pst.setInt(1, idUsuario); // Asignar el parámetro idUsuario
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            vista.jtxtNombre.setText(rs.getString("nombre"));
            vista.jtxtApellido.setText(rs.getString("apellido"));
            vista.jtxtDni.setText(rs.getString("identificacion"));
            vista.jtxtDireccion.setText(rs.getString("direccion"));
            vista.jtxtTelefono.setText(rs.getString("telefono"));
            vista.jtxtCorreo.setText(rs.getString("correo"));
            vista.jtxtUsuario.setText(rs.getString("usuario"));
            vista.jtxtPassword.setText(rs.getString("password"));
            vista.jComboBox_Rol.setSelectedItem(rs.getString("rol"));

            // Si deseas mostrar el campo estado en la interfaz, puedes hacerlo así
            // vista.jComboBox_Estado.setSelectedItem(rs.getInt("estado") == 1 ? "Activo" : "Inactivo");
        }
        
        con.close();
    } catch (SQLException e) {
        System.out.println("Error al seleccionar usuario: " + e);
    }
}

}