package example.mapbox.sla.mapboxdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class AddMarkersWithCustomInfoWindowActivity extends AppCompatActivity {
    // MapBox
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_markers_custom_infowindow);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Markers with custom info window");
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
                        //region CUSTOM MARKER INFO WINDOW
                        CustomMarkerInfoWindow customMarkerInfoWindow = new CustomMarkerInfoWindow(AddMarkersWithCustomInfoWindowActivity.this);
                        mapboxMap.setInfoWindowAdapter(customMarkerInfoWindow);
                        mapboxMap.setOnInfoWindowCloseListener(new MapboxMap.OnInfoWindowCloseListener() {
                            @Override
                            public void onInfoWindowClose(Marker marker) {

                            }
                        });
                        //endregion

                        //region ADD MARKERS
                        mapboxMap.setAllowConcurrentMultipleOpenInfoWindows(true); //Enable concurrent opening of marker info window
                        mapboxMap.getUiSettings().setDeselectMarkersOnTap(false);  //Control hiding of marker info window on map tap

                        MarkerOptions markerOptions1 = new MarkerOptions();
                        markerOptions1.position(new LatLng(1.34, 103.82)).title("Marker 1").snippet("Marker snippet 1!");
                        final Marker marker1 = mapboxMap.addMarker(markerOptions1);
                        mapboxMap.selectMarker(marker1);

                        MarkerOptions markerOptions2 = new MarkerOptions();
                        markerOptions2.position(new LatLng(1.35, 103.72)).title("Marker 2").snippet("Marker snippet 2!");
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
                        //endregion
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
}
