package backend.coumap.store.service;

import backend.coumap.region.domain.Region;
import backend.coumap.region.repository.RegionRepository;
import backend.coumap.store.domain.Store;
import backend.coumap.store.dto.StoreRequest;
import backend.coumap.store.dto.StoreResponse;
import backend.coumap.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private static final double EARTH_RADIUS_METERS = 6371e3;

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;

    /**
     * 가맹점 등록
     */
    public StoreResponse createStore(StoreRequest request) {
        if (storeRepository.existsByNameAndAddressAndRegionId(
                request.getName(), request.getAddress(), request.getRegionId())) {
            throw new IllegalArgumentException("이미 존재하는 가맹점입니다.");
        }
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지역이 존재하지 않습니다. ID=" + request.getRegionId()));

        Store store = Store.builder()
                .name(request.getName())
                .category(request.getCategory())
                .region(region)
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isFranchise(request.getIsFranchise())
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
    public List<StoreResponse> getStores(Long regionId, String category, String name) {
        List<Store> stores;

        if (regionId != null && category != null && name != null) {
            stores = storeRepository.findByRegionIdAndCategoryAndNameContaining(regionId, category, name);
        } else if (regionId != null && category != null) {
            stores = storeRepository.findByRegionIdAndCategory(regionId, category);
        } else if (regionId != null && name != null) {
            stores = storeRepository.findByRegionIdAndNameContaining(regionId, name);
        } else if (category != null && name != null) {
            stores = storeRepository.findByCategoryAndNameContaining(category, name);
        } else if (name != null) {
            stores = storeRepository.findByNameContaining(name);
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
                .filter(store -> {
                    // null 체크 추가
                    if (store.getLatitude() == null || store.getLongitude() == null) {
                        log.warn("Store ID {} has null coordinates", store.getId());
                        return false;
                    }
                    return distance(lat, lng, store.getLatitude(), store.getLongitude()) <= radius;
                })
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2 - lat1);
        double Δλ = Math.toRadians(lon2 - lon1);

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIUS_METERS * c;
    }

    /**
     * 가맹점 영업 상태 조회
     */
    public List<StoreResponse> getOpenStores(Long regionId) {
        List<Store> stores = storeRepository.findByRegionId(regionId);
        LocalTime now = LocalTime.now();

        return stores.stream()
                .filter(store -> isOpen(store.getOpeningHours(), now))
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private boolean isOpen(String openingHours, LocalTime now) {
        if (openingHours == null || openingHours.trim().isEmpty()) {
            return false;
        }

        try {
            // "09:00~18:00" 형식 파싱
            if (!openingHours.contains("~")) {
                return false;
            }

            String[] parts = openingHours.trim().split("~");
            if (parts.length != 2) {
                return false;
            }

            LocalTime start = LocalTime.parse(parts[0].trim());
            LocalTime end = LocalTime.parse(parts[1].trim());

            // 자정을 넘는 경우 (예: 22:00~02:00)
            if (end.isBefore(start)) {
                return !now.isBefore(start) || !now.isAfter(end);
            }

            return !now.isBefore(start) && !now.isAfter(end);

        } catch (DateTimeParseException e) {
            log.warn("Invalid opening hours format: {}", openingHours);
            return false;
        }
    }
}