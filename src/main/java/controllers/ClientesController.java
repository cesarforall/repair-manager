package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import models.Cliente;
import service.ClienteService;

import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientesController {

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, Integer> columnaId;
    @FXML
    private TableColumn<Cliente, String> columnaNombre;
    @FXML
    private TableColumn<Cliente, String> columnaDetalle;
    
   

    private ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
    	columnaId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
    	columnaNombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
    	columnaDetalle.setCellValueFactory(new PropertyValueFactory<>("Detalle"));
    	
    	addContextMenu();
    	cargarClientes();
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
    		refreshTable();
    	}
    }
    
    private void cargarClientes() {
    	List<Cliente> clientes = clienteService.getAll();
    	ObservableList<Cliente> observableClientes = FXCollections.observableArrayList(clientes);
    	tablaClientes.setItems(observableClientes);
    }
    
    private void refreshTable() {
    	List<Cliente> clientes = clienteService.getAll();
    	tablaClientes.setItems(FXCollections.observableArrayList(clientes));
    }
}