package com.ripplereach.ripplereach.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
  @NotNull private Long postId;

  @NotNull private Long userId;

  @NotBlank
  @Size(max = 500)
  private String content;
}
