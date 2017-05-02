package example.mapbox.sla.mapboxdemo.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Jeremy on 20/4/2016.
 */
class GetSessionTokenTask extends AsyncTask<String, Void, JSONObject> {

    private SessionTokenTaskListener sessionTokenTaskListener;
    private Context context;
    private String url;
    private String publictoken_url = "https://developers.onemap.sg/publicapi/publicsessionid";
    RequestQueue requestQueue;
    private RequestFuture<JSONObject> sessionIdFuture = RequestFuture.newFuture();
    private SharedPreferences sharedPreferences;

    public GetSessionTokenTask(Context context, SessionTokenTaskListener delegate) {
        this.sessionTokenTaskListener = delegate;
        this.context = context;
        this.requestQueue = MySingleton.getInstance(context).getRequestQueue();
        this.requestQueue.getCache().clear();
        this.url = publictoken_url;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sessionTokenTaskListener.onPreExecutingGetSessionTokenTask();
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result)
    {
        if(result==null) {
            sessionTokenTaskListener.finishGettingSessionToken(null);
            return;
        }

        Log.i("TEST", result.toString());
        try {
            String token = result.getString("access_token");
            sessionTokenTaskListener.finishGettingSessionToken(token);
        }
        catch (JSONException e) {
            Log.e("TEST", "", e);
        }
    }

    public void startGettingSessionTokenVolley() {

        sessionTokenTaskListener.onPreExecutingGetSessionTokenTask();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response==null) {
                            sessionTokenTaskListener.finishGettingSessionToken(null);
                            return;
                        }

                        Log.i("TEST", response.toString());
                        try {
                            String token = response.getString("access_token");
                            sessionTokenTaskListener.finishGettingSessionToken(token);
                        }
                        catch (JSONException e) {
                            Log.e("TEST", "", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sessionTokenTaskListener.finishGettingSessionToken(null);
                    }
                });

        jsonObjectRequest.setRetryPolicy(VolleyUtils.getDefaultRetryPolicy());
        requestQueue.add(jsonObjectRequest);
    }

    public interface SessionTokenTaskListener {
        void onPreExecutingGetSessionTokenTask();
        void finishGettingSessionToken(String token);
    }
}
