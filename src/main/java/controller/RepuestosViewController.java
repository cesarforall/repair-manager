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
import model.Repuesto;
import service.RepuestoService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.StatusAware;
import util.StatusMessage;
import util.TableColumnBuilder;
import util.StatusMessage.Type;

public class RepuestosViewController implements StatusAware {
	@FXML
	private TableView<Repuesto> repuestosTable;
	
	Label placeholderLabel;
	
	private Consumer<StatusMessage> statusMessageCallback;
	
	RepuestoService repuestoService = new RepuestoService();
	
	@FXML
    public void initialize() {
		TableColumnBuilder.addColumn(repuestosTable, "Id", 80, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getIdRepuesto()));
		
		TableColumnBuilder.addColumn(repuestosTable, "Nombre", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getNombre()));
		
		TableColumnBuilder.addColumn(repuestosTable, "Precio", 100, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getPrecio()));
		    	
    	placeholderLabel = new Label("Cargando Repuestos...");
    	repuestosTable.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	// Carga los repuestos en el hilo de JavaFX después de que la interfaz esté lista
    	Platform.runLater(this::loadSpareParts);
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
    	GenericContextMenuBuilder.attach(repuestosTable, repuesto -> {
    		MenuItem delete = new MenuItem("Eliminar");
    		delete.setOnAction(e -> deleteSpareParts(repuesto));
    		
    		return Arrays.asList(delete);
    	});
    }
    
	private void deleteSpareParts(Repuesto repuesto) {
    	if (repuesto != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar " + repuesto.getNombre() + "?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				RepuestoService repuestoService = new RepuestoService();
    				repuestoService.delete(repuesto);
    				
        			updateStatusMessage(new StatusMessage(Type.INFO, "Se ha eliminado " + repuesto.getNombre() + "."));
				} catch (ServiceException e) {
					Platform.runLater(() -> {
						updateStatusMessage(new StatusMessage(Type.ERROR, "Se ha producido un error al eliminar el repuesto."));						
					});
					LoggerUtil.logError(e.getMessage(), e);
				}    			
    		}
    		refreshSpareParts();    		
    	}
    }
	
    private void loadSpareParts() {
    	new Thread(() -> {
    		try {
    			List<Repuesto> repuestos = repuestoService.findAll();
            	ObservableList<Repuesto> observableRepuestos = FXCollections.observableArrayList(repuestos);
            	        	
            	Platform.runLater(() -> {
            		if (repuestos == null || repuestos.isEmpty()) {
    					repuestosTable.setPlaceholder(new Label("No se encontraron repuestos en la base de datos."));
    					repuestosTable.getItems().clear();
    				} else {
    					repuestosTable.setItems(observableRepuestos);
    				}        		
            	});
			} catch (ServiceException e) {
				Platform.runLater(() -> {
					placeholderLabel = new Label("Error al cargar los repuestos");
		        	repuestosTable.setPlaceholder(placeholderLabel);
				});
				LoggerUtil.logError(e.getMessage(), e);
			}
    		
    	}).start();    	
    }
    
    @FXML
    private void refreshSpareParts() {
    	List<Repuesto> repuestos = repuestoService.findAll();
    	repuestosTable.setItems(FXCollections.observableArrayList(repuestos));
    	
    	if (repuestos == null || repuestos.isEmpty()) {
            repuestosTable.setPlaceholder(new Label("No se encontraron repuestos en la base de datos."));
        }
    }
}
