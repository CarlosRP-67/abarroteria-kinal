package main.java.com.programadoreschidos.abarroteria.kinal;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.com.programadoreschidos.abarroteria.kinal.util.SceneManager;

public class Main extends Application{
    
    private Stage stage;
    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;

        // Icono de la ventana / barra de tareas
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/main/resources/images/kinal-logo.png")));

        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.showLoginView();
        stage.show();
    }    
    public static void main(String[] args) {
        
        launch();
        
    }
}