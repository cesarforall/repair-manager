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
import model.Dispositivo;
import service.DispositivoService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.StatusAware;
import util.StatusMessage;
import util.TableColumnBuilder;
import util.Utils;
import util.StatusMessage.Type;

public class DevicesViewController implements StatusAware {
	@FXML
	private TableView<Dispositivo> devicesTable;
	
	Label placeholderLabel;
	
	private Consumer<StatusMessage> statusMessageCallback;
	
	DispositivoService dispositivoService = new DispositivoService();
	
	@FXML
    public void initialize() {
		TableColumnBuilder.addColumn(devicesTable, "Id", 80, cellData ->
		new SimpleObjectProperty<>(Utils.formatIntToId("D", cellData.getValue().getIdDispositivo())));
		
		TableColumnBuilder.addColumn(devicesTable, "Tipo", 100, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getTipo()));
		
		TableColumnBuilder.addColumn(devicesTable, "Fabricante", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getFabricante()));
		
		TableColumnBuilder.addColumn(devicesTable, "Modelo", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getModelo()));
		
		TableColumnBuilder.addColumn(devicesTable, "Número de serie", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getNumeroSerie()));
		    	
    	placeholderLabel = new Label("Cargando Dispositivos...");
    	devicesTable.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	Platform.runLater(this::loadDevices);
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
    	GenericContextMenuBuilder.attach(devicesTable, dispositivo -> {
    		MenuItem delete = new MenuItem("Eliminar");
    		delete.setOnAction(e -> deleteDevice(dispositivo));
    		
    		return Arrays.asList(delete);
    	});
    }
    
	private void deleteDevice(Dispositivo dispositivo) {
    	if (dispositivo != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar \"" + dispositivo.getFabricante() + dispositivo.getModelo() + "\"?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				DispositivoService dispositivoService = new DispositivoService();
    				dispositivoService.delete(dispositivo);
    				
        			updateStatusMessage(new StatusMessage(Type.INFO, "Se ha eliminado \"" + dispositivo.getFabricante() + dispositivo.getModelo() + "\"."));
				} catch (ServiceException e) {
					Platform.runLater(() -> {
						updateStatusMessage(new StatusMessage(Type.ERROR, "Se ha producido un error al eliminar el dispositivo."));						
					});
					LoggerUtil.logError(e.getMessage(), e);
				}    			
    		}
    		refreshDevices();    		
    	}
    }
	
    private void loadDevices() {
    	new Thread(() -> {
    		try {
    			List<Dispositivo> dispositivos = dispositivoService.findAll();
            	ObservableList<Dispositivo> observableDevices = FXCollections.observableArrayList(dispositivos);
            	        	
            	Platform.runLater(() -> {
            		if (dispositivos == null || dispositivos.isEmpty()) {
    					devicesTable.setPlaceholder(new Label("No se encontraron dispositivos en la base de datos."));
    					devicesTable.getItems().clear();
    				} else {
    					devicesTable.setItems(observableDevices);
    				}        		
            	});
			} catch (ServiceException e) {
				Platform.runLater(() -> {
					placeholderLabel = new Label("Error al cargar los dispositivos");
		        	devicesTable.setPlaceholder(placeholderLabel);
				});
				LoggerUtil.logError(e.getMessage(), e);
			}
    		
    	}).start();    	
    }
    
    @FXML
    private void refreshDevices() {
    	List<Dispositivo> dispositivos = dispositivoService.findAll();
    	devicesTable.setItems(FXCollections.observableArrayList(dispositivos));
    	
    	if (dispositivos == null || dispositivos.isEmpty()) {
            devicesTable.setPlaceholder(new Label("No se encontraron dispositivos en la base de datos."));
        }
    }
}
