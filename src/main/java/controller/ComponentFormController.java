package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Componente;
import service.ComponenteService;

public class ComponentFormController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private Label messageLabel;
	
	ComponenteService componenteService = new ComponenteService();
	
	public void addComponent() {
		String componentName = nameTextField.getText();
		String priceText = priceTextField.getText();
		
	    if (componentName == null || componentName.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Nombre\" es obligatorio.");
	    } else if (priceText == null || priceText.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Precio\" es obligatorio.");							
		} else if (!priceText.matches("\\d+(\\.\\d+)?")) {
			messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Precio\" debe ser un número válido.");
		} else {
			Double componentPrice = Double.parseDouble(priceText);
			
			try {
				Componente component = new Componente(componentName, componentPrice);
				
				componenteService.save(component);
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Componente \"" + componentName + "\" añadido correctamente.");
				nameTextField.setText(null);
				priceTextField.setText(null);
			} catch (Exception e) {
				System.err.println("Error en ComponentsController al añadir component.");
				e.printStackTrace();
			}
		}
	}
}
