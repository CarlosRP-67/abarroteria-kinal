package main.java.com.programadoreschidos.abarroteria.kinal.service;

import javafx.collections.ObservableList;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Usuario;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public ObservableList<Usuario> findUsuarios() {
        return usuarioRepository.findAll();
    }

    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.update(usuario);
    }

    public void eliminarUsuario(String idUsuarios) {
        usuarioRepository.delete(idUsuarios);
    }
}