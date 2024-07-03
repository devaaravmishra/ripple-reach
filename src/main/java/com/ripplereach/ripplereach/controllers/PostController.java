package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.PostRequest;
import com.ripplereach.ripplereach.dtos.PostResponse;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Post;
import com.ripplereach.ripplereach.services.PostService;
import com.ripplereach.ripplereach.utilities.SortValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@RequestMapping("/api/posts")
@AllArgsConstructor
@Tag(name = "Post", description = "The Post API. Contains all the operations that can be performed on a post.")
public class PostController {
    private final PostService postService;
    private final Mapper<Post, PostResponse> postResponseMapper;
    public static final List<String> ALLOWED_SORT_PROPERTIES = Arrays.asList(
            "createdAt", "totalUpvotes", "title"
    );

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Post",
            description = "Creates a new post.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object", implementation = PostRequest.class)))
    )
    public ResponseEntity<PostResponse> createPost(@Valid @ModelAttribute PostRequest postRequest) {
        Post post = postService.create(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseMapper.mapTo(post));
    }

    @GetMapping
    @Operation(
            summary = "Get All Posts",
            description = "Retrieves all posts."
    )
    public ResponseEntity<Page<PostResponse>> getAllPosts(@RequestParam(defaultValue = "10") Integer limit,
                                                          @RequestParam(defaultValue = "0") Integer offset,
                                                          @RequestParam(defaultValue = "createdAt,desc") String sort_by) {

        List<Sort.Order> orders = SortValidator.validateSort(sort_by, ALLOWED_SORT_PROPERTIES);
        Pageable pageable = createPageRequestUsing(offset, limit, Sort.by(orders));

        Page<Post> posts = postService.findAll(pageable);
        Page<PostResponse> postsResponse = posts.map(postResponseMapper::mapTo);

        return ResponseEntity.status(HttpStatus.OK)
                .body(postsResponse);
    }

    @GetMapping("/{postId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Get Post by ID",
            description = "Retrieves the post by its ID.",
            parameters = @Parameter(name = "postId", description = "ID of the post to be retrieved", required = true)
    )
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseMapper.mapTo(post));
    }

    @GetMapping("/community/{communityId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Get Posts by community",
            description = "Retrieves the post by its community ID.",
            parameters = @Parameter(name = "postId", description = "ID of the community whose posts needs to be retrieved", required = true)
    )
    public ResponseEntity<Page<PostResponse>> getPostsByCommunity(@PathVariable Long communityId,
                                                                  @RequestParam(defaultValue= "10") Integer limit,
                                                                  @RequestParam(defaultValue = "0") Integer offset,
                                                                  @RequestParam(defaultValue = "createdAt,desc") String sort_by) {
        List<Sort.Order> orders = SortValidator.validateSort(sort_by, ALLOWED_SORT_PROPERTIES);
        Pageable pageable = createPageRequestUsing(offset, limit, Sort.by(orders));

        Page<Post> posts = postService.findAllByCommunity(communityId, pageable);
        Page<PostResponse> postResponses = posts.map(postResponseMapper::mapTo);

        return ResponseEntity.status(HttpStatus.OK)
                .body(postResponses);
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Post",
            description = "Updates an existing post by its ID.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "object", implementation = PostRequest.class))),
            parameters = @Parameter(name = "postId", description = "ID of the post to be updated", required = true)
    )
    public ResponseEntity<PostResponse>
    updatePost(@PathVariable Long postId, @Valid @ModelAttribute PostRequest postRequest) {
        Post post = postService.update(postId, postRequest);
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

    private Pageable createPageRequestUsing(int page, int size, Sort sortBy) {
        return PageRequest.of(page, size, sortBy);
    }
}
