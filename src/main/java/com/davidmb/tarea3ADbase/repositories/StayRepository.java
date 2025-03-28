package com.davidmb.tarea3ADbase.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.Stay;


@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {

	List<Stay> findAllByPilgrim_Id(Long id);
	
	boolean existsByPilgrim_IdAndStop_IdAndDate(Long pilgrimId, Long stopId, LocalDate date);
	
	Stay findByPilgrim_IdAndStop_IdAndDate(Long pilgrimId, Long stopId, LocalDate date);
	
}
