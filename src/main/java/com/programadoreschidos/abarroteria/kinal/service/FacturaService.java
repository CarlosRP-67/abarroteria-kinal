package main.java.com.programadoreschidos.abarroteria.kinal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.dto.response.FacturaDetalleDTOResponse;
import main.java.com.programadoreschidos.abarroteria.kinal.model.DetalleFactura;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Factura;
import main.java.com.programadoreschidos.abarroteria.kinal.model.ItemCarrito;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.FacturaRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.ProductoRepository;

public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ProductoRepository productoRepository;

    public FacturaService(FacturaRepository facturaRepository, ProductoRepository productoRepository) {
        this.facturaRepository = facturaRepository;
        this.productoRepository = productoRepository;
    }

    public ObservableList<Factura> findFacturas() {
        return facturaRepository.findAll();
    }

    public ObservableList<FacturaDetalleDTOResponse> findDetallesByFactura(String idFactura) {
        return facturaRepository.findDetallesByFactura(idFactura);
    }

    public ObservableList<FacturaDetalleDTOResponse> findHistorialUnificado() {
        return facturaRepository.findAllUnificado();
    }

    /**
     * Recibe el carrito armado en pantalla (producto + cantidad), calcula el
     * monto total, genera los IDs, guarda todo en una sola transacción y
     * descuenta el stock vendido.
     */
    public Factura generarFactura(String idCliente, List<ItemCarrito> carrito) {
        if (idCliente == null || idCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar el ID del cliente.");
        }
        if (carrito == null || carrito.isEmpty()) {
            throw new IllegalArgumentException("El carrito no puede estar vacío.");
        }

        BigDecimal montoTotal = BigDecimal.ZERO;
        for (ItemCarrito item : carrito) {
            if (item.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
            }
            if (item.getCantidad() > item.getProducto().getStock()) {
                throw new IllegalArgumentException(
                    "Stock insuficiente para '" + item.getProducto().getNombreProducto() + "'."
                );
            }
            montoTotal = montoTotal.add(item.getSubtotal());
        }

        String idFactura = "FAC-" + UUID.randomUUID().toString().substring(0, 8);
        Factura factura = new Factura(idFactura, idCliente.trim(), montoTotal, LocalDateTime.now());

        List<DetalleFactura> detalles = new ArrayList<>();
        for (ItemCarrito item : carrito) {
            String idDetalle = "DET-" + UUID.randomUUID().toString().substring(0, 8);
            detalles.add(new DetalleFactura(idDetalle, item.getProducto().getIdProducto(), idFactura, item.getCantidad()));
        }

        facturaRepository.save(factura, detalles);

        // Descontar stock ya que la venta se confirmó
        for (ItemCarrito item : carrito) {
            Producto p = item.getProducto();
            Producto actualizado = new Producto(
                p.getIdProducto(), p.getNombreProducto(), p.getStock() - item.getCantidad(), p.getPrecio()
            );
            productoRepository.actualizar(actualizado);
        }

        return factura;
    }
}