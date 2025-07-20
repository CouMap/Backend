package backend.coumap.store.controller;

import backend.coumap.store.dto.StoreRequest;
import backend.coumap.store.dto.StoreResponse;
import backend.coumap.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
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
        return storeService.getStores(regionId, category);
    }
}