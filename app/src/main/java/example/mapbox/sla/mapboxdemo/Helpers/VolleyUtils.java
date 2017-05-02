package example.mapbox.sla.mapboxdemo.Helpers;

import com.android.volley.DefaultRetryPolicy;


/**
 * Created by Jeremy on 6/9/16.
 */
public class VolleyUtils {
    public static DefaultRetryPolicy getDefaultRetryPolicy() {
        return new DefaultRetryPolicy(
                120*1000, //2 mins
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}
