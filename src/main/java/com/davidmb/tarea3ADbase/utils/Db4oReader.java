package com.davidmb.tarea3ADbase.utils;

import java.time.LocalDate;

import com.davidmb.tarea3ADbase.models.ContractedGroup;
import com.davidmb.tarea3ADbase.models.Service;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

public class Db4oReader {
	  public static void main(String[] args) {
		  // Recomendado en versiones más recientes de DB4O 
		  EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
	      ObjectContainer db = Db4oEmbedded.openFile(config, "db.db4o");
	      LocalDate fecha = LocalDate.now();
	      LocalDate fecha2 = LocalDate.of(2025, 03, 19);
	      System.out.println(fecha.compareTo(fecha2));
	        try {
	            System.out.println("Contenido de la base de datos:");
	            ObjectSet<Object> servicesResult = db.queryByExample(Service.class);
	            System.out.println("-".repeat(50));
	            System.out.println("------------------- Servicios --------------------");
	            System.out.println("-".repeat(50));
	            while (servicesResult.hasNext()) {
	                System.out.println(servicesResult.next());
	            }
	            
	            
	            ObjectSet<Object> groupResult = db.queryByExample(ContractedGroup.class);
	            System.out.println("-".repeat(50));
	            System.out.println("-------------- Conjunto contratado ---------------");
	            System.out.println("-".repeat(50));
	            while (groupResult.hasNext()) {
	                System.out.println(groupResult.next());
	            }
	        } finally {
	            db.close();
	        }
	    }
}
