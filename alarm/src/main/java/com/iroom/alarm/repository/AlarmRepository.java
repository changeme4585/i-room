package com.iroom.alarm.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.iroom.alarm.entity.Alarm;

@RepositoryRestResource(exported = false)
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

	// 근로자용 알림 조회
	List<Alarm> findByWorkerIdOrderByOccuredAtDesc(Long workerId);

	// 관리자용 알림 조회
	List<Alarm> findByOccuredAtAfterOrderByOccuredAtDesc(LocalDateTime time);
}
