package com.ripplereach.ripplereach.controllers;

import com.ripplereach.ripplereach.dtos.*;
import com.ripplereach.ripplereach.mappers.Mapper;
import com.ripplereach.ripplereach.models.Category;
import com.ripplereach.ripplereach.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "The Category API. Contains all the operations that can be performed on a category.")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final Mapper<Category, CategoryResponseDto> categoryResponseMapper;

    @GetMapping
    @Operation(
            summary = "Get All Categories",
            description = "Retrieves all categories."
    )
    public ResponseEntity<GetAllCategoryResponseDto> getAllCategories() {
        List<Category> categoryList = categoryService.getAll();

        GetAllCategoryResponseDto getAllCategoryResponseDto = GetAllCategoryResponseDto
                .builder()
                .categories(categoryList)
                .build();

        return new ResponseEntity<>(getAllCategoryResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @Operation(
            summary = "Get Category by ID",
            description = "Retrieves the category by its ID."
    )
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);

        CategoryResponseDto categoryResponseDto = CategoryResponseDto
                .builder()
                .category(category)
                .build();

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Create Category",
            description = "Creates a new category."
    )
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        Category category = categoryService.create(
                categoryRequestDto.getName(),
                categoryRequestDto.getDescription());

        CategoryResponseDto categoryResponseDto = categoryResponseMapper.mapTo(category);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Update Category",
            description = "Updates an existing category by its ID."
    )
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        Category category = categoryService.update(
                categoryId,
                categoryRequestDto.getName(),
                categoryRequestDto.getDescription());

        CategoryResponseDto categoryResponseDto = categoryResponseMapper.mapTo(category);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{categoryId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Partial Update Category",
            description = "Partially updates an existing category by its ID."
    )
    public ResponseEntity<CategoryResponseDto> partialUpdateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto) {

        Category category = categoryService.partialUpdate(
                categoryId,
                categoryUpdateRequestDto.getName(),
                categoryUpdateRequestDto.getDescription());

        CategoryResponseDto categoryResponseDto = categoryResponseMapper.mapTo(category);

        return new ResponseEntity<>(categoryResponseDto, HttpStatus.OK);
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
