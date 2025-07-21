package backend.coumap.category.domain;

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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;  // 업종 코드

    @Column(nullable = false)
    private String name;  // 업종 이름

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Store> stores;
}