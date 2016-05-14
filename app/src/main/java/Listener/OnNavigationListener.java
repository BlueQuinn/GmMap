package Listener;

import com.google.android.gms.location.places.Place;

/**
 * Created by lequan on 4/23/2016.
 */
public interface OnNavigationListener
{
    void onNavigation(Place startLocation, Place endLocation);
}
