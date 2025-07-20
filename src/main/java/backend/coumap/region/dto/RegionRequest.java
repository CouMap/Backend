package backend.coumap.region.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionRequest {
    private String province; // 시/도
    private String city;     // 시/군/구
    private String town;     // 읍/면/동
    private String code;
}
