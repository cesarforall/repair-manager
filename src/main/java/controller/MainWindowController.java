package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import util.StatusMessage;

public class MainWindowController {

    @FXML
    private TabPane mainTabPane;
    @FXML
    private Menu fileMenu;
    @FXML
    private Menu viewMenu;
    
    @FXML
    private Label statusMessageLabel;
    
    @FXML
    public void initialize() {
    	createMenuItem(fileMenu, "Nuevo Cliente", this::openClientForm);
    	createMenuItem(fileMenu, "Nuevo Dispositivo", this::openDeviceForm);
    	createMenuItem(fileMenu, "Nuevo Repuesto", this::openPartsView);
    	createMenuItem(fileMenu, "Nuevo Estado", this::openStatesView);
    	createMenuItem(fileMenu, "Nueva Reparación", this::openRepairForm);
    	
    	createMenuItem(viewMenu, "Clientes", this::openClientsView);
    	createMenuItem(viewMenu, "Reparaciones", this::openRepairsView);
    	
    	statusMessageLabel.setText("Vulcano Lite ha iniciado correctamente.");
    }

    public void openClientsView() {
    	openTab("Clientes", "/views/clientesView.fxml", true);
    }
    
    public void openClientForm() {
    	openTab("Nuevo Cliente", "/views/clienteForm.fxml", false);
    }
    
    public void openDeviceForm() {
    	openTab("Nuevo Dispositivo", "/views/dispositivoForm.fxml", false);
    }        
    
    public void openPartsView() {
    	openTab("Nuevo Repuesto", "/views/repuestosView.fxml", false);
    }
    
    public void openStatesView() {
    	openTab("Nuevo Estado", "/views/estadosView.fxml", false);
    }
    
    public void openRepairForm() {
    	openTab("Nueva Reparación", "/views/reparacionesFormView.fxml", false);
    }
    
    public void openRepairsView() {
    	openTab("Reparaciones", "/views/repairsView.fxml", true);
    }
    
    private void createMenuItem(Menu menu, String text, Runnable action) {
    	MenuItem item = new MenuItem(text);
    	item.setOnAction(e -> action.run());
    	menu.getItems().add(item);
    }
    
    private void openTab(String title, String fxmlPath, boolean allowMultiple) {
    	if (!allowMultiple) {
    		for (Tab tab : mainTabPane.getTabs()) {
    			if (tab.getText().equals(title)) {
    				mainTabPane.getSelectionModel().select(tab);
    				return;
    			}
    		}
    	}
    	
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent content = loader.load();
			
			Tab newTab = new Tab(title);
			newTab.setContent(content);
			
			mainTabPane.getTabs().add(newTab);
			mainTabPane.getSelectionModel().select(newTab);
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
