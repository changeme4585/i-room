package com.iroom.sensor.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WorkerHealth_table")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerHealth {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private Long workerId;

	private Double latitude;
	private Double longitude;

	private Integer heartRate;
	private Float bodyTemperature;

	@Builder
	public WorkerHealth(Long workerId) {
		this.workerId = workerId;
	}

	//위치 업데이트
	public void updateLocation(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	//생체 정보 업데이트
	public void updateVitalSign(Integer heartRate, Float bodyTemperature) {
		this.heartRate = heartRate;
		this.bodyTemperature = bodyTemperature;
	}

}
