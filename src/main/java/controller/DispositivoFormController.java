package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Dispositivo;
import service.DispositivoService;
import util.LoggerUtil;

public class DispositivoFormController {
	@FXML
	private TextField typeTextField;
	@FXML
	private TextField manufacturerTextField;
	@FXML
	private TextField modelTextField;
	@FXML
	private TextField snTextField;
	@FXML
	private Label messageLabel;
	
	DispositivoService dispositivoService = new DispositivoService();
	
	public void addDevice() {
		String type = typeTextField.getText();
		String deviceManufacturer = manufacturerTextField.getText();
		String deviceModel = modelTextField.getText();
		String deviceSN = snTextField.getText();
		
		if (type == null || type.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Tipo\" es obligatorio.");
		} else if (deviceManufacturer == null || deviceManufacturer.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Nombre\" es obligatorio.");
	    } else if (deviceModel == null || deviceModel.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Modelo\" es obligatorio.");
	    } else if (deviceSN == null || deviceSN.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Número de serie\" es obligatorio.");							
		} else {
			try {
				Dispositivo dispositivo = new Dispositivo(type, deviceManufacturer, deviceModel, deviceSN);
				
				dispositivoService.save(dispositivo);
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Dispositivo \"" + deviceManufacturer + "\" añadido correctamente.");
				manufacturerTextField.setText(null);
				modelTextField.setText(null);
				snTextField.setText(null);			
			} catch (Exception e) {
				messageLabel.setStyle("-fx-text-fill: red;");
		        messageLabel.setText("Ha ocurrido un error al añadir el dispositivo. Inténtelo de nuevo.");
				LoggerUtil.logError("Error en DispositivoFormController al añadir dispositivo.", e);
			}
		}
	}
}
