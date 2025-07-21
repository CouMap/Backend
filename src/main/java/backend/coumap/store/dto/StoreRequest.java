package backend.coumap.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    private String name;
    private Long categoryId;
    private Long regionId;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isFranchise;
    private Long annualSales;
    private String businessDays;
    private String openingHours;
}