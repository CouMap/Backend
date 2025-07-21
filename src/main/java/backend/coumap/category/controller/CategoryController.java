package backend.coumap.category.controller;

import backend.coumap.category.dto.CategoryRequest;
import backend.coumap.category.dto.CategoryResponse;
import backend.coumap.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 등록
     */
    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    /**
     * 카테고리 전체 조회
     */
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 카테고리 상세 조회
     */
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
}
