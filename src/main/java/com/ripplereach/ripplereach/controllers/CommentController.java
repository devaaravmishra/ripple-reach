package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.CommentRequest;
import com.ripplereach.ripplereach.dtos.CommentResponse;
import com.ripplereach.ripplereach.dtos.CommentUpdateRequest;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Comment;
import com.ripplereach.ripplereach.services.CommentService;
import com.ripplereach.ripplereach.utilities.SortValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@Tag(name = "Comment", description = "The Comment API. Contains all the operations that can be performed on comments.")
public class CommentController {
    private final CommentService commentService;
    private final Mapper<Comment, CommentResponse> commentResponseMapper;
    public static final List<String> ALLOWED_SORT_PROPERTIES = Arrays.asList(
            "createdAt", "totalUpvotes"
    );

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Comment",
            description = "Creates a new comment.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.
                    RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CommentRequest.class)))
    )
    public ResponseEntity<CommentResponse>
    createComment(@Valid @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = commentService.createComment(
                commentRequest.getPostId(),
                commentRequest.getUserId(),
                commentRequest.getContent()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/{commentId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Comment",
            description = "Updates an existing comment by its ID.",
            parameters = @Parameter
                    (name = "commentId", description = "ID of the comment to update", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.
                    RequestBody(content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentUpdateRequest.class)))
    )
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) {
        CommentResponse comment = commentService.updateComment(commentId, commentUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("/{commentId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Delete Comment",
            description = "Deletes an existing comment by its ID.",
            parameters = @Parameter(name = "commentId", description = "ID of the comment to delete", required = true)
    )
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/posts/{postId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Get Comments by Post ID",
            description = "Retrieves all comments for a specific post by its ID.",
            parameters = @Parameter(name = "postId", description = "ID of the post to get comments for", required = true)
    )
    public ResponseEntity<Page<CommentResponse>> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "createdAt,desc") String sort_by) {
        List<Sort.Order> orders = SortValidator.validateSort(sort_by, ALLOWED_SORT_PROPERTIES);
        Pageable pageable = createPageRequestUsing(offset, limit, Sort.by(orders));

        Page<CommentResponse> comments = commentService.getCommentsByPostId(postId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @GetMapping("/users/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Get Comments by User ID",
            description = "Retrieves all comments by a specific user by their ID.",
            parameters = @Parameter(name = "userId", description = "ID of the user to get comments for", required = true)
    )
    public ResponseEntity<Page<CommentResponse>> getCommentsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "createdAt,desc") String sort_by) {
        List<Sort.Order> orders = SortValidator.validateSort(sort_by, ALLOWED_SORT_PROPERTIES);
        Pageable pageable = createPageRequestUsing(offset, limit, Sort.by(orders));

        Page<CommentResponse> comments = commentService.getCommentsByUserId(userId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    private Pageable createPageRequestUsing(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
