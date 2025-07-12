package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import model.Estado;
import model.Reparacion;
import service.EstadoService;
import service.ReparacionService;
import service.ServiceException;
import util.GenericContextMenuBuilder;
import util.LoggerUtil;
import util.StatusAware;
import util.StatusMessage;
import util.StatusMessage.Type;
import util.TableColumnBuilder;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepairsViewController implements StatusAware {
	private MainWindowController mainWindowController;
	
	private Consumer<StatusMessage> statusMessageCallback;
	
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
    	TableColumn<Reparacion, String> docLinkColumn = new TableColumn<>("Enlace de documentos");
    	docLinkColumn.setPrefWidth(150);
    	docLinkColumn.setCellValueFactory(cellData ->
    	    new SimpleObjectProperty<>(cellData.getValue().getEnlaceDocumento())
    	);

    	docLinkColumn.setCellFactory(new Callback<TableColumn<Reparacion, String>, TableCell<Reparacion, String>>() {
    	    @Override
    	    public TableCell<Reparacion, String> call(TableColumn<Reparacion, String> param) {
    	        return new TableCell<>() {
    	            private final Hyperlink link = new Hyperlink("Abrir carpeta");

    	            {
    	            	link.setStyle("-fx-text-fill: blue;");
    	            	
    	                link.setOnAction(e -> {
    	                    String path = getItem();
    	                    if (path != null && !path.isBlank()) {
    	                    	File file = new File(path);
    	                    	if (file.exists()) {
    	                    		try {
        	                            Desktop.getDesktop().open(new File(path));
        	                        } catch (IOException ex) {
        	                        	updateStatusMessage(new StatusMessage(StatusMessage.Type.ERROR, "Error al abrir la carpeta"));
        	                        }
								} else {
									updateStatusMessage(new StatusMessage(StatusMessage.Type.ERROR, "La carpeta ya no existe. Es posible que haya sido movida o eliminada."));
								}    	                        
    	                    }
    	                });
    	            }

    	            @Override
    	            protected void updateItem(String item, boolean empty) {
    	                super.updateItem(item, empty);
    	                setGraphic((empty || item == null || item.isBlank()) ? null : link);
    	            }
    	        };
    	    }
    	});

    	repairsTable.getColumns().add(docLinkColumn);

    	TableColumnBuilder.addColumn(repairsTable, "Total", 100, cellData -> {
    	    double ingresos = cellData.getValue().getIngresos();
    	    double gastos = cellData.getValue().getGastos();
    	    double total = ingresos - gastos;
    	    return new SimpleObjectProperty<>(total);
    	});

    	
    	placeholderLabel = new Label("Cargando Reparaciones...");
    	repairsTable.setPlaceholder(placeholderLabel);
    	
    	addContextMenu();
    	
    	Platform.runLater(this::loadRepairs);
    }
    
    public void setMainWindowController(MainWindowController mainWindowController) {
    	this.mainWindowController = mainWindowController;
    }
    
    private void addContextMenu() {
    	List<Estado> estados;
    	try {
    		EstadoService estadoService = new EstadoService();        	
    		estados = estadoService.findAll().stream().filter(e -> !e.getNombre().equalsIgnoreCase("Cerrada")).toList();
		} catch (Exception e) {
			throw new ControllerException("Error al obtener los estados.");
		}    	
    	
    	GenericContextMenuBuilder.attach(repairsTable, repair -> {
    		MenuItem delete = new MenuItem("Eliminar");
    		delete.setOnAction(e -> deleteRepair(repair));
    		
    		MenuItem view = new MenuItem("Ver");
    		view.setOnAction(e -> openRepairTab(repair));
    		
    		if (repair.getEstado().getNombre().equalsIgnoreCase("Cerrada")) {
    			return Arrays.asList(delete, view);
			} else {
				Menu statesItem = new Menu("Cambiar estado");

        		for (Estado estado : estados) {
        			MenuItem stateItem = new MenuItem(estado.getNombre());
        			stateItem.setOnAction(e -> {
        				ReparacionService.updateState(repair, estado);
        				refreshRepairs();
        			});
        			statesItem.getItems().add(stateItem);
        		}
    			return Arrays.asList(delete, view, statesItem);

			}
    	});
    }
    
    private void openRepairTab(Reparacion currentRepair) {
    	ReparacionService reparacionService = new ReparacionService();
    	Reparacion repair;
    	try {
			repair = reparacionService.findById(currentRepair.getIdReparacion());			
			if (repair != null) {
				String repairId = "R" + Integer.toString(currentRepair.getIdReparacion());
		    	try {
		    		String fxmlPath = "/views/repairView.fxml";
					mainWindowController.openTab(repairId, fxmlPath , false, currentRepair);
					
				} catch (Exception e) {
					throw new ControllerException(e.getMessage(), e);
				}
			} else {
				mainWindowController.setStatusMessage(new StatusMessage(Type.ERROR, "Si realizaste cambios previamente, pulsa \"Refrescar\" para actualizar la tabla."));
			}			
		} catch (Exception e) {
			mainWindowController.setStatusMessage(new StatusMessage(Type.ERROR, "Ha habido un error al abrir la reparación."));
			LoggerUtil.logError(e.getMessage(), e);
		}    	    	
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
	                    repairsTable.setPlaceholder(new Label("No se encontraron reparaciones en la base de datos."));
	                    repairsTable.getItems().clear();
	                } else {
	                    repairsTable.setItems(observableRepairs);
	                }
	            });
			} catch (ServiceException e) {
	            Platform.runLater(() -> {
	                placeholderLabel = new Label("Error al cargar las reparaciones");
	                repairsTable.setPlaceholder(placeholderLabel);
	            });
	            LoggerUtil.logError(e.getMessage(), e);
			}
    	}).start();
    }
    
    public void refreshRepairs() {
    	List<Reparacion> repairs = ReparacionService.findAll();
    	repairsTable.setItems(FXCollections.observableArrayList(repairs));
    	
    	if (repairs == null || repairs.isEmpty()) {
            repairsTable.setPlaceholder(new Label("No se encontraron reparaciones en la base de datos."));
        }
    }

	@Override
	public void setStatusCallback(Consumer<StatusMessage> callback) {
		this.statusMessageCallback = callback;		
	}
	
    
    private void updateStatusMessage(StatusMessage statusMessage) {
    	if (statusMessageCallback != null) {
    		Platform.runLater(() -> {
    			statusMessageCallback.accept(new StatusMessage(statusMessage.getType(), statusMessage.getMessage()));
    		});
    	}
    }
}