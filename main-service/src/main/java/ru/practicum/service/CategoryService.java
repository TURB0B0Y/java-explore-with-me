package ru.practicum.service;

import ru.practicum.dto.category.CategoryDTO;
import ru.practicum.dto.category.CreateCategoryDTO;
import ru.practicum.filter.PageFilter;

import java.util.List;

public interface CategoryService {

    CategoryDTO create(CreateCategoryDTO dto);

    void delete(int categoryId);

    CategoryDTO update(int categoryId, CreateCategoryDTO dto);

    List<CategoryDTO> getAll(PageFilter pageFilter);

    CategoryDTO getById(int categoryId);

}
