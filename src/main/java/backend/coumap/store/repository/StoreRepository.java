package backend.coumap.store.repository;

import backend.coumap.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByRegionId(Long regionId);

    List<Store> findByCategoryId(Long categoryId);
    List<Store> findByRegionIdAndCategoryId(Long regionId, Long categoryId);

    boolean existsByNameAndAddressAndRegionId(String name, String address, Long regionId);

    List<Store> findByRegionIdAndCategoryIdAndNameContaining(Long regionId, Long categoryId, String name);
    List<Store> findByRegionIdAndNameContaining(Long regionId, String name);
    List<Store> findByCategoryIdAndNameContaining(Long categoryId, String name);
    List<Store> findByNameContaining(String name);
}