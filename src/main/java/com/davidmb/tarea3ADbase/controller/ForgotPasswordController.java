package com.davidmb.tarea3ADbase.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.UserService;
import com.davidmb.tarea3ADbase.utils.Alerts;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;


@Controller
public class ForgotPasswordController implements Initializable {
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	@FXML
	private TextField emailField;

	@FXML
	private TextField passwordField;

	@FXML
	private TextField passwordField2;

	@FXML
	private Label lblPasswordError;

	@FXML
	private Button changePasswordBtn;

	@FXML
	private Button backToLoginBtn;

	@FXML
	private Button helpBtn;

	@FXML
	private Label lblEmailError;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private UserService userService;

	@Autowired
	private Alerts alert;

	@FXML
	private void resetPassword() {
		if (!validateData()) {
			return;
		}

		String email = emailField.getText();
		String newPassword = passwordField.getText();
		User user = userService.findByEmail(email);

		try {

			userService.resetPassword(user, newPassword);
			String message = "La contraseña del usuario " + email + " se ha restablecido con éxito.";
			alert.info("Contraseña restablecida", "Contraseña restablecida", message);
			stageManager.switchScene(FxmlView.LOGIN);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}

	@FXML
	private void backToLogin() {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	private boolean validateData() {

		boolean ret = false;
		StringBuilder message = new StringBuilder();
		String email = emailField.getText();
		String password = passwordField.getText();
		String confirmPassword = passwordField2.getText();
		User emailExists = userService.findByEmail(email);

		// Validar Email
		if (email.isEmpty()) {
			message.append("El Email no puede estar vacío.\n");
		} else if (emailExists == null) {
			message.append("El Email no existe.\n");
		} else if (email.length() > 50) {
			message.append("El Email no puede tener más de 50 caracteres.\n");
		} else if (!email.matches(EMAIL_PATTERN)) {
			message.append("El Email no tiene un formato válido.\n");
		}

		// Validar Password
		if (password.isEmpty()) {
			message.append("La contraseña no puede estar vacía.\n");
		} else if (password.length() < 8) {
			message.append("La contraseña debe tener al menos 8 caracteres.\n");
		} else if (!password.matches(".*\\d.*")) {
			message.append("La contraseña debe contener al menos un número.\n");
		} else if (!password.matches(".*[a-z].*")) {
			message.append("La contraseña debe contener al menos una letra minúscula.\n");
		} else if (!password.matches(".*[A-Z].*")) {
			message.append("La contraseña debe contener al menos una letra mayúscula.\n");
		} else if (!password.matches(".*[!@#$%^&*].*")) {
			message.append("La contraseña debe contener al menos un carácter especial.\n");
		} else if (!password.equals(confirmPassword)) {
			message.append("Las contraseñas no coinciden.\n");
		}

		if (message.length() > 0) {
			alert.error("Error", "Error al restablecer la contraseña", message);
		} else {
			ret = true;
		}
		return ret;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		helpBtn.setTooltip(new Tooltip("Ayuda"));
		changePasswordBtn.setTooltip(new Tooltip("Cambiar contraseña"));
		backToLoginBtn.setTooltip(new Tooltip("Volver al login"));
		emailField.setTooltip(new Tooltip("Introduce tu email"));
		passwordField.setTooltip(new Tooltip("Introduce tu nueva contraseña"));
		passwordField2.setTooltip(new Tooltip("Confirma tu nueva contraseña"));
	}
}
