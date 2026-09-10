package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Factura;
import main.java.com.programadoreschidos.abarroteria.kinal.model.ItemCarrito;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Producto;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.FacturaRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.service.DashboadService;
import main.java.com.programadoreschidos.abarroteria.kinal.service.FacturaService;
import main.java.com.programadoreschidos.abarroteria.kinal.util.FacturaPdfGenerator;

public class FacturaController implements Initializable {

    private final FacturaService facturaService;
    private final DashboadService dashboardService;
    private final FacturaRepository facturaRepository = new FacturaRepository();

    private final ObservableList<ItemCarrito> carrito = FXCollections.observableArrayList();

    // ---- Catálogo de productos ----
    @FXML
    private TableView<Producto> tableCatalogo;
    @FXML
    private TableColumn<Producto, String> columnCatalogoId;
    @FXML
    private TableColumn<Producto, String> columnCatalogoNombre;
    @FXML
    private TableColumn<Producto, Integer> columnCatalogoStock;
    @FXML
    private TableColumn<Producto, BigDecimal> columnCatalogoPrecio;

    // ---- Carrito ----
    @FXML
    private TableView<ItemCarrito> tableCarrito;
    @FXML
    private TableColumn<ItemCarrito, String> columnCarritoProducto;
    @FXML
    private TableColumn<ItemCarrito, Integer> columnCarritoCantidad;
    @FXML
    private TableColumn<ItemCarrito, BigDecimal> columnCarritoSubtotal;

    // ---- Formulario ----
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtIdCliente;
    @FXML
    private Label lblTotal;

    public FacturaController(FacturaService facturaService, DashboadService dashboardService) {
        this.facturaService = facturaService;
        this.dashboardService = dashboardService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleLoadCatalogo();
        handleLoadCarrito();
    }

    private void handleLoadCatalogo() {
        columnCatalogoId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        columnCatalogoNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        columnCatalogoStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnCatalogoPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tableCatalogo.setItems(dashboardService.findProducto());
        tableCatalogo.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void handleLoadCarrito() {
        columnCarritoProducto.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProducto().getNombreProducto())
        );
        columnCarritoCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnCarritoSubtotal.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSubtotal())
        );
        tableCarrito.setItems(carrito);
        tableCarrito.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    public void handleLoadCatalogoVisual() {
        handleLoadCatalogo();
    }

    @FXML
    private void handleAgregarAlCarrito() {
        Producto seleccionado = tableCatalogo.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione un producto del catálogo.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "La cantidad debe ser un número entero.");
            return;
        }

        if (cantidad <= 0) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "La cantidad debe ser mayor a 0.");
            return;
        }
        if (cantidad > seleccionado.getStock()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Stock insuficiente",
                "Solo hay " + seleccionado.getStock() + " unidades disponibles.");
            return;
        }

        for (ItemCarrito item : carrito) {
            if (item.getProducto().getIdProducto().equals(seleccionado.getIdProducto())) {
                item.setCantidad(item.getCantidad() + cantidad);
                tableCarrito.refresh();
                actualizarTotal();
                txtCantidad.clear();
                return;
            }
        }

        carrito.add(new ItemCarrito(seleccionado, cantidad));
        actualizarTotal();
        txtCantidad.clear();
    }

    @FXML
    private void handleQuitarDelCarrito() {
        ItemCarrito seleccionado = tableCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Seleccione una línea del carrito para quitar.");
            return;
        }
        carrito.remove(seleccionado);
        actualizarTotal();
    }

    @FXML
    private void handleGenerarFactura() {
        String idCliente = txtIdCliente.getText() != null ? txtIdCliente.getText().trim() : "";

        if (idCliente.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Debe ingresar el ID del cliente.");
            return;
        }

        if (carrito.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "El carrito está vacío. Agregue productos para facturar.");
            return;
        }

        if (!facturaRepository.existeCliente(idCliente)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Cliente no encontrado", 
                "El ID de cliente '" + idCliente + "' no existe en la base de datos.\nPor favor, regístrelo primero.");
            return;
        }

        try {
            // 1. Guarda la factura en la base de datos y obtiene el objeto resultante
            Factura facturaGenerada = facturaService.generarFactura(idCliente, carrito);
            
            // 2. Genera el PDF con la utilidad existente y lo abre de inmediato
            FacturaPdfGenerator.generarYAbrirPdf(facturaGenerada, carrito);
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Factura generada y PDF creado correctamente.");
            
            // 3. Limpieza de interfaz
            carrito.clear();
            txtIdCliente.clear();
            actualizarTotal();
            handleLoadCatalogo(); // Refrescar stock visual en el catálogo
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo generar la factura o el PDF: " + e.getMessage());
        }
    }

    private void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrito item : carrito) {
            total = total.add(item.getSubtotal());
        }
        lblTotal.setText("Total: Q" + total.toPlainString());
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}