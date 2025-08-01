package com.iroom.modulecommon.dto.event;

import java.time.LocalDateTime;

import com.iroom.modulecommon.enums.BloodType;
import com.iroom.modulecommon.enums.Gender;
import com.iroom.modulecommon.enums.WorkerRole;

public record WorkerEvent(
	Long id,
	String name,
	String email,
	String phone,
	WorkerRole role,
	BloodType bloodType,
	Gender gender,
	Integer age,
	Float weight,
	Float height,
	String jobTitle,
	String occupation,
	String department,
	String faceImageUrl,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}