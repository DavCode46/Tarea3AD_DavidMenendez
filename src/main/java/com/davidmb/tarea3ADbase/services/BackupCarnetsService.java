package com.davidmb.tarea3ADbase.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davidmb.tarea3ADbase.models.BackupCarnets;
import com.davidmb.tarea3ADbase.models.BackupCarnets.CarnetBackupJson;
import com.davidmb.tarea3ADbase.models.Pilgrim;
import com.davidmb.tarea3ADbase.models.PilgrimStops;
import com.davidmb.tarea3ADbase.models.Stay;
import com.davidmb.tarea3ADbase.models.Stop;
import com.davidmb.tarea3ADbase.repositories.BackupCarnetsRepository;

@Service
public class BackupCarnetsService {

	@Autowired
	private BackupCarnetsRepository backupCarnetsRepository;
	
	@Autowired 
	private PilgrimService pilgrimService;
	
	@Autowired
	private StopService stopService;
	
	@Autowired
	private StayService stayService;
	
	private BackupCarnets saveBackup(BackupCarnets backup) {
		return backupCarnetsRepository.save(backup);
	}
	
	public List<BackupCarnets> getAllBackups() {
		return backupCarnetsRepository.findAll();
	}
	
	public boolean backupAllCarnets() {
	    List<Pilgrim> pilgrims = pilgrimService.findAll();
	    List<CarnetBackupJson> backupList = new ArrayList<>();
	    boolean ret = false;

	    for(Pilgrim p : pilgrims) {
	        try {
	            List<PilgrimStops> stopsList = stopService.findAllByPilgrimId(p.getId());
	            List<Stay> staysList = stayService.findAllByPilgrimId(p.getId());

	            CarnetBackupJson backup = new CarnetBackupJson();
	            backup.setPilgrimId(p.getId());
	            backup.setPilgrimName(p.getName());
	            backup.setPilgrimNationality(p.getNationality());
	            backup.setdOfIssue(p.getCarnet().getDoExp());
	            backup.setIssuedIn(p.getCarnet().getInitialStop().getName());
	            backup.setToday(LocalDate.now());
	            backup.setDistance(p.getCarnet().getDistance());
	            backup.setBackupDate(LocalDate.now());

	            List<CarnetBackupJson.StopJson> stops = stopsList.stream().map(s -> {
	                CarnetBackupJson.StopJson stop = new CarnetBackupJson.StopJson();
	                stop.setOrder(stopsList.indexOf(s) + 1);
	                stop.setName(s.getStop().getName());
	                stop.setRegion(s.getStop().getRegion());
	                return stop;
	            }).collect(Collectors.toList());
	            backup.setStops(stops);

	            List<CarnetBackupJson.StayJson> estanciasJson = staysList.stream().map(stay -> {
	                CarnetBackupJson.StayJson e = new CarnetBackupJson.StayJson();
	                e.setId(stay.getId());
	                e.setStayDate(stay.getDate());
	                Stop stop = stopService.find(stay.getStop().getId());
	                e.setStop(stop.getName());
	                e.setVip(stay.isVip());
	                return e;
	            }).collect(Collectors.toList());
	            backup.setStays(estanciasJson);

	            backupList.add(backup);
	        } catch(Exception ex) {
	            ex.printStackTrace();  
	        }
	    }

	    BackupCarnets backupContainer = new BackupCarnets();
	    backupContainer.setFileName("backupcarnets" + LocalDate.now().toString());
	    backupContainer.setCarnets(backupList);
   
	    saveBackup(backupContainer);
	    ret = true;

	    return ret;
	}

}
