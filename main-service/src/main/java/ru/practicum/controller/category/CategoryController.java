package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDTO;
import ru.practicum.filter.PageFilter;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getCategories(@Valid PageFilter pageFilter) {
        log.info("getCategories {}", pageFilter);
        return categoryService.getAll(pageFilter);
    }

    @GetMapping("/{categoryId}")
    public CategoryDTO getCategories(@PathVariable int categoryId) {
        log.info("getCategories {}", categoryId);
        return categoryService.getById(categoryId);
    }

}
