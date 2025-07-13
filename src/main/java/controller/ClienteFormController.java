package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Cliente;
import service.ClienteService;

public class ClienteFormController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField phoneNumberTextField;
	@FXML
	private TextField detailTextField;
	@FXML
	private Label messageLabel;
	
	ClienteService clienteService = new ClienteService();
	
	public void createCliente() {
		String inputName = nameTextField.getText();
		String phoneNumber = phoneNumberTextField.getText();
		String inputDetails = detailTextField.getText();
		
		System.out.println(inputName);
		
		if (inputName == null || inputName.trim().isEmpty()) {
			messageLabel.setStyle("-fx-text-fill: red;");
			messageLabel.setText(" El campo \"Nombre\" es obligatorio.");								
		}else if (!phoneNumber.matches("\\d+")) {
	    	messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("Ingrese un número válido.");
	    } else {
			try {
				Cliente cliente = new Cliente(inputName, phoneNumber, inputDetails);
				
				clienteService.save(cliente);
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Cliente \"" + inputName + "\" añadido correctamente.");
				nameTextField.setText(null);
				detailTextField.setText(null);
				phoneNumberTextField.setText(null);			
			} catch (Exception e) {
				messageLabel.setStyle("-fx-text-fill: red;");
		        messageLabel.setText("Error al crear Cliente.");
			}
		}
	}
}
