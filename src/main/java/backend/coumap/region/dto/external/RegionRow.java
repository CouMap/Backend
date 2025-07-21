package backend.coumap.region.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionRow {

    @JsonProperty("region_cd")
    private String regionCd;

    @JsonProperty("sido_cd")
    private String sidoCd;

    @JsonProperty("sgg_cd")
    private String sggCd;

    @JsonProperty("umd_cd")
    private String umdCd;

    @JsonProperty("locatadd_nm")
    private String locataddNm;

    public RegionRow() {
    }

    public String getRegionCd() {
        return regionCd;
    }

    public void setRegionCd(String regionCd) {
        this.regionCd = regionCd;
    }

    public String getSidoCd() {
        return sidoCd;
    }

    public void setSidoCd(String sidoCd) {
        this.sidoCd = sidoCd;
    }

    public String getSggCd() {
        return sggCd;
    }

    public void setSggCd(String sggCd) {
        this.sggCd = sggCd;
    }

    public String getUmdCd() {
        return umdCd;
    }

    public void setUmdCd(String umdCd) {
        this.umdCd = umdCd;
    }

    public String getLocataddNm() {
        return locataddNm;
    }

    public void setLocataddNm(String locataddNm) {
        this.locataddNm = locataddNm;
    }
}