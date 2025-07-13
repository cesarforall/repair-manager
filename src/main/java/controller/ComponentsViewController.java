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
import model.Componente;
import service.ComponenteService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.StatusAware;
import util.StatusMessage;
import util.TableColumnBuilder;
import util.StatusMessage.Type;

public class ComponentsViewController implements StatusAware {
	@FXML
	private TableView<Componente> componentesTable;
	
	Label placeholderLabel;
	
	private Consumer<StatusMessage> statusMessageCallback;
	
	ComponenteService componenteService = new ComponenteService();
	
	@FXML
    public void initialize() {
		TableColumnBuilder.addColumn(componentesTable, "Id", 80, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getidComponente()));
		
		TableColumnBuilder.addColumn(componentesTable, "Nombre", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getNombre()));
		
		TableColumnBuilder.addColumn(componentesTable, "Precio", 100, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getPrecio()));
		    	
    	placeholderLabel = new Label("Cargando Componentes...");
    	componentesTable.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	// Carga los componentes en el hilo de JavaFX después de que la interfaz esté lista
    	Platform.runLater(this::loadComponents);
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
    	GenericContextMenuBuilder.attach(componentesTable, componente -> {
    		MenuItem delete = new MenuItem("Eliminar");
    		delete.setOnAction(e -> deleteComponents(componente));
    		
    		return Arrays.asList(delete);
    	});
    }
    
	private void deleteComponents(Componente componente) {
    	if (componente != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar " + componente.getNombre() + "?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				ComponenteService componenteService = new ComponenteService();
    				componenteService.delete(componente);
    				
        			updateStatusMessage(new StatusMessage(Type.INFO, "Se ha eliminado " + componente.getNombre() + "."));
				} catch (ServiceException e) {
					Platform.runLater(() -> {
						updateStatusMessage(new StatusMessage(Type.ERROR, "Se ha producido un error al eliminar el componente."));						
					});
					LoggerUtil.logError(e.getMessage(), e);
				}    			
    		}
    		refreshComponents();    		
    	}
    }
	
    private void loadComponents() {
    	new Thread(() -> {
    		try {
    			List<Componente> componentes = componenteService.findAll();
            	ObservableList<Componente> observableComponentes = FXCollections.observableArrayList(componentes);
            	        	
            	Platform.runLater(() -> {
            		if (componentes == null || componentes.isEmpty()) {
    					componentesTable.setPlaceholder(new Label("No se encontraron componentes en la base de datos."));
    					componentesTable.getItems().clear();
    				} else {
    					componentesTable.setItems(observableComponentes);
    				}        		
            	});
			} catch (ServiceException e) {
				Platform.runLater(() -> {
					placeholderLabel = new Label("Error al cargar los componentes");
		        	componentesTable.setPlaceholder(placeholderLabel);
				});
				LoggerUtil.logError(e.getMessage(), e);
			}
    		
    	}).start();    	
    }
    
    @FXML
    private void refreshComponents() {
    	List<Componente> componentes = componenteService.findAll();
    	componentesTable.setItems(FXCollections.observableArrayList(componentes));
    	
    	if (componentes == null || componentes.isEmpty()) {
            componentesTable.setPlaceholder(new Label("No se encontraron componentes en la base de datos."));
        }
    }
}
