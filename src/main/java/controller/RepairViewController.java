package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Estado;
import model.Reparacion;
import model.Repuesto;
import model.RepuestoReparacion;
import service.ReparacionService;
import service.RepuestoReparacionService;
import service.RepuestoService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.TableColumnBuilder;

public class RepairViewController {
	@FXML
	Label repairIdLabel;
	@FXML
	ComboBox<Estado> stateComboBox;
	@FXML
	Label stateLabel;
	@FXML
	Label deviceLabel;
	@FXML
	Label clientLabel;
	@FXML
	Label inDateLabel;
	@FXML
	Label outDateLabel;
	@FXML
	TextArea commentsTextArea;
	@FXML
	ComboBox<Repuesto> partsComboBox;
	@FXML
	TextField quantityTextField;
	@FXML
	TableView<RepuestoReparacion> partsTable;
	@FXML
	Label partsMessageLabel;
	@FXML
	Label messageLabel;
	
	Label placeholderLabel;
	
	private Reparacion repair;
	
	RepuestoService repuestoService = new RepuestoService();
	RepuestoReparacionService repuestoReparacionService = new RepuestoReparacionService();
	
	@FXML
	public void initialize() {
		TableColumnBuilder.addColumn(partsTable, "Id", 80, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getRepuesto().getIdRepuesto()));
		
		TableColumnBuilder.addColumn(partsTable, "Nombre", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getRepuesto().getNombre()));
		
		TableColumnBuilder.addColumn(partsTable, "Precio", 100, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getRepuesto().getPrecio()));
		
		TableColumnBuilder.addColumn(partsTable, "Cantidad", 100, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getCantidad()));
		    	
    	placeholderLabel = new Label("Cargando Repuestos...");
    	partsTable.setPlaceholder(placeholderLabel);
		
		Platform.runLater(this::loadRepair);
		Platform.runLater(this::loadParts);
		Platform.runLater(this::loadPartsRepair);
		Platform.runLater(this::addContextMenu);
	}
	
	public void setRepair(Reparacion repair) {
		this.repair = repair;
	}
	
	public void addPart() {
	    Repuesto selectedPart = partsComboBox.getValue();
	    String quantityText = quantityTextField.getText();

	    if (selectedPart == null) {
	        partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("No se encontraron componentes para añadir.");
	    } else if(quantityText.isBlank()){
	    	partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("El campo \"Cantidad\" es obligatorio.");
	    }else if(Integer.parseInt(quantityText) <= 0){
	    	partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("La cantidad no puede ser inferior a 1.");
	    }else {
	        try {
	            int quantity = Integer.parseInt(quantityText);

	            RepuestoReparacion partRepair = new RepuestoReparacion(repair, selectedPart, quantity);
	            RepuestoReparacionService service = new RepuestoReparacionService();
	            service.save(partRepair);

	            refreshPartsTable();
	            
	            partsMessageLabel.setStyle("-fx-text-fill: green;");
	            partsMessageLabel.setText("Componente añadido correctamente.");
	            quantityTextField.clear();

	        } catch (ServiceException e) {
	            partsMessageLabel.setStyle("-fx-text-fill: red;");
	            partsMessageLabel.setText("Se ha producido un error al añadir el componente.");
	            LoggerUtil.logError(e.getMessage(), e);
	        }
	    }
	}

	private void removePart(RepuestoReparacion part) {
	    try {
	        RepuestoReparacionService service = new RepuestoReparacionService();
	        service.delete(part);
	        refreshPartsTable();
	        partsMessageLabel.setStyle("-fx-text-fill: green;");
	        partsMessageLabel.setText("Componente eliminado correctamente.");
	    } catch (ServiceException e) {
	        partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("Error al eliminar el componente.");
	        LoggerUtil.logError(e.getMessage(), e);
	    }
	}
	
	private void loadRepair() {
		Platform.runLater(() -> {
			if (repair != null) {
				repairIdLabel.setText("Reparación: " + repair.getIdReparacion());
				stateLabel.setText(repair.getEstado().getNombre());
				deviceLabel.setText(repair.getDispositivo().getNombre());
				clientLabel.setText(repair.getCliente().getNombre());
				inDateLabel.setText(formatEntryDate(repair.getFechaEntrada()));
				outDateLabel.setText(repair.getFechaSalida() != null ? repair.getFechaEntrada() : "");
				commentsTextArea.setText(repair.getDetalle());
			}
		});
	}
	
	@FXML
    private void loadParts() {
    	new Thread(() -> {
    		try {
                List<Repuesto> parts = repuestoService.findAll();
                ObservableList<Repuesto> observableParts = FXCollections.observableArrayList(parts);
                
                Platform.runLater(() -> {
                    partsComboBox.setItems(observableParts);
                    if (!observableParts.isEmpty()) {
                        partsComboBox.getSelectionModel().selectFirst();
                    }
                });
            } catch (ServiceException e) {
                Platform.runLater(() -> {
                    partsComboBox.setPromptText("Error al cargar");
                });
                LoggerUtil.logError(e.getMessage(), e);
            }
    	}).start();    	
    }
    
    private void loadPartsRepair() {
    	new Thread(() -> {
    		try {
    			List<RepuestoReparacion> repuestos = repuestoReparacionService.findAll();
            	ObservableList<RepuestoReparacion> observableRepuestos = FXCollections.observableArrayList(repuestos);
            	        	
            	Platform.runLater(() -> {
            		if (repuestos == null || repuestos.isEmpty()) {
    					partsTable.setPlaceholder(new Label("No se encontraron componentes en la reparación."));
    					partsTable.getItems().clear();
    				} else {
    					partsTable.setItems(observableRepuestos);
    				}        		
            	});
			} catch (ServiceException e) {
				Platform.runLater(() -> {
					placeholderLabel = new Label("Error al cargar los componentes");
		        	partsTable.setPlaceholder(placeholderLabel);
				});
				LoggerUtil.logError(e.getMessage(), e);
			}
    	}).start();    	
    }
    
    @FXML
    private void refreshPartsTable() {
    	List<RepuestoReparacion> repuestos = repuestoReparacionService.findAll();
    	partsTable.setItems(FXCollections.observableArrayList(repuestos));
    	
    	if (repuestos == null || repuestos.isEmpty()) {
            partsTable.setPlaceholder(new Label("No se encontraron componentes en la repación"));
        }
    }
    
    @FXML
    private void updateComment() {
    	try {
    		Reparacion newRepair = new Reparacion(repair);
    		newRepair.setDetalle(commentsTextArea.getText());
    		ReparacionService repairService = new ReparacionService();
    		repairService.update(newRepair);
    		messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Comentarios guardados correctamente.");
    		
    	} catch (Exception e) {
    		LoggerUtil.logError(e.getMessage(), e);
    		messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Ha ocurrido un error al guardar los comentarios.");
    	}
    }
    
    private void addContextMenu() {
        GenericContextMenuBuilder.attach(partsTable, part -> {
            MenuItem delete = new MenuItem("Eliminar");
            delete.setOnAction(e -> removePart(part));
            return Arrays.asList(delete);
        });
    }
	
	public String formatEntryDate(String date) {
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    LocalDate localDate = LocalDate.parse(date, inputFormatter);
	    return localDate.format(outputFormatter);
	}
}
