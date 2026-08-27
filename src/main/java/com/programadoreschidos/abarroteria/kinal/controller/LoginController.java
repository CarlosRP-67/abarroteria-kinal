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
            try{
                LoginDTOResponse response = authService.login(new LoginDTORequest(txtFieldEmail.getText(), txtFieldPassword.getText()));
                
                // 1. Muestras tu alerta de bienvenida
                sceneManager.showAlertInfo("Bienvenido: " + response.getNombre(), "Es bueno verte:", "inicio de Sesion correcto", Alert.AlertType.INFORMATION);
                
                // 2. ¡AQUÍ ESTÁ LO QUE FALTA! Llamas a tu SceneManager para abrir el Dashboard
                sceneManager.showDashboardView();
                
            }catch(RuntimeException e){
                sceneManager.showAlertInfo("Error al iniciar sesion", "Verificar campos", "No se ha podido iniciar sesion", Alert.AlertType.WARNING);
            }
        }
    }

}