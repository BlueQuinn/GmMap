package Fragment;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import DTO.Destination;
import Sqlite.SqliteHelper;
import vmwares.in.lequan.gmmap.MainActivity;

/**
 * Created by lequan on 4/28/2016.
 */
public class HistoryFragment extends DestinationFragment
{
    Geocoder geocoder;
    String dbName;

    public HistoryFragment()
    {

    }

    //SqliteHelper MainActivity.dbHelper;

    //SQLiteDatabase MainActivity.dbHelper;
    @Override
    void loadDestination()
    {
        load();
    }

    @Override
    void init()
    {
        /*Activity activity = getActivity();
        geocoder = new Geocoder(activity, Locale.getDefault());
        dbName = "Destination.sqlite";//getActivity().getApplication().getFilesDir() + "/" + "Destination";
        //MainActivity.dbHelper = new SqliteHelper(activity.getApplicationContext(), "Destination.sqlite");
        try
        {
            MainActivity.dbHelper.createDataBase();
            MainActivity.dbHelper.openDataBase();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/
    }

    void load()
    {
        ArrayList<HashMap<String, String>> listRow = MainActivity.dbHelper.getAll("History");
        //for (int i = 0; i < list.size(); ++i)
        for (HashMap<String, String> row : listRow)
        {
            /*double lat = Double.parseDouble(row.get("Lat"));
            double lng = Double.parseDouble(row.get("Lng"));
            try
            {
                String[] address = getAddress(new LatLng(lat, lng));
                list.add(new Contact(address[0], address[1]));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/


            list.add(new Destination(row.get("Place"), row.get("Address")));
        }

    }

    String[] getAddress(LatLng position) throws IOException
    {
        String currentAddress = "";
        Address address = geocoder.getFromLocation(position.latitude, position.longitude, 3).get(0);
        for (int i = 0; i < address.getMaxAddressLineIndex() - 1; i++)
            currentAddress += address.getAddressLine(i) + ", ";
        currentAddress += address.getAddressLine(address.getMaxAddressLineIndex() - 1);

        String featureName = address.getFeatureName();
        if (featureName == null)
        {
            featureName = currentAddress;
            currentAddress = "";
        }
        return new String[]{featureName, currentAddress};
    }

    public void save(double lat, double lng)
    {
        MainActivity.dbHelper.delete("History", lat, lng);
        MainActivity.dbHelper.insert("History", lat, lng);
    }

    public void save(String place, String address)
    {
        MainActivity.dbHelper.delete("History", place);
        MainActivity.dbHelper.insert("History", place, address);
    }

    @Override
    void action(int position)
    {
        listener.onSelected(list.get(position).getName());
    }
}
