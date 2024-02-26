package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDTO;
import ru.practicum.dto.category.CreateCategoryDTO;
import ru.practicum.exception.NotFoundException;
import ru.practicum.filter.PageFilter;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDTO create(CreateCategoryDTO dto) {
        Category newCategory = new Category();
        newCategory.setName(dto.getName());
        return CategoryMapper.toDto(categoryRepository.save(newCategory));
    }

    @Override
    @Transactional
    public void delete(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public CategoryDTO update(int categoryId, CreateCategoryDTO dto) {
        Category fromDb = getCategoryById(categoryId);
        fromDb.setName(dto.getName());
        return CategoryMapper.toDto(
                categoryRepository.save(fromDb)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAll(PageFilter pageFilter) {
        Pageable pageable = PageRequest.of(pageFilter.getFrom() / pageFilter.getSize(), pageFilter.getSize());
        return categoryRepository.findAll(pageable).getContent().stream()
                .map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getById(int categoryId) {
        Category category = getCategoryById(categoryId);
        return CategoryMapper.toDto(category);
    }

    private Category getCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=%s was not found", categoryId));
    }

}
