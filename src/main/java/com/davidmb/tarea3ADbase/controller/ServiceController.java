package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.db.DB4oConnection;
import com.davidmb.tarea3ADbase.models.Service;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.ServicesService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.ui.StopCell;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class ServiceController implements Initializable {

	@FXML
	private Label loggedInUser;

	@FXML
	private TextField serviceName;

	@FXML
	private TextField servicePrice;

	@FXML
	private Button reset;

	@FXML
	private Button saveService;

	@FXML
	private Button btnHelp;

	@FXML
	private Button btnReturn;

	@FXML
	private TableView<Stop> stopsTable;

	@FXML
	private TableColumn<Stop, Long> colStopId;

	@FXML
	private TableColumn<Stop, String> colStopName;

	@FXML
	private TableColumn<Stop, String> colStopRegion;

	@FXML
	private TableView<Service> servicesTable;

	@FXML
	private TableColumn<Service, Long> colServiceId;

	@FXML
	private TableColumn<Service, String> colServiceName;

	@FXML
	private TableColumn<Service, String> colServicePrice;

	@FXML
	private TableColumn<Service, String> colServiceStops;

	@FXML
	private TableColumn<Service, Boolean> colActions;

	@FXML
	private ListView<Stop> selectedStopsList;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private Session session;

	@Autowired
	private StopService stopService;

	@Autowired
	private ServicesService servicesService;

	private ObservableList<Service> servicesList = FXCollections.observableArrayList();
	private ObservableList<Stop> stopList = FXCollections.observableArrayList();
	ObservableList<Stop> selectedStops = FXCollections.observableArrayList();
	private Service serviceBeingEdited = null;

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}

	@FXML
	public void onReturn() {
		stageManager.switchScene(FxmlView.ADMIN);
	}

	@FXML
	void reset(ActionEvent event) {
		clearFields();
	}

	@FXML
	private void onSaveService(ActionEvent event) {
		String name = serviceName.getText().trim();
		String priceText = servicePrice.getText().trim();

		if (!validateData(name, priceText)) {
			return;
		}

		double price = Double.parseDouble(priceText);
		List<Long> stops = getSelectedStops();

		if (serviceBeingEdited != null) {
			serviceBeingEdited.setServiceName(name);
			serviceBeingEdited.setPrice(price);
			serviceBeingEdited.setStopIds(stops);
			
			if (showConfirmAlert(serviceBeingEdited)) {
				servicesService.update(serviceBeingEdited);
				saveAlert(serviceBeingEdited);
				serviceBeingEdited = null;
				saveService.setText("Guardar");
				clearFields();
			} else {
				saveAlert(null);
			}
		} else {
			if (servicesService.findByName(name) == null) {
				Service service = new Service(name, price, stops);
				Long id = servicesService.getMaxId() + 1;
				service.setId(id);
				if (showConfirmAlert(service)) {
					servicesService.save(service);
					saveAlert(service);
					clearFields();
				} else {
					saveAlert(null);
				}
			} else {
				StringBuilder message = new StringBuilder();
				message.append("El servicio ").append(name).append(" ya está registrado.");
				showErrorAlert(message);
			}

		}
		loadServicesDetail();
	}

	private void clearFields() {
		serviceName.clear();
		servicePrice.clear();
		stopsTable.getSelectionModel().clearSelection();
		selectedStopsList.getItems().clear();
	}

	private void saveAlert(Service service) {

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		if (service != null) {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
			alert.setTitle("Servicio registrado con éxito.");
			alert.setHeaderText("Servicio registrado con éxito.");
			alert.setContentText("El servicio " + service.getServiceName() + " ha sido registrado con éxito.");
		} else {
			alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/info.png")));
			alert.setTitle("Registro cancelado");
			alert.setHeaderText("Registro cancelado");
			alert.setContentText("Registro del servicio cancelado, vuelve a introducir los datos");
		}
		// Cambiar el ícono de la ventana

		alert.showAndWait();
	}

	private void showErrorAlert(StringBuilder message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al registrar el servicio");
		alert.setHeaderText("Error al registrar el servicio");
		alert.setContentText(message.toString());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private boolean showConfirmAlert(Service service) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Registrar servicio");
		alert.setHeaderText("¿Confirma los datos del servicio?");
		alert.setContentText("Servicio: " + service.getServiceName() + " Precio: " + service.getPrice());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/confirm.png")));
		alert.showAndWait();
		return alert.getResult().getButtonData().isDefaultButton();
	}

	private boolean validateData(String name, String priceText) {
		StringBuilder message = new StringBuilder();

		if (name.isEmpty()) {
			message.append("El nombre del servicio no puede estar vacío.\n");
		}

		if (priceText.isEmpty()) {
			message.append("El precio no puede estar vacío.\n");
		} else {
			try {
				Double.parseDouble(priceText);
			} catch (NumberFormatException e) {
				message.append("El precio debe ser un número válido.\n");
			}
		}

		// Si hay mensajes de error, mostrar alerta
		if (message.length() > 0) {
			showErrorAlert(message);
			return false;
		}

		return true;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		loadStopDetails();

		User user = session.getLoggedInUser();

		if (user != null) {
			loggedInUser.setText("Usuario: " + user.getUsername());
		}

		servicesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		stopsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		stopsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				getSelectedStops();
			}
		});
		selectedStopsList.setCellFactory(param -> new StopCell());

		setColumnProperties();

		// Add all stops into table
		loadServicesDetail();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {

		colServiceId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
		colServicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colServiceStops.setCellValueFactory(new PropertyValueFactory<>("stopsIds"));
		colActions.setCellFactory(cellFactory);

		colStopId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colStopName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colStopRegion.setCellValueFactory(new PropertyValueFactory<>("region"));
	}

	Callback<TableColumn<Service, Boolean>, TableCell<Service, Boolean>> cellFactory = new Callback<TableColumn<Service, Boolean>, TableCell<Service, Boolean>>() {
		@Override
		public TableCell<Service, Boolean> call(final TableColumn<Service, Boolean> param) {
			final TableCell<Service, Boolean> cell = new TableCell<Service, Boolean>() {
				Image imgEdit = new Image(getClass().getResourceAsStream("/icons/edit.png"));
				final Button btnEdit = new Button();

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							Service service = getTableView().getItems().get(getIndex());
							updateService(service);
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
						iv.setFitWidth(20);
						iv.setFitHeight(20);
						iv.setImage(imgEdit);
						iv.setPreserveRatio(true);
						iv.setSmooth(true);
						iv.setCache(true);
						btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updateService(Service service) {
					serviceBeingEdited = service;
					saveService.setText("Actualizar");
					serviceName.setText(service.getServiceName());
					servicePrice.setText(String.valueOf(service.getPrice()));
					selectedStopsList.getItems().clear();
					List<Stop> stops = new ArrayList<>();
					for (Long id : service.getStopIds()) {
						Stop stop = stopService.find(id);
						stops.add(stop);
					}
					selectedStopsList.getItems().addAll(stops);
				}
			};
			return cell;
		}
	};

	private void loadServicesDetail() {
		servicesList.clear();
		servicesList.addAll(servicesService.findAll());
		servicesTable.setItems(servicesList);
	}

	private void loadStopDetails() {
		stopList.clear();
		stopList.addAll(stopService.findAll());

		stopsTable.setItems(stopList);

		DB4oConnection.getInstance().closeConnection();
	}

	private List<Long> getSelectedStops() {
		selectedStops = stopsTable.getSelectionModel().getSelectedItems();
		List<Long> stops = new ArrayList<>();
		for (Stop stop : selectedStops) {
			if (!stops.contains(stop.getId())) {
				stops.add(stop.getId());
			}
		}
		selectedStopsList.setItems(selectedStops);
		return stops;
	}

}
