package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Cliente;
import service.ClienteService;

public class FormClienteController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField detailTextField;
	@FXML
	private TextField phoneNumberTextField;
	
	ClienteService clienteService = new ClienteService();
	
	public void createCliente() {
		String inputName = nameTextField.getText();
		String inputDetails = detailTextField.getText();
		String phoneNumber = phoneNumberTextField.getText();
		
		Cliente cliente = new Cliente(inputName, inputDetails);
		
		try {
			clienteService.create(cliente);
		} catch (Exception e) {
			System.err.println("Error al crear Cliente.");
			e.printStackTrace();
		}
	}
}
