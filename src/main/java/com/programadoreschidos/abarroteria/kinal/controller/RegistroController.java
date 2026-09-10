package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Usuario;
import main.java.com.programadoreschidos.abarroteria.kinal.security.jbcrypt.BCrypt;
import main.java.com.programadoreschidos.abarroteria.kinal.service.UsuarioService;
import main.java.com.programadoreschidos.abarroteria.kinal.util.SceneManager;

public class RegistroController {

    private final SceneManager sceneManager;
    private final UsuarioService usuarioService;

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private Button btnVerPassword;
    @FXML private ComboBox<String> cbRol;

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnCancelar;

    public RegistroController(SceneManager sceneManager, UsuarioService usuarioService) {
        this.sceneManager = sceneManager;
        this.usuarioService = usuarioService;
    }

    @FXML
    public void initialize() {
        // Cargar los roles solicitados (1: Admin, 2: Usuario)
        cbRol.getItems().addAll("Admin", "Usuario");

        // Sincronizar el texto entre el PasswordField y el TextField normal
        txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());

        // Mostrar la contraseña al mantener presionado el botón del ojito
        btnVerPassword.setOnMousePressed(event -> {
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
        });

        // Ocultar la contraseña al soltar el botón
        btnVerPassword.setOnMouseReleased(event -> {
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
        });
    }

    @FXML
    private void guardarUsuario(ActionEvent event) {
        String nombreCompleto = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String password = txtPassword.getText();
        String rol = cbRol.getValue();

        if (nombreCompleto.isEmpty() || email.isEmpty() || password == null || password.isEmpty() || rol == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor completa nombre, correo, contraseña y rol.");
            return;
        }

        // Separa "Nombre Apellido" en nombre y apellido (el formulario solo tiene un campo de nombre)
        String nombre = nombreCompleto;
        String apellido = "";
        int espacio = nombreCompleto.indexOf(' ');
        if (espacio > 0) {
            nombre = nombreCompleto.substring(0, espacio);
            apellido = nombreCompleto.substring(espacio + 1).trim();
        }

        int idRol = "Admin".equals(rol) ? 1 : 2;
        String idUsuario = "usr-" + UUID.randomUUID().toString().substring(0, 8);
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        Usuario nuevoUsuario = new Usuario(idUsuario, nombre, apellido, email, hash, idRol);

        try {
            usuarioService.guardarUsuario(nuevoUsuario);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado correctamente.");
            limpiarCampos(event);
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Correo duplicado", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el usuario en la base de datos.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        txtNombre.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtPasswordVisible.clear();
        cbRol.getSelectionModel().clearSelection();
    }

    @FXML
    private void cancelarRegistro(ActionEvent event) {
        try {
            sceneManager.showLoginView(); // Regresa al login exitosamente
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}