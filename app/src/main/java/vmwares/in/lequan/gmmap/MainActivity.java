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
import android.widget.ImageButton;
import android.widget.TextView;

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
import java.util.Locale;

import AsyncTask.NavigateAst;
import Fragment.PlacePickerFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, View.OnLongClickListener, GoogleMap.OnMyLocationChangeListener {
    GoogleMap map;
    Geocoder geocoder;
    Button btnTrack;
    Button btnDirection;
    //PlacePickerFragment fragmentPlace;
LatLng myLocation;
    Marker marker;
    ImageButton btnMenu;
    TextView txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        geocoder = new Geocoder(this, Locale.getDefault());

        btnTrack = (Button) findViewById(R.id.btnTrack);
        btnDirection = (Button) findViewById(R.id.btnDirection);
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        txtSearch = (TextView) findViewById(R.id.txtSearch);

        btnTrack.setOnClickListener(this);
        btnDirection.setOnClickListener(this);
        txtSearch.setOnClickListener(this);
        btnMenu.setOnClickListener(this);

        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);


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
        marker = map.addMarker(new MarkerOptions().position(new LatLng(0,0)));
        marker.setVisible(false);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(14.058324, 108.277199), 15));
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

        //initLocation();
        map.setOnMyLocationChangeListener(this);
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        myLocation = point;
        //marker.setPosition(point);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, map.getCameraPosition().zoom));
        map.setOnMyLocationChangeListener(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && data != null)
        {
            String address = data.getStringExtra("address");
            txtSearch.setText(address);

            LatLng position = data.getParcelableExtra("position");
            marker.setPosition(position);
            marker.setVisible(true);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnTrack:
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
                map.setOnMyLocationChangeListener(this);
                LatLng position = myLocation;
                try {
                    String currentAddress = "";
                    Address address = geocoder.getFromLocation(position.latitude, position.longitude, 1).get(0);
                    for (int i = 0; i < address.getMaxAddressLineIndex() - 1; i++)
                        currentAddress += address.getAddressLine(i) + ", ";
                    currentAddress += address.getAddressLine(address.getMaxAddressLineIndex() - 1);
                    txtSearch.setText(currentAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, map.getCameraPosition().zoom));
                break;

            case R.id.txtSearch:
                startActivityForResult(new Intent(this, DestinationActivity.class).putExtra("address", txtSearch.getText().toString()), 1);
                break;

            case R.id.btnDirection:
                Intent intent = new Intent(this, DirectionActivity.class);
                intent.putExtra("position", map.getCameraPosition().target);
                intent.putExtra("zoom", map.getCameraPosition().zoom);
                startActivity(intent);
                break;
        }
        /*if (v.getId() == R.id.btnTrack) {
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
            map.setOnMyLocationChangeListener(this);
            LatLng position = myLocation;
            try {
                String currentAddress = "";
                Address address = geocoder.getFromLocation(position.latitude, position.longitude, 1).get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex() - 1; i++)
                    currentAddress += address.getAddressLine(i) + ", ";
                currentAddress += address.getAddressLine(address.getMaxAddressLineIndex() - 1);
                //fragmentPlace.findPlace(currentAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, map.getCameraPosition().zoom));
        } else {

        }*/
    }

    @Override
    public boolean onLongClick(View v) {
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(), map.getCameraPosition().zoom));

        return false;
    }
}

