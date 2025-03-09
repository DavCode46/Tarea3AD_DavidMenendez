package com.davidmb.tarea3ADbase.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;




public class Service {
	Long id;
	String serviceName;
	double price;
	
	
	List<Long> stopIds = new ArrayList<>();
	List<Long> contractedGroups = new ArrayList<>();
	
	public Service() {
	}
	
	public Service(String serviceName, double price, List<Long> stopIds) {
		this.serviceName = serviceName;
		this.price = price;
		this.stopIds = stopIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<Long> getStopIds() {
		return stopIds;
	}
	
	public String getStopsIds() {
	    if (stopIds == null || stopIds.isEmpty()) {
	        return "";
	    }
	    return String.join("-", stopIds.stream().map(String::valueOf).toArray(String[]::new));
	}


	public void setStopIds(List<Long> stopIds) {
		this.stopIds = stopIds;
	}
	
	public List<Long> getContractedGroups() {
		return contractedGroups;
	}
	
	public void setContractedGroups(List<Long> contractedGroups) {
		this.contractedGroups = contractedGroups;
	}
	


	@Override
	public int hashCode() {
		return Objects.hash(contractedGroups, id, price, serviceName, stopIds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		return Objects.equals(contractedGroups, other.contractedGroups) && Objects.equals(id, other.id)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(serviceName, other.serviceName) && Objects.equals(stopIds, other.stopIds);
	}

	@Override
	public String toString() {
		return "Servicio: " + "Nombre: " + serviceName + " - Precio: " + price;
	}
}
