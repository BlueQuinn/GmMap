package AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import Listener.OnLoadListener;
import MapAPI.Place;
import Utils.JsonUtils;

/**
 * Created by lequan on 5/13/2016.
 */
public class FindPlaceAst extends AsyncTask<Double, Integer, ArrayList<LatLng>>
{
    Context context;
    OnLoadListener listener;
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
    protected void onPostExecute(ArrayList<LatLng> result)
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
    protected ArrayList<LatLng> doInBackground(Double... params)
    {
        String url = Place.createPlaceUrlRequest(context, params[0].doubleValue(), params[1].doubleValue(), type);
        return Place.getPlaces(JsonUtils.getJSON(url));
    }

}
