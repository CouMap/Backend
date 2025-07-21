package backend.coumap.region.repository;

import backend.coumap.region.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByCode(String code);

    List<Region> findByProvinceContaining(String province);
    List<Region> findByCityContaining(String city);
    List<Region> findByTownContaining(String town);
    List<Region> findByProvinceContainingAndCityContaining(String province, String city);
    List<Region> findByProvinceContainingAndCityContainingAndTownContaining(String province, String city, String town);
}