package example.mapbox.sla.mapboxdemo;

import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

public class AddMarkerActivity extends AppCompatActivity {
    // MapBox
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add marker");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.getMapAsync(new OnMapReadyCallback() { //Callback when map finish loading and is ready to be used
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Constants.DEFAULT_BASEMAP_URL, new Style.OnStyleLoaded() { //SET CUSTOM BASE MAP URL
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // refer to https://docs.mapbox.com/help/tutorials/first-steps-android-sdk/#add-a-marker
                        style.addImage("marker-icon-id",
                                BitmapFactory.decodeResource(
                                        AddMarkerActivity.this.getResources(), R.drawable.mapbox_marker_icon_default));

                        //Add a marker at latitude:1.358479 and longitude:103.815201
                        GeoJsonSource geoJsonSource = new GeoJsonSource("SYMBOL_SOURCE_ID", Feature.fromGeometry(
                                Point.fromLngLat(103.815201, 1.358479)));
                        style.addSource(geoJsonSource);

                        SymbolLayer symbolLayer = new SymbolLayer("SYMBOL_LAYER_ID", "SYMBOL_SOURCE_ID");
                        symbolLayer.withProperties(
                                PropertyFactory.iconImage("marker-icon-id"),
                                PropertyFactory.iconAllowOverlap(true)
                        );

                        style.addLayer(symbolLayer);

                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                // refer to https://docs.mapbox.com/android/maps/examples/symbol-layer-info-window/
                                handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));

                                return false;
                            }
                        });
                    }
                });
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

    /**
     * This method handles click events for SymbolLayer symbols.
     *
     * @param screenPoint the point on screen clicked
     */
    private void handleClickIcon(PointF screenPoint) {
        Toast toast = Toast.makeText(getApplicationContext(), "Marker clicked", Toast.LENGTH_LONG);

        toast.show();
    }
}
