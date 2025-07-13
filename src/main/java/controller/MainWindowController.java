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
import model.Reparacion;
import util.StatusAware;
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
    	createMenuItem(fileMenu, "Nuevo Componente", this::openPartForm);
    	createMenuItem(fileMenu, "Nuevo Estado", this::openStateForm);
    	createMenuItem(fileMenu, "Nueva Reparación", this::openRepairForm);
    	
    	createMenuItem(viewMenu, "Clientes", this::openClientsView);
    	createMenuItem(viewMenu, "Dispositivos", this::openDevicesView);
    	createMenuItem(viewMenu, "Componentes", this::openPartsView);
    	createMenuItem(viewMenu, "Estados", this::openStatesView);
    	createMenuItem(viewMenu, "Reparaciones", this::openRepairsView);
    	
    	statusMessageLabel.setText("Vulcano Lite ha iniciado correctamente.");
    }

    public void openClientForm() {
    	openTab("Nuevo Cliente", "/views/clienteForm.fxml", false);
    }
    public void openDeviceForm() {
    	openTab("Nuevo Dispositivo", "/views/dispositivoForm.fxml", false);
    }        
    public void openPartForm() {
    	openTab("Nuevo Componente", "/views/componentForm.fxml", false);
    }
    public void openStateForm() {
    	openTab("Nuevo Estado", "/views/stateForm.fxml", false);
    }
    public void openRepairForm() {
    	openTab("Nueva Reparación", "/views/reparacionesFormView.fxml", false);
    }
    
    public void openClientsView() {
    	openTab("Clientes", "/views/clientesView.fxml", true);
    }
    public void openDevicesView() {
    	openTab("Dispositivos", "/views/devicesView.fxml", true);
    }
    public void openPartsView() {
    	openTab("Componentes", "/views/componentsView.fxml", true);
    }
    public void openStatesView() {
    	openTab("Estados", "/views/estadosView.fxml", true);
    }
    public void openRepairsView() {
    	openTab("Reparaciones", "/views/repairsView.fxml", true);
    }
    
    private void createMenuItem(Menu menu, String text, Runnable action) {
    	MenuItem item = new MenuItem(text);
    	item.setOnAction(e -> action.run());
    	menu.getItems().add(item);
    }
    
    public void openTab(String title, String fxmlPath, boolean allowMultiple) {
        openTab(title, fxmlPath, allowMultiple, null);
    }
    
    public void openTab(String title, String fxmlPath, boolean allowMultiple, Object data) {
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
			
			Object controller = loader.getController();
			if (controller instanceof StatusAware) {
				((StatusAware) controller).setStatusCallback(this::setStatusMessage);
			}
			if (controller instanceof RepairsViewController) {
				((RepairsViewController) controller).setMainWindowController(this);
			}
			if (controller instanceof RepairViewController && data instanceof Reparacion) {
				((RepairViewController) controller).setRepair((Reparacion) data);
			}
			if (controller instanceof RepairViewController) {
			    ((RepairViewController) controller).setMainWindowController(this);
			    ((RepairViewController) controller).setCurrentTab(newTab);
			}
			
			mainTabPane.getTabs().add(newTab);
			mainTabPane.getSelectionModel().select(newTab);
		} catch (Exception e) {
			System.err.println("Error al abrir la vista " + title);
			e.printStackTrace();
		}
    }
    
    public void closeTab(Tab tab) {
        if (tab != null) {
            TabPane tabPane = tab.getTabPane();
            if (tabPane != null) {
                tabPane.getTabs().remove(tab);
            }
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
