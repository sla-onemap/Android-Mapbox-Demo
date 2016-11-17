//.........................................
//MAPBOX JAVA DEMOSTRATION TO LOAD SLA MAP
//Demostration shows basic steps to animate camera, add marker, load SLA map
//
//For more information please visit
//https://www.mapbox.com/android-sdk/#mapbox_android_sdk
//.........................................

package example.mapbox.sla.mapboxdemo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends FragmentActivity {

    // MapBox
    private MapView mapView;

    private String vibrant_city = "https://maps-json.onemap.sg/Default.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //VERY IMPORTANT LINE OR ELSE APP WILL CRASH
        mapView.setStyleUrl(vibrant_city); //SET CUSTOM BASE MAP URL
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                //ZOOM TO LOCATION
                LatLng zoomLocation = new LatLng(1.358479,103.815201);
                CameraPosition position = new CameraPosition.Builder()
                        .target(zoomLocation)
                        .zoom(12) // Sets the zoom
                        .build(); // Creates a CameraPosition from the builder
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 2000);
                //ZOOM TO LOCATION ENDS

                //ADD MARKER
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(zoomLocation).title("Marker Title").snippet("This is a marker snippet");
                mapboxMap.addMarker(markerOptions);
                //ADD MARKER ENDS

            }
        });
    }
}
