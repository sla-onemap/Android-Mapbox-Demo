//.........................................
//MAPBOX JAVA DEMOSTRATION TO LOAD SLA MAP
//Demostration shows basic steps to animate camera, add marker, load SLA map
//
//For more information please visit
//https://www.mapbox.com/android-sdk/#mapbox_android_sdk
//.........................................

package example.mapbox.sla.mapboxdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MainActivity extends FragmentActivity {
    // MapBox
    private MapView mapView;

    private DrawerLayout mDrawerLayout;

    // SLA Basemap url, for more information go to https://docs.onemap.sg/#basemap

    public class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startAddMarkerActivity();
                    break;
                case 1:
                    startAddPolylineActivity();
                    break;
                case 2:
                    startAddMarkersWithCustomInfoWindowActivity();
                    break;
                case 3:
                    startPolygonLayerActivity();
                    break;
                case 4:
                    startShowNightBaseMapActivity();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView mDrawerList;
        ImageView iv_openleftmenu;

        String[] mLeftDrawerItemTitles;

        //Must be before all mapbox operations
        Mapbox.getInstance(MainActivity.this, getResources().getString(R.string.mapbox_access_code));

        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState); //This is essential for mapbox to work
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {
                mapboxMap.setStyle(Constants.DEFAULT_BASEMAP_URL, new Style.OnStyleLoaded() { //SET CUSTOM BASE MAP URL
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        //region ZOOM TO LOCATION
                        LatLng zoomLocation = new LatLng(1.358479, 103.815201);
                        CameraPosition position = new CameraPosition.Builder()
                                .target(zoomLocation)
                                .zoom(12) // Sets the zoom
                                .build(); // Creates a CameraPosition from the builder
                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 2000);
                        //endregion

                        //region remove mapbox attrition and logo
                        mapboxMap.getUiSettings().setAttributionEnabled(false);
                        mapboxMap.getUiSettings().setLogoEnabled(false);
                        //endregion
                    }
                });
            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        mLeftDrawerItemTitles = getResources().getStringArray(R.array.left_drawer_items);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mLeftDrawerItemTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        iv_openleftmenu = findViewById(R.id.iv_openleftmenu);
        iv_openleftmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
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

    //region Helpers
    private void startAddMarkerActivity() {
        Intent intent = new Intent(this, AddMarkerActivity.class);
        startActivity(intent);
    }

    private void startAddPolylineActivity() {
        Intent intent = new Intent(this, AddPolylineActivity.class);
        startActivity(intent);
    }

    private void startAddMarkersWithCustomInfoWindowActivity() {
        Intent intent = new Intent(this, AddMarkersWithCustomInfoWindowActivity.class);
        startActivity(intent);
    }

    private void startPolygonLayerActivity() {
        Intent intent = new Intent(this, AddPolygonLayerActivity.class);
        startActivity(intent);
    }

    private void startShowNightBaseMapActivity() {
        Intent intent = new Intent(this, ShowNightBaseMapActivity.class);
        startActivity(intent);
    }
    //endregion
}
