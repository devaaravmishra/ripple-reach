package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityResponseDto {
    private Community community;
}
