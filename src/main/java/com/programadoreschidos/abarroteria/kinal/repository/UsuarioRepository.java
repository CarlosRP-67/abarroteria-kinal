package main.java.com.programadoreschidos.abarroteria.kinal.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.config.DataBaseConnection;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Usuario;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

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

    public void save(Usuario usuario){
        String sql = "INSERT INTO usuarios (id_usuarios, nombre, apellido, email, contrasena_hash, id_roles) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            pstm.setString(1, usuario.getIdUsuarios());
            pstm.setString(2, usuario.getNombre());
            pstm.setString(3, usuario.getApellido());
            pstm.setString(4, usuario.getEmail());
            pstm.setString(5, usuario.getContrasenaHash());
            pstm.setInt(6, usuario.getId_roles());
            pstm.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("El correo '" + usuario.getEmail() + "' ya se encuentra registrado.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar el usuario.");
        }
    }
}