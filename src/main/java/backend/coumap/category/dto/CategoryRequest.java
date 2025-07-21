package backend.coumap.category.dto;

import lombok.Getter;

@Getter
public class CategoryRequest {
    private String code;  // 업종 코드
    private String name;  // 업종 이름
}
