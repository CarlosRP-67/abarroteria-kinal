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

    public void save(Usuario usuario) {
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
            throw new RuntimeException("Error al registrar el usuario.");
        }
    }

    public void update(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, email = ?, contrasena_hash = ?, id_roles = ? WHERE id_usuarios = ?;";
        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            pstm.setString(1, usuario.getNombre());
            pstm.setString(2, usuario.getApellido());
            pstm.setString(3, usuario.getEmail());
            pstm.setString(4, usuario.getContrasenaHash());
            pstm.setInt(5, usuario.getId_roles());
            pstm.setString(6, usuario.getIdUsuarios());
            pstm.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("El correo '" + usuario.getEmail() + "' ya pertenece a otro usuario.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el usuario.");
        }
    }

    public void delete(String idUsuarios) {
        String sql = "DELETE FROM usuarios WHERE id_usuarios = ?;";
        try (PreparedStatement pstm = DataBaseConnection.getDataBaseConnection().prepareStatement(sql)) {
            pstm.setString(1, idUsuarios);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el usuario.");
        }
    }
}