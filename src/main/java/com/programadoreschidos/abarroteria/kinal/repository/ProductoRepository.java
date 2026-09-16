package main.java.com.programadoreschidos.abarroteria.kinal.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.config.DataBaseConnection;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;

public class ProductoRepository {

    public ObservableList<Producto> findAll() {
        String sql = "SELECT * FROM productos;";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            ObservableList<Producto> lista = FXCollections.observableArrayList();

            while (rs.next()) {
                lista.add(new Producto(
                    rs.getString("id_productos"),
                    rs.getString("nombre_producto"),
                    rs.getInt("stock"),
                    rs.getBigDecimal("precio")
                ));
            }
            return lista;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error en la consulta.");
        }
    }

    public void save(Producto producto) {
        String sql = "INSERT INTO productos (id_productos, nombre_producto, stock, precio) VALUES (?, ?, ?, ?);";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            pstm.setString(1, producto.getIdProducto());
            pstm.setString(2, producto.getNombreProducto());
            pstm.setInt(3, producto.getStock());
            pstm.setBigDecimal(4, producto.getPrecio());

            pstm.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("El ID '" + producto.getIdProducto() + "' ya se encuentra registrado.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el producto.");
        }
    }

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

    public void actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre_producto = ?, stock = ?, precio = ? WHERE id_productos = ?;";

        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            pstm.setString(1, producto.getNombreProducto());
            pstm.setInt(2, producto.getStock());
            pstm.setBigDecimal(3, producto.getPrecio());
            pstm.setString(4, producto.getIdProducto());

            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el producto en la base de datos.");
        }
    }
}