package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;
import main.java.com.programadoreschidos.abarroteria.kinal.service.DashboadService;
import main.java.com.programadoreschidos.abarroteria.kinal.util.SceneManager;

public class DashboardController implements Initializable {
    private DashboadService dashboardService;
    private SceneManager sceneManager;
    
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
    
    public DashboardController(DashboadService dashboardService, SceneManager sceneManager){
        this.dashboardService = dashboardService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleLoadDataTableView();
    }   
    
    @FXML
    private void handleAgregarProducto() {
        TextInputDialog dialogId = new TextInputDialog();
        dialogId.setTitle("Nuevo Producto");
        dialogId.setHeaderText("Paso 1 de 4: Código de Producto");
        dialogId.setContentText("Ingrese el ID:");
        dialogId.getEditor().setPromptText("Ejemplo: prod-67");
        Optional<String> resultId = dialogId.showAndWait();

        if (!resultId.isPresent() || resultId.get().trim().isEmpty()) return;
        String id = resultId.get().trim();

        TextInputDialog dialogNombre = new TextInputDialog();
        dialogNombre.setTitle("Nuevo Producto");
        dialogNombre.setHeaderText("Paso 2 de 4: Nombre del Producto");
        dialogNombre.setContentText("Ingrese el Nombre:");
        dialogNombre.getEditor().setPromptText("Ejemplo: Galletas Chiky");
        Optional<String> resultNombre = dialogNombre.showAndWait();

        if (!resultNombre.isPresent() || resultNombre.get().trim().isEmpty()) return;
        String nombre = resultNombre.get().trim();

        TextInputDialog dialogStock = new TextInputDialog();
        dialogStock.setTitle("Nuevo Producto");
        dialogStock.setHeaderText("Paso 3 de 4: Cantidad en Stock");
        dialogStock.setContentText("Ingrese el Stock (número entero):");
        dialogStock.getEditor().setPromptText("Ejemplo: 50");
        Optional<String> resultStock = dialogStock.showAndWait();

        if (!resultStock.isPresent() || resultStock.get().trim().isEmpty()) return;

        TextInputDialog dialogPrecio = new TextInputDialog();
        dialogPrecio.setTitle("Nuevo Producto");
        dialogPrecio.setHeaderText("Paso 4 de 4: Precio Unitario");
        dialogPrecio.setContentText("Ingrese el Precio:");
        dialogPrecio.getEditor().setPromptText("Ejemplo: 12.50");
        Optional<String> resultPrecio = dialogPrecio.showAndWait();

        if (!resultPrecio.isPresent() || resultPrecio.get().trim().isEmpty()) return;

        try {
            int stock = Integer.parseInt(resultStock.get().trim());
            BigDecimal precio = new BigDecimal(resultPrecio.get().trim());

            Producto nuevoProducto = new Producto(id, nombre, stock, precio);

            dashboardService.guardarProducto(nuevoProducto);
            handleLoadDataTableView();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto añadido correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El Stock debe ser un entero y el Precio un número decimal válido.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "ID Duplicado", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el producto en la base de datos.");
        }
    }

    @FXML
    private void handleActualizarProducto() {
        Producto productoSeleccionado = tableProducto.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Por favor seleccione un producto de la tabla para actualizar.");
            return;
        }

        TextInputDialog dialogNombre = new TextInputDialog(productoSeleccionado.getNombreProducto());
        dialogNombre.setTitle("Actualizar Producto");
        dialogNombre.setHeaderText("Modificar Nombre (ID: " + productoSeleccionado.getIdProducto() + ")");
        dialogNombre.setContentText("Nombre:");
        Optional<String> resultNombre = dialogNombre.showAndWait();

        if (!resultNombre.isPresent() || resultNombre.get().trim().isEmpty()) return;
        String nuevoNombre = resultNombre.get().trim();

        TextInputDialog dialogStock = new TextInputDialog(String.valueOf(productoSeleccionado.getStock()));
        dialogStock.setTitle("Actualizar Producto");
        dialogStock.setHeaderText("Modificar Stock");
        dialogStock.setContentText("Stock:");
        Optional<String> resultStock = dialogStock.showAndWait();

        if (!resultStock.isPresent() || resultStock.get().trim().isEmpty()) return;

        TextInputDialog dialogPrecio = new TextInputDialog(productoSeleccionado.getPrecio().toString());
        dialogPrecio.setTitle("Actualizar Producto");
        dialogPrecio.setHeaderText("Modificar Precio");
        dialogPrecio.setContentText("Precio:");
        Optional<String> resultPrecio = dialogPrecio.showAndWait();

        if (!resultPrecio.isPresent() || resultPrecio.get().trim().isEmpty()) return;

        try {
            int nuevoStock = Integer.parseInt(resultStock.get().trim());
            BigDecimal nuevoPrecio = new BigDecimal(resultPrecio.get().trim());

            Producto productoActualizado = new Producto(
                productoSeleccionado.getIdProducto(),
                nuevoNombre,
                nuevoStock,
                nuevoPrecio
            );

            dashboardService.actualizarProducto(productoActualizado);
            handleLoadDataTableView();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto actualizado correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El Stock debe ser un entero y el Precio un número decimal válido.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el producto en la base de datos.");
        }
    }
    
    @FXML
    private void handleLoadDataTableView(){
        tableColumnIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        tableColumnNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        tableColumnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableColumnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tableProducto.setItems(dashboardService.findProducto());
    }

    @FXML
    private void handleEliminarProducto() {
        Producto productoSeleccionado = tableProducto.getSelectionModel().getSelectedItem();

        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Por favor seleccione un producto de la tabla para eliminar.");
            return;
        }

        try {
            dashboardService.eliminarProducto(productoSeleccionado.getIdProducto());
            handleLoadDataTableView();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el producto.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}