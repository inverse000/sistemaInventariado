package controlador;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import vista.*;
import java.beans.PropertyVetoException;

public class FrmPrincipal_Ctrl_Menu {

    public javax.swing.JTable jTable_productos;
    private FromMenu menu;
    public static JDesktopPane desktopPane;

    public FrmPrincipal_Ctrl_Menu(FromMenu menu) {
        this.menu = menu;
        init();
    }

    private void init() {
        // Set default size and properties of the menu
        menu.setSize(new Dimension(1820, 700));
        menu.setExtendedState(menu.MAXIMIZED_BOTH);
        menu.setLocationRelativeTo(null);
        menu.setTitle("Sistema de ventas");
        menu.setLayout(null);

        FromMenu.desktopPane = new JDesktopPane();
        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        FromMenu.desktopPane.setBounds(0, 0, ancho, (alto - 120));
        menu.add(FromMenu.desktopPane);

      

        menu.jMenuItem_gestionar_usuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_gestionar_usuariosActionPerformed(e);
            }
        });

    

        menu.jMenuItem_gestionar_producto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_gestionar_productoActionPerformed(e);
            }
        });

        menu.jMenuItem_actualizar_stock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_actualizar_stockActionPerformed(e);
            }
        });
        
        
        menu.jMenuItem_Entradas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_EntradasrActionPerformed(e);
            }
        });

     
        menu.jMenuItem_gestionar_proveedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_gestionar_proveedoresActionPerformed(e);
            }

        });


        menu.jMenuItem_nuevo_cliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_nuevo_clienteActionPerformed(e);

            }
        });

        menu.jMenuItem_gestionar_cliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_gestionar_clienteActionPerformed(e);
            }
        });

        menu.jMenuItem_nueva_venta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_nueva_ventaActionPerformed(e);
            }
        });

        menu.jMenuItem_gestionar_venta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_gestionar_ventaActionPerformed(e);
            }
        });

        menu.jMenuItem_reportes_clientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_reportes_clientesActionPerformed(e);
            }
        });

        menu.jMenuItem_reportes_categorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_reportes_categoriasActionPerformed(e);
            }
        });

        menu.jMenuItem_reportes_productos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_reportes_productosActionPerformed(e);
            }
        });

        menu.jMenuItem_reportes_ventas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jjMenuItem_reportes_ventasActionPerformed(e);
            }
        });

        menu.jMenuItem_ver_historial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_ver_historialActionPerformed(e);
            }
        });

        menu.jMenuItem_InformacionEmpresa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_InformacionEmpresaActionPerformed(e);
            }
        });

        menu.jMenuItem_cerrar_sesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jMenuItem_cerrar_sesionActionPerformed(e);
            }
        });

    }

   
    private InterGestionarUsuario interGestionarUsuario;
    private InterProducto interProducto;
    private InterGestionarProducto interGestionarProducto;
    private InterProovedor interProovedor;
    private InterGestionarProveedor interGestionarProveedor;
    private InterGestionarCategoria interGestionarCategoriaProducto;
    private InterCliente interCliente;
    private InterFacturacion interFacturacion;
    private InterConfiguracion interConfiguracion;
    private InterHistorialVentas interHistorialVentas;

  

    private void jMenuItem_gestionar_usuariosActionPerformed(ActionEvent evt) {

        if (interGestionarUsuario == null || interGestionarUsuario.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interGestionarUsuario = new InterGestionarUsuario();
            Frm_Ctrl_GestionarUsuario ctrl_GestionarUsuario = new Frm_Ctrl_GestionarUsuario(interGestionarUsuario);

        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interGestionarUsuario.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }

            interGestionarUsuario.toFront();
        }

    }

    private void jMenuItem_nuevo_productoActionPerformed(ActionEvent evt) {

        if (interProducto == null || interProducto.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interProducto = new InterProducto();
            Ctrl_Producto ctrl_Producto = new Ctrl_Producto(interProducto);

        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interProducto.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }

            interProducto.toFront();
        }

    }

    private void jMenuItem_gestionar_productoActionPerformed(ActionEvent evt) {

        if (interGestionarProducto == null || interGestionarProducto.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interGestionarProducto = new InterGestionarProducto();
            Frm_Ctrl_GestionarProducto ctrl_GestionarProducto = new Frm_Ctrl_GestionarProducto(interGestionarProducto);

        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interGestionarProducto.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }

            interGestionarProducto.toFront();
        }
    }

    //este sera eliminado
    private void jMenuItem_actualizar_stockActionPerformed(java.awt.event.ActionEvent evt) {

        InterActualizarStock actualizarStock = new InterActualizarStock();
        Frm_Ctrl_Stock stock = new Frm_Ctrl_Stock(actualizarStock);

    }
    

    ///////////////////////////////INVENTARIO////////////////////////////////////////
    
     private void jMenuItem_EntradasrActionPerformed(java.awt.event.ActionEvent evt) {

        InterEntradas interEntradas = new InterEntradas();
        interEntradas.setTitle("Registro de Entradas");
        interEntradas.setSize(new Dimension(983, 708));
        interEntradas.setLocation(400,50);
        interEntradas.setVisible(true);
        FromMenu.desktopPane.add(interEntradas);
        

    }
    
    
    
    
    

    private void jMenuItem_gestionar_proveedoresActionPerformed(ActionEvent evt) {

        if (interGestionarProveedor == null || interGestionarProveedor.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interGestionarProveedor = new InterGestionarProveedor();
            Frm_Ctrl_GestionarProveedores frm_Ctrl_GestionarProveedores = new Frm_Ctrl_GestionarProveedores(interGestionarProveedor);

        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interGestionarProveedor.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }

            interGestionarProveedor.toFront();
        }

    }

    

    private void jMenuItem_gestionar_categoriaActionPerformed(ActionEvent evt) {

        if (interGestionarCategoriaProducto == null || interGestionarCategoriaProducto.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interGestionarCategoriaProducto = new InterGestionarCategoria();
            Frm_Ctrl_GestionarCategoria frm_Ctrl_GestionarCategoriaMenu = new Frm_Ctrl_GestionarCategoria(interGestionarCategoriaProducto);

        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interGestionarCategoriaProducto.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }

            interGestionarCategoriaProducto.toFront();
        }

    }

    private void jMenuItem_nuevo_clienteActionPerformed(ActionEvent evt) {

        if (interCliente == null || interCliente.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interCliente = new InterCliente();
            Ctrl_Cliente ctrl_Cliente = new Ctrl_Cliente(interCliente);
        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interCliente.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }

            interCliente.toFront();
        }

    }

    private void jMenuItem_gestionar_clienteActionPerformed(ActionEvent evt) {

        InterGestionarCliente interGestionarCliente = new InterGestionarCliente();
        Frm_Ctrl_GestionarCliente frm_Ctrl_GestionarCliente = new Frm_Ctrl_GestionarCliente(interGestionarCliente);

    }

    private void jMenuItem_nueva_ventaActionPerformed(ActionEvent evt) {

        if (interFacturacion == null || interFacturacion.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interFacturacion = new InterFacturacion();

            Frm_Ctrl_RegistrarVenta ctrl_RegistrarVenta = new Frm_Ctrl_RegistrarVenta(interFacturacion);

        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interFacturacion.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            interFacturacion.toFront();
        }

    }

    private void jMenuItem_gestionar_ventaActionPerformed(ActionEvent evt) {

        InterGestionarVentas interGestionarVentas = new InterGestionarVentas();
        Frm_Ctrl_GestionarVentas ctrl_GestionarVentas = new Frm_Ctrl_GestionarVentas(interGestionarVentas);

    }

    private void jMenuItem_reportes_clientesActionPerformed(ActionEvent evt) {

        Reportes reporte = new Reportes();
        reporte.ReportesClientes();

    }

    private void jMenuItem_reportes_categoriasActionPerformed(ActionEvent evt) {

        Reportes reporte = new Reportes();
        reporte.ReportesCategorias();

    }

    private void jMenuItem_reportes_productosActionPerformed(java.awt.event.ActionEvent evt) {

        Reportes reporte = new Reportes();
        reporte.ReportesProductos();

    }

    private void jjMenuItem_reportes_ventasActionPerformed(java.awt.event.ActionEvent evt) {

        Reportes reporte = new Reportes();
        reporte.ReportesVentas();

    }
 
    private void jMenuItem_ver_historialActionPerformed(java.awt.event.ActionEvent evt) {

        if (interHistorialVentas == null || interHistorialVentas.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interHistorialVentas = new InterHistorialVentas();
            Frm_Ctrl_HistorialVentas frm_Ctrl_HistorialVentas = new Frm_Ctrl_HistorialVentas(interHistorialVentas);
            
        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interHistorialVentas.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            
            interHistorialVentas.toFront();
        }

    }

    private void jMenuItem_InformacionEmpresaActionPerformed(ActionEvent evt) {

        if (interConfiguracion == null || interConfiguracion.isClosed()) {
            // No existe una instancia previa o está cerrada, crea una nueva
            interConfiguracion = new InterConfiguracion();
            Frm_Ctrl_Configuracion frm_Ctrl_HistorialVentas = new Frm_Ctrl_Configuracion(interConfiguracion);
            
        } else {
            // Ya existe una instancia, colócala al frente
            try {
                interConfiguracion.setSelected(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            
            interConfiguracion.toFront();
        }

    }

    private void jMenuItem_cerrar_sesionActionPerformed(ActionEvent evt) {

        int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea cerrar sesión?", "Confirmar cierre de sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            FromLogin fl = new FromLogin();
            Frm_Ctrl_Login cl = new Frm_Ctrl_Login(fl);
            fl.setVisible(true);
            fl.setLocationRelativeTo(null);
            menu.dispose();

        }

    }

    
    
    
     
}

