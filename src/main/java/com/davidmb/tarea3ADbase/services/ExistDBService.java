package com.davidmb.tarea3ADbase.services;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.dtos.CarnetDTO;
import com.davidmb.tarea3ADbase.repositories.ExistDBRepository;

@Service	
public class ExistDBService {

	@Autowired 
	private ExistDBRepository existDBRepository;
	
	public void storeCarnet(String stopName, File carnetFile) {
		existDBRepository.storeCarnet(stopName, carnetFile);
	}
	
	public List<CarnetDTO> getCarnetsDTOByStop(String stopName) {
		return existDBRepository.getCarnetsDTOByStop(stopName);
	}
}
