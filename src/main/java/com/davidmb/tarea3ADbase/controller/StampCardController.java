package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.dtos.ServiceResponse;
import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.ContractedGroup;
import com.davidmb.tarea3ADbase.models.PaymodeEnum;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.Service;
import com.davidmb.tarea3ADbase.models.Stay;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.ContractedGroupService;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.PilgrimStopsService;
import com.davidmb.tarea3ADbase.services.ServicesService;
import com.davidmb.tarea3ADbase.services.StayService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.ui.ServiceCell;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Controller
public class StampCardController implements Initializable {

	@FXML
	private VBox vboxSelectedList;

	@FXML
	private TextField extraTextField;

	@FXML
	private Button btnReturn;

	@FXML
	private Button helpBtn;

	@FXML
	private Label stopId;

	@FXML
	private Label totalPrice;

	@FXML
	private CheckBox cbStay;

	@FXML
	private RadioButton rbYes;

	@FXML
	private RadioButton rbNo;

	@FXML
	private RadioButton rbCard;

	@FXML
	private RadioButton rbCash;

	@FXML
	private RadioButton rbBizum;

	@FXML
	private ComboBox<String> cbPilgrims;

	@FXML
	private Button reset;

	@FXML
	private Button stampCard;

	@FXML
	private TableView<StayView> pilgrimsTable;

	@FXML
	private TableColumn<StayView, String> colPilgrimName;

	@FXML
	private TableColumn<StayView, String> colPilgrimNationality;

	@FXML
	private TableColumn<StayView, String> colStopDate;

	@FXML
	private TableColumn<StayView, Boolean> colStay;

	@FXML
	private TableColumn<StayView, Boolean> colVip;

	@FXML
	private TableView<Service> servicesTable;

	@FXML
	private TableColumn<Service, Long> colServiceId;

	@FXML
	private TableColumn<Service, String> colServiceName;

	@FXML
	private TableColumn<Service, String> colServicePrice;

	@FXML
	private ListView<Service> selectedServicesList;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private PilgrimService pilgrimService;

	@Autowired
	private StopService stopService;

	@Autowired
	private PilgrimStopsService pilgrimStopsService;

	@Autowired
	private ServicesService servicesService;

	@Autowired
	private StayService stayService;

	@Autowired
	private ContractedGroupService contractedGroupService;

	@Autowired
	private Session session;

	private User user;

