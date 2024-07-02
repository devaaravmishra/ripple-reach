package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.CategoryRequest;
import com.ripplereach.ripplereach.dtos.CategoryResponse;
import com.ripplereach.ripplereach.dtos.CategoryUpdateRequest;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Category;
import com.ripplereach.ripplereach.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "The Category API. Contains all the operations that can be performed on a category.")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final Mapper<Category, CategoryResponse> categoryResponseMapper;

    @GetMapping
    @Operation(
            summary = "Get All Categories",
            description = "Retrieves all categories."
    )
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Category> categories = categoryService.findAll(pageable);

        Page<CategoryResponse> response = categories
                .map(categoryResponseMapper::mapTo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @Operation(
            summary = "Get Category by ID",
            description = "Retrieves the category by its ID."
    )
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);

        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .category(category)
                .build();

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Category",
            description = "Creates a new category."
    )
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.create(
                categoryRequest.getName(),
                categoryRequest.getDescription());

        CategoryResponse categoryResponse = categoryResponseMapper.mapTo(category);

        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Category",
            description = "Updates an existing category by its ID."
    )
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.update(
                categoryId,
                categoryRequest.getName(),
                categoryRequest.getDescription());

        CategoryResponse categoryResponse = categoryResponseMapper.mapTo(category);

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PatchMapping("/{categoryId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Partial Update Category",
            description = "Partially updates an existing category by its ID."
    )
    public ResponseEntity<CategoryResponse> partialUpdateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest) {

        Category category = categoryService.partialUpdate(
                categoryId,
                categoryUpdateRequest.getName(),
                categoryUpdateRequest.getDescription());

        CategoryResponse categoryResponse = categoryResponseMapper.mapTo(category);

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Delete Category",
            description = "Deletes an existing category by its ID."
    )
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
