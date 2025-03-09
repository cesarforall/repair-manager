package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML
    private TabPane tabPane;

    @FXML
    public void abrirClientes() {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clientes.fxml"));
        	Parent tablaClientes = loader.load();

            Tab nuevaPestaña = new Tab("Clientes " + (tabPane.getTabs().size() + 1));
            nuevaPestaña.setContent(tablaClientes);
            
            tabPane.getTabs().add(nuevaPestaña);
            tabPane.getSelectionModel().select(nuevaPestaña);
        } catch (IOException e) {
            //e.printStackTrace();
        	System.out.println("MainController");
        }
    }
}
