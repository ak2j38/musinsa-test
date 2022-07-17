package com.parkwoojin.musinsa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.parkwoojin.musinsa.controller.dto.CategoryRequest;
import com.parkwoojin.musinsa.controller.dto.CategoryResponse;
import com.parkwoojin.musinsa.controller.dto.CategoryUpdate;
import com.parkwoojin.musinsa.exception.ExistSubCategoryException;
import com.parkwoojin.musinsa.repository.CategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql("classpath:sql/autoIncrementInit.sql")
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    CategoryRequest categoryRequest1;
    CategoryRequest categoryRequest2;
    CategoryRequest categoryRequest3;
    CategoryRequest categoryRequest4;

    @BeforeEach
    void setUp() {
        categoryRequest1 = new CategoryRequest("상의", 1, "001", null);
        categoryRequest2 = new CategoryRequest("하의", 1, "002", null);
        categoryRequest3 = new CategoryRequest("반소매 티셔츠", 2, "001001", 1L);
        categoryRequest4 = new CategoryRequest("청바지", 2, "002001", 2L);


        categoryService.save(categoryRequest1);
        categoryService.save(categoryRequest2);
        categoryService.save(categoryRequest3);
        categoryService.save(categoryRequest4);
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }


    @Test
    @DisplayName("findAll 메소드를 호출하면 저장된 모든 CategoryResponse 리스트를 리턴한다.")
    void findAllTest() {
        // when
        List<CategoryResponse> categories = categoryService.findAll();

        // then
        assertThat(categories.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("code와 depth가 중복되지 않는 데이터로 저장 시도를 하면 저장 성공한다.")
    void saveSuccessTest() {
        // given
        CategoryRequest saveCategory = new CategoryRequest("긴팔 티셔츠", 2, "001002", 1L);

        // when
        CategoryResponse savedCategory = categoryService.save(saveCategory);

        // then
        assertAll(
            () -> assertThat(saveCategory.getCode()).isEqualTo(savedCategory.getCode()),
            () -> assertThat(saveCategory.getName()).isEqualTo(savedCategory.getName())
        );
    }

    @Test
    @DisplayName("code와 depth가 중복되는 데이터로 저장 시도를 하면 IllegalArgumentException을 던진다.")
    void saveFailTest() {
        // given
        CategoryRequest saveCategory = new CategoryRequest("반팔 티셔츠", 2, "001001", 1L);

        // when
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> categoryService.save(saveCategory));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 코드입니다. 확인 후 다시 시도해주세요.");
    }

    @Test
    @DisplayName("존재하지 않는 Id로 카테고리 업데이트 시도 하면 NoSuchElementException 예외를 던진다.")
    void updateFailTest() {
        // given
        Long updateId = -1L;
        String updateName = "반팔 티셔츠";
        CategoryUpdate categoryUpdate = new CategoryUpdate(updateName);

        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
            () -> categoryService.update(updateId, categoryUpdate));

        // then
        assertThat(e.getClass()).isEqualTo(NoSuchElementException.class);
    }

    @Test
    @DisplayName("존재하는 Id로 카테고리 업데이트 시도 하면 name이 업데이트 된다.")
    void updateSuccessTest() {
        // given
        Long updateId = 3L;
        String updateName = "반팔 티셔츠";
        CategoryUpdate categoryUpdate = new CategoryUpdate(updateName);

        // when
        CategoryResponse updatedCategory = categoryService.update(updateId, categoryUpdate);

        // then
        assertThat(updatedCategory.getName()).isEqualTo(updateName);
    }

    @Test
    @DisplayName("존재하지 않는 Id로 카테고리 삭제 시도를 하면 NoSuchElementException 예외를 던진다.")
    void deleteFailTest1() {
        // given
        Long findId = -1L;

        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
            () -> categoryService.delete(findId));

        // then
        assertThat(e.getClass()).isEqualTo(NoSuchElementException.class);
    }

    @Test
    @DisplayName("하위에 카테고리가 존재하는 카테고리를 삭제 시도 하면 ExistSubCategoryException 예외를 던진다.")
    void deleteFailTest2() {
        // given
        Long findId = 1L;

        // when
        ExistSubCategoryException e = assertThrows(ExistSubCategoryException.class,
            () -> categoryService.delete(findId));

        // then
        assertThat(e.getMessage()).isEqualTo("하위에 카테고리가 존재합니다. 먼저 삭제해주세요.");
    }

    @Test
    @DisplayName("존재하는 Id로 카테고리를 조회하면 해당하는 카테고리를 리턴한다.")
    void findByIdSuccessTest() {
        // given
        Long findId = 1L;

        // when
        CategoryResponse findCategory = categoryService.findById(findId);

        // then
        assertThat(findCategory.getName()).isEqualTo("상의");
    }

    @Test
    @DisplayName("존재하지 않는 Id로 카테고리를 조회하면 NoSuchElementException 예외가 발생한다.")
    void findByIdFailTest() {
        // given
        Long findId = -1L;

        // when
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
            () -> categoryService.findById(findId));

        // then
        assertThat(e.getClass()).isEqualTo(NoSuchElementException.class);
    }
}
