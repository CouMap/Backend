package backend.coumap.region.controller;

import backend.coumap.region.dto.RegionRequest;
import backend.coumap.region.dto.RegionResponse;
import backend.coumap.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    /**
     * 모든 지역 목록 조회
     */
    @GetMapping
    public List<RegionResponse> getAllRegions() {
        return regionService.getAllRegions();
    }

    /**
     * 새로운 지역 등록
     */
    @PostMapping
    public RegionResponse addRegion(@RequestBody RegionRequest request) {
        return regionService.createRegion(request);
    }
}
