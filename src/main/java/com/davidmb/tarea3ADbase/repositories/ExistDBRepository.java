package com.davidmb.tarea3ADbase.repositories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import com.davidmb.tarea3ADbase.db.ExistDBConnection;
import com.davidmb.tarea3ADbase.dtos.CarnetDTO;
import com.davidmb.tarea3ADbase.utils.ParserXMLToCarnet;

@Repository
public class ExistDBRepository {

	@Autowired
	private ExistDBConnection existDBConnection;
	
	
	  public void storeCarnet(String stopName, File carnetFile) {
	        try {
	            String stopCollectionPath = existDBConnection.getURI() + "/tarea5_ad/" + stopName;
	            Collection stopCollection = DatabaseManager.getCollection(stopCollectionPath, existDBConnection.getUSER(), existDBConnection.getPASSWORD());

	            if (stopCollection == null) {
	                existDBConnection.createStopCollection(stopName);
	                stopCollection = DatabaseManager.getCollection(stopCollectionPath, existDBConnection.getUSER(), existDBConnection.getPASSWORD());
	            }
	            
	            String formattedCarnetName = carnetFile.getName().replaceAll(" ","_");

	            XMLResource resource = (XMLResource) stopCollection.createResource(formattedCarnetName, "XMLResource");
	            resource.setContent(carnetFile);
	            stopCollection.storeResource(resource);

	            System.out.println("Carnet almacenado en ExistDB para la parada " + stopName + ": " + formattedCarnetName);
	        } catch (XMLDBException e) {
	            e.printStackTrace();
	        }
	    }

	    
	    public List<CarnetDTO> getCarnetsDTOByStop(String stopName) {
	        List<CarnetDTO> carnetList = new ArrayList<>();
	        try {
	            String stopCollectionPath = existDBConnection.getURI() + "/tarea5_ad/" + stopName;
	            Collection stopCollection = DatabaseManager.getCollection(stopCollectionPath, existDBConnection.getUSER(), existDBConnection.getPASSWORD());
	            if (stopCollection == null) {
	                System.out.println("No existe la colección para la parada: " + stopName);
	                return carnetList;
	            }
	            String[] resources = stopCollection.listResources();
	            System.out.println("Carnets almacenados en la parada " + stopName + ":");
	            for (String resourceName : resources) {
	                System.out.println(resourceName);
	                XMLResource resource = (XMLResource) stopCollection.getResource(resourceName);
	                if (resource != null) {           
	                    String content = (String) resource.getContent();                
	                    CarnetDTO dto = ParserXMLToCarnet.parse(content);
	                    if (dto != null) {
	                        carnetList.add(dto);
	                    }
	                }
	            }
	        } catch (XMLDBException e) {
	            e.printStackTrace();
	        }
	        return carnetList;
	    }

}
