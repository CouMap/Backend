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
                .isFranchise(request.getIsFranchise() != null ? request.getIsFranchise() : false) // ✅ 추가
                .annualSales(request.getAnnualSales())                                           // ✅ 추가
                .businessDays(request.getBusinessDays())                                         // ✅ 추가
                .openingHours(request.getOpeningHours())                                         // ✅ 추가
                .build();

        Store saved = storeRepository.save(store);
        return StoreResponse.fromEntity(saved);
    }

    /**
     * 가맹점 전체 조회 (필터링 지원)
     */
    public List<StoreResponse> getStores(Long regionId, String category) {
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
}