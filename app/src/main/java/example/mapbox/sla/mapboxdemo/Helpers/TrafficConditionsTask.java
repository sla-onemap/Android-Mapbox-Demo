package example.mapbox.sla.mapboxdemo.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import example.mapbox.sla.mapboxdemo.R;


/**
 * Created by user on 5/12/16.
 */

public class TrafficConditionsTask implements
        GetSessionTokenTask.SessionTokenTaskListener {

    private int mode;

    private TrafficConditionsTaskListener mListener;
    private Context context;
    private RequestQueue requestQueue;

    private ProgressDialog progress;

    private final int GET_TRAFFIC_CONDITIONS = 1;

    public TrafficConditionsTask(Context context, TrafficConditionsTaskListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.requestQueue = MySingleton.getInstance(context).getRequestQueue();
    }

    private void startGettingSessionToken() {
        GetSessionTokenTask getPublicSessionTokenTask = new GetSessionTokenTask(context, this);
        getPublicSessionTokenTask.startGettingSessionTokenVolley();
    }

    public void startGetTrafficCondition() {
        mode = GET_TRAFFIC_CONDITIONS;
        startGettingSessionToken();
    }


    private void startGetTrafficConditionVolleyTask(String token) {
        String url = getTrafficConditionUrl(token);
        Log.i("TEST", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response == null) {
                            progress.dismiss();
                            Toast.makeText(context, "Unable to get traffic condition", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progress.dismiss();
                            Log.i("TEST", response.toString());
                            mListener.onFinishGetTrafficCondition(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(context, "Unable to get traffic condition", Toast.LENGTH_SHORT);
                    }
                });
        this.requestQueue.getCache().clear();
        jsonArrayRequest.setRetryPolicy(VolleyUtils.getDefaultRetryPolicy());
        this.requestQueue.add(jsonArrayRequest);
    }

    private String getTrafficConditionUrl(String token) {
        final String TRAFFIC_CONDITION_URL = "https://developers.onemap.sg/publicapi/traffic/retrieveTrafficData?token={{token}}";
        return TRAFFIC_CONDITION_URL.replace("{{token}}", token);
    }

    //region GetSessionTokenTask delegate methods
    //................................................................
    @Override
    public void onPreExecutingGetSessionTokenTask() {
        progress = ProgressDialog.show(context, "", context.getString(R.string.general_loading_msg), true);
        progress.setCancelable(true);
        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
    }
    @Override
    public void finishGettingSessionToken(String token) {
        if(token != null) {
            switch (mode) {
                case GET_TRAFFIC_CONDITIONS:
                    startGetTrafficConditionVolleyTask(token);
                    break;
            }
        }
    }
    //endregion

    public interface TrafficConditionsTaskListener {
        void onFinishGetTrafficCondition(JSONArray res);
    }
}
