package Fragment;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import DTO.Destination;
import vmwares.in.lequan.gmmap.MainActivity;

/**
 * Created by lequan on 4/28/2016.
 */
public class FavouriteFragment extends DestinationFragment
{
    public FavouriteFragment()
    {

    }

    @Override
    void loadDestination()
    {
        load();
    }

    @Override
    void init()
    {

    }

    void load()
    {
        ArrayList<HashMap<String, String>> listRow = MainActivity.dbHelper.getAll("Favourite");
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
