package backend.coumap.category.service;

import backend.coumap.category.domain.Category;
import backend.coumap.category.dto.CategoryRequest;
import backend.coumap.category.dto.CategoryResponse;
import backend.coumap.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 등록
     */
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("이미 존재하는 업종 코드입니다.");
        }
        Category category = Category.builder()
                .code(request.getCode())
                .name(request.getName())
                .build();

        Category saved = categoryRepository.save(category);
        return CategoryResponse.fromEntity(saved);
    }

    /**
     * 카테고리 전체 조회
     */
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 상세 조회
     */
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다. ID=" + id));
        return CategoryResponse.fromEntity(category);
    }
}