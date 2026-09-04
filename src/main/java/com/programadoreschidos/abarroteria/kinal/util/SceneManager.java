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
import main.java.com.programadoreschidos.abarroteria.kinal.controller.UsuarioController;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.AuthRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.ProductoRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.repository.UsuarioRepository;
import main.java.com.programadoreschidos.abarroteria.kinal.service.AuthService;
import main.java.com.programadoreschidos.abarroteria.kinal.service.DashboadService;
import main.java.com.programadoreschidos.abarroteria.kinal.service.UsuarioService;

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

        // Reseteamos las restricciones de tamaño para que la vista de login
        // conserve su tamaño original y no herede las del dashboard.
        stage.setMinWidth(0);
        stage.setMinHeight(0);

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

            UsuarioRepository usuarioRepository = new UsuarioRepository();

            UsuarioService usuarioService = new UsuarioService(usuarioRepository);

            return new DashboardController(dashboardService, this);

        }

        if(clazz == UsuarioController.class){

            UsuarioRepository usuarioRepository = new UsuarioRepository();

            UsuarioService usuarioService = new UsuarioService(usuarioRepository);

            return new UsuarioController(usuarioService);

        }

        try{

            return clazz.getDeclaredConstructor().newInstance();

        }catch(Exception e){

            throw new RuntimeException("Error al crear el constructor (Dashboard)");

        }

        });

        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);

        stage.setScene(scene);

        // Solo el dashboard necesita un tamaño mínimo para verse bien responsivo.
        stage.setMinWidth(700);

        stage.setMinHeight(450);

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