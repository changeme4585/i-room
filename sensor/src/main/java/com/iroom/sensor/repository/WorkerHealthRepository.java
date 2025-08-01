package com.iroom.sensor.repository;

import com.iroom.sensor.entity.WorkerHealth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface WorkerHealthRepository extends JpaRepository<WorkerHealth, Long> {
	Optional<WorkerHealth> findByWorkerId(Long workerId);
}
