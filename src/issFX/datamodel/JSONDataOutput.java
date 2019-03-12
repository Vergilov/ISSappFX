package issFX.datamodel;
import org.json.JSONObject;

import static java.lang.Long.valueOf;

public interface JSONDataOutput {

    static Long getTimestamp(JSONObject obj) {
        int res = obj.getInt("timestamp");
        return valueOf(res);
    }

    static Double getLatitude(JSONObject obj) {
        return Double.parseDouble(obj.getJSONObject("iss_position").getString("latitude"));
    }

    static Double getLongitude(JSONObject obj) {
        return Double.parseDouble(obj.getJSONObject("iss_position").getString("longitude"));
    }
}


