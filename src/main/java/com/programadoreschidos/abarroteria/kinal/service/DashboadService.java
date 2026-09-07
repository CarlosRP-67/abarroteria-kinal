package main.java.com.programadoreschidos.abarroteria.kinal.service;

import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.ProductoRepository;

public class DashboadService {
    
    private final ProductoRepository productoRepository;

    public DashboadService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public ObservableList<Producto> findProducto() {
        return productoRepository.findAll();
    }

    // Método que comunica el controlador con el repositorio para eliminar
    public void eliminarProducto(String idProducto) {
        productoRepository.delete(idProducto);
    }
    
    public void guardarProducto(Producto producto) {
        productoRepository.save(producto); // Revisa si en tu repositorio el método se llama save(), saveProducto() o insert()
    }
    
    public void actualizarProducto(Producto producto) {
    productoRepository.actualizar(producto);
}

}