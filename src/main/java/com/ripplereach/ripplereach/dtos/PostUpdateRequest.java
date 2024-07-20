package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest {
  private String title;
  private String content;
  private List<MultipartFile> attachments;

  @Pattern(
      regexp =
          "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
      message = Messages.INVALID_URL)
  private String link;
}
