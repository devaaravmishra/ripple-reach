package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Comment;
import com.ripplereach.ripplereach.models.PostAttachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private List<Comment> comments;
    private List<PostAttachment> attachments;
    private Instant createdAt;
    private Instant updatedAt;
}
