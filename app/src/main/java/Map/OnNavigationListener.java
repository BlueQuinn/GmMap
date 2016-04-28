package Map;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by lequan on 4/23/2016.
 */
public interface OnNavigationListener
{
    void onNavigation(Place startLocation, Place endLocation);
}
