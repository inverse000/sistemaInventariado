#crear base de datos/;
create database bd_sistema_ventas_general;

use bd_sistema_ventas_general;



CREATE TABLE Configuracion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ruc VARCHAR(30),
    nombre VARCHAR(100),
    telefono VARCHAR(30),
    direccion VARCHAR(100),
    razon VARCHAR(100)
);

use bd_sistema_ventas_general;	
select * from Configuracion;




#crear tabla usuarios/;

create table tb_usuario(

idUsuario INT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR(30) NOT NULL,
apellido VARCHAR(30) NOT NULL,
identificacion VARCHAR(30) NOT NULL,
direccion VARCHAR(100),
telefono VARCHAR(30) ,
correo VARCHAR(100) ,
usuario VARCHAR(30)  NOT NULL,
password VARCHAR(30) NOT NULL,
rol VARCHAR(15) NOT NULL,
estado INT(1) NOT NULL

);

select * from tb_usuario;
use bd_sistema_ventas_general;	
show tables;


#crear tabla cliente/;

create table tb_cliente(

idCliente INT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR(30) NOT NULL,
apellido VARCHAR(30) NOT NULL,
identificacion VARCHAR(30) UNIQUE NOT NULL,
direccion VARCHAR(100),
telefono VARCHAR(30),
correo VARCHAR(100),
estado INT(1) NOT NULL

);

select * from tb_cliente;
use bd_sistema_ventas_general;	
show tables;



#crear tabla proveedor/;


CREATE TABLE tb_proveedor (

    idProveedor INT PRIMARY KEY AUTO_INCREMENT,
    razonSocial VARCHAR(100) NOT NULL,
    identificacion VARCHAR(100) UNIQUE NOT NULL,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    direccion VARCHAR(200),		
    telefono VARCHAR(20),
    correo VARCHAR(100),
    estado INT(1) NOT NULL
    
);

select * from tb_proveedor;

use bd_sistema_ventas_general;	




#crear tabla categoria/////////////////;

create table tb_categoria(

idCategoria int PRIMARY KEY AUTO_INCREMENT,
nombreCategoria VARCHAR(200) UNIQUE NOT NULL,
estado INT(1) NOT NULL

);


ALTER TABLE tb_categoria auto_increment = 0;

select * from tb_categoria;

use bd_sistema_ventas_general;	

show tables;






show tables;

#crear tabla producto////////////////////;


CREATE TABLE tb_producto (

    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    idCategoria INT NOT NULL,
    idProveedor INT NOT NULL,
    ubicacion VARCHAR(100),
    cantidad_actual INT NOT NULL,
    stock_min INT ,
    stock_max INT ,
    precioCosto DOUBLE(10,2) NOT NULL,
    precioVenta DOUBLE(10,2) NOT NULL,
    porcentajeIgv INT(2) NOT NULL,
    precio_total DOUBLE(10,2) NOT NULL,
    idImagen VARCHAR (300) ,
    estado INT(1) NOT NULL,
    FOREIGN KEY (idCategoria) REFERENCES tb_categoria(idCategoria),  
    FOREIGN KEY (idProveedor) REFERENCES tb_proveedor(idProveedor) 
);


create table Imagenes (
idImagen int not null auto_increment primary key,
imagen blob,
nombre varchar(100)
)engine=Innodb;



select * from Imagenes;

show tables;



CREATE TABLE tb_entradas (
	
    idEntradas INT AUTO_INCREMENT PRIMARY KEY,
    motivo VARCHAR(30) NOT NULL,
    documento VARCHAR(100) NOT NULL,
    idProducto INT NOT NULL,
    idCategoria INT NOT NULL,
    idProveedor INT NOT NULL,
    ubicacion VARCHAR (100),
    fecha DATE NOT NULL,
    cantidad INT NOT NULL,
    precioCosto DOUBLE NOT NULL CHECK (precioCosto>0),
	total DOUBLE NOT NULL,
    obs VARCHAR (300),
    estado INT(1),
    FOREIGN KEY (idProducto) REFERENCES tb_producto(idProducto),
    FOREIGN KEY (idCategoria) REFERENCES tb_categoria(idCategoria),
    FOREIGN KEY (idProveedor) REFERENCES tb_proveedor(idProveedor)
    
);


