package AsyncTask;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import Map.OnLoadListener;
import Map.Navigate;

/**
 * Created by lequan on 4/22/2016.
 */
public class NavigateAst extends AsyncTask<LatLng, Integer, ArrayList<LatLng>>
{
    OnLoadListener listener;
    String mode;

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public void setOnLoadListener(OnLoadListener listener)
    {
        this.listener = listener;
    }

    public NavigateAst()
    {
        mode = "driving";
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
    protected ArrayList<LatLng> doInBackground(LatLng... params)
    {

        return Navigate.getDirection(Navigate.getJSON(params[0], params[1], mode));
        //Navigate.getJSON(new LatLng(10.777433, 106.677568), new LatLng(10.774086, 106.671757), "driving");
       // Navigate.getDirection(Navigate.getJSON(new LatLng(10.777433, 106.677568), new LatLng(10.774086, 106.671757), "driving"));
        //return null;
    }

}
