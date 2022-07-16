package com.parkwoojin.musinsa.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryRequest {

    private String name;
    private Integer depth;
    private String code;
    private Long parentId;

}
