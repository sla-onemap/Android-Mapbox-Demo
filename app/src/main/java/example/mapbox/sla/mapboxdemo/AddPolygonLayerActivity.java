package example.mapbox.sla.mapboxdemo;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.commons.geojson.Polygon;

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

    private List<LatLng[]> polygons = new ArrayList();
    private String polygonJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_polygon_layer);

        getSupportActionBar().setTitle("Add Polygon Layer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final JSONObject polygonJson = TextResUtils.readJSONFromRes(AddPolygonLayerActivity.this, R.raw.polygons);
        Log.i("TEST", polygonJson.toString());

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.setStyleUrl(Constants.DEFAULT_BASEMAP_URL); //SET CUSTOM BASE MAP URL
        //Callback when map finish loading and is ready to be used
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                AddPolygonLayerActivity.this.mapboxMap =  mapboxMap;

                //Process polygon json file
                try {
                    JSONArray Polygons = polygonJson.getJSONArray("Polygons");
                    for(int i=0; i<Polygons.length();i++) {
                        JSONObject polygonElem = Polygons.getJSONObject(i);
                        JSONArray coordinates = polygonElem.getJSONArray("coordinates");
                        for(int j=0; j<coordinates.length(); j++) {
                            JSONArray ringCoords = coordinates.getJSONArray(j);
                            LatLng [] latLngs = new LatLng[ringCoords.length()];
                            for(int k=0; k<ringCoords.length(); k++) {
                                JSONArray coordinate = ringCoords.getJSONArray(k);
                                latLngs[k] = new LatLng(coordinate.getDouble(1), coordinate.getDouble(0));
                            }
                            polygons.add(latLngs);
                        }
                    }

                    mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng point) {
                            handlePolygonLayerClick(point);
                        }
                    });
                }
                catch (JSONException e) {
                    Log.e("TEST", "", e);
                }

                drawLandQueryPolygonLayer(polygons, "#A52A2A");
            }
        });
    }

    private void drawLandQueryPolygonLayer(List<LatLng[]> polygons, String fillColorCode) {


        // Create a list to store our line coordinates.
        List<List<Position>> polygonsArr = new ArrayList<>();

        for (LatLng[] polygon : polygons) {
            List<Position> polygonCoordinates = new ArrayList<>();
            for (LatLng point : polygon) {
                //For each point in a polygon, form Position array
                polygonCoordinates.add(Position.fromCoordinates(point.getLongitude(), point.getLatitude()));
            }
            polygonsArr.add(polygonCoordinates);
        }

        Polygon polygonsString = Polygon.fromCoordinates(polygonsArr);
        FeatureCollection featureCollection =
                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(polygonsString)});

        Source geoJsonSource = new GeoJsonSource("POLYGON_SRC_ID", featureCollection);
        mapboxMap.addSource(geoJsonSource);

        FillLayer fillLayer = new FillLayer("POLYGON_LAYER_ID", "POLYGON_SRC_ID");
        fillLayer.setProperties(
                PropertyFactory.fillColor(Color.parseColor(fillColorCode)),
                PropertyFactory.fillOpacity(0.7f));

        mapboxMap.addLayer(fillLayer);
    }

    public void handlePolygonLayerClick(LatLng point) {

        if(mapboxMap == null) return;

        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = mapboxMap.queryRenderedFeatures(pixel, "POLYGON_LAYER_ID");


        ArrayList<LatLng> polygonCoords = (ArrayList<LatLng>) features.get(0).getGeometry().getCoordinates();
        Log.i("TEST", "Selected polygon " + polygonCoords.toString());
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
