package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Repuesto;
import service.RepuestoService;

public class PartFormController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private Label messageLabel;
	
	RepuestoService repuestoService = new RepuestoService();
	
	public void addSparePart() {
		String sparePartName = nameTextField.getText();
		String priceText = priceTextField.getText();
		
	    if (sparePartName == null || sparePartName.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Nombre\" es obligatorio.");
	    } else if (priceText == null || priceText.trim().isEmpty()) {
	        messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Precio\" es obligatorio.");							
		} else if (!priceText.matches("\\d+(\\.\\d+)?")) {
			messageLabel.setStyle("-fx-text-fill: red;");
	        messageLabel.setText("El campo \"Precio\" debe ser un número válido.");
		} else {
			Double sparePartPrice = Double.parseDouble(priceText);
			
			try {
				Repuesto repuesto = new Repuesto(sparePartName, sparePartPrice);
				
				repuestoService.save(repuesto);
				messageLabel.setStyle("-fx-text-fill: green;");
				messageLabel.setText("Repuesto \"" + sparePartName + "\" añadido correctamente.");
				nameTextField.setText(null);
				priceTextField.setText(null);
			} catch (Exception e) {
				System.err.println("Error en RepuestosController al añadir repuesto.");
				e.printStackTrace();
			}
		}
	}
}
