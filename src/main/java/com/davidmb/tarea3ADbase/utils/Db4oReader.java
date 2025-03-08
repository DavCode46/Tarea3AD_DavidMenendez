package com.davidmb.tarea3ADbase.utils;

import com.davidmb.tarea3ADbase.models.ContractedGroup;
import com.davidmb.tarea3ADbase.models.Service;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class Db4oReader {
	  public static void main(String[] args) {
	        ObjectContainer db = Db4o.openFile("db.db4o");
	        
	        try {
	            System.out.println("Contenido de la base de datos:");
	            ObjectSet<Object> servicesResult = db.queryByExample(Service.class);
	            System.out.println("-".repeat(50));
	            System.out.println("Servicios");
	            while (servicesResult.hasNext()) {
	                System.out.println(servicesResult.next());
	            }
	            
	            
	            ObjectSet<Object> groupResult = db.queryByExample(ContractedGroup.class);
	            System.out.println("-".repeat(50));
	            System.out.println("Conjunto contratado");
	            while (groupResult.hasNext()) {
	                System.out.println(groupResult.next());
	            }
	        } finally {
	            db.close();
	        }
	    }
}
