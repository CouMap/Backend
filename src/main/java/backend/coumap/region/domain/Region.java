package backend.coumap.region.domain;

import backend.coumap.store.domain.Store;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String province; // 시/도

    @Column(nullable = false)
    private String city;     // 시/군/구

    @Column(nullable = true)
    private String town;     // 읍/면/동

    @Column(unique = true, nullable = false)
    private String code;  // 행정코드

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<Store> stores; // 해당 지역의 가맹점들
}
