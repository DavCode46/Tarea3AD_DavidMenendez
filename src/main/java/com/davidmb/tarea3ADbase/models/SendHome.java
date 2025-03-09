package com.davidmb.tarea3ADbase.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SendHome extends Service implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	double weight;
	int[] volume = new int[3];
	boolean urgent = false;
	Long stopId;;

	@Embedded
	Address address;

	public SendHome() {
	}

	public SendHome(double weight, int[] volume, boolean urgent, Address address, Long stopId) {
		this.weight = weight;
		this.volume = volume;
		this.urgent = urgent;
		this.address = address;
		this.stopId = stopId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int[] getVolume() {
		return volume;
	}

	public void setVolume(int[] volume) {
		this.volume = volume;
	}

	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Long getStopId() {
		return stopId;
	}

	public void setStopId(Long stopId) {
		this.stopId = stopId;
	}

	public String getAddressStreet() {
		return address.getStreet();
	}

	public String getAddressLocality() {
		return address.getLocality();
	}

	public String getVolumeFormatted() {
		return volume[0] + "x" + volume[1] + "x" + volume[2];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(volume);
		result = prime * result + Objects.hash(address, id, stopId, urgent, weight);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SendHome other = (SendHome) obj;
		return Objects.equals(address, other.address) && Objects.equals(id, other.id)
				&& Objects.equals(stopId, other.stopId) && urgent == other.urgent && Arrays.equals(volume, other.volume)
				&& Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight);
	}

	@Override
	public String toString() {
		return "Envío a casa: " + "\nPeso: " + weight + "Medidas: " + volume[0] + "x" + volume[1] + "x" + volume[2] + "\nUrgente" + urgent;
	}

}
