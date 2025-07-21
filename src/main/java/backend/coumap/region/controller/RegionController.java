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

    /**
     * 행정안전부 API에서 지역 데이터 동기화
     */
    @PostMapping("/sync")
    public String syncRegionData() {
        try {
            regionService.syncRegionData();
            return "지역 데이터 동기화가 완료되었습니다.";
        } catch (Exception e) {
            return "지역 데이터 동기화 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    /**
     * 지역별 검색
     */
    @GetMapping("/search")
    public List<RegionResponse> searchRegions(@RequestParam(required = false) String province,
                                              @RequestParam(required = false) String city,
                                              @RequestParam(required = false) String town) {
        return regionService.searchRegions(province, city, town);
    }
}