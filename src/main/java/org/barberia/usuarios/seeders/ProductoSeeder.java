package org.barberia.usuarios.seeders;

import org.barberia.usuarios.service.ProductoService;

public class ProductoSeeder {
      

    private ProductoService productoService;
    ProductoSeeder(
        ProductoService productoService
    ) {
        this.productoService = productoService;
    }
    
}
