package controller;

import java.awt.Desktop;
import java.io.File;
import java.time.LocalDate;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Estado;
import model.Reparacion;
import model.Componente;
import model.ComponenteReparacion;
import service.EstadoService;
import service.ReparacionService;
import service.ComponenteReparacionService;
import service.ComponenteService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.StatusAware;
import util.StatusMessage;
import util.TableColumnBuilder;
import util.StatusMessage.Type;
import util.Utils;

public class RepairViewController implements StatusAware{
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
	ComboBox<Componente> partsComboBox;
	@FXML
	TextField quantityTextField;
	@FXML
	TableView<ComponenteReparacion> partsTable;
	@FXML
	Label partsMessageLabel;
	@FXML
	Label messageLabel;
	@FXML
	private TextField incomeTextField;
	@FXML
	private Button addIncomeButton;
	@FXML
	private Label expensesLabel;
	@FXML
	private Label totalLabel;
	@FXML
	private Label profitLabel;
	@FXML
	private Label pathLabel;
	@FXML
	private Button saveButton;
	@FXML
	private Button refreshPartsButton;
	@FXML
	private Button addPartButton;
	@FXML
	private Button finishButton;
	@FXML
	private Button deleteButton;

	private MainWindowController mainWindowController;
	Tab repairTab;
	
	Label placeholderLabel;
	
	private Consumer<StatusMessage> statusMessageCallback;
	
	private Reparacion repair;
	
	Utils utils = new Utils();
	
	ComponenteService componenteService = new ComponenteService();
	ComponenteReparacionService componenteReparacionService = new ComponenteReparacionService();
	
	@FXML
	public void initialize() {
		TableColumnBuilder.addColumn(partsTable, "Id", 80, cellData ->
		new SimpleObjectProperty<>(Utils.formatIntToId("C", cellData.getValue().getComponente().getidComponente())));
		
		TableColumnBuilder.addColumn(partsTable, "Nombre", 200, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getComponente().getNombre()));
		
		TableColumnBuilder.addColumn(partsTable, "Valor", 100, cellData ->
		new SimpleObjectProperty<>(Utils.formatDoubleToEuros(cellData.getValue().getComponente().getPrecio() * cellData.getValue().getCantidad())));
		
		TableColumnBuilder.addColumn(partsTable, "Cantidad", 100, cellData ->
		new SimpleObjectProperty<>(cellData.getValue().getCantidad()));
		    	
    	placeholderLabel = new Label("Cargando Componentes...");
    	partsTable.setPlaceholder(placeholderLabel);
		
