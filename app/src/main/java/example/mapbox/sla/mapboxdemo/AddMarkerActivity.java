package example.mapbox.sla.mapboxdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class AddMarkerActivity extends AppCompatActivity {

    // MapBox
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        getSupportActionBar().setTitle("Add marker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.setStyleUrl(Constants.DEFAULT_BASEMAP_URL); //SET CUSTOM BASE MAP URL
        //Callback when map finish loading and is ready to be used
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                //Add a marker at latitude:1.358479 and longitude:103.815201
                LatLng latLng = new LatLng(1.358479, 103.815201);
                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(latLng).title("Marker Title").snippet("Marker snippet");
                final Marker marker1 = mapboxMap.addMarker(markerOptions1);
                mapboxMap.selectMarker(marker1);
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
}
