package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Usuario;
import main.java.com.programadoreschidos.abarroteria.kinal.security.jbcrypt.BCrypt;
import main.java.com.programadoreschidos.abarroteria.kinal.service.UsuarioService;

public class UsuarioController implements Initializable {

    private UsuarioService usuarioService;

    @FXML private TableView<Usuario> tableUsuario;
    @FXML private TableColumn<Usuario, String> tableColumnIdUsuario;
    @FXML private TableColumn<Usuario, String> tableColumnNombreUsuario;
    @FXML private TableColumn<Usuario, String> tableColumnApellidoUsuario;
    @FXML private TableColumn<Usuario, String> tableColumnEmailUsuario;
    @FXML private TableColumn<Usuario, Integer> tableColumnRolUsuario;

    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleLoadDataTableView();
    }

    @FXML
    private void handleLoadDataTableView() {
        tableColumnIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuarios"));
        tableColumnNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tableColumnApellidoUsuario.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        tableColumnEmailUsuario.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnRolUsuario.setCellValueFactory(new PropertyValueFactory<>("id_roles"));
        tableUsuario.setItems(usuarioService.findUsuarios());
        tableUsuario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
    }

    @FXML
    private void handleButtonNuevo() {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Usuario");
        dialog.setHeaderText("Ingrese los datos del nuevo usuario");

        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Ya no necesitamos pedir el ID manualmente porque se generará y hasheará automáticamente
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");
        TextField txtApellido = new TextField();
        txtApellido.setPromptText("Apellido");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");
        TextField txtRol = new TextField();
        txtRol.setPromptText("Rol (Ej: 1 o 2)");

        grid.add(new javafx.scene.control.Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new javafx.scene.control.Label("Apellido:"), 0, 1);
        grid.add(txtApellido, 1, 1);
        grid.add(new javafx.scene.control.Label("Email:"), 0, 2);
        grid.add(txtEmail, 1, 2);
        grid.add(new javafx.scene.control.Label("Contraseña:"), 0, 3);
        grid.add(txtContrasena, 1, 3);
        grid.add(new javafx.scene.control.Label("Rol (ID):"), 0, 4);
        grid.add(txtRol, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                try {
                    // Generar un ID único y hashearlo (usando UUID combinado con BCrypt o hash)
                    String rawId = java.util.UUID.randomUUID().toString();
                    String hashedId = BCrypt.hashpw(rawId, BCrypt.gensalt()).substring(0, 20); // Un ID seguro y único

                    // Hashear la contraseña usando BCrypt
                    String passwordHash = BCrypt.hashpw(txtContrasena.getText(), BCrypt.gensalt());
                    int rolId = Integer.parseInt(txtRol.getText().trim());

                    return new Usuario(
                        hashedId, // ID ya hasheado y único
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtEmail.getText(),
                        passwordHash,
                        rolId
                    );
                } catch (Exception e) {
                    showAlert("Error", "Verifique que el rol sea un número válido y los campos no estén vacíos.");
                }
            }
            return null;
        });

        Optional<Usuario> result = dialog.showAndWait();
        result.ifPresent(usuario -> {
            try {
                usuarioService.registrarUsuario(usuario);
                handleLoadDataTableView();
                showAlert("Éxito", "Usuario creado con ID hasheado y contraseña cifrada.");
            } catch (Exception e) {
                showAlert("Error", "No se pudo registrar el usuario en la base de datos.");
            }
        });
    }

    @FXML
    private void handleButtonEditar() {
        Usuario usuarioSeleccionado = tableUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            showAlert("Advertencia", "Por favor selecciona un usuario de la tabla para editar.");
            return;
        }

        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle("Editar Usuario");
        dialog.setHeaderText("Modifique los datos del usuario");

        ButtonType actualizarButtonType = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(actualizarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField(usuarioSeleccionado.getNombre());
        TextField txtApellido = new TextField(usuarioSeleccionado.getApellido());
        TextField txtEmail = new TextField(usuarioSeleccionado.getEmail());
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Dejar en blanco para no cambiar");
        TextField txtRol = new TextField(String.valueOf(usuarioSeleccionado.getId_roles()));

        grid.add(new javafx.scene.control.Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new javafx.scene.control.Label("Apellido:"), 0, 1);
        grid.add(txtApellido, 1, 1);
        grid.add(new javafx.scene.control.Label("Email:"), 0, 2);
        grid.add(txtEmail, 1, 2);
        grid.add(new javafx.scene.control.Label("Nueva Contraseña:"), 0, 3);
        grid.add(txtContrasena, 1, 3);
        grid.add(new javafx.scene.control.Label("Rol (ID):"), 0, 4);
        grid.add(txtRol, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == actualizarButtonType) {
                try {
                    String passwordHash;
                    // Si el campo de contraseña está vacío, mantenemos el hash anterior, si no, generamos uno nuevo
                    if (txtContrasena.getText() == null || txtContrasena.getText().trim().isEmpty()) {
                        passwordHash = usuarioSeleccionado.getContrasenaHash();
                    } else {
                        passwordHash = BCrypt.hashpw(txtContrasena.getText(), BCrypt.gensalt());
                    }

                    int rolId = Integer.parseInt(txtRol.getText().trim());

                    usuarioSeleccionado.setNombre(txtNombre.getText());
                    usuarioSeleccionado.setApellido(txtApellido.getText());
                    usuarioSeleccionado.setEmail(txtEmail.getText());
                    usuarioSeleccionado.setContrasenaHash(passwordHash);
                    usuarioSeleccionado.setId_roles(rolId);

                    return usuarioSeleccionado;
                } catch (Exception e) {
                    showAlert("Error", "Verifique que el rol sea un número válido.");
                }
            }
            return null;
        });

        Optional<Usuario> result = dialog.showAndWait();
        result.ifPresent(usuarioModificado -> {
            try {
                usuarioService.actualizarUsuario(usuarioModificado);
                handleLoadDataTableView();
                showAlert("Éxito", "Usuario actualizado correctamente.");
            } catch (Exception e) {
                showAlert("Error", "No se pudo actualizar el usuario.");
            }
        });
    }

    @FXML
    private void handleButtonEliminar() {
        Usuario usuarioSeleccionado = tableUsuario.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            showAlert("Advertencia", "Por favor selecciona un usuario de la tabla para eliminar.");
            return;
        }

        try {
            usuarioService.eliminarUsuario(usuarioSeleccionado.getIdUsuarios());
            handleLoadDataTableView();
            showAlert("Éxito", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            showAlert("Error", "No se pudo eliminar el usuario.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Cambiado Alerttype por AlertType
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}