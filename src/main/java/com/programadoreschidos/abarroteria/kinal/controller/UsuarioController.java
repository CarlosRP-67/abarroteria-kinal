package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Usuario;
import main.java.com.programadoreschidos.abarroteria.kinal.service.UsuarioService;

public class UsuarioController implements Initializable {

    private UsuarioService usuarioService;

    @FXML
    private TableView<Usuario> tableUsuario;
    @FXML
    private TableColumn<Usuario, String> tableColumnIdUsuario;
    @FXML
    private TableColumn<Usuario, String> tableColumnNombreUsuario;
    @FXML
    private TableColumn<Usuario, String> tableColumnApellidoUsuario;
    @FXML
    private TableColumn<Usuario, String> tableColumnEmailUsuario;
    @FXML
    private TableColumn<Usuario, Integer> tableColumnRolUsuario;

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
    tableUsuario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // <-- nuevo
}
}