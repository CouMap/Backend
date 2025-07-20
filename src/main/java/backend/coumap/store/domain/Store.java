package backend.coumap.store.domain;

import backend.coumap.region.domain.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;         // 가맹점명

    @Column(nullable = false)
    private String address;      // 주소

    private Double latitude;     // 위도
    private Double longitude;    // 경도
    private String category;     // 업종
    private Boolean isFranchise; // 프랜차이즈 여부
    private Long annualSales;    // 연 매출

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;       // 지역 연관관계
}