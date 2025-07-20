package backend.coumap.region.service;

import backend.coumap.region.dto.RegionResponse;
import backend.coumap.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;

    public List<RegionResponse> getAllRegions() {
        return List.of();
    }
}