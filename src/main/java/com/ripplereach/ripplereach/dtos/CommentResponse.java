package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Upvote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private PostResponse post;
    private UserResponse author;
    private Long totalUpvotes;
    private Set<Upvote> upvotes = new HashSet<>();
    private Instant createdAt;
    private Instant updatedAt;
}
