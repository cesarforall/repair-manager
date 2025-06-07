package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import model.Estado;
import model.Reparacion;
import model.Repuesto;
import service.RepuestoService;
import service.ServiceException;
import util.LoggerUtil;
import util.TableColumnBuilder;

public class RepairViewController {
	@FXML
	Label repairIdLabel;
	@FXML
	ComboBox<Estado> stateComboBox;
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
	
	Label placeholderLabel;
	
	private Reparacion repair;
	
	RepuestoService repuestoService = new RepuestoService();
	
	@FXML
	public void initialize() {
		Platform.runLater(this::loadRepair);
		Platform.runLater(this::loadParts);
	}
	
	public void setRepair(Reparacion repair) {
		this.repair = repair;
	}
	
	private void loadRepair() {
		Platform.runLater(() -> {
			if (repair != null) {
				repairIdLabel.setText("Reparación: " + repair.getIdReparacion());
				stateComboBox.getSelectionModel().select(repair.getEstado());
				deviceLabel.setText(repair.getDispositivo().getNombre());
				clientLabel.setText(repair.getCliente().getNombre());
				inDateLabel.setText(formatEntryDate(repair.getFechaEntrada()));
				outDateLabel.setText(repair.getFechaSalida() != null ? repair.getFechaEntrada() : "");
				commentsTextArea.setText(repair.getDetalle());
			}
		});
	}
	
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
	
	public String formatEntryDate(String date) {
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    LocalDate localDate = LocalDate.parse(date, inputFormatter);
	    return localDate.format(outputFormatter);
	}
}
