package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;
import main.java.com.programadoreschidos.abarroteria.kinal.service.DashboadService;
import main.java.com.programadoreschidos.abarroteria.kinal.util.SceneManager;

public class DashboardController implements Initializable {

    private DashboadService dashboardService;
    private SceneManager sceneManager;

    // ---- Panel Productos ----
    @FXML
    private TableView<Producto> tableProducto;
    @FXML
    private TableColumn<Producto, String> tableColumnIdProducto;
    @FXML
    private TableColumn<Producto, String> tableColumnNombreProducto;
    @FXML
    private TableColumn<Producto, Integer> tableColumnStock;
    @FXML
    private TableColumn<Producto, BigDecimal> tableColumnPrecio;

    // ---- Navegación ----
    @FXML
    private AnchorPane panelProductos;
    @FXML
    private AnchorPane panelUsuarios; // nodo raíz inyectado desde fx:include
    @FXML
    private Button btnNavProductos;
    @FXML
    private Button btnNavUsuarios;
    @FXML
    private Button btnRegresarInicio;

    public DashboardController(DashboadService dashboardService, SceneManager sceneManager){
        this.dashboardService = dashboardService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleLoadDataTableView();
        marcarBotonActivo(btnNavProductos);
    }

    @FXML
    private void handleLoadDataTableView(){
        tableColumnIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        tableColumnNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        tableColumnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableColumnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tableProducto.setItems(dashboardService.findProducto());
        tableProducto.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    private void handleShowProductos() {
        panelProductos.setVisible(true);
        panelProductos.setManaged(true);
        panelUsuarios.setVisible(false);
        panelUsuarios.setManaged(false);
        marcarBotonActivo(btnNavProductos);
    }

    @FXML
    private void handleShowUsuarios() {
        panelUsuarios.setVisible(true);
        panelUsuarios.setManaged(true);
        panelProductos.setVisible(false);
        panelProductos.setManaged(false);
        marcarBotonActivo(btnNavUsuarios);
    }

    private void marcarBotonActivo(Button activo) {
        btnNavProductos.getStyleClass().remove("nav-button-active");
        btnNavUsuarios.getStyleClass().remove("nav-button-active");
        if (!activo.getStyleClass().contains("nav-button-active")) {
            activo.getStyleClass().add("nav-button-active");
        }
    }

    @FXML
    private void handleEliminarProducto() {
        Producto productoSeleccionado = tableProducto.getSelectionModel().getSelectedItem();
        if (productoSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor seleccione un producto de la tabla para eliminar.");
            alert.showAndWait();
            return;
        }
        try {
            dashboardService.eliminarProducto(productoSeleccionado.getIdProducto());
            handleLoadDataTableView();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Producto eliminado correctamente.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo eliminar el producto.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRegresarInicio() {
        try {
            sceneManager.showLoginView();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo regresar a la pantalla de inicio.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleActualizarProductos() {
    handleLoadDataTableView();
 }
}