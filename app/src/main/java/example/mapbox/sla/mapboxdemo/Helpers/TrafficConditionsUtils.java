package example.mapbox.sla.mapboxdemo.Helpers;

import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.utils.PolylineUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 5/12/16.
 */

public class TrafficConditionsUtils {

    public static final String GREEN_COLOR = "#1de9b6";
    public static final String RED_COLOR = "#ff1644";
    public static final String ORANGE_COLOR = "#ff9d00";

    public static final String LINES_LAYER_GREEN_CATA_SRC_ID = "LINES_LAYER_GREEN_CATA_SRC_ID";
    public static final String LINES_LAYER_ORANGE_CATA_SRC_ID = "LINES_LAYER_ORANGE_CATA_SRC_ID";
    public static final String LINES_LAYER_RED_CATA_SRC_ID = "LINES_LAYER_RED_CATA_SRC_ID";

    public static final String LINES_LAYER_GREEN_CATA_LYR_ID = "LINES_LAYER_GREEN_CATA_LYR_ID";
    public static final String LINES_LAYER_ORANGE_CATA_LYR_ID = "LINES_LAYER_ORANGE_CATA_LYR_ID";
    public static final String LINES_LAYER_RED_CATA_LYR_ID = "LINES_LAYER_RED_CATA_LYR_ID";

    public static final String LINES_LAYER_GREEN_CATB_SRC_ID = "LINES_LAYER_GREEN_CATB_SRC_ID";
    public static final String LINES_LAYER_ORANGE_CATB_SRC_ID = "LINES_LAYER_ORANGE_CATB_SRC_ID";
    public static final String LINES_LAYER_RED_CATB_SRC_ID = "LINES_LAYER_RED_CATB_SRC_ID";

    public static final String LINES_LAYER_GREEN_CATB_LYR_ID = "LINES_LAYER_GREEN_CATB_LYR_ID";
    public static final String LINES_LAYER_ORANGE_CATB_LYR_ID = "LINES_LAYER_ORANGE_CATB_LYR_ID";
    public static final String LINES_LAYER_RED_CATB_LYR_ID = "LINES_LAYER_RED_CATB_LYR_ID";

    public static LatLng[] getPolylineFromTrafficConditionJsonObject(JSONObject trafficConditionElem) {
        try {
            String encodedPolyline = trafficConditionElem.getString("geojson");
            List<Position> decodedPoints = PolylineUtils.decode(encodedPolyline, 5);


            Position[] before = new Position[decodedPoints.size()];
            for (int i = 0; i < decodedPoints.size(); i++) {
                before[i] = decodedPoints.get(i);
            }

            LatLng[] points = new LatLng[before.length];
            for (int i = 0; i < before.length; i++) {
                points[i] = new LatLng(before[i].getLatitude(), before[i].getLongitude());
            }
            return points;
        }
        catch (JSONException e) {
            Log.e("TEST", "Error getting polyline from traffic condition response element", e);
            return null;
        }
    }

    public static String getColorCatFromTrafficConditionJsonObjects(JSONObject trafficConditionElem) {
        try {
            int speedband = trafficConditionElem.getInt("speedband");
            String roadcat = trafficConditionElem.getString("roadcat");

            //We are not showing C and D because of minor roads

            if(speedband == 3 && (roadcat.equals("C") || roadcat.equals("D"))){
                return GREEN_COLOR + "_" + roadcat;
            }else if((speedband == 1 || speedband == 2)&& (roadcat.equals("D"))) {
                return GREEN_COLOR + "_" + roadcat;
            }else if(speedband == 3 && (roadcat.equals("A") || roadcat.equals("B") )) {
                return ORANGE_COLOR + "_" + roadcat;
            }else if(speedband == 2  && (roadcat.equals("A") || roadcat.equals("B") )) {
                return ORANGE_COLOR + "_" + roadcat;
            }else if((speedband == 2|| speedband == 1) && (roadcat.equals("C") )) {
                return ORANGE_COLOR + "_" + roadcat;
            }else if(speedband == 2  && (roadcat.equals("A") || roadcat.equals("B") )) {
                return ORANGE_COLOR + "_" + roadcat;
            }else if(speedband == 1  && (roadcat.equals("A") || roadcat.equals("B") )) {
                return RED_COLOR + "_" + roadcat;
            }
            else {
                return null;
            }
        }
        catch (JSONException e) {
            Log.e("TEST", "Error getting color from traffic condition response element", e);
            return null;
        }
    }
}
