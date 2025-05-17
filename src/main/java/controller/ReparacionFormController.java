package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Cliente;
import model.Dispositivo;
import model.Estado;

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
	
	@FXML
	public void addReparacion() {
		return;
	}
}
