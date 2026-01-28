package com.zapateria.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.zapateria.models.Producto;
import org.junit.jupiter.api.Test;

public class ProductoServiceTest {

    @Test
    public void testCreacionProductoYPrecio() {
        // Escenario: Crear un producto de la zapatería
        Producto producto = new Producto();
        producto.setNombre("Zapato Deportivo Runner");
        producto.setPrecio(55.99);

        // Verificación
        assertNotNull(producto.getNombre(), "El nombre no debe ser nulo");
        assertEquals(55.99, producto.getPrecio(), "El precio debe coincidir con el asignado");
    }
}