package backend.coumap.region.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionApiResponse {

    @JsonProperty("StanReginCd")
    private List<StanReginCdWrapper> stanReginCd;

    public RegionApiResponse() {
    }

    public List<StanReginCdWrapper> getStanReginCd() {
        return stanReginCd;
    }

    public void setStanReginCd(List<StanReginCdWrapper> stanReginCd) {
        this.stanReginCd = stanReginCd;
    }

    public List<RegionRow> getRows() {
        if (stanReginCd != null && stanReginCd.size() >= 2) {
            StanReginCdWrapper dataWrapper = stanReginCd.get(1);
            if (dataWrapper != null && dataWrapper.getRow() != null && !dataWrapper.getRow().isEmpty()) {
                return dataWrapper.getRow();
            }
        }
        return null;
    }
}