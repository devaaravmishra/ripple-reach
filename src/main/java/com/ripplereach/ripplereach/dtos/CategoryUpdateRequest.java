package com.ripplereach.ripplereach.dtos;

import com.ripplereach.ripplereach.constants.Messages;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {
    @Size(message = Messages.CATEGORY_NAME_SIZE, min = 3, max = 25)
    private String name;

    @Size(message = Messages.CATEGORY_DESC_SIZE, min = 10, max = 500)
    private String description;
}
