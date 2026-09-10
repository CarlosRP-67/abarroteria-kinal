package main.java.com.programadoreschidos.abarroteria.kinal.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.config.DataBaseConnection;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Usuario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioRepository {

    public ObservableList<Usuario> findAll(){
        String sql = "SELECT * FROM usuarios;";
        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            ObservableList<Usuario> lista = FXCollections.observableArrayList();
            while(rs.next()){
                lista.add(new Usuario(
                    rs.getString("id_usuarios"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("contrasena_hash"),
                    rs.getInt("id_roles")
                ));
            }
            return lista;
        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Error en la consulta de usuarios.");
        }
    }
    
    public boolean existeCliente(String idCliente) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE id_cliente = ?";
        // Corregido: usamos DataBaseConnection y Connection con sus respectivos imports
        try (Connection conn = DataBaseConnection.getDataBaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}