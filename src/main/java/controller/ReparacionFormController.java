package controller;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.Cliente;
import model.Dispositivo;
import model.Estado;
import service.ClienteService;

public class ReparacionFormController {
	@FXML
	private DatePicker fechaEntradaDatePicker;
	@FXML
	private ComboBox<Dispositivo> dispositivoComboBox;
	@FXML
	private ComboBox<Cliente> clienteComboBox;
	@FXML
	private ComboBox<Estado> estadoComboBox;
	@FXML
	private TextField detalleTextField;
	@FXML
	private Label messageLabel;
	
	ClienteService clienteService;
	
	@FXML
	public void initialize() {
		fechaEntradaDatePicker.setValue(java.time.LocalDate.now());
		
		Platform.runLater(this::updateClienteComboBox);		
	}
	
	@FXML
	public void addReparacion() {
		return;
	}
	
	@FXML	
	private void updateClienteComboBox() {
		new Thread(() -> {
			clienteService = new ClienteService();
			List<Cliente> clientes = clienteService.findAll();
			clienteComboBox.setItems(FXCollections.observableArrayList(clientes));
			
			clienteComboBox.setConverter(new StringConverter<Cliente>() {
				@Override
				public String toString(Cliente cliente) {
					return cliente != null ? cliente.toString() : "";
				}

				@Override
				public Cliente fromString(String string) {
					return null;
				}
			});
		}).start();		
	};
}
