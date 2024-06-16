package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Community;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommunityResponseDto {
    private List<Community> communities;
}
