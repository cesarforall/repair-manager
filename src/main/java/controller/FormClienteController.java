package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Cliente;
import service.ClienteService;

public class FormClienteController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField detailTextField;
	@FXML
	private TextField phoneNumberTextField;
	@FXML
	private Label messageLabel;
	
	ClienteService clienteService = new ClienteService();
	
	public void createCliente() {
		String inputName = nameTextField.getText();
		String inputDetails = detailTextField.getText();
		String phoneNumber = phoneNumberTextField.getText();
		
		System.out.println(inputName);
		
		if (inputName == null || inputName.trim().isEmpty()) {
			messageLabel.setStyle("-fx-text-fill: red;");
			messageLabel.setText(" El campo \"Nombre\" es obligatorio.");								
		} else {
			try {
				Cliente cliente = new Cliente(inputName, inputDetails);
				
				clienteService.create(cliente);
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Cliente \"" + inputName + "\" añadido correctamente.");
				nameTextField.setText(null);
				detailTextField.setText(null);
				phoneNumberTextField.setText(null);			
			} catch (Exception e) {
				System.err.println("Error al crear Cliente.");
				e.printStackTrace();
			}
		}
	}
}
