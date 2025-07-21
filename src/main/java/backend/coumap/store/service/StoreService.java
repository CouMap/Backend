package backend.coumap.store.service;

import backend.coumap.region.domain.Region;
import backend.coumap.region.repository.RegionRepository;
import backend.coumap.store.domain.Store;
import backend.coumap.store.dto.StoreRequest;
import backend.coumap.store.dto.StoreResponse;
import backend.coumap.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;

    /**
     * 가맹점 등록
     */
    public StoreResponse createStore(StoreRequest request) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지역이 존재하지 않습니다. ID=" + request.getRegionId()));

        Store store = Store.builder()
                .name(request.getName())
                .category(request.getCategory())
                .region(region)
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isFranchise(request.getIsFranchise() != null ? request.getIsFranchise() : false)
                .annualSales(request.getAnnualSales())
                .businessDays(request.getBusinessDays())
                .openingHours(request.getOpeningHours())
                .build();

        Store saved = storeRepository.save(store);
        return StoreResponse.fromEntity(saved);
    }

    /**
     * 가맹점 전체 조회 (필터링 지원)
     */
    public List<StoreResponse> getAllStores(Long regionId, String category) {
        List<Store> stores;

        if (regionId != null && category != null) {
            stores = storeRepository.findByRegionIdAndCategory(regionId, category);
        } else if (regionId != null) {
            stores = storeRepository.findByRegionId(regionId);
        } else if (category != null) {
            stores = storeRepository.findByCategory(category);
        } else {
            stores = storeRepository.findAll();
        }

        return stores.stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 가맹점 상세 조회
     */
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("가맹점이 존재하지 않습니다. ID=" + id));
        return StoreResponse.fromEntity(store);
    }

    /**
     * 근처 가맹점 조회
     */
    public List<StoreResponse> getNearbyStores(double lat, double lng, double radius) {
        List<Store> stores = storeRepository.findAll();
        return stores.stream()
                .filter(s -> distance(lat, lng, s.getLatitude(), s.getLongitude()) <= radius)
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371e3;
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2 - lat1);
        double Δλ = Math.toRadians(lon2 - lon1);

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}