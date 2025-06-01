package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import util.LoggerUtil;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mainWindow.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Vulcano Lite");
            primaryStage.setScene(scene);
            primaryStage.show();            
            
            primaryStage.setOnCloseRequest(event -> LoggerUtil.close());

        } catch (Exception e) {
        	System.err.println("Ocurrió un error inesperado en la aplicación. Por favor, contacte con el soporte técnico.");
            LoggerUtil.logError(e.getMessage(), e);
            LoggerUtil.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
