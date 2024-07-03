package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.models.Community;
import com.ripplereach.ripplereach.models.PostAttachment;
import com.ripplereach.ripplereach.models.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse extends RepresentationModel<PostResponse> {
    private Long id;
    private String title;
    private String content;
    private User author;
    private Community community;
    private List<PostAttachment> attachments;
    private Long totalUpvotes;
    private Long totalComments;
    private String link;
    private Instant createdAt;
    private Instant updatedAt;
}
