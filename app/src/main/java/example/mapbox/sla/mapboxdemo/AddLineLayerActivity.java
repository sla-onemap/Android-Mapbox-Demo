package example.mapbox.sla.mapboxdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.functions.CameraFunction;
import com.mapbox.mapboxsdk.style.functions.stops.ExponentialStops;
import com.mapbox.mapboxsdk.style.functions.stops.Stop;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.MultiLineString;
import com.mapbox.services.commons.models.Position;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.mapbox.sla.mapboxdemo.Helpers.TrafficConditionsTask;
import example.mapbox.sla.mapboxdemo.Helpers.TrafficConditionsUtils;

import static com.mapbox.mapboxsdk.style.functions.stops.Stop.stop;

public class AddLineLayerActivity extends AppCompatActivity
implements TrafficConditionsTask.TrafficConditionsTaskListener {

    // MapBox
    private MapView mapView;
    private MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_line_layer);

        getSupportActionBar().setTitle("Add Traffic Line Layer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.setStyleUrl(Constants.DEFAULT_BASEMAP_URL); //SET CUSTOM BASE MAP URL
        //Callback when map finish loading and is ready to be used
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                AddLineLayerActivity.this.mapboxMap =  mapboxMap;
                TrafficConditionsTask trafficConditionsTask = new TrafficConditionsTask(AddLineLayerActivity.this, AddLineLayerActivity.this);
                trafficConditionsTask.startGetTrafficCondition();
            }
        });
    }

    //........................................
    //Do not remove, essential to use Mapbox
    //........................................
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    //........................................

    //region TrafficConditionsTaskListener methods
    public void onFinishGetTrafficCondition(JSONArray res) {

        ArrayList<LatLng[]> greenCatAlinesArr = new ArrayList<>();
        ArrayList<LatLng[]> orangeCatAlinesArr = new ArrayList<>();
        ArrayList<LatLng[]> redCatAlinesArr = new ArrayList<>();
        ArrayList<LatLng[]> greenCatBlinesArr = new ArrayList<>();
        ArrayList<LatLng[]> orangeCatBlinesArr = new ArrayList<>();
        ArrayList<LatLng[]> redCatBlinesArr = new ArrayList<>();

        for(int i=0; i<res.length(); i++) {
            try {
                JSONObject trafficConditionElem = res.getJSONObject(i);
                String colorCodeWithCat = TrafficConditionsUtils.getColorCatFromTrafficConditionJsonObjects(trafficConditionElem) ;

                String colorCodeWithCatArr[] = colorCodeWithCat.split("_");
                String colorCode = colorCodeWithCatArr[0];
                String cat = colorCodeWithCatArr[1];

                LatLng[] points = TrafficConditionsUtils.getPolylineFromTrafficConditionJsonObject(trafficConditionElem);
                if(points!=null && colorCode!=null) {

                    //Note: Not adding cat C and cat D minor roads
                    if(colorCode.equals(TrafficConditionsUtils.GREEN_COLOR) && cat.equals("A")) {
                        greenCatAlinesArr.add(points);
                    }
                    else if(colorCode.equals(TrafficConditionsUtils.ORANGE_COLOR) && cat.equals("A")) {
                        orangeCatAlinesArr.add(points);
                    }
                    else if(colorCode.equals(TrafficConditionsUtils.RED_COLOR) && cat.equals("A")) {
                        redCatAlinesArr.add(points);
                    }

                    else if(colorCode.equals(TrafficConditionsUtils.GREEN_COLOR) && cat.equals("B")) {
                        greenCatBlinesArr.add(points);
                    }
                    else if(colorCode.equals(TrafficConditionsUtils.ORANGE_COLOR) && cat.equals("B")) {
                        orangeCatBlinesArr.add(points);
                    }
                    else if(colorCode.equals(TrafficConditionsUtils.RED_COLOR) && cat.equals("B")) {
                        redCatBlinesArr.add(points);
                    }
                }
            }
            catch (JSONException e) {
                Log.e("TEST", "", e);
            }
        }

        removeAllTrafficConditionLayers();
        addTrafficConditionLayer(orangeCatAlinesArr, TrafficConditionsUtils.ORANGE_COLOR, "A");
        addTrafficConditionLayer(redCatAlinesArr, TrafficConditionsUtils.RED_COLOR, "A");
        addTrafficConditionLayer(orangeCatBlinesArr, TrafficConditionsUtils.ORANGE_COLOR, "B");
        addTrafficConditionLayer(redCatBlinesArr, TrafficConditionsUtils.RED_COLOR, "B");
    }
    //

    private void addTrafficConditionLayer(ArrayList<LatLng[]> linesArr, String colorCode, String cat) {

        String layerID = "";
        String sourceID = "";
        if(cat.equals("A")) {
            if (colorCode.equals(TrafficConditionsUtils.GREEN_COLOR)) {
                layerID = TrafficConditionsUtils.LINES_LAYER_GREEN_CATA_LYR_ID;
                sourceID = TrafficConditionsUtils.LINES_LAYER_GREEN_CATA_SRC_ID;
            } else if (colorCode.equals(TrafficConditionsUtils.ORANGE_COLOR)) {
                layerID = TrafficConditionsUtils.LINES_LAYER_ORANGE_CATA_LYR_ID;
                sourceID = TrafficConditionsUtils.LINES_LAYER_ORANGE_CATA_SRC_ID;
            } else if (colorCode.equals(TrafficConditionsUtils.RED_COLOR)) {
                layerID = TrafficConditionsUtils.LINES_LAYER_RED_CATA_LYR_ID;
                sourceID = TrafficConditionsUtils.LINES_LAYER_RED_CATA_SRC_ID;
            } else {
                return;
            }
        }
        else {
            if (colorCode.equals(TrafficConditionsUtils.GREEN_COLOR)) {
                layerID = TrafficConditionsUtils.LINES_LAYER_GREEN_CATB_LYR_ID;
                sourceID = TrafficConditionsUtils.LINES_LAYER_GREEN_CATB_SRC_ID;
            } else if (colorCode.equals(TrafficConditionsUtils.ORANGE_COLOR)) {
                layerID = TrafficConditionsUtils.LINES_LAYER_ORANGE_CATB_LYR_ID;
                sourceID = TrafficConditionsUtils.LINES_LAYER_ORANGE_CATB_SRC_ID;
            } else if (colorCode.equals(TrafficConditionsUtils.RED_COLOR)) {
                layerID = TrafficConditionsUtils.LINES_LAYER_RED_CATB_LYR_ID;
                sourceID = TrafficConditionsUtils.LINES_LAYER_RED_CATB_SRC_ID;
            }  else {
                return;
            }
        }

        // Create a list to store our line coordinates.
        List<List<Position>> linesCoordinatesArr = new ArrayList<>();

        for (LatLng[] line : linesArr) {
            List<Position> lineCoordinates = new ArrayList<>();
            for (LatLng point : line) {
                //For each point in a line, form Position array
                lineCoordinates.add(Position.fromCoordinates(point.getLongitude(), point.getLatitude()));
            }
            linesCoordinatesArr.add(lineCoordinates);
        }

        //Create a GeoJSON FeatureCollection so we can add the line to our map as a layer.
        MultiLineString linesString = MultiLineString.fromCoordinates(linesCoordinatesArr);
        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(linesString)});

        Source geoJsonSource = new GeoJsonSource(sourceID, featureCollection);
        mapboxMap.addSource(geoJsonSource);

        LineLayer lineLayer = new LineLayer(layerID, sourceID);

        // The layer properties for our line.
        // This is where we make the line dotted, set the color, etc.
        lineLayer.setProperties(
                PropertyFactory.lineCap(com.mapbox.mapboxsdk.style.layers.Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(getTrafficLineWidthStops(cat)),
                PropertyFactory.lineColor(Color.parseColor(colorCode)),
                PropertyFactory.lineOpacity(1f)
        );

        mapboxMap.addLayer(lineLayer);
    }

    private void removeAllTrafficConditionLayers() {
        if(mapboxMap != null) {
            if (mapboxMap.getLayer(TrafficConditionsUtils.LINES_LAYER_RED_CATA_LYR_ID) != null) {
                mapboxMap.removeLayer(TrafficConditionsUtils.LINES_LAYER_RED_CATA_LYR_ID);
                mapboxMap.removeSource(TrafficConditionsUtils.LINES_LAYER_RED_CATA_SRC_ID);

            }
            if (mapboxMap.getLayer(TrafficConditionsUtils.LINES_LAYER_ORANGE_CATA_LYR_ID) != null) {
                mapboxMap.removeLayer(TrafficConditionsUtils.LINES_LAYER_ORANGE_CATA_LYR_ID);
                mapboxMap.removeSource(TrafficConditionsUtils.LINES_LAYER_ORANGE_CATA_SRC_ID);
            }
            if (mapboxMap.getLayer(TrafficConditionsUtils.LINES_LAYER_RED_CATB_LYR_ID) != null) {
                mapboxMap.removeLayer(TrafficConditionsUtils.LINES_LAYER_RED_CATB_LYR_ID);
                mapboxMap.removeSource(TrafficConditionsUtils.LINES_LAYER_RED_CATB_SRC_ID);

            }
            if (mapboxMap.getLayer(TrafficConditionsUtils.LINES_LAYER_ORANGE_CATB_LYR_ID) != null) {
                mapboxMap.removeLayer(TrafficConditionsUtils.LINES_LAYER_ORANGE_CATB_LYR_ID);
                mapboxMap.removeSource(TrafficConditionsUtils.LINES_LAYER_ORANGE_CATB_SRC_ID);
            }
        }
    }

    //Setting line width for different levels of zoom
    private static CameraFunction getTrafficLineWidthStops(String cat) {
        //Setting line width for different levels of zoom
        if(cat.equals("A")) {
            Stop stop1 =    stop(10, PropertyFactory.lineWidth(0f));
            Stop stop2 =    stop(10.499499f, PropertyFactory.lineWidth(0f));
            Stop stop3 =    stop(10.499500f, PropertyFactory.lineWidth(2f));
            Stop stop4 =    stop(12.499999f, PropertyFactory.lineWidth(6.5f));
            Stop stop5 =    stop(12.5f, PropertyFactory.lineWidth(5f));
            Stop stop6 =    stop(13.49999f, PropertyFactory.lineWidth(10f));
            Stop stop7 =    stop(13.5f, PropertyFactory.lineWidth(5f));
            Stop stop8 =    stop(14.49999f, PropertyFactory.lineWidth(10f));
            Stop stop9 =    stop(14.5f, PropertyFactory.lineWidth(6f));
            Stop stop10 =   stop(15.49999f, PropertyFactory.lineWidth(10f));
            Stop stop11 =   stop(15.5f, PropertyFactory.lineWidth(3f));
            Stop stop12 =   stop(18.0f, PropertyFactory.lineWidth(6.5f));

            ExponentialStops exponentialStops = new ExponentialStops(stop1, stop2, stop3, stop4, stop5, stop6, stop7, stop8, stop9, stop10, stop11, stop12);
            CameraFunction cameraFunction = CameraFunction.zoom(exponentialStops);
            return cameraFunction;
        }
        else {
            Stop stop1 =    stop(10, PropertyFactory.lineWidth(0f));
            Stop stop2 =    stop(13.499999f, PropertyFactory.lineWidth(0f));
            Stop stop3 =    stop(13.5f, PropertyFactory.lineWidth(3f));
            Stop stop4 =    stop(14.49999f, PropertyFactory.lineWidth(6f));
            Stop stop5 =    stop(14.5f, PropertyFactory.lineWidth(3f));
            Stop stop6 =    stop(15.49999f, PropertyFactory.lineWidth(5f));
            Stop stop7 =    stop(15.5f, PropertyFactory.lineWidth(2.5f));
            Stop stop8 =    stop(16.49999f, PropertyFactory.lineWidth(3.5f));
            Stop stop9 =    stop(16.5f, PropertyFactory.lineWidth(2.5f));
            Stop stop10 =   stop(18.0f, PropertyFactory.lineWidth(5.5f));

            ExponentialStops exponentialStops = new ExponentialStops(stop1, stop2, stop3, stop4, stop5, stop6, stop7, stop8, stop9, stop10);
            CameraFunction cameraFunction = CameraFunction.zoom(exponentialStops);
            return cameraFunction;
        }
    }
}
