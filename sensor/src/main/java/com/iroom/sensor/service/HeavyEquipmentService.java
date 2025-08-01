package com.iroom.sensor.service;

import com.iroom.sensor.dto.HeavyEquipment.EquipmentUpdateLocationRequest;
import com.iroom.sensor.dto.HeavyEquipment.EquipmentUpdateLocationResponse;
import com.iroom.sensor.dto.HeavyEquipment.EquipmentRegisterRequest;
import com.iroom.sensor.dto.HeavyEquipment.EquipmentRegisterResponse;
import com.iroom.sensor.entity.HeavyEquipment;
import com.iroom.sensor.repository.HeavyEquipmentRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HeavyEquipmentService {

	private final HeavyEquipmentRepository heavyEquipmentRepository;

	//등록 기능
	public EquipmentRegisterResponse register(EquipmentRegisterRequest request) {
		HeavyEquipment equipment = HeavyEquipment.builder()
			.name(request.name())
			.type(request.type())
			.radius(request.radius())
			.build();

		HeavyEquipment saved = heavyEquipmentRepository.save(equipment);
		return new EquipmentRegisterResponse(saved);
	}

	//위치 업데이트 기능
	public EquipmentUpdateLocationResponse updateLocation(EquipmentUpdateLocationRequest request) {
		HeavyEquipment equipment = heavyEquipmentRepository.findById(request.id())
			.orElseThrow(() -> new EntityNotFoundException("장비 없음 "));

		equipment.updateLocation(request.latitude(), request.longitude());

		return new EquipmentUpdateLocationResponse(equipment.getId(), equipment.getLatitude(),
			equipment.getLongitude());
	}

}
