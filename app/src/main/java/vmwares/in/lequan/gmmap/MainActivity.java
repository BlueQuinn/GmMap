package vmwares.in.lequan.gmmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

import Sqlite.SqliteHelper;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, View.OnLongClickListener, GoogleMap.OnMyLocationChangeListener {
    GoogleMap map;
    Geocoder geocoder;
    Button btnTrack, btnDirection, btnFavourite, btnPlace;
    //PlacePickerFragment fragmentPlace;
public static LatLng myLocation;
    Marker marker;
    ImageButton btnMenu;
    TextView txtSearch;
    String place = "", address = "";
    public static SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this, Locale.getDefault());

        btnTrack = (Button) findViewById(R.id.btnTrack);
        btnDirection = (Button) findViewById(R.id.btnDirection);
        btnFavourite = (Button) findViewById(R.id.btnFavourite);
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnPlace = (Button) findViewById(R.id.btnPlace);
        txtSearch = (TextView) findViewById(R.id.txtSearch);

        btnTrack.setOnClickListener(this);
        btnDirection.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        btnPlace.setOnClickListener(this);
        txtSearch.setOnClickListener(this);
        btnMenu.setOnClickListener(this);

        initDatabase();

        myLocation = new LatLng(10.762689, 106.68233989999999);
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

    void initDatabase()
    {
        dbHelper = new SqliteHelper(getApplicationContext(), "Destination.sqlite");
        try
        {
            MainActivity.dbHelper.createDataBase();
            MainActivity.dbHelper.openDataBase();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        marker = map.addMarker(new MarkerOptions().position(new LatLng(0,0)));
        marker.setVisible(false);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
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
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, map.getCameraPosition().zoom));
        map.setOnMyLocationChangeListener(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && data != null)
        {
            place = data.getStringExtra("place");
            address = data.getStringExtra("address");
            txtSearch.setText(place);

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
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
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
                try
                {
                    String currentAddress = "";
                    Address address = geocoder.getFromLocation(position.latitude, position.longitude, 1).get(0);
                    for (int i = 0; i < address.getMaxAddressLineIndex() - 1; i++)
                        currentAddress += address.getAddressLine(i) + ", ";
                    currentAddress += address.getAddressLine(address.getMaxAddressLineIndex() - 1);
                    txtSearch.setText(currentAddress);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, map.getCameraPosition().zoom));
                break;
            }

            case R.id.txtSearch:
                startActivityForResult(new Intent(this, DestinationActivity.class).putExtra("address", txtSearch.getText().toString()), 1);
                break;

            case R.id.btnDirection:
            {
                Intent intent = new Intent(this, DirectionActivity.class);
                intent.putExtra("position", map.getCameraPosition().target);
                intent.putExtra("zoom", map.getCameraPosition().zoom);
                startActivity(intent);
                break;
            }

            case R.id.btnFavourite:
            {
                if (place.length() > 1 && address.length() > 1)
                {
                    dbHelper.insert("Favourite", place, address);
                    Toast.makeText(getApplicationContext(), "Saved to Favourite", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.btnPlace:
            {
                Intent i = new Intent(this, PlaceActivity.class);
                startActivity(i);
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(), map.getCameraPosition().zoom));

        return false;
    }
}

