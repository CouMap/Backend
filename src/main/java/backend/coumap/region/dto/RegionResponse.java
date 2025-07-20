package backend.coumap.region.dto;

import backend.coumap.region.domain.Region;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionResponse {
    private Long id;
    private String name;
    private String code;

    public static RegionResponse fromEntity(Region region) {
        return RegionResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .code(region.getCode())
                .build();
    }
}
