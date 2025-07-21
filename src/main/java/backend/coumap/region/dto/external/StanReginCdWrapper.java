package backend.coumap.region.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StanReginCdWrapper {

    @JsonProperty("row")
    private List<RegionRow> row;

    public StanReginCdWrapper() {
    }

    public List<RegionRow> getRow() {
        return row;
    }

    public void setRow(List<RegionRow> row) {
        this.row = row;
    }
}