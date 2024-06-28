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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
@Tag(name = "Community", description = "The Community API. Contains all the operations that can be performed on a community.")
@AllArgsConstructor
public class CommunityController {
    private final CommunityService communityService;
    private final Mapper<Community, CommunityResponseDto> communityResponseMapper;

    @GetMapping("/{communityId}")
    @Operation(
            summary = "Get Community by ID",
            description = "Retrieves the community by its ID."
    )
    public ResponseEntity<CommunityResponseDto> getCommunity(@PathVariable Long communityId) {
      Community community = communityService.findById(communityId);

      CommunityResponseDto communityResponseDto = communityResponseMapper.mapTo(community);

      return ResponseEntity.status(HttpStatus.OK).body(communityResponseDto);
    }

    @GetMapping
    @Operation(
            summary = "Get All Communities",
            description = "Retrieves all communities."
    )
    public ResponseEntity<GetAllCommunityResponseDto> getAllCommunities() {
        List<Community> communities = communityService.getAll();

        GetAllCommunityResponseDto getAllCommunityResponseDto = GetAllCommunityResponseDto
                .builder()
                .communities(communities)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(getAllCommunityResponseDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Community",
            description = "Creates a new community.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CommunityRequestDto.class)))
    )
    public ResponseEntity<CommunityResponseDto> createCommunity
            (@Valid @ModelAttribute CommunityRequestDto communityRequestDto) {
        Community community = communityService.create(communityRequestDto);

        CommunityResponseDto communityResponseDto = communityResponseMapper.mapTo(community);

        return ResponseEntity.status(HttpStatus.CREATED).body(communityResponseDto);
    }

    @PutMapping("/{communityId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Community",
            description = "Updates an existing community by its ID.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CommunityRequestDto.class)))
    )
    public ResponseEntity<CommunityResponseDto> updateCommunity
            (@PathVariable Long communityId, @Valid @ModelAttribute CommunityRequestDto communityRequestDto) {
        Community community = communityService.update(communityId, communityRequestDto);

        CommunityResponseDto communityResponseDto = communityResponseMapper.mapTo(community);

        return ResponseEntity.status(HttpStatus.OK).body(communityResponseDto);
    }

    @PatchMapping("/{communityId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Partial Update Community",
            description = "Partially updates an existing community by its ID.",
            requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = CommunityUpdateRequestDto.class)))
    )
    public ResponseEntity<CommunityResponseDto> partialUpdateCommunity
            (@PathVariable Long communityId, @Valid @ModelAttribute CommunityUpdateRequestDto communityRequestDto) {
        Community community = communityService.partialUpdate(communityId, communityRequestDto);

        CommunityResponseDto communityResponseDto = communityResponseMapper.mapTo(community);

        return ResponseEntity.status(HttpStatus.OK).body(communityResponseDto);
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
