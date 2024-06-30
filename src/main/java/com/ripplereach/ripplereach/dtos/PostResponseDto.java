package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.PostAttachment;
import com.ripplereach.ripplereach.models.Upvote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private List<PostAttachment> attachments;
    private Long totalUpvotes;
    private Set<Upvote> upvotes = new HashSet<>();
    private Long totalComments;
    private String link;
    private Instant createdAt;
    private Instant updatedAt;
}
