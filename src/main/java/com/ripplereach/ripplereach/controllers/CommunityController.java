package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Community;
import com.ripplereach.ripplereach.services.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/communities")
@Tag(name = "Community", description = "The Community API. Contains all the operations that can be performed on a community.")
@AllArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final Mapper<Community, CommunityResponse> communityResponseMapper;

    @GetMapping("/{communityId}")
    @Operation(
            summary = "Get Community by ID",
            description = "Retrieves the community by its ID."
    )
    public ResponseEntity<CommunityResponse> getCommunity(@PathVariable Long communityId) {
      Community community = communityService.findById(communityId);

      CommunityResponse communityResponse = communityResponseMapper.mapTo(community);

      return ResponseEntity.status(HttpStatus.OK).body(communityResponse);
    }

    @GetMapping
    @Operation(
            summary = "Retrieves All Communities",
            description = "Get all communities."
    )
    public ResponseEntity<Page<CommunityResponse>> getAllCommunities(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Community> communities = communityService.findAll(pageable);

        Page<CommunityResponse> communityResponse = communities
                .map(communityResponseMapper::mapTo);

        return ResponseEntity.status(HttpStatus.OK).body(communityResponse);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Community",
            description = "Creates a new community.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CommunityRequest.class)))
    )
    public ResponseEntity<CommunityResponse> createCommunity
            (@Valid @ModelAttribute CommunityRequest communityRequest) {
        Community community = communityService.create(communityRequest);

        CommunityResponse communityResponse = communityResponseMapper.mapTo(community);

        return ResponseEntity.status(HttpStatus.CREATED).body(communityResponse);
    }

    @PutMapping("/{communityId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Community",
            description = "Updates an existing community by its ID.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CommunityRequest.class)))
    )
    public ResponseEntity<CommunityResponse> updateCommunity
            (@PathVariable Long communityId, @Valid @ModelAttribute CommunityRequest communityRequest) {
        Community community = communityService.update(communityId, communityRequest);

        CommunityResponse communityResponse = communityResponseMapper.mapTo(community);

        return ResponseEntity.status(HttpStatus.OK).body(communityResponse);
    }

    @PatchMapping("/{communityId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Partial Update Community",
            description = "Partially updates an existing community by its ID.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CommunityUpdateRequest.class)))
    )
    public ResponseEntity<CommunityResponse> partialUpdateCommunity
            (@PathVariable Long communityId, @Valid @ModelAttribute CommunityUpdateRequest communityRequestDto) {
        Community community = communityService.partialUpdate(communityId, communityRequestDto);

        CommunityResponse communityResponse = communityResponseMapper.mapTo(community);

        return ResponseEntity.status(HttpStatus.OK).body(communityResponse);
    }

    @DeleteMapping("/{communityId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Delete Community",
            description = "Deletes an existing community by its ID."
    )
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long communityId) {
        communityService.delete(communityId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
