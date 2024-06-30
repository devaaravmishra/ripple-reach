package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.CommentRequestDto;
import com.ripplereach.ripplereach.dtos.CommentResponseDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Comment;
import com.ripplereach.ripplereach.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@Tag(name = "Comment", description = "The Comment API. Contains all the operations that can be performed on comments.")
public class CommentController {
    private final CommentService commentService;
    private final Mapper<Comment, CommentResponseDto> commentResponseMapper;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Comment",
            description = "Creates a new comment.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CommentRequestDto.class)))
    )
    public ResponseEntity<CommentResponseDto>
    createComment(@Valid @RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = commentService.createComment(
                commentRequestDto.getPostId(),
                commentRequestDto.getUserId(),
                commentRequestDto.getContent()
        );

        CommentResponseDto commentResponseDto = commentResponseMapper.mapTo(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @PutMapping("/{commentId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Comment",
            description = "Updates an existing comment by its ID.",
            parameters = @Parameter(name = "commentId", description = "ID of the comment to update", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    )
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody String content) {
        Comment comment = commentService.updateComment(commentId, content);

        CommentResponseDto commentResponseDto = commentResponseMapper.mapTo(comment);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
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

    @GetMapping("/post/{postId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Get Comments by Post ID",
            description = "Retrieves all comments for a specific post by its ID.",
            parameters = @Parameter(name = "postId", description = "ID of the post to get comments for", required = true)
    )
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);

        List<CommentResponseDto> response = comments.stream()
                .map(commentResponseMapper::mapTo)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Get Comments by User ID",
            description = "Retrieves all comments by a specific user by their ID.",
            parameters = @Parameter(name = "userId", description = "ID of the user to get comments for", required = true)
    )
    public ResponseEntity<List<CommentResponseDto>> getCommentsByUserId(@PathVariable Long userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);

        List<CommentResponseDto> response = comments.stream()
                .map(commentResponseMapper::mapTo)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
