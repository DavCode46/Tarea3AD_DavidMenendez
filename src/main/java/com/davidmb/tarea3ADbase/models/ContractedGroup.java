package com.davidmb.tarea3ADbase.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContractedGroup {
	Long id;
	double totalPrice;
	char payMode;
	String extra = null;
	Long stayId;
	List<Long> serviceIds = new ArrayList<>();

	public ContractedGroup() {
	}

	public ContractedGroup(double totalPrice, char payMode, Long stayId, List<Long> serviceIds) {
		this.totalPrice = totalPrice;
		this.payMode = payMode;
		this.stayId = stayId;
		this.serviceIds = serviceIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public char getPayMode() {
		return payMode;
	}

	public void setPayMode(char payMode) {
		this.payMode = payMode;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Long getStayId() {
		return stayId;
	}

	public void setStayId(Long stayId) {
		this.stayId = stayId;
	}

	public List<Long> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(List<Long> serviceIds) {
		this.serviceIds = serviceIds;
	}

	@Override
	public int hashCode() {
		return Objects.hash(extra, id, payMode, serviceIds, stayId, totalPrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContractedGroup other = (ContractedGroup) obj;
		return Objects.equals(extra, other.extra) && Objects.equals(id, other.id) && payMode == other.payMode
				&& Objects.equals(serviceIds, other.serviceIds) && Objects.equals(stayId, other.stayId)
				&& Double.doubleToLongBits(totalPrice) == Double.doubleToLongBits(other.totalPrice);
	}

	@Override
	public String toString() {
		String payModeStr = "";
		switch (payMode) {
			case 'E' -> payModeStr = "Efectivo";
			case 'B' -> payModeStr = "Bizum";
			case 'T' -> payModeStr = "Tarjeta";
		}

		return "Grupo contratado: " + "precio total: " + totalPrice + ", modo de pago: " + payModeStr
                + (extra != null ? ", extra: " + extra : "") + ", id de estancia: " + stayId + ", ids de servicios: "
                + serviceIds;
	}

}