use bd_sistema_ventas_general;	




CREATE TABLE tb_salidas (

	idSalidas INT AUTO_INCREMENT PRIMARY KEY,
    motivo VARCHAR(30) NOT NULL,
    documento VARCHAR(100) NOT NULL,
    idProducto INT NOT NULL,
    idCategoria INT NOT NULL,
    destinatario INT NOT NULL,
    ubicacion VARCHAR (100),
    fecha DATE NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL CHECK (precio_unitario>0),
	total DOUBLE NOT NULL,
    obs VARCHAR (300),
    estado INT(1),
    FOREIGN KEY (idProducto) REFERENCES tb_producto(idProducto),
    FOREIGN KEY (idCategoria) REFERENCES tb_categoria(idCategoria)
    
);


CREATE TABLE tb_inventario (

    idInventario INT AUTO_INCREMENT PRIMARY KEY,
    idProducto INT NOT NULL,
    idCategoria INT NOT NULL,
    ubicacion VARCHAR(100) ,
    fecha_ultima_entrada DATE NOT NULL,
    fecha_ultima_salida DATE NOT NULL,
    entradas INT NOT NULL DEFAULT 0,
    salidas INT NOT NULL DEFAULT 0,
    stock_disponible INT NOT NULL DEFAULT 0,
    obs VARCHAR(300),
    estado INT(1) NOT NULL,
    FOREIGN KEY (idProducto) REFERENCES tb_producto(idProducto),
    FOREIGN KEY (idCategoria) REFERENCES tb_categoria(idCategoria)

);


use bd_sistema_ventas_general;	


DELIMITER //

-- Trigger to update inventory after an entry is inserted (tb_entradas)
CREATE TRIGGER trg_after_ingreso_insert_inventario
AFTER INSERT ON tb_entradas
FOR EACH ROW
BEGIN
  -- Check if product exists in inventory
  IF NOT EXISTS (SELECT 1 FROM tb_inventario WHERE idProducto = NEW.idProducto) THEN
    -- Insert new entry with initial stock from this transaction
    INSERT INTO tb_inventario (idProducto, idCategoria, ubicacion, fecha_ultima_entrada, fecha_ultima_salida, entradas, stock_disponible, estado)
    VALUES (NEW.idProducto, NEW.idCategoria, NEW.ubicacion, NEW.fecha, CURDATE(), NEW.cantidad, NEW.cantidad, 1);
  ELSE
    -- Update existing inventory
    UPDATE tb_inventario
    SET  
      entradas = entradas + NEW.cantidad,
      stock_disponible = stock_disponible + NEW.cantidad,
      fecha_ultima_entrada = NEW.fecha
    WHERE idProducto = NEW.idProducto;
  END IF;
END;
//


-- Trigger to update inventory after a sale is inserted (tb_salidas)
CREATE TRIGGER trg_after_salida_insert_inventario
AFTER INSERT ON tb_salidas
FOR EACH ROW
BEGIN
  UPDATE tb_inventario
  SET  
    salidas = salidas + NEW.cantidad,
    stock_disponible = stock_disponible - NEW.cantidad,
    fecha_ultima_salida = NEW.fecha
  WHERE idProducto = NEW.idProducto;

  -- Add error handling here to check if stock_disponible goes negative
END;
//

-- Trigger to update inventory after an entry is deleted (tb_entradas)
CREATE TRIGGER trg_after_ingreso_delete_inventario
AFTER DELETE ON tb_entradas
FOR EACH ROW
BEGIN
  -- Check if product exists in inventory
  IF EXISTS (SELECT 1 FROM tb_inventario WHERE idProducto = OLD.idProducto) THEN
    -- Update inventory by removing the deleted entry's quantity
    UPDATE tb_inventario
    SET  
      entradas = entradas - OLD.cantidad,
      stock_disponible = stock_disponible - OLD.cantidad
    WHERE idProducto = OLD.idProducto;
  END IF;
