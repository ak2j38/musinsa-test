package com.parkwoojin.musinsa.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String code;
    private Integer depth;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parentCategory;
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.PERSIST)
    private List<Category> subCategories = new ArrayList<>();

    private Category(String name, String code, Integer depth, Category parent) {
        this.id = null;
        this.name = name;
        this.code = code;
        this.depth = depth;
        this.parentCategory = parent;
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
    }

    public static Category of(String name, String code, Integer depth, Category parent) {
        return new Category(name, code, depth, parent);
    }

    public void addSubCategory(Category child) {
        this.subCategories.add(child);
    }

    public void updateName(String name) {
        if (name != null) {
            this.name = name;
            this.setUpdatedAt(LocalDateTime.now());
        }
    }
}
