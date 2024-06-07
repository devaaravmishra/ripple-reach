package com.ripplereach.ripplereach.models;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String phone;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "university_id", referencedColumnName = "id")
  private University university;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "company_id", referencedColumnName = "id")
  private Company company;

  private String profession;
  private Boolean isVerified;

  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;
}
