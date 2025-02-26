package com.davidmb.tarea3ADbase.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa una parada, en cada parada se almacena una lista de
 * peregrinos
 */
@Entity
@Table(name = "Paradas")
public class Stop implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre", nullable = false, length = 100)
	private String name;

	@Column(name = "region", nullable = false, length = 3)
	private String region;

	@Column(name = "responsable", nullable = false, length = 100)
	private String manager;

	@Column(name = "id_usuario", nullable = true)
	private Long userId;

	@OneToMany(mappedBy = "stop", cascade = { CascadeType.MERGE}, fetch = FetchType.EAGER)
	private List<PilgrimStops> pilgrimStops = new ArrayList<>();

	public Stop() {
		super();
	}

	public Stop(String name, String region, String manager) {
		super();
		this.name = name;
		this.region = region;
		this.manager = manager;
	}
	public Stop(String name, String region, String manager, Long userId) {
		super();
		this.name = name;
		this.region = region;
		this.manager = manager;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<PilgrimStops> getPilgrimStops() {
		return pilgrimStops;
	}

	public void setPilgrimStops(List<PilgrimStops> pilgrimStops) {
		this.pilgrimStops = pilgrimStops;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Stop stop = (Stop) obj;
	    return Objects.equals(id, stop.id); 
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}

	@Override
	public String toString() {
		return name + " - " + region + "\n";
	}

}
