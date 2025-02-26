package com.davidmb.tarea3ADbase.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa a un peregrino, cuando se crea un peregrino
 * también se crea un carnet con los datos de la parada donde se
 * está registrando el peregrino.
 */
@Entity
@Table(name = "Peregrinos")
public class Pilgrim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "nacionalidad", nullable = false, length = 100)
    private String nationality;

    @Column(name = "id_usuario", nullable = false)
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_carnet", referencedColumnName = "id", nullable = false)
    private Carnet carnet;

    @OneToMany(mappedBy = "pilgrim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stay> stays = new ArrayList<>();

    @OneToMany(mappedBy = "pilgrim", cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    private List<PilgrimStops> pilgrimStops = new ArrayList<>();

    public Pilgrim() {
    }

    public Pilgrim(String name, String nationality, Carnet carnet, Long userId) {
        this.name = name;
        this.nationality = nationality;
        this.carnet = carnet;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Carnet getCarnet() {
        return carnet;
    }

    public void setCarnet(Carnet carnet) {
        this.carnet = carnet;
    }

    public List<Stay> getStays() {
        return stays;
    }

    public void setStays(List<Stay> stays) {
        this.stays = stays;
    }

    public List<PilgrimStops> getPilgrimStops() {
        return pilgrimStops;
    }

    public void setPilgrimStops(List<PilgrimStops> pilgrimStops) {
        this.pilgrimStops = pilgrimStops;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nationality, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Pilgrim other = (Pilgrim) obj;
        return Objects.equals(id, other.id) &&
               Objects.equals(name, other.name) &&
               Objects.equals(nationality, other.nationality) &&
               Objects.equals(userId, other.userId);
    }


    @Override
    public String toString() {
        return "Peregrino: " + name + "\nNacionalidad: " + nationality + "\nCarnet: " + carnet;
    }

}
