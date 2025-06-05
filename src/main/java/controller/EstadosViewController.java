package controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import model.Estado;
import service.EstadoService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.StatusAware;
import util.StatusMessage;
import util.TableColumnBuilder;
import util.StatusMessage.Type;

public class EstadosViewController implements StatusAware {
	@FXML
    private TableView<Estado> estadosTable;
	
	Label placeholderLabel;
	
	private Consumer<StatusMessage> statusMessageCallback;
	
	EstadoService estadoService = new EstadoService();
	
	@FXML
    public void initialize() {
    	TableColumnBuilder.addColumn(estadosTable, "Id", 80, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getIdEstado()));
		
		TableColumnBuilder.addColumn(estadosTable, "Nombre", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getNombre()));
		
		TableColumnBuilder.addColumn(estadosTable, "Descripción", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getDescripcion()));
		    	
    	placeholderLabel = new Label("Cargando Estados...");
    	estadosTable.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	// Carga los Estados en el hilo de JavaFX después de que la interfaz esté lista
    	Platform.runLater(this::loadEstados);
    }
	
	@Override
    public void setStatusCallback(Consumer<StatusMessage> statusMessageCallback) {
    	this.statusMessageCallback = statusMessageCallback;
    }
    
    private void updateStatusMessage(StatusMessage statusMessage) {
    	if (statusMessageCallback != null) {
    		Platform.runLater(() -> {
    			statusMessageCallback.accept(new StatusMessage(statusMessage.getType(), statusMessage.getMessage()));
    		});
    	}
    }
	
    private void addContextMenu() {
    	GenericContextMenuBuilder.attach(estadosTable, estado -> {
    		MenuItem delete = new MenuItem("Eliminar");
    		delete.setOnAction(e -> deleteState(estado));
    		
    		return Arrays.asList(delete);
    	});
    }
	
	private void deleteState(Estado estado) {
    	if (estado != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar \"" + estado.getNombre() + "\"?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				EstadoService estadoService = new EstadoService();
    				estadoService.delete(estado);
    				
        			updateStatusMessage(new StatusMessage(Type.INFO, "Se ha eliminado " + estado.getNombre() + "."));
				} catch (ServiceException e) {
					Platform.runLater(() -> {
						updateStatusMessage(new StatusMessage(Type.ERROR, "Se ha producido un error al eliminar el estado."));						
					});
					LoggerUtil.logError(e.getMessage(), e);
				}    			
    		}
    		refreshEstados();    		
    	}
    }
	
    private void loadEstados() {
    	new Thread(() -> {
    		try {
    			List<Estado> estados = estadoService.findAll();
            	ObservableList<Estado> observableEstados = FXCollections.observableArrayList(estados);
            	        	
            	Platform.runLater(() -> {
            		if (estados == null || estados.isEmpty()) {
    					estadosTable.setPlaceholder(new Label("No se encontraron estados en la base de datos."));
    					estadosTable.getItems().clear();
    				} else {
    					estadosTable.setItems(observableEstados);
    				}         		
            	});
			} catch (ServiceException e) {
				Platform.runLater(() -> {
					placeholderLabel = new Label("Error al cargar los estados.");
		        	estadosTable.setPlaceholder(placeholderLabel);
	            });
			}
    		
    	}).start();    	
    }
    
    @FXML
    private void refreshEstados() {
    	List<Estado> estados = estadoService.findAll();
    	estadosTable.setItems(FXCollections.observableArrayList(estados));
    	
    	if (estados == null || estados.isEmpty()) {
            estadosTable.setPlaceholder(new Label("No se encontraron estados en la base de datos."));
        }
    }
}
