package com.davidmb.tarea3ADbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Carnet;


@Repository
public interface CarnetRepository extends JpaRepository<Carnet, Long> {

	
}
