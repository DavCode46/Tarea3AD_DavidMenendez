package com.davidmb.tarea3ADbase.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.BackupCarnets;

@Repository
public interface BackupCarnetsRepository extends MongoRepository<BackupCarnets, String> {

}
