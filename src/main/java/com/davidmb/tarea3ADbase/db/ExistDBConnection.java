package com.davidmb.tarea3ADbase.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

public class ExistDBConnection {

	  private static final String URI;
	    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
	    private static final String USER;
	    private static final String PASSWORD;
	    private Collection collection = null;
	    
	    static {
	     
	        try (InputStream input = ExistDBConnection.class.getClassLoader().getResourceAsStream("existdb.properties")) {
	            if (input == null) {
	                throw new IOException("Archivo de propiedades no encontrado: existdb.properties");
	            }

	            Properties properties = new Properties();
	            properties.load(input);

	            URI = properties.getProperty("existdb.uri");
	            USER = properties.getProperty("existdb.user");
	            PASSWORD = properties.getProperty("existdb.password");

	        } catch (IOException e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error al cargar la configuración de ExistDB");
	        }
	    }

	    public ExistDBConnection() {
	        try {
	          
	            Class<?> cl = Class.forName(DRIVER);
	            Database database = (Database) cl.getDeclaredConstructor().newInstance();
	            DatabaseManager.registerDatabase(database);

	          
	            collection = DatabaseManager.getCollection(URI, USER, PASSWORD);
	            if (collection == null) {
	                createCollection("tarea5_ad");
	                collection = DatabaseManager.getCollection(URI, USER, PASSWORD);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public void createCollection(String collectionPath) {
	        try {
	            String fullPath = URI + collectionPath;
	            Collection collection = DatabaseManager.getCollection(fullPath, USER, PASSWORD);

	            if (collection == null) {
	                Collection parent = DatabaseManager.getCollection(URI + collectionPath.substring(0, collectionPath.lastIndexOf('/')), USER, PASSWORD);
	                if (parent != null) {
	                    CollectionManagementService mgtService = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
	                    mgtService.createCollection(collectionPath.substring(collectionPath.lastIndexOf('/') + 1));
	                    System.out.println("Colección creada: " + collectionPath);
	                } else {
	                    System.out.println("No se pudo crear la colección, la ruta padre no existe: " + collectionPath);
	                }
	            } else {
	                System.out.println("La colección ya existe: " + collectionPath);
	            }
	        } catch (XMLDBException e) {
	            e.printStackTrace();
	        }
	    }

	  
	    public void createStopCollection(String stopName) {
	        createCollection("/tarea5_ad/" + stopName);
	    }
	    
	    public void storeCarnet(String stopName, String carnetXML, String filename) {
	        try {
	            String stopCollectionPath = URI + "/" + stopName;
	            Collection stopCollection = DatabaseManager.getCollection(stopCollectionPath, USER, PASSWORD);

	            if (stopCollection == null) {
	                createStopCollection(stopName);
	                stopCollection = DatabaseManager.getCollection(stopCollectionPath, USER, PASSWORD);
	            }

	         
	            File file = new File(filename);
	            FileWriter writer = new FileWriter(file);
	            writer.write(carnetXML);
	            writer.close();

	            XMLResource resource = (XMLResource) stopCollection.createResource(file.getName(), "XMLResource");
	            resource.setContent(file);
	            stopCollection.storeResource(resource);

	            System.out.println("Carnet almacenado en ExistDB para la parada " + stopName + ": " + filename);
	            file.delete(); 
	        } catch (XMLDBException | IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public void listCarnetsByStop(String stopName) {
	        try {
	            String stopCollectionPath = URI + "/" + stopName;
	            Collection stopCollection = DatabaseManager.getCollection(stopCollectionPath, USER, PASSWORD);

	            if (stopCollection == null) {
	                System.out.println("No existe la colección para la parada: " + stopName);
	                return;
	            }

	            String[] resources = stopCollection.listResources();
	            System.out.println("Carnets almacenados en la parada " + stopName + ":");
	            for (String resource : resources) {
	                System.out.println(resource);
	            }
	        } catch (XMLDBException e) {
	            e.printStackTrace();
	        }
	    }

	   
	    public void close() {
	        try {
	            if (collection != null) {
	                collection.close();
	            }
	        } catch (XMLDBException e) {
	            e.printStackTrace();
	        }
	    }
}
