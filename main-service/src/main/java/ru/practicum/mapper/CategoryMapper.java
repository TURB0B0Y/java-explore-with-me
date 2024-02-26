package ru.practicum.mapper;

import ru.practicum.dto.category.CategoryDTO;
import ru.practicum.model.Category;

public class CategoryMapper {

    public static CategoryDTO toDto(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

}
