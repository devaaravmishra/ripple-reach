package com.ripplereach.ripplereach.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {
    private int status;
    private String type;
    private String title;
    private String message;
    private Map<String, String> errors;
    @Builder.Default
    private long timestamp = System.currentTimeMillis();
}
