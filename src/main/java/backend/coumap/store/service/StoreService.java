package backend.coumap.store.service;

import backend.coumap.category.domain.Category;
import backend.coumap.category.repository.CategoryRepository;
import backend.coumap.region.domain.Region;
import backend.coumap.region.repository.RegionRepository;
import backend.coumap.store.domain.Store;
import backend.coumap.store.dto.NearbyStoreResponse;
import backend.coumap.store.dto.StoreRequest;
import backend.coumap.store.dto.StoreResponse;
import backend.coumap.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private static final double EARTH_RADIUS_METERS = 6371e3;

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;

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

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. ID=" + request.getCategoryId()));

        Store store = Store.builder()
                .name(request.getName())
                .category(category)
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
        return StoreResponse.fromEntity(saved, null);
    }

    /**
     * 가맹점 전체 조회 (필터링 지원) - categoryId로 변경
     */
    public List<StoreResponse> getStores(Long regionId, Long categoryId, String name, Double latitude, Double longitude) {
        List<Store> stores;

        if (regionId != null && categoryId != null && name != null) {
            stores = storeRepository.findByRegionIdAndCategoryIdAndNameContaining(regionId, categoryId, name);
        } else if (regionId != null && categoryId != null) {
            stores = storeRepository.findByRegionIdAndCategoryId(regionId, categoryId);
        } else if (regionId != null && name != null) {
            stores = storeRepository.findByRegionIdAndNameContaining(regionId, name);
        } else if (categoryId != null && name != null) {
            stores = storeRepository.findByCategoryIdAndNameContaining(categoryId, name);
        } else if (name != null) {
            stores = storeRepository.findByNameContaining(name);
        } else if (regionId != null) {
            stores = storeRepository.findByRegionId(regionId);
        } else if (categoryId != null) {
            stores = storeRepository.findByCategoryId(categoryId);
        } else {
            stores = storeRepository.findAll();
        }

        return stores.stream()
                .map(store -> {
                    Double distance = (latitude != null && longitude != null)
                            ? calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude())
                            : null;
                    return StoreResponse.fromEntity(store, distance);
                })
                .collect(Collectors.toList());
    }

    /**
     * 가맹점 상세 조회
     */
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("가맹점이 존재하지 않습니다. ID=" + id));
        return StoreResponse.fromEntity(store, null);
    }

    /**
     * 근처 가맹점 조회
     */
    public List<NearbyStoreResponse> getNearbyStores(double latitude, double longitude, int radius) {
        return storeRepository.findAll().stream()
                .map(store -> {
                    double distance = calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude());
                    if (distance <= radius) {
                        return NearbyStoreResponse.fromEntity(store, distance);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 두 지점 간 거리 계산 (단위: m)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
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
                .map(store -> StoreResponse.fromEntity(store, null))
                .collect(Collectors.toList());
    }

    private boolean isOpen(String openingHours, LocalTime now) {
        if (openingHours == null || openingHours.trim().isEmpty()) {
            return false;
        }

        try {
            if (!openingHours.contains("~")) {
                return false;
            }

            String[] parts = openingHours.trim().split("~");
            if (parts.length != 2) {
                return false;
            }

            LocalTime start = LocalTime.parse(parts[0].trim());
            LocalTime end = LocalTime.parse(parts[1].trim());

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