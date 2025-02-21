package com.davidmb.tarea3ADbase.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.db.DB4oConnection;
import com.davidmb.tarea3ADbase.models.Service;
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
		} catch (Exception e) {
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

//	public boolean existsByName(String name) {
//		ObjectContainer db = DB4oConnection.getInstance().getDb();
//		try {
//			Query query = db.query();
//			query.constrain(Service.class);
//			query.descend("serviceName").constrain(name);
//			List<Service> result = query.execute();
//			return !result.isEmpty();
//		} finally {
//			db.close();
//		}
//	}
	
	public Service findByName(String name) {
	    ObjectContainer db = DB4oConnection.getInstance().getDb();
	    try {
	        Query query = db.query();
	        query.constrain(Service.class);
	        query.descend("serviceName").constrain(name);
	        List<Service> result = query.execute();
	        return result.isEmpty() ? null : result.get(0); 
	    } finally {
	        db.close();
	    }
	}

	
	public boolean checkDisponibility(String serviceName, Long stopId) {
	    ObjectContainer db = DB4oConnection.getInstance().getDb();
	    try {
	        Query query = db.query();
	        query.constrain(Service.class);
	        query.descend("serviceName").constrain(serviceName);
	        query.descend("stopIds").constrain(stopId).contains();

	        List<Service> result = query.execute();
	        return !result.isEmpty();
	    } catch (Exception e) {
	        e.printStackTrace(); 
	        return false;
	    }
	}


	public Service findById(Long id) {
	    ObjectContainer db = DB4oConnection.getInstance().getDb();
	    try {
	        Query query = db.query();
	        query.constrain(Service.class);
	        query.descend("id").constrain(id);
	        List<Service> result = query.execute();
	        return result.isEmpty() ? null : result.get(0); 
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
	    } finally {
	        db.close();
	    }
	}

	public List<Service> findByStopId(Long stopId) {
		ObjectContainer db = DB4oConnection.getInstance().getDb();

		Query query = db.query();
		query.constrain(Service.class);
		query.descend("stopIds").constrain(stopId);
		List<Service> result = query.execute();
		return result;

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
