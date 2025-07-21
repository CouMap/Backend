package backend.coumap.store.controller;

import backend.coumap.store.dto.NearbyStoreResponse;
import backend.coumap.store.dto.StoreRequest;
import backend.coumap.store.dto.StoreResponse;
import backend.coumap.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * 가맹점 등록
     */
    @PostMapping
    public StoreResponse addStore(@RequestBody StoreRequest request) {
        return storeService.createStore(request);
    }

    /**
     * 가맹점 전체 조회
     */
    @GetMapping
    public List<StoreResponse> getAllStores(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude
    ) {
        return storeService.getStores(regionId, category, name, latitude, longitude);
    }

    /**
     * 가맹점 상세 조회
     */
    @GetMapping("/{id}")
    public StoreResponse getStoreById(@PathVariable Long id) {
        return storeService.getStoreById(id);
    }

    /**
     * 근처 가맹점 조회
     */
    @GetMapping("/nearby")
    public List<NearbyStoreResponse> getNearbyStores(@RequestParam double latitude,
                                                     @RequestParam double longitude,
                                                     @RequestParam int radius) {
        return storeService.getNearbyStores(latitude, longitude, radius);
    }

    /**
     * 영업중 가맹점 조회
     */
    @GetMapping("/open")
    public List<StoreResponse> getOpenStores(@RequestParam Long regionId) {
        return storeService.getOpenStores(regionId);
    }

}