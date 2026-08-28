package main.java.com.programadoreschidos.abarroteria.kinal.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.config.DataBaseConnection;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductoRepository {
    
    public ObservableList<Producto> findAll(){
        String sql = "SELECT * FROM productos;";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            ObservableList<Producto> lista = FXCollections.observableArrayList();

            while(rs.next()){
                lista.add(new Producto(
                    rs.getString("id_productos"),
                    rs.getString("nombre_producto"),
                    rs.getInt("stock"),
                    rs.getBigDecimal("precio")        
                ));
            }
            return lista;

        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Error en la consulta.");
        }
    }

    // Nuevo método para eliminar en la base de datos
    public void delete(String idProducto) {
        String sql = "DELETE FROM productos WHERE id_productos = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            pstm.setString(1, idProducto);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el producto.");
        }
    }
}