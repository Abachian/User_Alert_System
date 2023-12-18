SELECT cliente.ID, cliente.Nombre, cliente.Apellido
FROM Clientes cliente
        INNER JOIN Ventas venta ON cliente.ID = venta.Id_cliente
WHERE venta.Fecha >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
GROUP BY cliente.ID, cliente.Nombre, cliente.Apellido
HAVING SUM(venta.Importe) > 100000;
