package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Dispositivo;
import service.DispositivoService;

public class DispositivoFormController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField modelTextField;
	@FXML
	private TextField snTextField;
	@FXML
	private Label messageLabel;
	
	DispositivoService dispositivoService = new DispositivoService();
	
	public void addDevice() {
		String deviceName = nameTextField.getText();
		String deviceModel = modelTextField.getText();
		String deviceSN = snTextField.getText();
		
	    if (deviceName == null || deviceName.trim().isEmpty()) {
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
				Dispositivo dispositivo = new Dispositivo(deviceName, deviceModel, deviceSN);
				
				dispositivoService.save(dispositivo);
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Dispositivo \"" + deviceName + "\" añadido correctamente.");
				nameTextField.setText(null);
				modelTextField.setText(null);
				snTextField.setText(null);			
			} catch (Exception e) {
				System.err.println("Error en DispositivoFormController al añadir dispositivo.");
				e.printStackTrace();
			}
		}
	}
}
