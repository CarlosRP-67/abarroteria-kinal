package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.programadoreschidos.abarroteria.kinal.util.SceneManager;

public class RegistroController {

    private final SceneManager sceneManager;

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private Button btnVerPassword;
    @FXML private ComboBox<String> cbRol;

    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnCancelar;

    public RegistroController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
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
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String rol = cbRol.getValue();
        
        System.out.println("Registrando usuario: " + nombre + " con rol: " + rol);
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