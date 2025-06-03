package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import model.Cliente;
import service.ClienteService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.StatusAware;
import util.StatusMessage;
import util.TableColumnBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientsViewController implements StatusAware {
    @FXML
    private TableView<Cliente> clientsTable;
    
    Label placeholderLabel;
    
    ObservableList<Cliente> observableClients;
    
    private Consumer<StatusMessage> statusMessageCallback;

    private ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
    	TableColumnBuilder.addColumn(clientsTable, "Id", 100, cellData ->
    		new SimpleObjectProperty<>(cellData.getValue().getIdCliente())
    	);
    	
    	TableColumnBuilder.addColumn(clientsTable, "Nombre", 150, cellData ->
    		new SimpleObjectProperty<>(cellData.getValue().getNombre())
    	);
    	
    	TableColumnBuilder.addColumn(clientsTable, "Detalle", 200, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getDetalle())
    	);
    	
    	placeholderLabel = new Label("Cargando Clientes...");
    	clientsTable.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	Platform.runLater(this::loadClients);
    }
    
    @Override
    public void setStatusCallback(Consumer<StatusMessage> statusMessageCallback) {
    	this.statusMessageCallback = statusMessageCallback;
    }
    
    private void updateStatusMessage(StatusMessage statusMessage) {
    	if (statusMessageCallback != null) {
    		Platform.runLater(() -> {
    			statusMessageCallback.accept(new StatusMessage(statusMessage.getType(), statusMessage.getMessage()));
    		});
    	}
    }
    
    private void addContextMenu() {
    	GenericContextMenuBuilder.attach(clientsTable, cliente -> {
    		MenuItem view = new MenuItem("Ver");
    		view.setOnAction(e -> viewClient(cliente));
    		
    		MenuItem delete = new MenuItem("Eliminar");
    		delete.setOnAction(e -> deleteClient(cliente));
    		
    		return Arrays.asList(view, delete);
    	});
    }
    
    private void viewClient(Cliente cliente) {
    	if (cliente != null) {
    		System.out.println("Ver cliente " + cliente.getNombre());
    	}
    }
    
    private void deleteClient(Cliente cliente) {
    	if (cliente != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar a " + cliente.getNombre() + " ?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				ClienteService clienteService = new ClienteService();
        			StatusMessage statusMessage = clienteService.delete(cliente);
        			updateStatusMessage(statusMessage);
				} catch (Exception e) {
					throw new ControllerException(e.getMessage(), e);
				}
    			
    		}
    		refreshClients();    		
    	}
    }
    
    private void loadClients() {
    	new Thread(() -> {
    		try {
				List<Cliente> clientes = clienteService.findAll();
				observableClients = FXCollections.observableArrayList(clientes);
				
				Platform.runLater(() -> {
	                if (clientes == null || clientes.isEmpty()) {
	                    clientsTable.setPlaceholder(new Label("No se encontraron clientes en la base de datos."));
	                    clientsTable.getItems().clear();
	                } else {
	                    clientsTable.setItems(observableClients);
	                }
	            });
			} catch (ServiceException e) {
				Platform.runLater(() -> {
					placeholderLabel = new Label("Error al cargar los clientes");
		        	clientsTable.setPlaceholder(placeholderLabel);
				});
				LoggerUtil.logError(e.getMessage(), e);
			}
    	}).start();
    }
    
    public void refreshClients() {
    	List<Cliente> clientes = clienteService.findAll();
    	clientsTable.setItems(FXCollections.observableArrayList(clientes));
    	
    	if (clientes == null || clientes.isEmpty()) {
            clientsTable.setPlaceholder(new Label("No se encontraron clientes en la base de datos."));
        }
    }
}