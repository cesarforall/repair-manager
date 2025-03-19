package controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML
    private TabPane mainTabPanel;

    @FXML
    public void abrirClientes() {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clientes.fxml"));
        	Parent tablaClientes = loader.load();

            Tab nuevaPestaña = new Tab("Clientes");
            nuevaPestaña.setContent(tablaClientes);
            
            mainTabPanel.getTabs().add(nuevaPestaña);
            mainTabPanel.getSelectionModel().select(nuevaPestaña);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abrirFormCliente() {
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/formCliente.fxml"));
			Parent formCliente = loader.load();
			
			Tab formClienteTab = new Tab("Añadir Cliente");
			formClienteTab.setContent(formCliente);
			
			mainTabPanel.getTabs().add(formClienteTab);
			mainTabPanel.getSelectionModel().select(formClienteTab);
		} catch (IOException e) {
			System.err.println("Error al abrir el formulario de Cliente.");
			e.printStackTrace();
		}
    }
}
