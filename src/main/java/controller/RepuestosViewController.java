package controller;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Repuesto;
import service.RepuestoService;

public class RepuestosViewController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private Label messageLabel;
    @FXML
    private TableView<Repuesto> repuestosTable;
    @FXML
    private TableColumn<Repuesto, String> nombreColumn;
    @FXML
    private TableColumn<Repuesto, String> precioColumn;
	
	RepuestoService repuestoService = new RepuestoService();
	
	@FXML
    public void initialize() {
    	nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
    	
    	Label placeholderLabel = new Label("Cargando Repuestos...");
    	placeholderLabel.setTranslateY(-150);
    	repuestosTable.setPlaceholder(placeholderLabel);
    	
    	// Carga los repuestos en el hilo de JavaFX después de que la interfaz esté lista
    	Platform.runLater(this::loadSpareParts);
    }
	
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
				
				refreshSpareParts();
			} catch (Exception e) {
				System.err.println("Error en RepuestosController al añadir repuesto.");
				e.printStackTrace();
			}
		}
	}
	
    private void loadSpareParts() {
    	new Thread(() -> {
    		try {
    			List<Repuesto> repuestos = repuestoService.findAll();
            	ObservableList<Repuesto> observableRepuestos = FXCollections.observableArrayList(repuestos);
            	        	
            	Platform.runLater(() -> {
            		if (observableRepuestos.isEmpty()) {
    					repuestosTable.setPlaceholder(null);
    				} else {
    					repuestosTable.setItems(observableRepuestos);
    				}        		
            	});
			} catch (Exception e) {
				System.err.println("Error en RepuestosController al cargar los repuestos.");
	            e.printStackTrace();

	            Platform.runLater(() -> {
	                messageLabel.setStyle("-fx-text-fill: red;");
	                messageLabel.setText("Error al cargar los repuestos.");
	            });
			}
    		
    	}).start();    	
    }
    
    private void refreshSpareParts() {
    	List<Repuesto> repuestos = repuestoService.findAll();
    	repuestosTable.setItems(FXCollections.observableArrayList(repuestos));
    }
}
