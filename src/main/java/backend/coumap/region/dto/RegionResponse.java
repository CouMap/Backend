package backend.coumap.region.dto;

import backend.coumap.region.domain.Region;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionResponse {
    private Long id;
    private String province;
    private String city;
    private String town;
    private String code;

    public static RegionResponse fromEntity(Region region) {
        return RegionResponse.builder()
                .id(region.getId())
                .province(region.getProvince())
                .city(region.getCity())
                .town(region.getTown())
                .code(region.getCode())
                .build();
    }
}