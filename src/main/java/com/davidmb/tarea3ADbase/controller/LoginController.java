package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class LoginController implements Initializable {

	@FXML
	private Button btnLogin;

	@FXML
	private PasswordField password;

	@FXML
	private TextField username;

	@FXML
	private Label lblLogin;

	@Autowired
	private UserService userService;

	@Lazy
	@Autowired
	private StageManager stageManager;
	
	@Autowired
	private Session session;

	@FXML
	private void login(ActionEvent event) throws IOException {
		if (userService.authenticate(getUsername(), getPassword())) {

			User user = userService.findByEmail(getUsername());
			session.setLoggedInUser(user);
			showSuccessAlert(user);
			
			switch(user.getRole().toUpperCase()) {
				case "ADMIN" -> stageManager.switchScene(FxmlView.ADMIN);
				case "PEREGRINO" -> stageManager.switchScene(FxmlView.PILGRIM);
				case "PARADA" -> stageManager.switchScene(FxmlView.STOP);
			}
			
			clearFields();          

		} else {
			showErrorAlert();
		}
	}
	
	private void showErrorAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error al iniciar sesión");
        alert.setHeaderText("Usuario o contraseña incorrectos");
        alert.setContentText("Por favor, introduzca un usuario y contraseña válidos");
        // Cambiar el ícono de la ventana
	    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
	    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/error.png")));
        alert.showAndWait();
	}
	
	private void showSuccessAlert(User user) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sesión iniciada como: " + user.getEmail());
		alert.setHeaderText(null);
		alert.setContentText("¡Bienvenido: " + user.getEmail() + "!\n");
		 // Cambiar el ícono de la ventana
	    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
	    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.showAndWait();
	}
	
	private void clearFields() {
		username.clear();
		password.clear();
	}

	@FXML
	private void openForgotPasswordView(ActionEvent event) throws IOException {
		// Cambiar a la vista de "Forgot Password"
		stageManager.switchScene(FxmlView.FORGOT_PASSWORD);
	}
	
	@FXML
	private void openRegisterPilgrimView(ActionEvent event) throws IOException {
		// Cambiar a la vista de "RegisterPilgrim"
		stageManager.switchScene(FxmlView.REGISTER_PILGRIM);
	}

	public String getPassword() {
		return password.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
