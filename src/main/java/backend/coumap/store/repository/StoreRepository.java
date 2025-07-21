package backend.coumap.store.repository;

import backend.coumap.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByRegionId(Long regionId);
    List<Store> findByCategory(String category);
    List<Store> findByRegionIdAndCategory(Long regionId, String category);
    boolean existsByNameAndAddressAndRegionId(String name, String address, Long regionId);
    List<Store> findByRegionIdAndCategoryAndNameContaining(Long regionId, String category, String name);
    List<Store> findByRegionIdAndNameContaining(Long regionId, String name);
    List<Store> findByCategoryAndNameContaining(String category, String name);
    List<Store> findByNameContaining(String name);
}