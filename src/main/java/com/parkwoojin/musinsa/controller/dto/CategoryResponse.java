package com.parkwoojin.musinsa.controller.dto;

import com.parkwoojin.musinsa.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryResponse {

    private Long id;
    private String name;
    private String code;
    private Integer depth;
    private Category parent;
    private List<Category> children = new ArrayList<>();

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getCode(), category.getDepth(),
            category.getParentCategory(), category.getSubCategories());
    }
}
