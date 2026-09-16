/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.programadoreschidos.abarroteria.kinal.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fila "aplanada" que resulta de unir facturas + detalles_facturas + productos.
 * Se usa para mostrar el historial/detalle de una factura en una sola TableView,
 * sin tener que hacer varias consultas desde el controller.
 *
 * @author informatica
 */
public class FacturaDetalleDTOResponse {
    private String idFactura;
    private String idCliente;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String idDetalleFactura;
    private String idProducto;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private int cantidadComprada;

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public void setIdDetalleFactura(String idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(int cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidadComprada));
    }

    public FacturaDetalleDTOResponse(String idFactura, String idCliente, BigDecimal monto, LocalDateTime fecha,
            String idDetalleFactura, String idProducto, String nombreProducto,
            BigDecimal precioUnitario, int cantidadComprada) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
        this.idDetalleFactura = idDetalleFactura;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidadComprada = cantidadComprada;
    }

}