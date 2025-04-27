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
import model.Estado;
import service.EstadoService;

public class EstadosViewController {
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField descriptionTextField;
	@FXML
	private Label messageLabel;
    @FXML
    private TableView<Estado> estadosTable;
    @FXML
    private TableColumn<Estado, String> nombreColumn;
    @FXML
    private TableColumn<Estado, String> descripcionColumn;
	
	EstadoService estadoService = new EstadoService();
	
	@FXML
    public void initialize() {
    	nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    	
    	Label placeholderLabel = new Label("Cargando Estados...");
    	placeholderLabel.setTranslateY(-150);
    	estadosTable.setPlaceholder(placeholderLabel);
    	
    	// Carga los Estados en el hilo de JavaFX después de que la interfaz esté lista
    	Platform.runLater(this::loadEstados);
    }
	
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
				
				refreshEstados();
			} catch (Exception e) {
				System.err.println("Error en EstadosController al añadir Estado.");
				e.printStackTrace();
			}
		}
	}
	
    private void loadEstados() {
    	new Thread(() -> {
    		try {
    			List<Estado> estados = estadoService.findAll();
            	ObservableList<Estado> observableEstados = FXCollections.observableArrayList(estados);
            	        	
            	Platform.runLater(() -> {
            		if (observableEstados.isEmpty()) {
    					estadosTable.setPlaceholder(null);
    				} else {
    					estadosTable.setItems(observableEstados);
    				}        		
            	});
			} catch (Exception e) {
				System.err.println("Error en EstadosController al cargar los Estados.");
	            e.printStackTrace();

	            Platform.runLater(() -> {
	                messageLabel.setStyle("-fx-text-fill: red;");
	                messageLabel.setText("Error al cargar los estados.");
	            });
			}
    		
    	}).start();    	
    }
    
    private void refreshEstados() {
    	List<Estado> estados = estadoService.findAll();
    	estadosTable.setItems(FXCollections.observableArrayList(estados));
    }
}
