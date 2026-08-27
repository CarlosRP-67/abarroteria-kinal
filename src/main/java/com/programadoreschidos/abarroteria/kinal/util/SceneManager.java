package main.java.com.programadoreschidos.abarroteria.kinal.util;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.com.programadoreschidos.abarroteria.kinal.controller.LoginController;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.AuthRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.service.AuthService;

public class SceneManager {
    
    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }
    
    public void showLoginView() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/login-view.fxml"));
        
        loader.setControllerFactory(
                
        clazz -> {
            if(clazz == LoginController.class){    
                AuthRepository authRepository = new AuthRepository();
                AuthService authService = new AuthService(authRepository);
                return new LoginController(authService, this);
            }
            
            try{
                return clazz.getDeclaredConstructor().newInstance();
            }catch(Exception e){   
                throw new RuntimeException("Error al crear el constructor: " + e.getMessage());
            }
            
        });
        
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.show();
        
       
    }
 
 // dashboard Stage
    public void showDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/dashboard-view.fxml"));
            
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 600);
            
            stage.setScene(scene);
            stage.setTitle("Abarrotería Kinal - Panel Principal");
            stage.centerOnScreen();
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlertInfo("Error", "No se pudo cargar", "Ocurrió un error al abrir el dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
   // alerta modal reutilizable
    public void showAlertInfo(String head, String tittle, String content, AlertType type){
        Alert alert = new Alert(type);
        alert.initOwner(this.stage);
        alert.setHeaderText(head);
        alert.setTitle(tittle);
        alert.setContentText(content);
        alert.showAndWait();
    }
   
}