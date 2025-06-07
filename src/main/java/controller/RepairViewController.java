package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Estado;
import model.Reparacion;

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
	
	private Reparacion repair;
	
	@FXML
	public void initialize() {
		Platform.runLater(this::loadRepair);
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
	
	public String formatEntryDate(String date) {
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    LocalDate localDate = LocalDate.parse(date, inputFormatter);
	    return localDate.format(outputFormatter);
	}
}
