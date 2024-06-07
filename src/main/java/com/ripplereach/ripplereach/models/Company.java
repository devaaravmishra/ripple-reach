package com.ripplereach.ripplereach.models;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  private Instant createdAt;
  private Instant updatedAt;
}
