package controlador;

import conexion.Conexion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import modelo.Cliente;
import vista.*;


public class Ctrl_Cliente {
    
     InterCliente vista;

     //controlador vacio
    public Ctrl_Cliente() {
    }

     //controlador con el parametro del interfaz
    public Ctrl_Cliente(InterCliente vista) {
        this.vista = vista;
        init();
    }

    
      //metodo para el inicio del interfaz
    
    private void init() {
        
       
        vista.setSize(new Dimension(400, 300));
        vista.setTitle("Nuevo Cliente");
        vista.setVisible(true);
        vista.setLocation(35, 45);
        
        
        FromMenu.desktopPane.add(vista); // Añadir al JDesktopPane
        vista.toFront();
        
        vista.txt_nombre.requestFocus();
        
        //el boton que se usara
        
         vista.jButton_Guardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton_GuardarActionPerformed(e);
            }
        });
         
    }
    
    //funcion del boton
    
    private void jButton_GuardarActionPerformed(ActionEvent evt) {

        Cliente cliente = new Cliente();

        if (!vista.txt_nombre.getText().isEmpty() && !vista.txt_apellido.getText().isEmpty() && !vista.txt_dni.getText().isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Correcto");

            if (!existeCliente(vista.txt_dni.getText().trim())) {

                cliente.setNombre(vista.txt_nombre.getText().trim());
                cliente.setApellido(vista.txt_apellido.getText().trim());
                cliente.setdni(vista.txt_dni.getText().trim());
                cliente.setTelefono(vista.txt_telefono.getText().trim());
                cliente.setDireccion(vista.txt_direccion.getText().trim());
                cliente.setEstado(1);

                if (guardar(cliente)) {
                    JOptionPane.showMessageDialog(null, "Registro Guardado");
                    vista.txt_nombre.setBackground(Color.green);
                    vista.txt_apellido.setBackground(Color.green);
                    vista.txt_dni.setBackground(Color.green);
                    vista.txt_telefono.setBackground(Color.green);
                    vista.txt_direccion.setBackground(Color.green);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al Guardar");
                }

            } else {
                JOptionPane.showMessageDialog(null, "El cliente ya esta registrado en la Base de Datos.");
                vista.txt_nombre.setBackground(Color.white);
                vista.txt_apellido.setBackground(Color.white);
                vista.txt_dni.setBackground(Color.white);
                vista.txt_telefono.setBackground(Color.white);
                vista.txt_direccion.setBackground(Color.white);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Completa todos los campos");
            vista.txt_nombre.setBackground(Color.red);
            vista.txt_apellido.setBackground(Color.red);
            vista.txt_dni.setBackground(Color.red);
            vista.txt_telefono.setBackground(Color.red);
            vista.txt_direccion.setBackground(Color.red);
        }
        
        //metodo limpiar
        Limpiar();

    }
    
    
    
    // metodos para optimizar la funcion del boton
    
       private void Limpiar() {
        vista.txt_nombre.setText("");
        vista.txt_apellido.setText("");
        vista.txt_dni.setText("");
        vista.txt_telefono.setText("");
        vista.txt_direccion.setText("");
    }
    
    
    
    
       
    /**
     * **************************************************
     * metodo para guardar un nuevo clientw
     * **************************************************
     */
    public boolean guardar(Cliente objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement("insert into tb_cliente values(?,?,?,?,?,?,?)");
            consulta.setInt(1, 0);//id
            consulta.setString(2, objeto.getNombre());
            consulta.setString(3, objeto.getApellido());
            consulta.setString(4, objeto.getdni());
            consulta.setString(5, objeto.getTelefono());
            consulta.setString(6, objeto.getDireccion());
            consulta.setInt(7, objeto.getEstado());
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar cliente: " + e);
        }
        return respuesta;
    }

    /**
     * ********************************************************************
     * metodo para consultar si el producto ya esta registrado en la BBDD
     * ********************************************************************
     */
    public boolean existeCliente(String dni) {
        boolean respuesta = false;
        String sql = "select dni from tb_cliente where dni = '" + dni + "';";
        Statement st;
        try {
            Connection cn = Conexion.conectar();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar cliente: " + e);
        }
        return respuesta;
    }

    /**
     * **************************************************
     * metodo para actualizar un cliente
     * **************************************************
     */
    public boolean actualizar(Cliente objeto, int idCliente) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {

            PreparedStatement consulta = cn.prepareStatement("update tb_cliente set nombre=?, apellido = ?, dni = ?, telefono= ?, direccion = ?, estado = ? where idCliente ='" + idCliente + "'");
            consulta.setString(1, objeto.getNombre());
            consulta.setString(2, objeto.getApellido());
            consulta.setString(3, objeto.getdni());
            consulta.setString(4, objeto.getTelefono());
            consulta.setString(5, objeto.getDireccion());
            consulta.setInt(6, objeto.getEstado());

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e);
        }
        return respuesta;
    }

    /**
     * **************************************************
     * metodo para eliminar un cliente
     * **************************************************
     */
    public boolean eliminar(int idCliente) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "delete from tb_cliente where idCliente ='" + idCliente + "'");
            consulta.executeUpdate();

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e);
        }
        return respuesta;
    }

}
