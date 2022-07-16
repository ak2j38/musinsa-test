package com.parkwoojin.musinsa.controller;

import com.parkwoojin.musinsa.controller.dto.CategoryRequest;
import com.parkwoojin.musinsa.controller.dto.CategoryResponse;
import com.parkwoojin.musinsa.controller.dto.CategoryUpdate;
import com.parkwoojin.musinsa.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PostMapping("/categories")
    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {
        return categoryService.save(categoryRequest);
    }

    @PutMapping("/categories/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, CategoryUpdate categoryUpdate) {
        return categoryService.update(id, categoryUpdate);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.ok().build();
    }
}

