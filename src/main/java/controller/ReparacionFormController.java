package controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
import model.Reparacion;
import service.ClienteService;
import service.DispositivoService;
import service.EstadoService;
import service.ReparacionService;
import service.ServiceException;
import util.Utils;

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
	ReparacionService reparacionService;
	
	@FXML
	public void initialize() {
		fechaEntradaDatePicker.setValue(java.time.LocalDate.now());
		
		Platform.runLater(this::loadData);	
	}
	
	@FXML
	public void addReparacion() {
		LocalDate fechaEntrada = fechaEntradaDatePicker.getValue();
		Dispositivo dispositivo = dispositivoComboBox.getValue();
		Cliente cliente = clienteComboBox.getValue();
		Estado estado = estadoComboBox.getValue();
		String detalle = detalleTextField.getText();
		
	    if (fechaEntrada == null) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Fecha de entrada\" es obligatorio.");
	    } else if (dispositivo == null) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Dispositivo\" es obligatorio.");
	    } else if (cliente == null) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Cliente\" es obligatorio.");							
		} else if (estado == null) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Estado\" es obligatorio.");							
		} else {
			
			try {
				reparacionService = new ReparacionService();
				
				Reparacion reparacion = new Reparacion(dispositivo, cliente, estado, detalle, fechaEntrada.toString(), null, null, 0.0, 0.0);
				
				reparacionService.save(reparacion);
							
				try {
					int repairId = reparacion.getIdReparacion();
					Path dirPath = Paths.get("docs", String.valueOf(repairId));
					int count = 1;						
					
					while (Files.exists(dirPath)) {
						dirPath = Paths.get("docs", String.valueOf(repairId) + " (" + String.valueOf(count++) + ")");
					}
					
					Files.createDirectories(dirPath);
					
					reparacion.setEnlaceDocumento(dirPath.toAbsolutePath().toString());
					reparacionService.update(reparacion);
					
				} catch (Exception e) {
					System.out.println("No se ha podido crear la carpeta.");
				}
				
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Reparación añadida correctamente.");
				
				fechaEntradaDatePicker.setValue(LocalDate.now());
	            dispositivoComboBox.setValue(null);
	            updateDispositivoComboBox();
	            clienteComboBox.setValue(null);
	            estadoComboBox.setValue(null);
	            detalleTextField.clear();		
			} catch (ServiceException e) {
				messageLabel.setStyle("-fx-text-fill: red;");
				messageLabel.setText("No se ha podido guardar la reparación: los datos no han sido actualizados.");
			}
		}
	}
	
	@FXML	
	private void updateDispositivoComboBox() {
		new Thread(() -> {
			dispositivoService = new DispositivoService();
			List<Dispositivo> dispositivos = dispositivoService.findAllAvailable();

			Platform.runLater(() -> {
				dispositivoComboBox.setItems(FXCollections.observableArrayList(dispositivos));
				dispositivoComboBox.setConverter(new StringConverter<Dispositivo>() {
					@Override
					public String toString(Dispositivo dispositivo) {
						return dispositivo != null ? Utils.formatIntToId("D", dispositivo.getIdDispositivo()) + " " + dispositivo.getTipo() + " " + dispositivo.getFabricante() : "";
					}

					@Override
					public Dispositivo fromString(String string) {
						return null;
					}
				});
			});
		}).start();
	}
	
	@FXML	
	private void updateClienteComboBox() {
		new Thread(() -> {
			clienteService = new ClienteService();
			List<Cliente> clientes = clienteService.findAll();

			Platform.runLater(() -> {
				clienteComboBox.setItems(FXCollections.observableArrayList(clientes));
				clienteComboBox.setConverter(new StringConverter<Cliente>() {
					@Override
					public String toString(Cliente cliente) {
						return cliente != null ? Utils.formatIntToId("CL", cliente.getIdCliente()) + " " + cliente.getNombre() : "";
					}

					@Override
					public Cliente fromString(String string) {
						return null;
					}
				});
			});
		}).start();
	}
	
	@FXML	
	private void updateEstadoComboBox() {
		new Thread(() -> {
			estadoService = new EstadoService();
			List<Estado> estados = estadoService.findAll()
					.stream()
					.filter(e -> !e.getNombre().equalsIgnoreCase("Cerrada"))
					.toList();

			Platform.runLater(() -> {
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
			});
		}).start();
	}
	
	private void loadData() {
		Platform.runLater(() -> {
			updateDispositivoComboBox();
			updateClienteComboBox();
			updateEstadoComboBox();
		});
	}
}
