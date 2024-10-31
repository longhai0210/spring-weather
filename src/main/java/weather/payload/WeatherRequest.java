package weather.payload;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherRequest {
    private String lat = null;
    private String lon = null;
    private String mode = null;
    private String units = "metric";
    private String lang = "vi";
    private String q = null;

    public WeatherRequest() {
    }

    public WeatherRequest(String lat, String lon, String mode, String units, String lang, String q) {
        this.lat = lat;
        this.lon = lon;
        this.q = q;
        this.mode = mode;
        this.units = !units.isEmpty() ? units : this.units;
        this.lang = !lang.isEmpty() ? lang : this.lang;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String toQueryString() {
        List<String> query = new ArrayList<>();
        if (lat != null) {
            query.add("lat=" + lat);
        }
        if (lon != null) {
            query.add("lon=" + lon);
        }
        if (mode != null) {
            query.add("mode=" + mode);
        }
        if (units != null) {
            query.add("units=" + units);
        }
        if (lang != null) {
            query.add("lang=" + lang);
        }
        if (q != null) {
            query.add("q=" + URLEncoder.encode(q, StandardCharsets.UTF_8));
        }
        return String.join("&", query);
    }
}
