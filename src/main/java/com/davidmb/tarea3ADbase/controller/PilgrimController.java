package com.davidmb.tarea3ADbase.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.davidmb.tarea3ADbase.auth.Session;
import com.davidmb.tarea3ADbase.config.StageManager;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.User;
import com.davidmb.tarea3ADbase.services.PilgrimService;
import com.davidmb.tarea3ADbase.services.StayService;
import com.davidmb.tarea3ADbase.services.StopService;
import com.davidmb.tarea3ADbase.utils.Alerts;
import com.davidmb.tarea3ADbase.utils.ExportarCarnetXML;
import com.davidmb.tarea3ADbase.utils.HelpUtil;
import com.davidmb.tarea3ADbase.utils.ShowPDFInModal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
public class PilgrimController implements Initializable {

	@FXML
	private Label lblName;

	@FXML
	private Label lblNationality;

	@FXML
	private Label lblDoExp;

	@FXML
	private Label lblDistance;

	@FXML
	private Label lnlnVips;

	@FXML
	private Label lblInitialStop;
	
	@FXML
	private Button logoutBtn;
	
	@FXML
	private Button exportCarnetBtn;
	
	@FXML
	private Button exportReportBtn;
	
	@FXML
	private Button helpBtn;

	@FXML
	private ImageView avatarImageView;

	@Autowired
	private PilgrimService pilgrimService;
	
	@Autowired
	private ExportarCarnetXML exportarCarnet;
	
	@Autowired 
	StayService stayService;
	
	@Autowired 
	StopService stopService;

	@Autowired
	private Session session;
	
	@Autowired
	private Alerts alert;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		helpBtn.setTooltip(new Tooltip("Pulsa F1 para ver la ayuda"));
		exportCarnetBtn.setTooltip(new Tooltip("Exportar carnet en XML"));
		exportReportBtn.setTooltip(new Tooltip("Exportar Informe del carnet"));
		logoutBtn.setTooltip(new Tooltip("Cerrar sesión"));
		User user = session.getLoggedInUser();
		System.out.println(user);
		loadUserProfile(user.getId());
	}
	
	@FXML
	private void showHelp() {
		HelpUtil.showHelp();
	}

	/**
	 * Cargar los datos del usuario y actualizarlos en la vista
	 */
	private void loadUserProfile(Long id) {
		Pilgrim pilgrim = pilgrimService.findByUserId(id);
		System.out.println(pilgrim);

		if (pilgrim != null) {
			Image avatar = new Image(getClass().getResourceAsStream("/images/pilgrim.png"));
			avatarImageView.setImage(avatar);
			lblName.setText(pilgrim.getName());
			lblNationality.setText("Nacionalidad: " + pilgrim.getNationality());
			lblDoExp.setText("Fecha Expedición: " + pilgrim.getCarnet().getDoExp().toString());
			lblDistance.setText("Distancia total: " + Double.valueOf(pilgrim.getCarnet().getDistance()).toString());
			lnlnVips.setText("Nº VIPS: " + Integer.valueOf(pilgrim.getCarnet().getnVips()).toString());
			lblInitialStop.setText("Parada Inicial: " + pilgrim.getCarnet().getInitialStop().getName());
		} else {
			alert.error("Error", "No se ha encontrado el peregrino", new StringBuilder("No se ha encontrado el peregrino"));
		}
	}

	/**
	 * Exportar los datos del carnet
	 */
	@FXML
	private void exportCarnet() {
		exportarCarnet = new ExportarCarnetXML(stayService, stopService);
		try {
			exportarCarnet.exportarCarnet(pilgrimService.findByUserId(session.getLoggedInUser().getId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void exportCarnetReport() {
	    try {
	        Pilgrim pilgrim = pilgrimService.findByUserId(session.getLoggedInUser().getId());
	        String outputPath = "src/main/resources/reports/paradas/" + pilgrim.getName() + "-carnet.pdf";

	        InputStream reportStream = getClass().getResourceAsStream("/templates/report/ReportCarnet.jasper");
	        if (reportStream == null) {
	            throw new JRException("No se pudo encontrar el archivo Report.jasper en resources/templates/report.");
	        }

	        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

	        Map<String, Object> params = new HashMap<>();
	        params.put("titulo", "Carnet" + pilgrim.getName());

	        URL imageUrl = getClass().getResource("/images/carnet.png");
	        if (imageUrl == null) {
	            throw new JRException("No se pudo encontrar la imagen carnet.png en resources/images.");
	        }

	        params.put("imagen", imageUrl.toExternalForm());

	        params.put("nombre", pilgrim.getName());
	        params.put("nacionalidad", pilgrim.getNationality());
	        params.put("fecha_expedicion", pilgrim.getCarnet().getDoExp().toString());
	        params.put("distancia", String.valueOf(pilgrim.getCarnet().getDistance()));
	        params.put("n_vips", String.valueOf(pilgrim.getCarnet().getnVips()));
	        params.put("nombre_parada", pilgrim.getCarnet().getInitialStop().getName());
	        params.put("region", pilgrim.getCarnet().getInitialStop().getRegion());

	        // Log de los parámetros
	        System.out.println("Parámetros del reporte:");
	        params.forEach((key, value) -> System.out.println(key + ": " + value));

	        JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
	        JasperExportManager.exportReportToPdfFile(print, outputPath);

	        // Mostrar el PDF en una ventana modal
	        ShowPDFInModal showPDFInModal = new ShowPDFInModal();
	       showPDFInModal.showPdfInModal(outputPath);
	    } catch (JRException e) {
	        e.printStackTrace();
	        // Manejar la excepción adecuadamente
	    }
	}

	@FXML
	private void logout(ActionEvent event) throws IOException {
		alert.logout();
	}

}
