package AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import DTO.Place;
import Listener.OnLoadListener;
import MapAPI.PlaceAPI;
import Utils.JsonUtils;

/**
 * Created by lequan on 5/13/2016.
 */
public class FindPlaceAst extends AsyncTask<Double, Integer, ArrayList<Place>>
{
    Context context;
    OnLoadListener<ArrayList<Place>> listener;
    String type;

    public void setOnLoadListener(OnLoadListener listener)
    {
        this.listener = listener;
    }

    public FindPlaceAst(Context context, String type)
    {
        this.context = context;
        this.type = type;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Place> result)
    {
        listener.onLoaded(result);
        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<Place> doInBackground(Double... params)
    {
        String url = PlaceAPI.createPlaceUrlRequest(context, params[0].doubleValue(), params[1].doubleValue(), type);
        return PlaceAPI.getPlaces(JsonUtils.getJSON(url));
    }

}
