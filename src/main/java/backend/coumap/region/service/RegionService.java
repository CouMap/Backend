package backend.coumap.region.service;

import backend.coumap.region.domain.Region;
import backend.coumap.region.dto.RegionRequest;
import backend.coumap.region.dto.RegionResponse;
import backend.coumap.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    /**
     * 모든 지역 목록 조회
     */
    public List<RegionResponse> getAllRegions() {
        return regionRepository.findAll()
                .stream()
                .map(RegionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 지역 등록
     */
    public RegionResponse createRegion(RegionRequest request) {
        Optional<Region> existing = regionRepository.findByCode(request.getCode());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 지역 코드입니다: " + request.getCode());
        }

        Region region = Region.builder()
                .province(request.getProvince())
                .city(request.getCity())
                .town(request.getTown())
                .code(request.getCode())
                .build();

        Region saved = regionRepository.save(region);
        return RegionResponse.fromEntity(saved);
    }

}