END;
//

-- Trigger to update inventory after a sale is deleted (tb_salidas)
CREATE TRIGGER trg_after_salida_delete_inventario
AFTER DELETE ON tb_salidas
FOR EACH ROW
BEGIN
  -- Check if product exists in inventory
  IF EXISTS (SELECT 1 FROM tb_inventario WHERE idProducto = OLD.idProducto) THEN
    -- Update inventory by adding back the deleted sale's quantity
    UPDATE tb_inventario
    SET  
      salidas = salidas - OLD.cantidad,
      stock_disponible = stock_disponible + OLD.cantidad
    WHERE idProducto = OLD.idProducto;
  END IF;
END;
//

DELIMITER ;

DELIMITER //

DELIMITER //

-- Trigger to create an entry for a new product with initial quantity
CREATE TRIGGER trg_after_producto_insert_ingreso
AFTER INSERT ON tb_producto
FOR EACH ROW
BEGIN
  -- Check if initial quantity is greater than 0
  IF NEW.cantidad_actual > 0 THEN
    -- Insert an entry into tb_entradas (including precioCosto)
    INSERT INTO tb_entradas (idProducto, idCategoria, idProveedor, ubicacion, fecha, cantidad, motivo, estado, precioCosto)
    VALUES (NEW.idProducto, NEW.idCategoria, NEW.idProveedor, NEW.ubicacion, CURDATE(), NEW.cantidad_actual, 'Cantidad inicial', 1, NEW.precioCosto);
  END IF;
END;
//

DELIMITER ;


use bd_sistema_ventas_general;


#crear tabla cabecera de venta/;

create table tb_cabecera_venta(
idCabeceraVenta INT(11) AUTO_INCREMENT PRIMARY KEY,
idCliente INT(11) NOT NULL,
valorPagar DOUBLE(10,2) NOT NULL,
fechaVenta DATE NOT NULL,
estado INT(1) NOT NULL

);

select * from tb_cabecera_venta;


show tables;

#crear tabla detalle de venta/;

create table tb_detalle_venta(
idDetalleVenta INT AUTO_INCREMENT PRIMARY KEY,
idCabeceraVenta INT NOT NULL,
idProducto INT NOT NULL,
cantidad INT NOT NULL,
precioUnitario DOUBLE(10,2) NOT NULL,
subtotal DOUBLE(10,2) NOT NULL,
descuento DOUBLE(10,2) NOT NULL,
igv DOUBLE(10,2) NOT NULL,
totalPagatb_productor DOUBLE(10,2) NOT NULL,
estado INT(1) NOT NULL
);




INSERT INTO Configuracion (ruc, nombre, telefono, direccion, razon)
VALUES ('123456789', 'Empresa XYZ', '987654321', 'Calle Principal 123', 'Razón Social ABC');


INSERT INTO tb_usuario VALUES (1, 'jeferson', 'Ramos', '12345678','Olivos',' 94949','@gmail.com', 'admin', '123', 'Admin', 1);

INSERT INTO tb_cliente (nombre, apellido, identificacion, direccion, telefono, correo, estado) VALUES
('Cliente', 'A', 'CLI123456', 'Calle del Cliente 123', '111222333', 'clientea@example.com', 1),
('Cliente', 'B', 'CLI789012', 'Avenida del Cliente 456', '444555666', 'clienteb@example.com', 1),
('Cliente', 'C', 'CLI345678', 'Boulevard del Cliente 789', '777888999', 'clientec@example.com', 1);

create table Estudiante (

id_est int,

nombre varchar(50),

direccion varchar(50),

fechanac Date

);

INSERT INTO tb_categoria (nombreCategoria, estado) VALUES
('Electrónicos', 1), 
('Ropa', 1), 
('Hogar', 1), 
('Deportes', 1), 
('Juguetes', 1);

-- Insertar datos en tb_proveedor

