package example.mapbox.sla.mapboxdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Use the Mapbox MarkerView Plugin to create a marker which uses a custom Android view as the icon.
 */
public class AddMarkersWithCustomInfoWindowActivity extends AppCompatActivity implements
        OnMapReadyCallback {
    private MapboxMap mapboxMap;
    private MapView mapView;
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_markers_custom_infowindow);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Markers with custom info window");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // refer to https://docs.mapbox.com/android/maps/examples/symbol-layer-info-window/
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Constants.DEFAULT_BASEMAP_URL, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                initMarkerViewManager();
                initLayout();
            }
        });
    }

    private void initMarkerViewManager() {
        // Initialize the MarkerViewManager
        markerViewManager = new MarkerViewManager(mapView, mapboxMap);
    }

    private void initLayout() {
        // Use an XML layout to create a View object
        View customView = LayoutInflater.from(AddMarkersWithCustomInfoWindowActivity.this).inflate(
                R.layout.custom_marker_info_window, null);
        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

        // Set the View's TextViews with content
        TextView titleTextView = customView.findViewById(R.id.tv_marker_title);
        titleTextView.setText("Marker title");

        // Use the View to create a MarkerView which will eventually be given to
        // the plugin's MarkerViewManager class
        markerView = new MarkerView(new LatLng(1.34, 103.82), customView);
        markerViewManager.addMarker(markerView);

        customView.setVisibility(View.VISIBLE);
    }

    //........................................
    //Do not remove, essential to use Mapbox
    //........................................
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (markerViewManager != null) {
            markerViewManager.onDestroy();
        }
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
