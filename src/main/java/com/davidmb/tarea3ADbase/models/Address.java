package com.davidmb.tarea3ADbase.models;

import java.util.Objects;

import jakarta.persistence.Embeddable;


@Embeddable
public class Address {
	
	String street;
	String locality;

	public Address() {
	}

	public Address(String street, String locality) {
		this.street = street;
		this.locality = locality;
	}


	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Override
	public int hashCode() {
		return Objects.hash(locality, street);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(locality, other.locality) && Objects.equals(street, other.street);
	}

	@Override
	public String toString() {
		return "Dirección: " + street + ", " + locality;
	}


}