INSERT INTO tb_proveedor (nombre, apellido,razonSocial, identificacion, telefono, direccion, correo, estado) VALUES
('Proveedor', 'A','Razon A', 'ID123456', '123456789', 'Calle Falsa 123', 'proveedora@example.com', 1),
('Proveedor', 'B','Razon B', 'ID789012', '987654321', 'Avenida Siempre Viva 456', 'proveedorb@example.com', 1),
('Proveedor', 'C','Razon C', 'ID345678', '456123789', 'Boulevard de los Sueños Rotos 789', 'proveedorc@example.com', 1);

-- Insertar datos en tb_producto
INSERT INTO tb_producto (nombre, descripcion, idCategoria, idProveedor, ubicacion, cantidad_actual, stock_min, stock_max, precioCosto, precioVenta, porcentajeIgv, precio_total, estado)
VALUES ('Producto A', 'Descripción del producto A', 1, 2, 'A1', 100, 10, 500, 40.00, 50.00, 18, 100.00, 1);

INSERT INTO tb_producto (nombre, descripcion, idCategoria, idProveedor, ubicacion, cantidad_actual, stock_min, stock_max, precioCosto, precioVenta, porcentajeIgv, precio_total, estado)
VALUES ('Producto B', 'Descripción del producto B', 2,1, 'B2', 0, 10, 1000, 9.00, 10.00, 18, 57.50, 1);

INSERT INTO tb_producto (nombre, descripcion, idCategoria, idProveedor, ubicacion, cantidad_actual, stock_min, stock_max, precioCosto, precioVenta, porcentajeIgv, precio_total, estado)
VALUES ('Producto C', 'Descripción del producto B', 2,1,  'B2', 500, 10, 1000, 9.00, 10.00, 18, 57.50, 1);

INSERT INTO tb_producto (nombre, descripcion, idCategoria, idProveedor, ubicacion, cantidad_actual, stock_min, stock_max, precioCosto, precioVenta, porcentajeIgv, precio_total, estado)
VALUES ('Producto D', 'Descripción del producto A', 1, 2, 'A1', 100, 10, 500, 40.00, 50.00, 18, 100.00, 1);

# Insertar datos en tb_entradas

INSERT INTO tb_entradas (motivo, documento, idProducto, idCategoria, idProveedor, ubicacion, fecha, cantidad, precioCosto, total, obs, estado)
VALUES 
('Compra adicional', 'DOC001', 1, 1, 1, 'A1', CURDATE(), 100, 40.00, 2000.00, 'Primera entrada de Producto A', 1),
('Compra adicional', 'DOC002', 2, 2, 2, 'B2', CURDATE(), 100, 9.00, 270.00, 'Primera entrada de Producto B', 1);

# Insertar datos en tb_salida

INSERT INTO tb_salidas (motivo, documento, idProducto, idCategoria, destinatario, ubicacion, fecha, cantidad, precio_unitario, total, obs, estado)
VALUES ('Venta', 'Doc002', 1, 1, 1, 'Almacén B', '2024-06-23', 100, 60.00, 3000.00, 'Observación B', 1);


SELECT nombre
FROM estudiante
WHERE nombre LIKE '_E%[aeiou]';

use bd_sistema_ventas_general;

select * from tb_usuario;

select * from tb_cliente;

SELECT * FROM tb_producto;

SELECT * FROM imagenes;

SELECT * FROM tb_proveedor;

SELECT * FROM tb_categoria;

SELECT * FROM tb_entradas;

delete from tb_entradas where idEntradas =6;

SELECT * FROM tb_salidas;

SELECT * FROM tb_inventario;

select * from tb_detalle_venta;


use bd_sistema_ventas_general;


SELECT p.idProducto, p.nombre, p.cantidad_actual, p.descripcion, c.nombreCategoria, p.ubicacion, p.precioCosto, p.precioVenta, p.porcentajeIgv,p.precio_total, p.estado 
                 FROM tb_producto p JOIN tb_categoria c 
                 ON p.idCategoria = c.idCategoria;




show tables;


select * from tb_usuario;
select * from tb_producto;
