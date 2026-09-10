package main.java.com.programadoreschidos.abarroteria.kinal.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.java.com.programadoreschidos.abarroteria.kinal.dto.request.LoginDTORequest;
import main.java.com.programadoreschidos.abarroteria.kinal.dto.response.LoginDTOResponse;
import main.java.com.programadoreschidos.abarroteria.kinal.service.AuthService;
import main.java.com.programadoreschidos.abarroteria.kinal.util.SceneManager;

public class LoginController implements Initializable {
    
    private final AuthService authService;
    private final SceneManager sceneManager;
                 
    @FXML
    private Button btnIniciarSesion;
    
    // 1. Añadimos la anotación y el botón para registrarse
    @FXML
    private Button btnRegistrarse; 
    
    @FXML
    private TextField txtFieldEmail;
    @FXML
    private TextField txtFieldPassword;
    
    public LoginController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void handleLogin(){
        if(txtFieldEmail.getText().isEmpty() || txtFieldPassword.getText().isEmpty()){
            sceneManager.showAlertInfo("Hay campos sin llenar", "No puedes dejar espacion en blanco", "Intenta de nuevo", Alert.AlertType.INFORMATION);
        } else {
            try {
                LoginDTOResponse response = authService.login(new LoginDTORequest(txtFieldEmail.getText(), txtFieldPassword.getText()));
                
                // 1. Muestras tu alerta de bienvenida
                sceneManager.showAlertInfo("Bienvenido: " + response.getNombre(), "Es bueno verte:", "inicio de Sesion correcto", Alert.AlertType.INFORMATION);
                
                // 2. Intentamos cargar la vista del Dashboard con su propio try-catch
                try {
                    sceneManager.showDashboardView();
                } catch (Exception e) {
                    sceneManager.showAlertInfo("Error de navegación", "No se pudo cargar la vista del dashboard", e.getMessage(), Alert.AlertType.ERROR);
                    e.printStackTrace(); // Útil para depurar en consola si la ruta del FXML falla
                }
                
            } catch(RuntimeException e){
                sceneManager.showAlertInfo("Error al iniciar sesion", "Verificar campos", "No se ha podido iniciar sesion", Alert.AlertType.WARNING);
            }
        }
    }

    // 2. Añadimos el método para controlar la acción del botón Registrarse
    @FXML
    public void handleIrARegistro() {
        try {
            // Llamamos al SceneManager para que maneje la transición a la vista de registro
            sceneManager.showRegistroView(); 
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error de navegación", "No se pudo cargar la vista de registro", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

}
