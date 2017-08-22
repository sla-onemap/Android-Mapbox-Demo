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
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import example.mapbox.sla.mapboxdemo.Helpers.MySingleton;

public class MainActivity extends FragmentActivity {

    // MapBox
    private MapView mapView;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ImageView iv_openleftmenu;

    private String[] mLeftDrawerItemTitles;

    private RequestQueue requestQueue;

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
                    startLineLayerActivity();
                    break;
                case 4:
                    startPolygonLayerActivity();
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

        mapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
            @Override
            public void onMapChanged(int change) {

            }
        });

        //Callback when map finish loading and is ready to be used
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                //region ZOOM TO LOCATION
                LatLng zoomLocation = new LatLng(1.358479,103.815201);
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

        this.requestQueue = MySingleton.getInstance(MainActivity.this).getRequestQueue();
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

    private void startLineLayerActivity() {
        Intent intent = new Intent(this, AddLineLayerActivity.class);
        startActivity(intent);
    }

    private void startPolygonLayerActivity() {
        Intent intent = new Intent(this, AddPolygonLayerActivity.class);
        startActivity(intent);
    }
    //endregion
}
