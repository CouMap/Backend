package backend.coumap.region.service;

import backend.coumap.region.domain.Region;
import backend.coumap.region.dto.RegionRequest;
import backend.coumap.region.dto.RegionResponse;
import backend.coumap.region.dto.external.RegionApiResponse;
import backend.coumap.region.dto.external.RegionRow;
import backend.coumap.region.repository.RegionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${region.api.service-key}")
    private String serviceKey;

    private static final String API_URL = "http://apis.data.go.kr/1741000/StanReginCd/getStanReginCdList";

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

    /**
     * 지역 검색 (시도, 시군구, 읍면동으로 검색)
     */
    public List<RegionResponse> searchRegions(String province, String city, String town) {
        List<Region> regions;

        if (province != null && city != null && town != null) {
            regions = regionRepository.findByProvinceContainingAndCityContainingAndTownContaining(province, city, town);
        } else if (province != null && city != null) {
            regions = regionRepository.findByProvinceContainingAndCityContaining(province, city);
        } else if (province != null) {
            regions = regionRepository.findByProvinceContaining(province);
        } else if (city != null) {
            regions = regionRepository.findByCityContaining(city);
        } else if (town != null) {
            regions = regionRepository.findByTownContaining(town);
        } else {
            regions = regionRepository.findAll();
        }

        return regions.stream()
                .map(RegionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 행정안전부 API에서 법정동코드 데이터를 가져와서 DB에 저장
     */
    public void syncRegionData() {
        try {
            int pageNo = 1;
            int numOfRows = 1000;
            boolean hasMoreData = true;

            while (hasMoreData) {
                String url = API_URL
                        + "?ServiceKey=" + serviceKey
                        + "&type=json"
                        + "&pageNo=" + pageNo
                        + "&numOfRows=" + numOfRows
                        + "&flag=Y";

                // String으로 JSON 응답을 받음
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        new URI(url), HttpMethod.GET, null, String.class);
                String jsonResponse = responseEntity.getBody();

                if (jsonResponse != null) {
                    List<RegionRow> rows = parseJsonResponse(jsonResponse);

                    if (rows != null && !rows.isEmpty()) {
                        saveRegionData(rows);

                        if (rows.size() < numOfRows) {
                            hasMoreData = false;
                        } else {
                            pageNo++;
                        }
                    } else {
                        hasMoreData = false;
                    }
                } else {
                    hasMoreData = false;
                }

                Thread.sleep(100);
            }

        } catch (Exception e) {
            log.error("API 동기화 중 오류 발생", e);
            throw new RuntimeException("지역 데이터 동기화 실패", e);
        }
    }

    /**
     * JSON 응답을 직접 파싱해서 RegionRow 리스트로 변환
     */
    private List<RegionRow> parseJsonResponse(String jsonResponse) {
        List<RegionRow> regionRows = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            JsonNode stanReginCdArray = rootNode.path("StanReginCd");
            if (stanReginCdArray.isArray() && stanReginCdArray.size() >= 2) {
                JsonNode rowArray = stanReginCdArray.get(1).path("row");

                if (rowArray.isArray()) {
                    for (JsonNode rowNode : rowArray) {
                        RegionRow row = new RegionRow();
                        row.setRegionCd(rowNode.path("region_cd").asText());
                        row.setSidoCd(rowNode.path("sido_cd").asText());
                        row.setSggCd(rowNode.path("sgg_cd").asText());
                        row.setUmdCd(rowNode.path("umd_cd").asText());
                        row.setLocataddNm(rowNode.path("locatadd_nm").asText());

                        regionRows.add(row);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JSON 파싱 중 오류 발생", e);
        }

        return regionRows;
    }

    /**
     * API에서 받은 지역 데이터를 DB에 저장
     */
    private void saveRegionData(List<RegionRow> rows) {
        List<Region> regionsToSave = new ArrayList<>();

        for (RegionRow row : rows) {
            if (regionRepository.findByCode(row.getRegionCd()).isEmpty()) {
                Region region = convertToRegion(row);
                regionsToSave.add(region);
            } else {
                log.info("이미 존재하는 지역 코드: {}", row.getRegionCd());
            }
        }

        if (!regionsToSave.isEmpty()) {
            regionRepository.saveAll(regionsToSave);
        }
    }

    /**
     * API 응답 데이터를 Region 엔티티로 변환
     */
    private Region convertToRegion(RegionRow row) {
        String fullAddress = row.getLocataddNm();

        String[] addressParts = parseAddress(fullAddress);

        return Region.builder()
                .province(addressParts[0])
                .city(addressParts[1])
                .town(addressParts[2])
                .code(row.getRegionCd())
                .build();
    }

    /**
     * 주소 문자열을 파싱하여 [시도, 시군구, 읍면동] 배열로 반환
     */
    private String[] parseAddress(String fullAddress) {
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            return new String[]{"", "", ""};
        }

        String[] result = new String[3];
        String[] parts = fullAddress.split(" ");

        result[0] = parts.length > 0 ? parts[0] : "";
        result[1] = parts.length > 1 ? parts[1] : "";
        result[2] = parts.length > 2 ? String.join(" ", Arrays.copyOfRange(parts, 2, parts.length)) : "";

        return result;
    }
}
