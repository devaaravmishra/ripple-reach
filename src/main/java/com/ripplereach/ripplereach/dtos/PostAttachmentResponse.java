package com.ripplereach.ripplereach.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ripplereach.ripplereach.serializers.ImageUrlSerializer;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostAttachmentResponse {
  private Long id;
  private String fileName;

  @JsonSerialize(using = ImageUrlSerializer.class)
  private String url;

  private Instant createdAt;
}
