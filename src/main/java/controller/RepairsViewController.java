package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import model.Reparacion;
import service.ReparacionService;
import util.GenericContextMenuBuilder;
import util.TableColumnBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepairsViewController {
    @FXML
    private TableView<Reparacion> repairsTable;
    
    Label placeholderLabel;
    
    ObservableList<Reparacion> observableRepairs;

    private ReparacionService ReparacionService = new ReparacionService();

    @FXML
    public void initialize() {
    	TableColumnBuilder.addColumn(repairsTable, "Id", 50, cellData ->
    		new SimpleObjectProperty<>(cellData.getValue().getIdReparacion())
    	);    	
    	TableColumnBuilder.addColumn(repairsTable, "Estado", 150, cellData ->
    		new SimpleObjectProperty<>(cellData.getValue().getEstado().getNombre())
    	);
    	TableColumnBuilder.addColumn(repairsTable, "Dispositivo", 200, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getDispositivo().getNombre())
    	);
    	TableColumnBuilder.addColumn(repairsTable, "Cliente", 200, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getCliente().getNombre())
    	);
    	TableColumnBuilder.addColumn(repairsTable, "Detalle", 200, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getDetalle())
    	);
    	TableColumnBuilder.addColumn(repairsTable, "F. entrada", 100, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getFechaEntrada())
    	);
    	TableColumnBuilder.addColumn(repairsTable, "F. salida", 100, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getFechaSalida())
    	);
    	TableColumnBuilder.addColumn(repairsTable, "Enlace de documentos", 150, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getEnlaceDocumento())
    	);
    	TableColumnBuilder.addColumn(repairsTable, "Precio", 100, cellData ->
			new SimpleObjectProperty<>(cellData.getValue().getPrecio())
    	);
    	
    	placeholderLabel = new Label("Cargando Reparaciones...");
    	repairsTable.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	Platform.runLater(this::loadRepairs);
    }
    
    private void addContextMenu() {
    	GenericContextMenuBuilder.attach(repairsTable, repair -> {
    		MenuItem delete = new MenuItem("Eliminar");
    		delete.setOnAction(e -> deleteRepair(repair));
    		
    		return Arrays.asList(delete);
    	});
    }
    
    private void deleteRepair(Reparacion reparacion) {
    	if (reparacion != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar la reparación " + reparacion.getIdReparacion() + "?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			ReparacionService ReparacionService = new ReparacionService();
    			ReparacionService.delete(reparacion);
    		}
    		refreshRepairs();
    	}
    }
    
    private void loadRepairs() {
    	new Thread(() -> {
    		try {
				List<Reparacion> repairs = ReparacionService.findAll();
				observableRepairs = FXCollections.observableArrayList(repairs);
				
				Platform.runLater(() -> {
	                if (repairs == null || repairs.isEmpty()) {
	                    repairsTable.setPlaceholder(new Label("No hay reparaciones."));
	                    repairsTable.getItems().clear();
	                } else {
	                    repairsTable.setItems(observableRepairs);
	                }
	            });
			} catch (Exception e) {
				System.err.println("Error en RepairsViewController al cargar las reparaciones.");
	            e.printStackTrace();
	            
	            placeholderLabel = new Label("Error al cargar las reparaciones");
	        	repairsTable.setPlaceholder(placeholderLabel);
			}
    	}).start();
    }
    
    public void refreshRepairs() {
    	List<Reparacion> repairs = ReparacionService.findAll();
    	repairsTable.setItems(FXCollections.observableArrayList(repairs));
    }
}