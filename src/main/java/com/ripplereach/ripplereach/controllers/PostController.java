package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.PostRequestDto;
import com.ripplereach.ripplereach.dtos.PostResponseDto;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@RequestMapping("/api/posts")
@AllArgsConstructor
@Tag(name = "Post", description = "The Post API. Contains all the operations that can be performed on a post.")
public class PostController {
    private final PostService postService;
    private final Mapper<Post, PostResponseDto> postResponseMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Post",
            description = "Creates a new post.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object", implementation = PostRequestDto.class)))
    )
    public ResponseEntity<PostResponseDto> createPost(@Valid @ModelAttribute PostRequestDto postRequestDto) {
        Post post = postService.create(postRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseMapper.mapTo(post));
    }

    @GetMapping
    @Operation(
            summary = "Get All Posts",
            description = "Retrieves all posts."
    )
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<Post> posts = postService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(posts.stream().map(postResponseMapper::mapTo)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{postId}")
    @Operation(
            summary = "Get Post by ID",
            description = "Retrieves the post by its ID.",
            parameters = @Parameter(name = "postId", description = "ID of the post to be retrieved", required = true)
    )
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseMapper.mapTo(post));
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Post",
            description = "Updates an existing post by its ID.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object", implementation = PostRequestDto.class))),
            parameters = @Parameter(name = "postId", description = "ID of the post to be updated", required = true)
    )
    public ResponseEntity<PostResponseDto>
    updatePost(@PathVariable Long postId, @Valid @ModelAttribute PostRequestDto postRequestDto) {
        Post post = postService.update(postId, postRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseMapper.mapTo(post));
    }

    @DeleteMapping("/{postId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Delete Post",
            description = "Deletes an existing post by its ID.",
            parameters = @Parameter(name = "postId", description = "ID of the post to be deleted", required = true)
    )
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
