package com.ripplereach.ripplereach.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ripplereach.ripplereach.models.Company;
import com.ripplereach.ripplereach.models.Role;
import com.ripplereach.ripplereach.models.University;
import com.ripplereach.ripplereach.serializers.ImageUrlSerializer;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private Long id;
  private String username;
  private String phone;
  private University university;
  private Company company;
  private String profession;
  private Boolean isVerified;

  @JsonSerialize(using = ImageUrlSerializer.class)
  private String avatar;

  private Set<Role> roles;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;
}
