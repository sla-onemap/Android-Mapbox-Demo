package example.mapbox.sla.mapboxdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class AddPolylineActivity extends AppCompatActivity {
    // MapBox
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_polyline);

        getSupportActionBar().setTitle("Add polyline");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.setStyleUrl(Constants.DEFAULT_BASEMAP_URL); //SET CUSTOM BASE MAP URL
        //Callback when map finish loading and is ready to be used
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                PolylineOptions polylineOptions = new PolylineOptions();
                LatLng polylinePoint1 = new LatLng(1.405050,103.711346);
                LatLng polylinePoint2 = new LatLng(1.334003,103.925236);
                polylineOptions .add(polylinePoint1,polylinePoint2)
                        .color(Color.RED)
                        .width(1f);
                mapboxMap.addPolyline(polylineOptions);
            }
        });
    }
}
