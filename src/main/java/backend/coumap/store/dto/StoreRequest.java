package backend.coumap.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String category;
    private Boolean isFranchise;
    private Long annualSales;
    private Long regionId; // 외래키
}
