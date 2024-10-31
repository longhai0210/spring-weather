package weather.payload;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FiveDaysRequest {
    private String q;
    private String lat;
    private String lon;
    private String cnt;
    private String units = "metric";
    private String lang = "vi";

    public FiveDaysRequest() {
    }

    public FiveDaysRequest(String q, String lat, String lon, String cnt, String units, String lang) {
        this.q = q;
        this.lat = lat;
        this.lon = lon;
        this.cnt = cnt;
        this.units = units == null ? "metric" : units;
        this.lang = lang == null ? "vi" : lang;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

     public String toQueryString() {
         List<String> query = new ArrayList<>();
         if (q != null) {
             query.add("q=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
         }
         if (lat != null) {
             query.add("lat=" + URLEncoder.encode(lat, StandardCharsets.UTF_8));
         }
         if (lon != null) {
             query.add("lon=" + URLEncoder.encode(lon, StandardCharsets.UTF_8));
         }
         if (cnt != null) {
             query.add("cnt=" + cnt);
         }
         if (units != null) {
             query.add("units=" + URLEncoder.encode(units, StandardCharsets.UTF_8));
         }
         if (lang != null) {
             query.add("lang=" + URLEncoder.encode(lang, StandardCharsets.UTF_8));
         }
         return String.join("&", query);
     }
}
