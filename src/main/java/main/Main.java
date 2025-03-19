package main;

import java.io.IOException;

import dao.RepuestoDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Repuesto;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
        	Repuesto repuesto = new RepuestoDAO().findById(1);
        	System.out.print(repuesto);
        	
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Vulcano Lite");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
