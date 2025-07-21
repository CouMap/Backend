package backend.coumap.category.dto;

import backend.coumap.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {
    private Long id;
    private String code;
    private String name;

    public static CategoryResponse fromEntity(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .build();
    }
}
