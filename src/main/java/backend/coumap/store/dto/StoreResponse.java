package backend.coumap.store.dto;

import backend.coumap.region.dto.RegionResponse;
import backend.coumap.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponse {
    private Long id;
    private String name;
    private String category;
    private RegionResponse region;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isFranchise;
    private Long annualSales;
    private String businessDays;
    private String openingHours;


    public static StoreResponse fromEntity(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .category(store.getCategory())
                .region(RegionResponse.fromEntity(store.getRegion()))
                .address(store.getAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .isFranchise(store.getIsFranchise())
                .annualSales(store.getAnnualSales())
                .businessDays(store.getBusinessDays())
                .openingHours(store.getOpeningHours())
                .build();
    }
}