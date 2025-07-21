package backend.coumap.store.dto;

import backend.coumap.category.dto.CategoryResponse;
import backend.coumap.region.dto.RegionResponse;
import backend.coumap.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponse {
    private Long id;
    private String name;
    private CategoryResponse category;  // String -> CategoryResponse로 변경
    private RegionResponse region;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isFranchise;
    private Long annualSales;
    private String businessDays;
    private String openingHours;
    private Double distance;

    public static StoreResponse fromEntity(Store store, Double distance) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .category(CategoryResponse.fromEntity(store.getCategory()))
                .region(RegionResponse.fromEntity(store.getRegion()))
                .address(store.getAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .isFranchise(store.getIsFranchise())
                .annualSales(store.getAnnualSales())
                .businessDays(store.getBusinessDays())
                .openingHours(store.getOpeningHours())
                .distance(distance)
                .build();
    }
}