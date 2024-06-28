package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.services.UpvoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/upvotes")
@AllArgsConstructor
@Tag(name = "Upvote", description = "The Upvote API. Contains all the operations that can be performed on upvotes.")
@Slf4j
public class UpvoteController {
    private final UpvoteService upvoteService;

    @PostMapping("/posts/{postId}/users/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Upvote Post",
            description = "Upvotes a post by the specified user.",
            parameters = {
                    @Parameter(name = "postId", description = "ID of the post to upvote", required = true),
                    @Parameter(name = "userId", description = "ID of the user upvoting the post", required = true)
            }
    )
    public ResponseEntity<String> upvotePost(@PathVariable Long postId, @PathVariable Long userId) {
        upvoteService.upvotePost(postId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("Post upvoted successfully.");
    }

    @DeleteMapping("/posts/{postId}/users/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Remove Upvote from Post",
            description = "Removes an upvote from a post by the specified user.",
            parameters = {
                    @Parameter(name = "postId", description = "ID of the post to remove upvote from", required = true),
                    @Parameter(name = "userId", description = "ID of the user removing the upvote", required = true)
            }
    )
    public ResponseEntity<String> removeUpvoteFromPost(@PathVariable Long postId, @PathVariable Long userId) {
        upvoteService.removeUpvoteFromPost(postId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("Upvote removed from post successfully.");
    }

    @PostMapping("/comments/{commentId}/users/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Upvote Comment",
            description = "Upvotes a comment by the specified user.",
            parameters = {
                    @Parameter(name = "commentId", description = "ID of the comment to upvote", required = true),
                    @Parameter(name = "userId", description = "ID of the user upvoting the comment", required = true)
            }
    )
    public ResponseEntity<String> upvoteComment(@PathVariable Long commentId, @PathVariable Long userId) {
        upvoteService.upvoteComment(commentId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("Comment upvoted successfully.");
    }

    @DeleteMapping("/comments/{commentId}/users/{userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Remove Upvote from Comment",
            description = "Removes an upvote from a comment by the specified user.",
            parameters = {
                    @Parameter(name = "commentId", description = "ID of the comment to remove upvote from", required = true),
                    @Parameter(name = "userId", description = "ID of the user removing the upvote", required = true)
            }
    )
    public ResponseEntity<String> removeUpvoteFromComment(@PathVariable Long commentId, @PathVariable Long userId) {
        upvoteService.removeUpvoteFromComment(commentId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("Upvote removed from comment successfully.");
    }
}
