package example.mapbox.sla.mapboxdemo;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by user on 2/12/16.
 */

public class CustomMarkerInfoWindow implements MapboxMap.InfoWindowAdapter {

    private Activity activity;

    public CustomMarkerInfoWindow(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View getInfoWindow(final Marker marker)
    {
        final View v = View.inflate(activity, R.layout.custom_marker_info_window, null);
        TextView tv_title =  (TextView) v.findViewById(R.id.tv_title);
        TextView tv_snippet =  (TextView) v.findViewById(R.id.tv_snippet);

        //Setting values from marker
        tv_title.setText(marker.getTitle());
        tv_snippet.setText(marker.getSnippet());
        return v;
    }
}
