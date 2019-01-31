package example.mapbox.sla.mapboxdemo;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.mapbox.sla.mapboxdemo.Helpers.TextResUtils;

public class AddPolygonLayerActivity extends AppCompatActivity {
    // MapBox
    private MapView mapView;
    private MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_polygon_layer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Polygon Layer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Constants.DEFAULT_BASEMAP_URL, new Style.OnStyleLoaded() { //SET CUSTOM BASE MAP URL
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        AddPolygonLayerActivity.this.mapboxMap = mapboxMap;
                        drawSampleAreaBoundariesLayer();
                    }
                });
            }
        });
    }

    private void drawSampleAreaBoundariesLayer() {
        try {
            final JSONObject des = TextResUtils.readJSONFromRes(AddPolygonLayerActivity.this, R.raw.des);
            JSONArray features = des.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject featureObj = features.getJSONObject(i);
                JSONObject geometry = featureObj.getJSONObject("geometry");
                JSONArray rings = geometry.getJSONArray("rings");
                List<LatLng[]> ringsArr = new ArrayList();

                for (int j = 0; j < rings.length(); j++) {
                    JSONArray ring = rings.getJSONArray(j);
                    LatLng[] ringLatLngs = new LatLng[ring.length()];

                    for (int k = 0; k < ring.length(); k++) {
                        JSONArray coordinate = ring.getJSONArray(k);
                        double lon = coordinate.getDouble(0);
                        double lat = coordinate.getDouble(1);
                        LatLng latlng = new LatLng(lat, lon);

                        ringLatLngs[k] = latlng;
                    }

                    ringsArr.add(ringLatLngs);
                }

                //Alpha, Red, Green, Blue
                int rgb = android.graphics.Color.argb(255, (100 + i) % 255, 0, 0); //Where Red, Green, Blue are the RGB components. The number 255 is for 100% Alpha

                drawSamplePolygonsLayer(ringsArr, rgb);
            }
        } catch (JSONException e) {
            Log.e("TEST", "", e);
        }
    }

    private void drawSamplePolygonsLayer() {
        //Process polygon json file
        List<LatLng[]> polygons = new ArrayList();

        try {
            final JSONObject polygonJson = TextResUtils.readJSONFromRes(AddPolygonLayerActivity.this, R.raw.sample_polygons);
            Log.i("TEST", polygonJson.toString());

            JSONArray Polygons = polygonJson.getJSONArray("Polygons");
            for (int i = 0; i < Polygons.length(); i++) {
                JSONObject polygonElem = Polygons.getJSONObject(i);
                JSONArray coordinates = polygonElem.getJSONArray("coordinates");

                for (int j = 0; j < coordinates.length(); j++) {
                    JSONArray ringCoords = coordinates.getJSONArray(j);
                    LatLng[] latLngs = new LatLng[ringCoords.length()];

                    for (int k = 0; k < ringCoords.length(); k++) {
                        JSONArray coordinate = ringCoords.getJSONArray(k);
                        latLngs[k] = new LatLng(coordinate.getDouble(1), coordinate.getDouble(0));
                    }

                    polygons.add(latLngs);
                }
            }

            mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                @Override
                public boolean onMapClick(@NonNull LatLng point) {
                    handlePolygonLayerClick(point);

                    return false;
                }
            });
        } catch (JSONException e) {
            Log.e("TEST", "", e);
        }

        drawSamplePolygonsLayer(polygons, "#A52A2A");
    }

    private void drawSamplePolygonsLayer(List<LatLng[]> polygons, String fillColorCodeHex) {
        // refer to https://docs.mapbox.com/android/maps/overview/data-driven-styling/#sources
        // Create a list to store our line coordinates
        List<Point> routeCoordinates = new ArrayList<>();

        for (LatLng[] polygon : polygons) {
            for (LatLng point : polygon) {
                //For each point in a polygon, form Point array
                routeCoordinates.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
            }
        }

        LineString lineString = LineString.fromLngLats(routeCoordinates);

        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});

        Source geoJsonSource = new GeoJsonSource("FILL_SOURCE_ID_" + fillColorCodeHex, featureCollection);
        mapboxMap.getStyle().addSource(geoJsonSource);

        FillLayer fillLayer = new FillLayer("FILL_LAYER_ID_" + fillColorCodeHex, "FILL_SOURCE_ID_" + fillColorCodeHex);
        fillLayer.setProperties(
                PropertyFactory.fillColor(Color.parseColor(fillColorCodeHex)),
                PropertyFactory.fillOpacity(0.7f));

        mapboxMap.getStyle().addLayer(fillLayer);
    }

    private void drawSamplePolygonsLayer(List<LatLng[]> polygons, int fillColorInt) {
        // Create a list to store our line coordinates
        List<Point> routeCoordinates = new ArrayList<>();

        for (LatLng[] polygon : polygons) {
            for (LatLng point : polygon) {
                //For each point in a polygon, form Point array
                routeCoordinates.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
            }
        }

        LineString lineString = LineString.fromLngLats(routeCoordinates);

        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});

        Source geoJsonSource = new GeoJsonSource("FILL_SOURCE_ID_" + String.valueOf(fillColorInt), featureCollection);
        mapboxMap.getStyle().addSource(geoJsonSource);

        FillLayer fillLayer = new FillLayer("FILL_LAYER_ID_" + String.valueOf(fillColorInt), "FILL_SOURCE_ID_" + String.valueOf(fillColorInt));
        fillLayer.setProperties(
                PropertyFactory.fillColor(fillColorInt),
                PropertyFactory.fillOpacity(0.7f));

        mapboxMap.getStyle().addLayer(fillLayer);

        //Handle click
        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                handlePolygonLayerClick(point);

                return false;
            }
        });
    }

    public void handlePolygonLayerClick(LatLng point) {
        if (mapboxMap == null) return;

        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(pixel);

        if (features != null && features.size() > 0) {
            // clicked on a layer
            ArrayList<LatLng> polygonCoords = (ArrayList<LatLng>) features.get(0).geometry();
            Log.i("TEST", "Selected polygon " + polygonCoords.toString());
            Toast.makeText(AddPolygonLayerActivity.this, "Selected polygon " + polygonCoords.toString(), Toast.LENGTH_SHORT).show();
        }
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
}