		Platform.runLater(this::loadRepair);
		Platform.runLater(this::loadParts);
		Platform.runLater(this::loadPartsRepair);
		Platform.runLater(this::addContextMenu);
	}
	
	public void setRepair(Reparacion repair) {
		clearRepairData();
		this.repair = repair;
	}
	
	public void setMainWindowController(MainWindowController mainWindowController) {
	    this.mainWindowController = mainWindowController;
	}
	
	public void setCurrentTab(Tab repairTab) {
		this.repairTab = repairTab;
	}
	
	public void addPart() {
		if (addPartButton.isDisabled()) return;
		
	    Componente selectedPart = partsComboBox.getValue();
	    String quantityText = quantityTextField.getText();

	    if (selectedPart == null) {
	        partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("No se encontraron componentes para añadir.");
	    } else if(quantityText.isBlank()){
	    	partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("El campo \"Cantidad\" es obligatorio.");
	    }else if (!quantityText.matches("\\d+")) {
	    	partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("La cantidad debe ser un número entero positivo.");
	    }else if(Integer.parseInt(quantityText) <= 0){
	    	partsMessageLabel.setStyle("-fx-text-fill: red;");
	        partsMessageLabel.setText("La cantidad no puede ser inferior a 1.");
	    }else {
	        try {
	            int quantity = Integer.parseInt(quantityText);

	            ComponenteReparacion partRepair = new ComponenteReparacion(repair, selectedPart, quantity);
	            ComponenteReparacionService service = new ComponenteReparacionService();
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

	private void removePart(ComponenteReparacion part) {
	    try {
	        ComponenteReparacionService service = new ComponenteReparacionService();
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
	
	private void clearRepairData() {
	    repairIdLabel.setText("");
	    stateLabel.setText("");
	    deviceLabel.setText("");
	    clientLabel.setText("");
	    inDateLabel.setText("");
	    outDateLabel.setText("");
	    commentsTextArea.clear();
	    incomeTextField.clear();
	    expensesLabel.setText("");
	    totalLabel.setText("");
	    profitLabel.setText("");
	    partsTable.getItems().clear();
	    partsMessageLabel.setText("");
	    messageLabel.setText("");
	}
	
	private void loadRepair() {
	    Platform.runLater(() -> {
	        if (repair != null) {
	            repairIdLabel.setText("Reparación: " + Utils.formatIntToId("R", repair.getIdReparacion()));
	            stateLabel.setText(repair.getEstado().getNombre());
	            deviceLabel.setText(Utils.formatIntToId("D", repair.getDispositivo().getIdDispositivo()) + " " + repair.getDispositivo().getTipo() + " " + repair.getDispositivo().getFabricante() + " " + repair.getDispositivo().getModelo());
	            clientLabel.setText(Utils.formatIntToId("CL", repair.getCliente().getIdCliente()) + " " + repair.getCliente().getNombre());
	            inDateLabel.setText(Utils.formatEntryDate(repair.getFechaEntrada()));
	            outDateLabel.setText(Utils.formatEntryDate(repair.getFechaSalida()) != null ? Utils.formatEntryDate(repair.getFechaSalida()) : "");
	            commentsTextArea.setText(repair.getDetalle());
	            incomeTextField.setText(Utils.formatDoubleToMoney(repair.getIngresos()));
	            pathLabel.setText(repair.getEnlaceDocumento());
	
	            double expenses = new ComponenteReparacionService().calculateTotalByRepair(repair.getIdReparacion());
	            double income = repair.getIngresos();
	            double total = income - expenses;
	            double profit = (income == 0) ? 0 : (total / income) * 100;
	
	            expensesLabel.setText(Utils.formatDoubleToEuros(expenses));
	            totalLabel.setText(Utils.formatDoubleToEuros(total));
	            profitLabel.setText(String.format("%.2f", profit) + " %");
	
	            if (repair.getEstado().getNombre().equalsIgnoreCase("Cerrada")) {
	                disableInputs();
	            }
	        }
	    });
	}

	
	@FXML
    private void loadParts() {
    	new Thread(() -> {
    		try {
                List<Componente> parts = componenteService.findAll();
                ObservableList<Componente> observableParts = FXCollections.observableArrayList(parts);
                
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
	            List<ComponenteReparacion> componentes = componenteReparacionService.findByRepairId(repair.getIdReparacion());
	            ObservableList<ComponenteReparacion> observableComponentes = FXCollections.observableArrayList(componentes);
	
	            Platform.runLater(() -> {
	                if (componentes == null || componentes.isEmpty()) {
	                    partsTable.setPlaceholder(new Label("No se encontraron componentes en la reparación."));
	                    partsTable.getItems().clear();
	                } else {
	                    partsTable.setItems(observableComponentes);
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
    	if (refreshPartsButton.isDisabled()) return;

        List<ComponenteReparacion> componentes = componenteReparacionService.findByRepairId(repair.getIdReparacion());
        partsTable.setItems(FXCollections.observableArrayList(componentes));

        if (componentes == null || componentes.isEmpty()) {
            partsTable.setPlaceholder(new Label("No se encontraron componentes en la reparación"));
        }

        addIncomeAndUpdate();
    }
    
    @FXML
    private void updateComment() {
    	if (saveButton.isDisabled()) return;
    	
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
    
    @FXML
    private void addIncomeAndUpdate() {
    	if (addIncomeButton.isDisabled()) return;
    	
        try {
        	if (!incomeTextField.getText().matches("\\d+(\\.|,)?\\d*")) {
        		messageLabel.setStyle("-fx-text-fill: red;");
        	    messageLabel.setText("El campo \"Ingresos\" debe ser un número válido.");
        	    return;
        	}
        	
        	String incomeString = incomeTextField.getText().replace(",", ".");        	
            double income = incomeString.isBlank() ? 0 : Double.parseDouble(incomeString);
            
            ComponenteReparacionService service = new ComponenteReparacionService();
            double expenses = service.calculateTotalByRepair(repair.getIdReparacion());
            double total = income - expenses;
            double profit = (income == 0) ? 0 : (total / income) * 100;

            incomeTextField.setText(Utils.formatDoubleToMoney(income));
            expensesLabel.setText(Utils.formatDoubleToEuros(expenses));
            totalLabel.setText(Utils.formatDoubleToEuros(total));
            profitLabel.setText(String.format("%.2f", profit) + " %");

            repair.setIngresos(income);
            repair.setGastos(expenses);

            ReparacionService reparacionService = new ReparacionService();
            reparacionService.update(repair);

        } catch (ServiceException e) {
            LoggerUtil.logError(e.getMessage(), e);
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Ha ocurrido un error al calcular los datos.");
        }
    }

    @FXML
    private void deleteRepair() {
    	if (deleteButton.isDisabled()) return;
    	
    	if (repair != null) {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmación de eliminación");
    		alert.setHeaderText("¿Seguro que deseas eliminar la reparación R" + this.repair.getIdReparacion() + "?");
    		alert.setContentText("Esta opción no se puede deshacer");
    		
    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			try {
    				ReparacionService reparacionService = new ReparacionService();
    				reparacionService.delete(repair);
    				
        			updateStatusMessage(new StatusMessage(Type.INFO, "Se ha eliminado R" + this.repair.getIdReparacion() + "."));
				} catch (ServiceException e) {
					Platform.runLater(() -> {
						updateStatusMessage(new StatusMessage(Type.ERROR, "Se ha producido un error al eliminar la reparacion"));						
					});
					LoggerUtil.logError(e.getMessage(), e);
				}    			
    		}
    		mainWindowController.closeTab(repairTab);    		
    	}
    }
    
    @FXML
    private void finishRepair() {
    	if (finishButton.isDisabled()) return;

    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Confirmación de cierre");
    	alert.setHeaderText("¿Seguro que deseas cerrar esta reparación?");
    	alert.setContentText("No se podrán modificar los datos.");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.isPresent() && result.get() == ButtonType.OK) {
    		try {
    			ReparacionService repairService = new ReparacionService();
    			EstadoService stateService = new EstadoService();
    			Estado stateFinish = new Estado("Cerrada", "Reparaciones terminadas");
    			Reparacion finishedRepair = new Reparacion(repair);

    			Estado existingState = stateService.findByName(stateFinish);
    			if (existingState == null) {
    				stateService.save(stateFinish);
    				existingState = stateFinish;
    			}

    			finishedRepair.setEstado(existingState);
    			finishedRepair.setFechaSalida(LocalDate.now().toString());

    			repairService.update(finishedRepair);

    			repair.setFechaSalida(finishedRepair.getFechaSalida());
    			outDateLabel.setText(Utils.formatEntryDate(finishedRepair.getFechaSalida()));

    			disableInputs();
    		} catch (Exception e) {
    			Platform.runLater(() -> {
    				updateStatusMessage(new StatusMessage(Type.ERROR, "Se ha producido un error al terminar la reparación"));
    			});
    			LoggerUtil.logError(e.getMessage(), e);
    		}
    	}
    }
    
    private void disableInputs() {
        if (stateComboBox != null) {
            stateComboBox.setDisable(true);
        }
        if (commentsTextArea != null) {
            commentsTextArea.setDisable(true);
        }
        if (incomeTextField != null) {
            incomeTextField.setDisable(true);
        }
        if (addIncomeButton != null) {
            addIncomeButton.setDisable(true);
        }
        if (quantityTextField != null) {
            quantityTextField.setDisable(true);
        }
        if (partsComboBox != null) {
            partsComboBox.setDisable(true);
        }
        if (partsTable != null) {
            partsTable.setDisable(true);
        }
        if (saveButton != null) {
            saveButton.setDisable(true);
        }
        if (refreshPartsButton != null) {
            refreshPartsButton.setDisable(true);
        }
        if (addPartButton != null) {
            addPartButton.setDisable(true);
        }
        if (finishButton != null) {
            finishButton.setDisable(true);
        }
        if (deleteButton != null) {
            deleteButton.setDisable(true);
        }
    }
    
    private void updateStatusMessage(StatusMessage statusMessage) {
    	if (statusMessageCallback != null) {
    		Platform.runLater(() -> {
    			statusMessageCallback.accept(new StatusMessage(statusMessage.getType(), statusMessage.getMessage()));
    		});
    	}
    }
 
    private void addContextMenu() {
        GenericContextMenuBuilder.attach(partsTable, part -> {
            MenuItem delete = new MenuItem("Eliminar");
            delete.setOnAction(e -> removePart(part));
            return Arrays.asList(delete);
        });
    }
    
    @FXML
    private void openDocumentFolder() {
		String path = repair.getEnlaceDocumento();
		if (path != null && !path.isEmpty()) {
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (Exception e) {
				updateStatusMessage(new StatusMessage(StatusMessage.Type.ERROR, "La carpeta ya no existe. Es posible que haya sido movida o eliminada."));
				LoggerUtil.logError("Error al abrir la carpeta.", e);
			}
		}
	}
	
	@Override
	public void setStatusCallback(Consumer<StatusMessage> statusMessageCallback) {
    	this.statusMessageCallback = statusMessageCallback;
    }
}