	ObservableList<Service> servicesList = FXCollections.observableArrayList();
	ObservableList<Service> selectedServices = FXCollections.observableArrayList();

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
		stageManager.switchScene(FxmlView.STOP);
	}

	@FXML
	void reset(ActionEvent event) {
		clearFields();
	}

	@FXML
	private void stampCard(ActionEvent event) {
		if (!validateData()) {
			return;
		}

		if (!cbStay.isSelected()) {
			rbNo.setSelected(true);
		}

		Pilgrim pilgrim = pilgrimService.find(Long.valueOf(cbPilgrims.getValue().split(" ")[1]));
		Stop stop = stopService.findByUserId(user.getId());

		if (pilgrimHasAlreadyStamped(pilgrim, stop)) {
			showErrorAlert(new StringBuilder("El peregrino ya ha sellado su carnet en esta parada"),
					new String("Error al sellar el Carnet"));
			return;
		}

		if (!getSelectedServices().isEmpty() && vboxSelectedList.isVisible()) {
			if (validateContractedGroupData()) {
				ServiceResponse<Pilgrim> serviceResponse = pilgrimService.stampCard(pilgrim, stop, rbYes.isSelected(),
						cbStay.isSelected());
				createAndSaveContractedGroup(pilgrim, stop);
				handleServiceResponse(serviceResponse, pilgrim);
				loadStayViews();
				return;
			} else {
				return;
			}
		}
		// Sellar carnet
		ServiceResponse<Pilgrim> serviceResponse = pilgrimService.stampCard(pilgrim, stop, rbYes.isSelected(),
				cbStay.isSelected());

		handleServiceResponse(serviceResponse, pilgrim);
		loadStayViews();
	}

	private boolean pilgrimHasAlreadyStamped(Pilgrim pilgrim, Stop stop) {
		return pilgrimStopsService.existsByPilgrimAndStopAndStopDate(pilgrim, stop, LocalDate.now());
	}

	private void createAndSaveContractedGroup(Pilgrim pilgrim, Stop stop) {

		Stay stay = stayService.findByPilgrimIdAndStopIdAndDate(pilgrim.getId(), stop.getId(), LocalDate.now());
		if (stay == null) {
			return;
		}

		ContractedGroup contractedGroup = new ContractedGroup();
		contractedGroup.setId(contractedGroupService.getNextId());
		contractedGroup.setServiceIds(getSelectedServices());
		contractedGroup.setStayId(stay.getId());
		contractedGroup.setTotalPrice(Double.parseDouble(totalPrice.getText().replace(" €", "")));
		contractedGroup.setPayMode(getSelectedPayMode());
		contractedGroup.setExtra(extraTextField.getText());

		if (contractedGroupService.save(contractedGroup)) {
			showContractedGroupAlert(contractedGroup);
		} else {
			showErrorAlert(new StringBuilder("Error al registrar el contrato de grupo"),
					new String("Error al registrar el contrato de grupo"));
		}
	}

	private char getSelectedPayMode() {
		if (rbCard.isSelected()) {
			return PaymodeEnum.TARJETA.getPayMode();
		} else if (rbCash.isSelected()) {
			return PaymodeEnum.EFECTIVO.getPayMode();
		}
		return PaymodeEnum.BIZUM.getPayMode();
	}

	private void handleServiceResponse(ServiceResponse<Pilgrim> serviceResponse, Pilgrim pilgrim) {
		if (serviceResponse == null) {
			showErrorAlert(new StringBuilder("Error al sellar el carnet del peregrino"),
					new String("Error al sellar el Carnet"));
			return;
		}

		if (serviceResponse.isSuccess()) {
			saveAlert(serviceResponse, pilgrim);
		} else {
			showServiceResponseError(serviceResponse, pilgrim);
		}
		clearFields();
	}
	
	@FXML
	private void sendHome() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SendHome.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/send.png")));
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearFields() {
		stopId.setText(null);
		cbStay.setSelected(false);
		rbYes.setSelected(false);
		rbNo.setSelected(false);
		cbPilgrims.getSelectionModel().clearSelection();
		servicesTable.getSelectionModel().clearSelection();
	}

	private void saveAlert(ServiceResponse<Pilgrim> serviceResponse, Pilgrim pilgrim) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(serviceResponse.getMessage());
		alert.setHeaderText(serviceResponse.getMessage());
		alert.setContentText("La parada del peregrino " + pilgrim.getName() + " con ID: " + pilgrim.getId()
				+ " ha sido registrada  \n");

		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));

		alert.showAndWait();
	}

	private void showContractedGroupAlert(ContractedGroup c) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Contrato de grupo");
		alert.setHeaderText("Contrato de grupo");
		alert.setContentText("El contrato de grupo ha sido registrado correctamente. \n" + "ID: " + c.getId() + "\n"
				+ "Precio total: " + c.getTotalPrice() + " €\n" + "Modo de pago: " + c.getPayMode() + "\n"
				+ "Observaciones: " + c.getExtra());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.showAndWait();
	}

	private void showServiceResponseError(ServiceResponse<Pilgrim> serviceResponse, Pilgrim pilgrim) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error al sellar el carnet del peregrino");
		alert.setHeaderText("Ha ocurrido un error al sellar el carnet del peregrino");
		alert.setContentText("Ha ocurrido un error al sellar el carnet del peregrino " + pilgrim.getName() + " con ID: "
				+ pilgrim.getId() + "\n" + serviceResponse.getMessage());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private void showErrorAlert(StringBuilder message, String header) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message.toString());
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
		alert.showAndWait();
	}

	private boolean validateData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();

		// Validar peregrino
		if (cbPilgrims.getValue() == null) {
			message.append("Debes seleccionar un peregrino.\n");
		}

		if (message.length() > 0) {
			showErrorAlert(message, "Error al sellar el carnet del peregrino");
		} else {
			ret = true;
		}
		return ret;
	}

	private boolean validateContractedGroupData() {
		boolean ret = false;
		StringBuilder message = new StringBuilder();

		// Validar servicios
		if (selectedServices.isEmpty()) {
			message.append("Debes seleccionar al menos un servicio.\n");
		}
		if (!rbCard.isSelected() && !rbCash.isSelected() && !rbBizum.isSelected()) {
			message.append("Debes seleccionar un modo de pago.\n");
		}
		if (extraTextField.getText().length() > 50) {
			message.append("El campo observaciones no puede superar los 50 caracteres.\n");
		}

		if (message.length() > 0) {
			showErrorAlert(message, "Error al registrar el contrato de grupo");
		} else {
			ret = true;
		}
		return ret;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		helpBtn.setTooltip(new Tooltip("Pulsa F1 para mostrar el menú de ayuda"));
		pilgrimsTable.setTooltip(new Tooltip("Tabla de peregrinos"));
		cbStay.setTooltip(new Tooltip("Alojarse"));
		rbYes.setTooltip(new Tooltip("Estancia VIP"));
		rbNo.setTooltip(new Tooltip("Estancia no VIP"));
		reset.setTooltip(new Tooltip("Limpiar formulario"));
		stampCard.setTooltip(new Tooltip("Sellar carnet"));

		user = session.getLoggedInUser();

		stopId.setText("Parada: " + user.getUsername());

		loadPilgrims();

		servicesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		servicesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				getSelectedServices();
			}
		});
		selectedServicesList.setCellFactory(param -> new ServiceCell());

		setColumnProperties();

		loadStayViews();
		loadServicesDetail();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {

		colServiceId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
		colServicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

		colPilgrimName.setCellValueFactory(new PropertyValueFactory<>("pilgrimName"));
		colPilgrimNationality.setCellValueFactory(new PropertyValueFactory<>("pilgrimNationality"));
		colStopDate.setCellValueFactory(new PropertyValueFactory<>("stopDate"));

		colStay.setCellValueFactory(new PropertyValueFactory<>("stay"));

		colStay.setCellFactory(column -> new TableCell<StayView, Boolean>() {
			private final ImageView imageView = new ImageView();

			@Override
			protected void updateItem(Boolean stay, boolean empty) {
				super.updateItem(stay, empty);
				if (empty || stay == null) {
					setGraphic(null);
				} else {
					Image image = new Image(stay ? "/icons/check.png" : "/icons/cross.png");
					imageView.setImage(image);
					imageView.setFitHeight(16);
					imageView.setFitWidth(16);
					setGraphic(imageView);
				}
			}
		});

		colVip.setCellValueFactory(new PropertyValueFactory<>("isVip"));
		colVip.setCellFactory(column -> new TableCell<StayView, Boolean>() {
			private final ImageView imageView = new ImageView();

			@Override
			protected void updateItem(Boolean isVip, boolean empty) {
				super.updateItem(isVip, empty);
				if (empty || isVip == null) {
					setGraphic(null);
				} else {
					Image image = new Image(isVip ? "/icons/check.png" : "/icons/cross.png");
					imageView.setImage(image);
					imageView.setFitHeight(16);
					imageView.setFitWidth(16);
					setGraphic(imageView);
				}
			}
		});

	}

	/*
	 * Add All users to observable list and update table
	 */
	private void loadStayViews() {
		ObservableList<StayView> stayViews = FXCollections.observableArrayList();

		List<StayView> stayViewList = pilgrimService
				.findAllStayViewsByStop(stopService.findByUserId(user.getId()).getId());
		stayViews.addAll(stayViewList);
		pilgrimsTable.setItems(stayViews);
	}

	private void loadServicesDetail() {
		servicesList.clear();
		servicesList.addAll(servicesService.findAll());
		servicesTable.setItems(servicesList);
	}

	private void loadPilgrims() {
		cbPilgrims.getItems().clear();
		pilgrimService.findAll()
				.forEach(pilgrim -> cbPilgrims.getItems().addAll("ID: " + pilgrim.getId() + " - " + pilgrim.getName()));
	}

	private List<Long> getSelectedServices() {
		double total = 0;
		selectedServices = servicesTable.getSelectionModel().getSelectedItems();
		List<Long> services = new ArrayList<>();
		for (Service service : selectedServices) {
			if (!services.contains(service.getId())) {
				services.add(service.getId());
				total += service.getPrice();
			}
		}
		selectedServicesList.setItems(selectedServices);
		totalPrice.setText(total + " €");
		return services;
	}

	/*
	 * Validations
	 */
}
