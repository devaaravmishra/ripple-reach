package com.ripplereach.ripplereach.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
  private int status;
  private String type;
  private String title;
  private String message;
  private Map<String, String> errors;
  @Builder.Default private long timestamp = System.currentTimeMillis();
}
