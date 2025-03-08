package com.davidmb.tarea3ADbase.utils;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.PilgrimStops;
import com.davidmb.tarea3ADbase.models.Stay;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.services.StayService;
import com.davidmb.tarea3ADbase.services.StopService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * La clase ExportarCarnetXML se encarga de generar un archivo XML que contiene
 * los datos de un objeto Peregrino, incluyendo su carnet, paradas y estancias.
 * El archivo se exporta a una ubicación en el sistema de archivos, y notifica
 * al usuario al finalizar.
 */
@Component
public class ExportarCarnetXML {

	private StayService stayService;

	private StopService stopService;

	public ExportarCarnetXML(StayService stayService, StopService stopService) {
		this.stayService = stayService;
		this.stopService = stopService;

	}

	/**
	 * Exporta los datos de un peregrino a un archivo XML en el sistema de archivos.
	 * El archivo contiene la información del carnet, las paradas y las estancias
	 * asociadas al peregrino.
	 *
	 * @param peregrino El objeto Peregrino que se exportará en formato XML.
	 * @throws Exception Si ocurre un error durante la creación del archivo XML.
	 */
	public void exportarCarnet(Pilgrim pilgrim) throws Exception {

		List<PilgrimStops> stopsList = stopService.findAllByPilgrimId(pilgrim.getId());

		List<Stay> staysList = stayService.findAllByPilgrimId(pilgrim.getId());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		// ------------------- Crear el elemento carnet ----------------
		Element carnetElem = doc.createElement("carnet");
		doc.appendChild(carnetElem);

		// ------------------- Crear los elementos hijos de carnet ----------------

		// ID
		Element idElem = doc.createElement("id");
		idElem.setTextContent(String.valueOf(pilgrim.getId()));
		carnetElem.appendChild(idElem);

		// Fecha de expedición
		Element doExpElem = doc.createElement("fechaexp");
		doExpElem.setTextContent(pilgrim.getCarnet().getDoExp().toString());
		carnetElem.appendChild(doExpElem);

		// Parada de expedición
		Element issuedElem = doc.createElement("expedidoen");
		issuedElem.setTextContent(pilgrim.getCarnet().getInitialStop().getName());
		carnetElem.appendChild(issuedElem);

		// Elemento Peregrino
		Element pilgrimElem = doc.createElement("peregrino");

		// Nombre
		Element nameElem = doc.createElement("nombre");
		nameElem.setTextContent(pilgrim.getName());

		// Nacionalidad
		Element nationalityElem = doc.createElement("nacionalidad");
		nationalityElem.setTextContent(pilgrim.getNationality());
		pilgrimElem.appendChild(nameElem);
		pilgrimElem.appendChild(nationalityElem);
		carnetElem.appendChild(pilgrimElem);

		// Fecha actual
		Element nowElem = doc.createElement("hoy");
		nowElem.setTextContent(LocalDate.now().toString());
		carnetElem.appendChild(nowElem);

		// Distancia total
		Element distanceElem = doc.createElement("distanciatotal");
		distanceElem.setTextContent(String.format("%.1f", pilgrim.getCarnet().getDistance()));
		carnetElem.appendChild(distanceElem);

		Element paradasElem = doc.createElement("paradas");
		for (int i = 0; i < stopsList.size(); i++) {
			// Parada parada = peregrino.getParadas().get(i);
			Element stopElem = doc.createElement("parada");
			Element order = doc.createElement("orden");
			order.setTextContent(String.valueOf(i + 1));
			stopElem.appendChild(order);
			Element stopNameElem = doc.createElement("nombre");
			stopNameElem.setTextContent(stopsList.get(i).getStop().getName());
			stopElem.appendChild(stopNameElem);
			Element region = doc.createElement("region");
			region.setTextContent(String.valueOf(stopsList.get(i).getStop().getRegion()));
			stopElem.appendChild(region);
			paradasElem.appendChild(stopElem);
		}
		carnetElem.appendChild(paradasElem);

		Element staysElem = doc.createElement("estancias");
		for (Stay stay : staysList) {
			Stop stop = stopService.find(stay.getStop().getId());
			Element stayElem = doc.createElement("estancia");

			Element stayIdElem = doc.createElement("id");
			stayIdElem.setTextContent(String.valueOf(stay.getId()));
			stayElem.appendChild(stayIdElem);

			Element dateElem = doc.createElement("fecha");
			dateElem.setTextContent(stay.getDate().toString());
			stayElem.appendChild(dateElem);

			Element stopElem = doc.createElement("parada");
			stopElem.setTextContent(stop.getName());
			stayElem.appendChild(stopElem);

			if (stay.isVip()) {
				Element vip = doc.createElement("vip");
				vip.setTextContent("Sí");
				stayElem.appendChild(vip);
			}

			staysElem.appendChild(stayElem);
		}
		carnetElem.appendChild(staysElem);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(new File("exports/pilgrims/carnet_" + pilgrim.getName() + ".xml"));
		transformer.transform(source, result);

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Exportar carnet");
		alert.setHeaderText(null);
		  Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		    alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/success.png")));
		alert.setContentText("El carnet de " + pilgrim.getName() + " ha sido exportado correctamente.");
		alert.showAndWait();
	}
}
