package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import model.Cliente;
import service.ClienteService;
import util.StatusMessage;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientesViewController {
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, Integer> columnaId;
    @FXML
    private TableColumn<Cliente, String> columnaNombre;
    @FXML
    private TableColumn<Cliente, String> columnaDetalle;
    
    Label placeholderLabel;
    
    ObservableList<Cliente> observableClientes;
    
    private Consumer<StatusMessage> statusMessageCallback;

    private ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
    	columnaId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
    	columnaNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
    	columnaDetalle.setCellValueFactory(new PropertyValueFactory<>("Detalle"));
    	
    	placeholderLabel = new Label("Cargando Clientes...");
    	tablaClientes.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	Platform.runLater(this::cargarClientes);
    }
    
    public void setStatusCallback(Consumer<StatusMessage> statusMessageCallback) {
    	this.statusMessageCallback = statusMessageCallback;
    }
    
    private void updateStatusMessage(StatusMessage.Type type, String message) {
    	if (statusMessageCallback != null) {
    		Platform.runLater(() -> {
    			statusMessageCallback.accept(new StatusMessage(type, message));
    		});
    	}
    }
    
    private void addContextMenu() {
    	tablaClientes.setRowFactory(tableView -> {
    		TableRow<Cliente> row = new TableRow<Cliente>();
    		ContextMenu contextMenu = new ContextMenu();
    		
    		MenuItem seeItem = new MenuItem("Ver");
    		seeItem.setOnAction(event -> seeCliente(row.getItem()));
    		
    		MenuItem deleteItem = new MenuItem("Eliminar");
    		deleteItem.setOnAction(event -> deleteCliente(row.getItem()));
    		
    		contextMenu.getItems().addAll(seeItem, deleteItem);
    		
    		row.setOnMouseClicked(event -> {
    			if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
    				contextMenu.show(row, event.getScreenX(), event.getScreenY());
    			}
    		});
    		
    		return row;
    	});
    }
    
    private void seeCliente(Cliente cliente) {
    	if (cliente != null) {
    		System.out.println("Ver cliente " + cliente.getNombre());
    	}
    }
    
    private void deleteCliente(Cliente cliente) {
    	if (cliente != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar a " + cliente.getNombre() + " ?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			ClienteService clienteService = new ClienteService();
    			clienteService.delete(cliente);
    		}
    		updateClientes();
    	}
    }
    
    private void cargarClientes() {
    	new Thread(() -> {
    		try {
				List<Cliente> clientes = clienteService.findAll();
				observableClientes = FXCollections.observableArrayList(clientes);
				
				tablaClientes.setItems(observableClientes);
			} catch (Exception e) {
				System.err.println("Error en ClientesListController al cargar los Clientes.");
	            e.printStackTrace();
	            
	            placeholderLabel = new Label("Error al cargar los clientes");
	        	tablaClientes.setPlaceholder(placeholderLabel);
			}
    	}).start();
    }
    
    public void updateClientes() {
    	List<Cliente> clientes = clienteService.findAll();
    	tablaClientes.setItems(FXCollections.observableArrayList(clientes));
    }
}