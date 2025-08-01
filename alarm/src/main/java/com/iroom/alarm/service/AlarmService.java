package com.iroom.alarm.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iroom.alarm.config.StompHandler;
import com.iroom.alarm.entity.Alarm;
import com.iroom.alarm.repository.AlarmRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {

	private final AlarmRepository alarmRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final StompHandler stompHandler;

	// 알림을 생성해서 저장(Kafka 이벤트 수신 시 사용)
	public void handleAlarmEvent(Long workerId, String type, Long incidentId, String description) {
		Alarm alarm = Alarm.builder()
			.workerId(workerId)
			.incidentId(incidentId)
			.incidentType(type)
			.incidentDescription(description)
			.build();

		alarmRepository.save(alarm);

		// WebSocket 실시간 전송
		String adminMessage = String.format("[%s] %s (작업자 ID: %d)", type, description, workerId);
		String workerMessage = String.format("[%s] %s", type, description);

		// 관리자에게 모든 알람 전송
		messagingTemplate.convertAndSend("/topic/alarms/admin", adminMessage);

		// 해당 근로자에게만 개별 알람 전송
		String sessionId = stompHandler.getSessionIdByUserId(workerId.toString());
		if (sessionId != null) {
			String workerDestination = "/queue/alarms-" + sessionId;
			messagingTemplate.convertAndSend(workerDestination, workerMessage);
		}
	}

	// 근로자의 알림 목록을 조회
	// 페이지네이션 적용 필요
	@PreAuthorize("hasAuthority('ROLE_WORKER') and #workerId == authentication.principal")
	public List<Alarm> getAlarmsForWorker(Long workerId) {
		return alarmRepository.findByWorkerIdOrderByOccuredAtDesc(workerId);
	}

	// 관리자용 전체 알림 목록을 조회
	// 페이지네이션 적용 필요
	@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN', 'ROLE_ADMIN', 'ROLE_READER')")
	public List<Alarm> getAlarmsForAdmin() {
		LocalDateTime time = LocalDateTime.now().minusHours(3);
		return alarmRepository.findByOccuredAtAfterOrderByOccuredAtDesc(time);
	}
}
