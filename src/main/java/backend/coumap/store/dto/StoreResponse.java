package backend.coumap.store.dto;

import backend.coumap.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponse {

    private Long id;
    private String name;
    private String address;
    private String category;
    private Boolean isFranchise;
    private Long annualSales;

    public static StoreResponse fromEntity(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .category(store.getCategory())
                .isFranchise(store.getIsFranchise())
                .annualSales(store.getAnnualSales())
                .build();
    }
}