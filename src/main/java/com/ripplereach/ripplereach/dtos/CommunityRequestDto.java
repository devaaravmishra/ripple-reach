package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.annotations.ValidFile;
import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityRequestDto {
    @NotBlank(message = Messages.COMMUNITY_NAME_REQUIRED)
    @Size(max = 100, message = Messages.COMMUNITY_NAME_SIZE)
    private String name;

    @NotBlank(message = Messages.COMMUNITY_DESC_REQUIRED)
    @Size(max = 500, message = Messages.COMMUNITY_DESC_SIZE, min = 10)
    private String desc;

    @ValidFile(message = Messages.COMMUNITY_IMAGE_REQUIRED, maxSize = 5048576) // 5MB size limit
    private MultipartFile image;

    @NotNull(message = Messages.COMMUNITY_ID_REQUIRED)
    private Long categoryId;
}
