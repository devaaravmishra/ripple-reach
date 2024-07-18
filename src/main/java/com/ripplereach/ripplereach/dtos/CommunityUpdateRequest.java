package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.annotations.ValidFile;
import com.ripplereach.ripplereach.constants.Messages;
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
public class CommunityUpdateRequest {
  @Size(max = 100, min = 5, message = Messages.COMMUNITY_NAME_SIZE)
  private String name;

  @Size(max = 500, message = Messages.COMMUNITY_DESC_SIZE, min = 10)
  private String desc;

  @ValidFile(maxSize = 5048576) // 5MB size limit
  private MultipartFile image;
}
