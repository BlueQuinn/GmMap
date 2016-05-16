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

import AsyncTask.DestinationAst;
import DTO.Destination;
import Sqlite.SqliteHelper;
import vmwares.in.lequan.gmmap.MainActivity;

/**
 * Created by lequan on 4/28/2016.
 */
public class HistoryFragment extends DestinationFragment
{
    public HistoryFragment()
    {

    }

    @Override
    void initAsyncTask()
    {
        asyncTask = new DestinationAst("History");
    }

    public void save(String place, String address)
    {
        MainActivity.dbHelper.delete("History", place);
        MainActivity.dbHelper.insert("History", place, address);
    }

    @Override
    void onSelected(int position)
    {
        listener.onSelected(list.get(position).getName());
    }
}
