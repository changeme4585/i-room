package com.iroom.user.worker.dto.request;

import com.iroom.user.common.annotation.ValidPassword;
import com.iroom.user.common.annotation.ValidPhone;
import com.iroom.user.worker.entity.Worker;
import com.iroom.modulecommon.enums.BloodType;
import com.iroom.modulecommon.enums.Gender;
import com.iroom.modulecommon.enums.WorkerRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

public record WorkerRegisterRequest(
        @NotBlank
        @Size(min = 2, max = 20)
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @ValidPassword
        String password,

        @NotBlank
        @ValidPhone
        String phone,

        BloodType bloodType,
        Gender gender,
        Integer age,
        Float weight,
        Float height,
        String jobTitle,
        String occupation,
        String department,
        String faceImageUrl
) {
    public Worker toEntity(PasswordEncoder encoder) {
        return Worker.builder()
                .name(this.name)
                .email(this.email)
                .password(encoder.encode(this.password))
                .phone(this.phone)
                .role(WorkerRole.WORKER)
                .bloodType(this.bloodType)
                .gender(this.gender)
                .age(this.age)
                .weight(this.weight)
                .height(this.height)
                .jobTitle(this.jobTitle)
                .occupation(this.occupation)
                .department(this.department)
                .faceImageUrl(this.faceImageUrl)
                .build();
    }
}
