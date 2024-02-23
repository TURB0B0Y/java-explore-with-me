package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDTO;
import ru.practicum.dto.category.CreateCategoryDTO;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createCategory(@RequestBody @Valid CreateCategoryDTO dto) {
        log.info("createCategory {}", dto);
        return categoryService.create(dto);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable  int categoryId) {
        log.info("deleteCategory {}", categoryId);
        categoryService.delete(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public CategoryDTO updateCategory(@PathVariable int categoryId, @RequestBody @Valid CreateCategoryDTO dto) {
        log.info("updateCategory {} {}", categoryId, dto);
        return categoryService.update(categoryId, dto);
    }

}
