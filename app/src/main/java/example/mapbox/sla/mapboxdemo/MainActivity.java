//.........................................
//MAPBOX JAVA DEMOSTRATION TO LOAD SLA MAP
//Demostration shows basic steps to animate camera, add marker, load SLA map
//
//For more information please visit
//https://www.mapbox.com/android-sdk/#mapbox_android_sdk
//.........................................

package example.mapbox.sla.mapboxdemo;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends FragmentActivity {

    // MapBox
    private MapView mapView;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ImageView iv_openleftmenu;

    private String[] mLeftDrawerItemTitles;

    // SLA Basemap url, for more information go to https://docs.onemap.sg/#basemap

    public class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startAddMarkerActivity();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Must be before all mapbox operations
        Mapbox.getInstance(MainActivity.this, getResources().getString(R.string.mapbox_access_code));

        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.setStyleUrl(Constants.DEFAULT_BASEMAP_URL); //SET CUSTOM BASE MAP URL


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

                //region ADD polyline
                PolylineOptions polylineOptions = new PolylineOptions();
                LatLng polylinePoint1 = new LatLng(1.405050,103.711346);
                LatLng polylinePoint2 = new LatLng(1.334003,103.925236);
                polylineOptions .add(polylinePoint1,polylinePoint2)
                                .color(Color.RED)
                                .width(1f);
                mapboxMap.addPolyline(polylineOptions);
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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mLeftDrawerItemTitles = getResources().getStringArray(R.array.left_drawer_items);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mLeftDrawerItemTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        iv_openleftmenu = (ImageView) findViewById(R.id.iv_openleftmenu);
        iv_openleftmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
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

    private void startAddMarkerActivity() {
        Intent intent = new Intent(this, AddMarkerActivity.class);
        startActivity(intent);
    }
}
