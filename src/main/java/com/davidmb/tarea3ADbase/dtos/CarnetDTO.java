package com.davidmb.tarea3ADbase.dtos;

import java.time.LocalDate;

public class CarnetDTO {
	private Long id;
	private LocalDate dateExpedition;
	private String issuedIn;
	private String pilgrimName;
	private String pilgrimNationality;
	private LocalDate today;
	private double totalDistance;


	public CarnetDTO() {
		super();
	}
	
	public CarnetDTO(Long id, LocalDate dateExpedition, String issuedIn, String pilgrimName, String pilgrimNationality,
			LocalDate today, double totalDistance) {
		super();
		this.id = id;
		this.dateExpedition = dateExpedition;
		this.issuedIn = issuedIn;
		this.pilgrimName = pilgrimName;
		this.pilgrimNationality = pilgrimNationality;
		this.today = today;
		this.totalDistance = totalDistance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateExpedition() {
		return dateExpedition;
	}

	public void setDateExpedition(LocalDate dateExpedition) {
		this.dateExpedition = dateExpedition;
	}

	public String getIssuedIn() {
		return issuedIn;
	}

	public void setIssuedIn(String issuedIn) {
		this.issuedIn = issuedIn;
	}

	public String getPilgrimName() {
		return pilgrimName;
	}

	public void setPilgrimName(String pilgrimName) {
		this.pilgrimName = pilgrimName;
	}

	public String getPilgrimNationality() {
		return pilgrimNationality;
	}

	public void setPilgrimNationality(String pilgrimNationality) {
		this.pilgrimNationality = pilgrimNationality;
	}

	public LocalDate getToday() {
		return today;
	}

	public void setToday(LocalDate today) {
		this.today = today;
	}

	public double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}
}
