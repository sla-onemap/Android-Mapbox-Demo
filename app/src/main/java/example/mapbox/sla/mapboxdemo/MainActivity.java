//.........................................
//MAPBOX JAVA DEMOSTRATION TO LOAD SLA MAP
//Demostration shows basic steps to animate camera, add marker, load SLA map
//
//For more information please visit
//https://www.mapbox.com/android-sdk/#mapbox_android_sdk
//.........................................

package example.mapbox.sla.mapboxdemo;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
            public void onMapReady(final MapboxMap mapboxMap) {


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
                mapboxMap.setAllowConcurrentMultipleOpenInfoWindows(true); //Enable concurrent opening of marker info window
                mapboxMap.getUiSettings().setDeselectMarkersOnTap(false);  //Control hiding of marker info window on map tap

                MarkerOptions markerOptions1 = new MarkerOptions();
                markerOptions1.position(zoomLocation).title("Marker 1").snippet("Marker snippet 1!");
                final Marker marker1 = mapboxMap.addMarker(markerOptions1);
                mapboxMap.selectMarker(marker1);

                MarkerOptions markerOptions2 = new MarkerOptions();
                markerOptions2.position( new LatLng(1.358479,103.715201)).title("Marker 2").snippet("Marker snippet 2!");
                final Marker marker2 = mapboxMap.addMarker(markerOptions2);
                mapboxMap.selectMarker(marker2);
                //endregion

                //region mapboxMap event handlers
                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Log.i("TEST", "On marker clicked");
                        //NOTE: Will not be fired if marker info window is shown.
                        return false;
                    }
                });
                mapboxMap.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                    @Override
                    public boolean onInfoWindowClick(@NonNull Marker marker) {
                        Log.i("TEST", "On marker info window clicked");
                        return true; //Set to true to disable hiding of info window when clicked
                    }
                });
                mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng point) {
                        //Handles long click event on app
                    }
                });
                //endregion

            }
        });
    }
}
