package com.example.Dailydone.Mapper;

import com.example.Dailydone.DTO.CategoryDTO;
import com.example.Dailydone.Entity.Category;

public class CategoryMapper {
    public static CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    public static Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());

        return category;
    }
}
