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
     * 모든 가맹점 목록 조회
     */
    @GetMapping
    public List<StoreResponse> getAllStores() {
        return storeService.getAllStores();
    }

    /**
     * 새로운 가맹점 추가
     */
    @PostMapping
    public StoreResponse addStore(@RequestBody StoreRequest request) {
        return storeService.createStore(request);
    }
}
