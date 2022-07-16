package com.parkwoojin.musinsa.service;

import com.parkwoojin.musinsa.controller.dto.CategoryRequest;
import com.parkwoojin.musinsa.controller.dto.CategoryResponse;
import com.parkwoojin.musinsa.controller.dto.CategoryUpdate;
import com.parkwoojin.musinsa.entity.Category;
import com.parkwoojin.musinsa.exception.ExistSubCategoryException;
import com.parkwoojin.musinsa.repository.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
            .map(CategoryResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public CategoryResponse save(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByCodeAndDepth(categoryRequest.getCode(), categoryRequest.getDepth())) {
            throw new IllegalArgumentException("이미 존재하는 코드입니다. 확인 후 다시 시도해주세요.");
        }

        Category parentCategory = null;
        int saveDepth = 1;

        if (categoryRequest.getParentId() != null) {
            parentCategory = categoryRepository.findById(categoryRequest.getParentId())
                .orElseThrow(NoSuchElementException::new);
            saveDepth = parentCategory.getDepth() + 1;
        }

        Category savedCategory = categoryRepository.save(Category.of(categoryRequest.getName(),
            categoryRequest.getCode(), saveDepth, parentCategory));

        if (parentCategory != null) {
            parentCategory.addSubCategory(savedCategory);
        }

        return CategoryResponse.from(savedCategory);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryUpdate categoryUpdate) {
        Category findCategory = categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);

        findCategory.updateName(categoryUpdate.getName());

        return CategoryResponse.from(findCategory);
    }

    @Transactional
    public void delete(Long id) {
        Category findCategory = categoryRepository.findById(id).orElseGet(null);
        if (findCategory == null) {
            throw new NoSuchElementException("해당하는 카테고리가 없습니다.");
        }

        if (findCategory.getSubCategories().isEmpty()) {
            throw new ExistSubCategoryException("하위에 카테고리가 존재합니다. 먼저 삭제해주세요.");
        }

        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        Category findCategory = categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);

        return CategoryResponse.from(findCategory);
    }
}
