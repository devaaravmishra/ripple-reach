package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.models.University;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String phone;
    private University university;
    private Company company;
    private String profession;
    private Boolean isVerified;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
