package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.cell.PropertyValueFactory;
import util.StatusMessage;

public class MainWindowController {

    @FXML
    private TabPane mainTabPanel;
    @FXML
    private Label statusMessageLabel;
    
    @FXML
    public void initialize() {
    	statusMessageLabel.setText("Vulcano Lite ha iniciado correctamente.");
    }

    @FXML
    public void abrirClientes() {
    	openTab("Clientes", "/views/clientesView.fxml", true);
    }
    
    @FXML
    public void abrirFormCliente() {
    	openTab("Nuevo Cliente", "/views/clienteForm.fxml", false);
    }
    
    @FXML
    public void openDeviceForm() {
    	openTab("Nuevo Dispositivo", "/views/dispositivoForm.fxml", false);
    }        
    
    @FXML
    public void openRepuestosView() {
    	openTab("Nuevo Repuesto", "/views/repuestosView.fxml", false);
    }
    
    @FXML
    public void openEstadosView() {
    	openTab("Nuevo Estado", "/views/estadosView.fxml", false);
    }
    
    @FXML
    public void openReparacionForm() {
    	openTab("Nueva Reparación", "/views/reparacionesFormView.fxml", false);
    }
    
    private void openTab(String title, String fxmlPath, boolean allowMultiple) {
    	if (!allowMultiple) {
    		for (Tab tab : mainTabPanel.getTabs()) {
    			if (tab.getText().equals(title)) {
    				mainTabPanel.getSelectionModel().select(tab);
    			}
    		}
    	}
    	
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent content = loader.load();
			
			Tab newTab = new Tab(title);
			newTab.setContent(content);
			
			mainTabPanel.getTabs().add(newTab);
			mainTabPanel.getSelectionModel().select(newTab);
		} catch (Exception e) {
			System.err.println("Error al abrir la vista " + title);
			e.printStackTrace();
		}
    }
    
    public void setStatusMessage(StatusMessage statusMessage) {
    	Platform.runLater(() -> {
    		switch(statusMessage.getType()) {
		        case INFO:
		            statusMessageLabel.setStyle("-fx-text-fill: black;");
		            break;
		        case ERROR:
		            statusMessageLabel.setStyle("-fx-text-fill: red;");
		            break;
    		}
    	statusMessageLabel.setText(statusMessage.getMessage());
    	});
    }
}
