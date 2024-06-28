package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private Post post;
    private User author;
    private Instant createdAt;
    private Instant updatedAt;
}
