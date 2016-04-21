package vmwares.in.lequan.gmmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import Fragment.PlacePickerFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, View.OnLongClickListener {
    GoogleMap map;
    Geocoder geocoder;
    //Marker currentMarker;
    GroundOverlay pin;
    Button btnTrack;
    Button btnDirection;
    PlacePickerFragment fragmentPlace;
    boolean track = true;
    LocationManager locationManager;
    Semaphore lock = new Semaphore(1);
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        geocoder = new Geocoder(this, Locale.getDefault());
        btnTrack = (Button) findViewById(R.id.btnTrack);
        btnDirection = (Button) findViewById(R.id.btnDirection);
        btnTrack.setOnClickListener(this);
        btnDirection.setOnClickListener(this);
        btnTrack.setOnLongClickListener(this);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragmentPlace = (PlacePickerFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        fragmentPlace.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) { // Handle the selected Place
                LatLng point = place.getLatLng();
                //currentMarker.setPosition(point);
                pin.setPosition(point);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
            }

            @Override
            public void onError(Status status) { // Handle the error
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                *//*FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, autocompleteFragment);
                transaction.commit();*//*
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.d("123", "123");


        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                .position(new LatLng(50, 6), 50f, 50f);
        MarkerOptions a = new MarkerOptions().position(new LatLng(50, 6));
        //currentMarker = map.addMarker(a);
        pin =  map.addGroundOverlay(newarkMap);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(14.058324, 108.277199), 16));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        initLocation();
        /*map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //Log.d("123", "" + track);

                //if (track) {
                    Log.d("123", "234");
                //    track = false;
                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    currentMarker.setPosition(point);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, map.getCameraPosition().zoom));
                //}
            }
        });*/

    }

    void initLocation()
    {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                //currentMarker.setPosition(point);
                pin.setPosition(point);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, map.getCameraPosition().zoom));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.removeUpdates(locationListener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnTrack) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            //LatLng position = currentMarker.getPosition();
            LatLng position = pin.getPosition();
            try {
                String currentAddress = "";
                Address address =geocoder.getFromLocation(position.latitude, position.longitude, 1).get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex() - 1; i++)
                    currentAddress += address.getAddressLine(i) + ", ";
                currentAddress += address.getAddressLine(address.getMaxAddressLineIndex() - 1);
                fragmentPlace.setText(currentAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, map.getCameraPosition().zoom));
        }
        else {
startActivity(new Intent(this, DirectionActivity.class));
        }
    }

    @Override
    public boolean onLongClick(View v) {
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(), map.getCameraPosition().zoom));

        return false;
    }
}

