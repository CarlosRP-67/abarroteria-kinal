package main.java.com.programadoreschidos.abarroteria.kinal.util;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.com.programadoreschidos.abarroteria.kinal.controller.DashboardController;
import main.java.com.programadoreschidos.abarroteria.kinal.controller.LoginController;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.AuthRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.ProductoRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.service.AuthService;
import main.java.com.programadoreschidos.abarroteria.kinal.service.DashboadService;

public class SceneManager {
 
    private final Stage stage;
 
    private final String FXML_PATH = "/main/resources/view/";
    public SceneManager(Stage stage) {
 
        this.stage = stage;
 
    }
 
    public void showLoginView() throws Exception{
 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + "login-view.fxml"));
 
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
 
                throw new RuntimeException("Error al crear el constructor: ");
 
            }
 
        });
 
        Parent root = loader.load();
 
        Scene scene = new Scene(root, 600, 600);
 
        stage.setScene(scene);  
 
        stage.centerOnScreen();
 
        stage.show();
 
    }
 
    public void showDashboardView()throws Exception{
 
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + "dashboard-view.fxml"));
 
        loader.setControllerFactory(
 
        clazz -> {
 
        if(clazz == DashboardController.class){
 
            ProductoRepository productoRepository = new ProductoRepository();
 
            DashboadService dashboardService = new DashboadService(productoRepository);
 
            return new DashboardController(dashboardService, this);
 
        }
 
        try{
 
            return clazz.getDeclaredConstructor().newInstance();
 
        }catch(Exception e){
 
            throw new RuntimeException("Error al crear el constructor (Dashboard)");
 
        }
 
        });
 
        Parent root = loader.load();
 
        Scene scene = new Scene(root, 600, 600);
 
        stage.setScene(scene);
 
        stage.centerOnScreen();
 
        stage.show();
 
    }
 
 
    public void showAlertInfo(String head, String title, String content, AlertType type){
 
        Alert alert = new Alert(type);
 
        alert.initOwner(this.stage);
 
        alert.setTitle(title);
 
        alert.setContentText(content);
 
        alert.showAndWait();
 
    }
 
}