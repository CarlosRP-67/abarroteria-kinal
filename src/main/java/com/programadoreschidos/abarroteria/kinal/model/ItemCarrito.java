/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.programadoreschidos.abarroteria.kinal.model;

import java.math.BigDecimal;

/**
 * Representa una línea del carrito antes de generar la factura.
 * No corresponde a ninguna tabla: es solo un helper para la TableView
 * mientras el usuario arma la compra en la pantalla de Facturas.
 *
 * @author informatica
 */
public class ItemCarrito {
    private Producto producto;
    private int cantidad;

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

}