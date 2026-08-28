/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.programadoreschidos.abarroteria.kinal.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.config.DataBaseConnection;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.FXCollections;




public class ProductoRepository {
    
 public ObservableList<Producto> findAll(){
 
        String sql = "select * from productos;";
 
        try(PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareCall(sql);){
 
            ResultSet rs = pstm.executeQuery();
 
            ObservableList<Producto> lista = FXCollections.observableArrayList();
 
            while(rs.next()){
 
                lista.add(new Producto(
 
                rs.getString("id_producto"),
 
                rs.getString("nombre_producto"),
 
                rs.getInt("stock"),
 
                rs.getBigDecimal("precio")        
 
                ));
 
            }
 
            return lista;
 
        }catch(SQLException e){
 
            throw new RuntimeException("Error en la consulta.");
 
        }
 
    }
 
}
    

