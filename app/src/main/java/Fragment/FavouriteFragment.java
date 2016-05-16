package Fragment;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import AsyncTask.DestinationAst;
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
    void initAsyncTask()
    {
        asyncTask = new DestinationAst("Favourite");
    }

    @Override
    void onSelected(int position)
    {
        listener.onSelected(list.get(position).getName());
    }
}
