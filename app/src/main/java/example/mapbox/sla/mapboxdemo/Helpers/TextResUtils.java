package example.mapbox.sla.mapboxdemo.Helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by user on 22/8/17.
 */

public class TextResUtils {
    public static JSONObject readJSONFromRes(final Context context, int resId) {
        //Get Data From Text Resource File Contains Json Data.
        InputStream inputStream = context.getResources().openRawResource(resId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            Log.e("TEST", "", e);
        }
        Log.v("TEST", byteArrayOutputStream.toString());
        try {
            // Parse the data into JSONObject to get original data in form of json.
            JSONObject jObject = new JSONObject(byteArrayOutputStream.toString());
            return jObject;

        } catch (JSONException e) {
            Log.e("TEST", "", e);
            return null;
        }
    }
}
