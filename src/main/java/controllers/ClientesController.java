package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Cliente;
import service.ClienteService;

import java.util.List;

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
    	cargarClientes();
    }
    
    private void cargarClientes() {
    	List<Cliente> clientes = clienteService.getAll();
    	ObservableList<Cliente> observableClientes = FXCollections.observableArrayList(clientes);
    	tablaClientes.setItems(observableClientes);
    }
}