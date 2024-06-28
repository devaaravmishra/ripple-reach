package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Long authorId;

    private List<MultipartFile> attachments;

    @Pattern(regexp = "^(https?://)?([\\\\da-z.-]+)\\\\.([a-z.]{2,6})([/\\\\w .-]*)*/?$",
            message = Messages.INVALID_URL)
    private String link;
}
