package MapAPI;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 5/13/2016.
 */
public class Place
{
    public static String createPlaceUrlRequest(Context context, double latitude, double longitude, String type)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 10000);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&key=" + context.getResources().getString(R.string.google_maps_key));
        return googlePlacesUrl.toString();
    }

    public static ArrayList<LatLng> getPlaces(JSONObject object)
    {
        ArrayList<LatLng> placesList = new ArrayList<>();
        try
        {
            JSONArray places = object.getJSONArray("results");
            for (int i = 0; i < places.length(); ++i)
            {
                JSONObject location = places.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                placesList.add(new LatLng(location.getDouble("lat"), location.getDouble("lng")));            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return placesList;
    }
}