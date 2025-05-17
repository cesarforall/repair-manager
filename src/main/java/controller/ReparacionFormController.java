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
import service.DispositivoService;
import service.EstadoService;

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
	DispositivoService dispositivoService;
	EstadoService estadoService;
	
	@FXML
	public void initialize() {
		fechaEntradaDatePicker.setValue(java.time.LocalDate.now());
		
		Platform.runLater(this::updateDispositivoComboBox);		
		Platform.runLater(this::updateClienteComboBox);
		Platform.runLater(this::updateEstadoComboBox);
	}
	
	@FXML
	public void addReparacion() {
		return;
	}
	
	@FXML	
	private void updateDispositivoComboBox() {
		new Thread(() -> {
			dispositivoService = new DispositivoService();
			List<Dispositivo> dispositivos = dispositivoService.findAll();
			dispositivoComboBox.setItems(FXCollections.observableArrayList(dispositivos));
			
			dispositivoComboBox.setConverter(new StringConverter<Dispositivo>() {
				@Override
				public String toString(Dispositivo dispositivo) {
					return dispositivo != null ? dispositivo.toString() : "";
				}

				@Override
				public Dispositivo fromString(String string) {
					return null;
				}
			});
		}).start();		
	};
	
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
	
	@FXML	
	private void updateEstadoComboBox() {
		new Thread(() -> {
			estadoService = new EstadoService();
			List<Estado> estados = estadoService.findAll();
			estadoComboBox.setItems(FXCollections.observableArrayList(estados));
			
			estadoComboBox.setConverter(new StringConverter<Estado>() {
				@Override
				public String toString(Estado estado) {
					return estado != null ? estado.toString() : "";
				}

				@Override
				public Estado fromString(String string) {
					return null;
				}
			});
		}).start();		
	};
}
