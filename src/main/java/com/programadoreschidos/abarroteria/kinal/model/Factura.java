/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.programadoreschidos.abarroteria.kinal.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author informatica
 */
public class Factura {
    private String idFactura;
    private String idCliente;
    private BigDecimal monto;
    private LocalDateTime fecha;

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

    public Factura(String idFactura, String idCliente, BigDecimal monto, LocalDateTime fecha) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }

}