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

import com.mapbox.mapboxsdk.annotations.Marker;
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
    // SLA Basemap url, for more information go to https://docs.onemap.sg/#basemap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //VERY IMPORTANT LINE OR ELSE APP WILL CRASH
        mapView.setStyleUrl(vibrant_city); //SET CUSTOM BASE MAP URL

        //Callback when map finish loading and is ready to be used
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                //region ZOOM TO LOCATION
                LatLng zoomLocation = new LatLng(1.358479,103.815201);
                CameraPosition position = new CameraPosition.Builder()
                        .target(zoomLocation)
                        .zoom(12) // Sets the zoom
                        .build(); // Creates a CameraPosition from the builder
                mapboxMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 2000);
                //endregion

                //region ADD MARKER
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(zoomLocation).title("Marker Title").snippet("This is a marker snippet!");
                mapboxMap.addMarker(markerOptions);
                //endregion

                //region CUSTOM MARKER INFO WINDOW
                //Comment out to use back default marker info window
                CustomMarkerInfoWindow customMarkerInfoWindow = new CustomMarkerInfoWindow(MainActivity.this);
                mapboxMap.setInfoWindowAdapter(customMarkerInfoWindow);
                mapboxMap.setOnInfoWindowCloseListener(new MapboxMap.OnInfoWindowCloseListener() {
                    @Override
                    public void onInfoWindowClose(Marker marker) {

                    }
                });
                //endregion

            }
        });
    }
}
