package backend.coumap.store.repository;

import backend.coumap.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByRegionId(Long regionId);
    List<Store> findByCategory(String category);
    List<Store> findByRegionIdAndCategory(Long regionId, String category);
}