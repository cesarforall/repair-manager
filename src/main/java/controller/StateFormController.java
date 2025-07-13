package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Estado;
import service.EstadoService;

public class StateFormController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField descriptionTextField;
	@FXML
	private Label messageLabel;
	
	EstadoService estadoService = new EstadoService();
	
	public void addEstado() {
		String estadoName = nameTextField.getText();
		String descriptionText = descriptionTextField.getText();
		
	    if (estadoName == null || estadoName.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Nombre\" es obligatorio.");
	    } else if (descriptionText == null || descriptionText.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Descripción\" es obligatorio."); 
	    } else {						
			try {
				Estado estado = new Estado(estadoName, descriptionText);
				
				estadoService.save(estado);
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Estado \"" + estadoName + "\" añadido correctamente.");
				nameTextField.setText(null);
				descriptionTextField.setText(null);
			} catch (Exception e) {
				messageLabel.setStyle("-fx-text-fill: red;");
		        messageLabel.setText("Este estado ya existe. Añada uno distinto.");
			}
		}
	}
}
