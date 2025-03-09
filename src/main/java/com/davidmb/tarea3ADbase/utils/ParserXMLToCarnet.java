package com.davidmb.tarea3ADbase.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.davidmb.tarea3ADbase.dtos.CarnetDTO;

@Component
public class ParserXMLToCarnet {
	
	
	 public static CarnetDTO parse(String xmlContent) {
	        try {
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            InputStream is = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
	            Document doc = builder.parse(is);
	            doc.getDocumentElement().normalize();
	            
	           
	            String idStr = doc.getElementsByTagName("id").item(0).getTextContent();
	            Long id = Long.parseLong(idStr);

	            String fechaExpStr = doc.getElementsByTagName("fechaexp").item(0).getTextContent();
	            LocalDate dateExpedition = LocalDate.parse(fechaExpStr); 

	            String issuedIn = doc.getElementsByTagName("expedidoen").item(0).getTextContent();

	        
	            NodeList pilgrimNodes = doc.getElementsByTagName("peregrino");
	            String pilgrimName = "";
	            String pilgrimNationality = "";
	            if (pilgrimNodes.getLength() > 0) {
	                Element pilgrimElem = (Element) pilgrimNodes.item(0);
	                pilgrimName = pilgrimElem.getElementsByTagName("nombre").item(0).getTextContent();
	                pilgrimNationality = pilgrimElem.getElementsByTagName("nacionalidad").item(0).getTextContent();
	            }

	            String todayStr = doc.getElementsByTagName("hoy").item(0).getTextContent();
	            LocalDate today = LocalDate.parse(todayStr);

	            String distStr = doc.getElementsByTagName("distanciatotal").item(0).getTextContent();	     
	            distStr = distStr.replace(',', '.');
	            double totalDistance = Double.parseDouble(distStr);

	     
	            CarnetDTO carnetDTO = new CarnetDTO();
	            carnetDTO.setId(id);
	            carnetDTO.setDateExpedition(dateExpedition);
	            carnetDTO.setIssuedIn(issuedIn);
	            carnetDTO.setPilgrimName(pilgrimName);
	            carnetDTO.setPilgrimNationality(pilgrimNationality);
	            carnetDTO.setToday(today);
	            carnetDTO.setTotalDistance(totalDistance);
	            
	            return carnetDTO;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
