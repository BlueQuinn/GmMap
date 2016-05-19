package MapAPI;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import DTO.Place;
import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 5/13/2016.
 */
public class PlaceAPI
{
    public static String createPlaceUrlRequest(Context context, double latitude, double longitude, String type)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 5000);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&key=" + context.getResources().getString(R.string.google_maps_key));
        Log.d("123", "url = " + googlePlacesUrl.toString());
        return googlePlacesUrl.toString();
    }

    public static ArrayList<Place> getPlaces(JSONObject object)
    {
        ArrayList<Place> placesList = new ArrayList<>();
        try
        {
            JSONArray places = object.getJSONArray("results");
            for (int i = 0; i < places.length(); ++i)
            {
                JSONObject item = places.getJSONObject(i);
                JSONObject location = item.getJSONObject("geometry").getJSONObject("location");
                Place place = new Place(location.getDouble("lat"), location.getDouble("lng"), item.getString("name"), item.getString("vicinity"));
                placesList.add(place);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return placesList;
    }
}