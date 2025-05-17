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
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clientesListView.fxml"));
        	Parent tablaClientes = loader.load();
        	
        	ClientesListController clientesListController = loader.getController();
        	clientesListController.setStatusCallback(this::setStatusMessage);

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
        for (Tab tab : mainTabPanel.getTabs()) {
            if (tab.getText().equals("Añadir Cliente")) {
                mainTabPanel.getSelectionModel().select(tab);
                return;
            }
        }
        
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/clienteForm.fxml"));
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
    
    @FXML
    public void openDeviceForm() {
    	for (Tab tab : mainTabPanel.getTabs()) {
            if (tab.getText().equals("Añadir Dispositivo")) {
                mainTabPanel.getSelectionModel().select(tab);
                return;
            }
    	}
    	
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dispositivoForm.fxml"));
    		Parent dispositivoForm = loader.load();
    		
    		Tab dispositivoFormTab = new Tab("Añadir Dispositivo");
    		dispositivoFormTab.setContent(dispositivoForm);
    		
    		mainTabPanel.getTabs().add(dispositivoFormTab);
    		mainTabPanel.getSelectionModel().select(dispositivoFormTab);
    	} catch (IOException e) {
    		System.err.println("Error al abrir el formulario de Dispositivo.");
			e.printStackTrace();
		}
    }        
    
    @FXML
    public void openRepuestosView() {
        for (Tab tab : mainTabPanel.getTabs()) {
            if (tab.getText().equals("Gestionar Repuestos")) {
                mainTabPanel.getSelectionModel().select(tab);
                return;
            }
        }
        
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/repuestosView.fxml"));
			Parent repuestosView = loader.load();
			
			Tab repuestosViewTab = new Tab("Gestionar Repuestos");
			repuestosViewTab.setContent(repuestosView);
			
			mainTabPanel.getTabs().add(repuestosViewTab);
			mainTabPanel.getSelectionModel().select(repuestosViewTab);
		} catch (IOException e) {
			System.err.println("Error al abrir la vista de Repuestos.");
			e.printStackTrace();
		}
    }
    
    @FXML
    public void openEstadosView() {
        for (Tab tab : mainTabPanel.getTabs()) {
            if (tab.getText().equals("Configurar Estados")) {
                mainTabPanel.getSelectionModel().select(tab);
                return;
            }
        }
        
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/estadosView.fxml"));
			Parent estadosView = loader.load();
			
			Tab estadosViewTab = new Tab("Configurar Estados");
			estadosViewTab.setContent(estadosView);
			
			mainTabPanel.getTabs().add(estadosViewTab);
			mainTabPanel.getSelectionModel().select(estadosViewTab);
		} catch (IOException e) {
			System.err.println("Error al abrir la vista de Estados.");
			e.printStackTrace();
		}
    }
    
    @FXML
    public void openReparacionForm() {
        for (Tab tab : mainTabPanel.getTabs()) {
            if (tab.getText().equals("Añadir Reparación")) {
                mainTabPanel.getSelectionModel().select(tab);
                return;
            }
        }
        
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/reparacionesFormView.fxml"));
			Parent reparacionFormView = loader.load();
			
			Tab reparacionFormTab = new Tab("Añadir Reparación");
			reparacionFormTab.setContent(reparacionFormView);
			
			mainTabPanel.getTabs().add(reparacionFormTab);
			mainTabPanel.getSelectionModel().select(reparacionFormTab);
		} catch (IOException e) {
			System.err.println("Error al abrir la vista de Reparaciones.");
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
