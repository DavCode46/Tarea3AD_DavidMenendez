package com.davidmb.tarea3ADbase.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Service;
import com.davidmb.tarea3ADbasedb.DB4oConnection;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;

@Repository
public class ServicesRepository {
	public void save(Service service) {
	    DB4oConnection connection = DB4oConnection.getInstance();
	    ObjectContainer db = connection.getDb();
	    
	    try {
	        db.store(service);
	        db.commit();  
	    } catch (Exception e) {
	        db.rollback();  
	        e.printStackTrace();
	    } finally {
	        db.close(); 
	    }
	}
	
	public void update(Service service) {
		DB4oConnection connection = DB4oConnection.getInstance();
		ObjectContainer db = connection.getDb();
		
		try {
			Query query = db.query();
			query.constrain(Service.class);
			query.descend("id").constrain(service.getId());
			List<Service> result = query.execute();
			
			if (!result.isEmpty()) {
                Service serviceToUpdate = result.get(0);
                serviceToUpdate.setServiceName(service.getServiceName());
                serviceToUpdate.setPrice(service.getPrice());
                serviceToUpdate.setStopIds(service.getStopIds());
                db.store(serviceToUpdate);
                db.commit();
            } 
		} catch(Exception e) {
            db.rollback();
            e.printStackTrace();
        } finally {
            db.close();
        }
	}

	public List<Service> findAll() {
		ObjectContainer db = DB4oConnection.getInstance().getDb();
		Query query = db.query();
		query.constrain(Service.class);
		return query.execute();		 
	}
	
	public boolean findByName(String name) {
		ObjectContainer db = DB4oConnection.getInstance().getDb();
		try {
			Query query = db.query();
			query.constrain(Service.class);
			query.descend("serviceName").constrain(name);
			List<Service> result = query.execute();
			return !result.isEmpty();
		} finally {
			db.close();
		}
	}
	
	public Long getMaxId() {
		ObjectContainer db = DB4oConnection.getInstance().getDb();
		
		try {
			Query query = db.query();
			query.constrain(Service.class);
			query.descend("id").orderDescending();
			List<Service> result = query.execute();

			if (result.isEmpty()) {
				return 0L;
			}

			return result.get(0).getId();
		} finally {
			db.close();
		}
	}
}
