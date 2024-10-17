
package controlador;

import conexion.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vista.*;
import static vista.InterGestionarCliente.jLabel_wallpaper;
import static vista.InterGestionarCliente.jTable_clientes;


public class Frm_Ctrl_GestionarCliente {
    
    InterGestionarCliente vista;
    
    private int idCliente;
     
    public Frm_Ctrl_GestionarCliente() {
    
    }
    
    public Frm_Ctrl_GestionarCliente(InterGestionarCliente vista) {
        
        this.vista = vista;
        init();
        
    }
    
    private void init() {
        
        vista.setSize(new Dimension(900, 500));
        vista.setTitle("Gestionar Clientes");
        vista.setVisible(true);
        vista.setLocation(470, 45);
        FromMenu.desktopPane.add(vista);

        vista.toFront();
        
         //Cargar tabla
       
        CargarTablaClientes();
        
         //insertar imagen en nuestro JLabel
        ImageIcon wallpaper = new ImageIcon("src/img/Fondo2.jpg");
        Icon icono = new ImageIcon(wallpaper.getImage().getScaledInstance(900, 500, WIDTH));
        jLabel_wallpaper.setIcon(icono);
        vista.repaint();
        
        
        
        //Botones
        
        
         vista.jButton_actualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton_actualizarActionPerformed(e);
            }

            
        });
        vista.jButton_eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton_eliminarActionPerformed(e);
            }

            
        });
         
    }
    
    
    
    //Funcion del boton
    
    private void jButton_actualizarActionPerformed(java.awt.event.ActionEvent evt) {                                                   
   
      
        if (vista.txt_nombre.getText().isEmpty() && vista.txt_apellido.getText().isEmpty()
                && vista.txt_dni.getText().isEmpty() && vista.txt_telefono.getText().isEmpty() && vista.txt_direccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡Completa todos los campos!");
        } else {
            
            

            Cliente cliente = new Cliente();
            Ctrl_Cliente controlCliente = new Ctrl_Cliente();
                
        

            
            cliente.setNombre(vista.txt_nombre.getText().trim());
            cliente.setApellido(vista.txt_apellido.getText().trim());
            cliente.setdni(vista.txt_dni.getText().trim());
            cliente.setTelefono(vista.txt_telefono.getText().trim());
            cliente.setDireccion(vista.txt_direccion.getText().trim());
            cliente.setEstado(1);

            if (controlCliente.actualizar(cliente, idCliente)) {
                JOptionPane.showMessageDialog(null, "¡Datos del cliente actualizados!");
                this.CargarTablaClientes();
                this.Limpiar();
            }
             else {
                JOptionPane.showMessageDialog(null, "El DNI ya está registrado en la base de datos");
            }
        }
       
    }
    
    
    
    
    
    private void jButton_eliminarActionPerformed(java.awt.event.ActionEvent evt) {                                                 
   
        Ctrl_Cliente controlCliente = new Ctrl_Cliente();
        if (idCliente == 0) {
            JOptionPane.showMessageDialog(null, "¡Seleccione un cliente!");
        } else {
            if (!controlCliente.eliminar(idCliente)) {
                JOptionPane.showMessageDialog(null, "¡Cliente Eliminado!");
                this.CargarTablaClientes();
                this.Limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "¡Error al eliminar cliente!");
                this.Limpiar();
            }
        }
        
    }
        
        
        
      
    
    /*
     * *****************************************************
     * metodo para limpiar
     * *****************************************************
     */
    private void Limpiar() {
        vista.txt_nombre.setText("");
        vista.txt_telefono.setText("");
        vista.txt_apellido.setText("");
        vista.txt_direccion.setText("");
        vista.txt_dni.setText("");
    }


    /*
     * *****************************************************
     * metodo para mostrar todos los clientes registrados
     * *****************************************************
     */
    private void CargarTablaClientes() {
        Connection con = Conexion.conectar();
        DefaultTableModel model = new DefaultTableModel();
        String sql = "select * from tb_cliente";
        Statement st;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            InterGestionarCliente.jTable_clientes = new JTable(model);
            InterGestionarCliente.jScrollPane1.setViewportView(InterGestionarCliente.jTable_clientes);

            model.addColumn("N°");//ID
            model.addColumn("nombre");
            model.addColumn("apellido");
            model.addColumn("Dni");
            model.addColumn("telefono");
            model.addColumn("direccion");
            model.addColumn("estado");

            while (rs.next()) {
                Object fila[] = new Object[7];
                for (int i = 0; i < 7; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                model.addRow(fila);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al llenar la tabla clientes: " + e);
        }
        //evento para obtener campo al cual el usuario da click
        //y obtener la interfaz que mostrara la informacion general
        jTable_clientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = jTable_clientes.rowAtPoint(e.getPoint());
                int columna_point = 0;

                if (fila_point > -1) {
                    idCliente = (int) model.getValueAt(fila_point, columna_point);
                    EnviarDatosClienteSeleccionado(idCliente);//metodo
                }
            }
        });
    }


    /*
     * **************************************************
     * Metodo que envia datos seleccionados
     * **************************************************
     */
    private void EnviarDatosClienteSeleccionado(int idCliente) {
        try {
            Connection con = Conexion.conectar();
            PreparedStatement pst = con.prepareStatement(
                    "select * from tb_cliente where idCliente = '" + idCliente + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                vista.txt_nombre.setText(rs.getString("nombre"));
                vista.txt_apellido.setText(rs.getString("apellido"));
                vista.txt_dni.setText(rs.getString("dni"));
                vista.txt_telefono.setText(rs.getString("telefono"));
                vista.txt_direccion.setText(rs.getString("direccion"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al seleccionar cliente: " + e);
        }
    }
       
    
}
