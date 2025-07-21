package backend.coumap.store.controller;

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
            @RequestParam(required = false) String category
    ) {
        return storeService.getAllStores(regionId, category);
    }

    /**
     * 가맹점 상세 조회
     */
    @GetMapping("/{id:[0-9]+}")
    public StoreResponse getStoreById(@PathVariable Long id) {
        return storeService.getStoreById(id);
    }

    /**
     * 근처 가맹점 조회
     */
    @GetMapping("/nearby")
    public List<StoreResponse> getNearbyStores(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "500") double radius
    ) {
        return storeService.getNearbyStores(lat, lng, radius);
    }
}