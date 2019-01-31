package example.mapbox.sla.mapboxdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class AddPolylineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapView mapView;

        setContentView(R.layout.activity_add_polyline);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add polyline");
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
                        List<Point> routeCoordinates = new ArrayList<Point>();

                        routeCoordinates.add(Point.fromLngLat(103.711346, 1.405050));
                        routeCoordinates.add(Point.fromLngLat(103.925236, 1.334003));

                        LineString lineString = LineString.fromLngLats(routeCoordinates);

                        FeatureCollection featureCollection =
                                FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(lineString)});

                        GeoJsonSource geoJsonSource = new GeoJsonSource("LAYER_SOURCE_ID", featureCollection);
                        mapboxMap.getStyle().addSource(geoJsonSource);

                        LineLayer lineLayer = new LineLayer("LINE_LAYER_ID", "LAYER_SOURCE_ID");

                        lineLayer.setProperties(
                                PropertyFactory.lineColor(Color.RED),
                                PropertyFactory.fillOpacity(1f));

                        mapboxMap.getStyle().addLayer(lineLayer);
                    }
                });
            }
        });
    }
}
