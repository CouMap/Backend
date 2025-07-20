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
     * 모든 가맹점 목록 조회
     */
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 가맹점 등록
     */
    public StoreResponse createStore(StoreRequest request) {
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지역이 존재하지 않습니다."));

        Store store = Store.builder()
                .name(request.getName())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .category(request.getCategory())
                .isFranchise(request.getIsFranchise())
                .annualSales(request.getAnnualSales())
                .region(region)
                .build();

        Store savedStore = storeRepository.save(store);
        return StoreResponse.fromEntity(savedStore);
    }
}