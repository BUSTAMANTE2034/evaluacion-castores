CREATE TABLE productos(
idProducto INT(6) PRIMARY KEY,
nombre VARCHAR(40),
precio DECIMAL(16,2)
);

CREATE TABLE ventas(
idVenta  INT(6) PRIMARY KEY,
idProducto INT(6),
cantidad INT(6),
FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);

DROP TABLE productos;

INSERT INTO productos (idProducto, nombre, precio) VALUES
(1, 'LAPTOP', 3000.00),
(2, 'PC', 4000.00),
(3, 'MOUSE', 100.00),
(4, 'TECLADO', 150.00),
(5, 'MONITOR', 2000.00),
(6, 'MICROFONO', 350.00),
(7, 'AUDIFONOS', 450.00);


INSERT INTO ventas (idVenta, idProducto, cantidad) VALUES
(1, 5, 8),
(2, 1, 15),
(3, 6, 13),
(4, 6, 4),
(5, 2, 3),
(6, 5, 1),
(7, 4, 5),
(8, 2, 5),
(9, 6, 2),
(10, 1, 8);





SELECT DISTINCT p.* FROM productos p INNER JOIN ventas v
on p.idProducto = v.idProducto ORDER BY idProducto 




SELECT p.idProducto,p.nombre,SUM(v.cantidad) as Total_vendidos
from productos p INNER JOIN ventas v on p.idProducto = v.idProducto
GROUP BY p.idProducto, p.nombre


SELECT p.idProducto , p.nombre,p.precio,  
COALESCE(SUM(v.cantidad),0) as Total_vendidos,
COALESCE(SUM(v.cantidad * p.precio),0) as Suma_Vendida 
from productos p LEFT JOIN  ventas v on p.idProducto = v.idProducto
GROUP BY p.idProducto ,p.nombre,p.precio








