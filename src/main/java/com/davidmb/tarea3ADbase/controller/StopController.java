package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.db.ExistDBConnection;
import com.davidmb.tarea3ADbase.dtos.CarnetDTO;
import com.davidmb.tarea3ADbase.dtos.StayView;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * 
 */

@Controller
public class StopController implements Initializable {

	@FXML
	private Button btnLogout;
	
	@FXML
	private Button helpBtn;
	
	@FXML
	private Button cleanBtn;
	
	@FXML
	private Button filterBtn;

	@FXML
	private Label stopId;

	@FXML
	private DatePicker dpStart;

	@FXML
	private DatePicker dpEnd;

	@FXML
	private TableView<StayView> pilgrimsTable;

	@FXML
	private TableColumn<StayView, String> colPilgrimName;

	@FXML
	private TableColumn<StayView, String> colPilgrimNationality;
	
	@FXML
	private TableColumn<StayView, String> colStopDate;

	@FXML
	private TableColumn<StayView, LocalDate> colDoS;

	@FXML
	private TableColumn<StayView, Boolean> colStay;

	@FXML
	private TableColumn<StayView, Boolean> colVip;
	
	@FXML
	private TableView<CarnetDTO> carnetsTable;
	
	@FXML
	private TableColumn<CarnetDTO, Long> colCarnetId;
	
	@FXML
	private TableColumn<CarnetDTO, LocalDate> colCarnetDate;
	
	@FXML
	private TableColumn<CarnetDTO, String> colCarnetStop;
	
	@FXML
	private TableColumn<CarnetDTO, String> colCarnetPilgrim;
	
	@FXML
	private TableColumn<CarnetDTO, LocalDate> colCarnetToday;
	
	@FXML
	private TableColumn<CarnetDTO, Double> colCarnetDistance;
	
	@FXML
	private TableColumn<CarnetDTO, String> colCarnetNationality;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private PilgrimService pilgrimService;

	@Autowired
	private StopService stopService;


	@Autowired
	private Session session;

	private User user;

	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}
	
	@FXML
	private void onStampCard() {
		stageManager.switchScene(FxmlView.STAMPCARD);
	}

	/**
	 * Logout and go to the login page
	 */
	@FXML
	private void logout(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("¿Estás seguro que quieres cerrar sesión?");
		// Cambiar el ícono de la ventana
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/logout.png")));
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			stageManager.switchScene(FxmlView.LOGIN);
		}
	}

	@FXML
	private void filterStays() {
		if (dpStart.getValue() != null && dpEnd.getValue() != null) {
			if (dpStart.getValue().isBefore(dpEnd.getValue())) {
				ObservableList<StayView> pilgrimStaysByDate = FXCollections.observableArrayList();
				List<StayView> stayViewList = pilgrimService.findStayViewsByStopBetweenDates(
						stopService.findByUserId(user.getId()).getId(), dpStart.getValue(), dpEnd.getValue());
				pilgrimStaysByDate.addAll(stayViewList);
				pilgrimsTable.setItems(pilgrimStaysByDate);
				dpStart.setValue(null);
				dpEnd.setValue(null);
			} else {
				showErrorAlert(new StringBuilder("La fecha de inicio debe ser anterior a la fecha de fin"),
						new String("Error al filtrar paradas"));
			}
		} else {
			showErrorAlert(new StringBuilder("Debes seleccionar una fecha de inicio y una fecha de fin para filtrar"),
					new String("Error al filtrar paradas"));
		}
	}

	@FXML
	private void clearFilters() {
		dpStart.setValue(null);
		dpEnd.setValue(null);
		loadStayViews();
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		btnLogout.setTooltip(new Tooltip("Cerrar sesión"));
		helpBtn.setTooltip(new Tooltip("Pulsa F1 para mostrar el menú de ayuda"));
		cleanBtn.setTooltip(new Tooltip("Limpiar filtros"));
		filterBtn.setTooltip(new Tooltip("Filtrar estancias"));
		pilgrimsTable.setTooltip(new Tooltip("Tabla de peregrinos"));
		dpStart.setTooltip(new Tooltip("Fecha de comienzo para filtrar estancias"));
		dpEnd.setTooltip(new Tooltip("Fecha final para filtrar estancias"));
		

		user = session.getLoggedInUser();

		stopId.setText("Parada: " + user.getUsername());

		pilgrimsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		setColumnProperties();

		loadStayViews();
		loadCarnets();
	}

	/*
	 * Set All userTable column properties
	 */
	private void setColumnProperties() {

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

		colDoS.setCellValueFactory(new PropertyValueFactory<>("stayDate"));

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
		
		// Carnet columns
		colCarnetId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colCarnetDate.setCellValueFactory(new PropertyValueFactory<>("dateExpedition"));
		colCarnetStop.setCellValueFactory(new PropertyValueFactory<>("issuedIn"));
		colCarnetPilgrim.setCellValueFactory(new PropertyValueFactory<>("pilgrimName"));
		colCarnetToday.setCellValueFactory(new PropertyValueFactory<>("today"));
		colCarnetDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
		colCarnetNationality.setCellValueFactory(new PropertyValueFactory<>("pilgrimNationality"));

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
	
	private void loadCarnets() {
		ObservableList<CarnetDTO> carnets = FXCollections.observableArrayList();
		Stop stop = stopService.findByUserId(user.getId());
		List<CarnetDTO> carnetList = ExistDBConnection.getInstance().getCarnetsDTOByStop(stop.getName());
		carnets.addAll(carnetList);
		carnetsTable.setItems(carnets);
	}

}
