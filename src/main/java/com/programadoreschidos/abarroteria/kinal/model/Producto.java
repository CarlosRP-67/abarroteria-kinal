/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.programadoreschidos.abarroteria.kinal.model;

import java.math.BigDecimal;

/**
 *
 * @author informatica
 */
public class Producto {
    private String idproducto;
    private String nombreProducto;
            private int stock;
            private BigDecimal precio;

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Producto(String idproducto, String nombreProducto, int stock, BigDecimal precio) {
        this.idproducto = idproducto;
        this.nombreProducto = nombreProducto;
        this.stock = stock;
        this.precio = precio;
    }



}
