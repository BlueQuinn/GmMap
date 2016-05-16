package vmwares.in.lequan.gmmap;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Adapter.MenuAdt;
import AsyncTask.FindPlaceAst;
import DTO.Jam;
import DTO.MenuSection;
import DTO.Menu;
import DTO.Place;
import Listener.OnLoadListener;
import Sqlite.SqliteHelper;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, View.OnLongClickListener, GoogleMap.OnMyLocationChangeListener,
        ExpandableListView.OnChildClickListener
{
    GoogleMap map;
    Geocoder geocoder;
    FloatingActionButton btnTrack, btnFavourite;
    ProgressBar prbLoading;
    //PlacePickerFragment fragmentPlace;
    public static LatLng myLocation;
    Marker marker;
    ImageButton btnMenu, btnVoice;
    TextView txtSearch;
    String place = "", address = "";
    public static SqliteHelper dbHelper;

    ExpandableListView lvLeftmenu;
    MenuAdt adapter;

    FrameLayout frameLayout;
    ArrayList<MenuSection> listSection = new ArrayList<>();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        btnTrack = (FloatingActionButton) findViewById(R.id.btnTrack);
        btnFavourite = (FloatingActionButton) findViewById(R.id.btnFavourite);
        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnVoice = (ImageButton) findViewById(R.id.btnVoice);
        txtSearch = (TextView) findViewById(R.id.txtSearch);
        lvLeftmenu = (ExpandableListView) findViewById(R.id.lvLeftMenu);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        prbLoading = (ProgressBar) findViewById(R.id.prbLoading);

        btnTrack.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        btnVoice.setOnClickListener(this);
        txtSearch.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        prbLoading.setVisibility(View.GONE);

        initMenu();
        setAdapter();
        initDatabase();

        myLocation = new LatLng(10.762689, 106.68233989999999);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

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

    void initMenu()
    {
        ArrayList<Menu> listMenu = new ArrayList<>();

        listMenu.add(new Menu("Tìm nhà hàng", R.drawable.restaurant));
        listMenu.add(new Menu("Tìm địa điểm", R.drawable.place));
        listMenu.add(new Menu("Tìm đường đi", R.drawable.direction));
        listMenu.add(new Menu("Tránh tắc đường", R.drawable.traffic_cone));
        listMenu.add(new Menu("Thông báo tắc đường", R.drawable.warning));
        listSection.add(new MenuSection("Tiện ích", listMenu));

        listMenu = new ArrayList<>();
        listMenu.add(new Menu("Bản đồ thường", R.drawable.normal));
        listMenu.add(new Menu("Xem từ vệ tinh", R.drawable.satellite));
        listMenu.add(new Menu("Xem địa hình", R.drawable.terrain));
        listSection.add(new MenuSection("Xem bản đồ", listMenu));

        listMenu = new ArrayList<>();
        listMenu.add(new Menu("Facebook", R.drawable.facebook));
        listMenu.add(new Menu("SMS", R.drawable.sms));
        listSection.add(new MenuSection("Chia sẻ", listMenu));
    }

    void setAdapter()
    {
        adapter = new MenuAdt(getApplicationContext(), R.layout.row_menu, R.layout.row_section, listSection);

        lvLeftmenu.setAdapter(adapter);
        lvLeftmenu.setOnChildClickListener(this);
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
    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        marker = map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        marker.setVisible(false);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
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
        map.setMyLocationEnabled(true);
        map.setContentDescription("");
        map.setTrafficEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        map.setOnMyLocationChangeListener(this);
    }

    @Override
    public void onMyLocationChange(Location location)
    {
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, map.getCameraPosition().zoom));
        map.setOnMyLocationChangeListener(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null)
        {
            switch (requestCode)
            {
                case 1:
                {
                    place = data.getStringExtra("place");
                    address = data.getStringExtra("address");
                    txtSearch.setText(place);

                    LatLng position = data.getParcelableExtra("position");
                    marker.setPosition(position);
                    marker.setVisible(true);
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17));
                    break;
                }

                case 2:
                {
                    if (resultCode == RESULT_OK)
                    {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        txtSearch.setText(result.get(0));
                        startActivityForResult(new Intent(this, DestinationActivity.class).putExtra("address", txtSearch.getText().toString()), 1);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v)
    {
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
                    {
                        currentAddress += address.getAddressLine(i) + ", ";
                    }
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

            case R.id.btnFavourite:
            {
                if (place.length() > 1 && address.length() > 1)
                {
                    dbHelper.insert("Favourite", place, address);
                    Toast.makeText(getApplicationContext(), "Đã lưu vào Yêu thích", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Hãy chọn địa điểm trước", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.btnVoice:
            {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bạn cần tìm gì ?");
                startActivityForResult(intent, 2);
                break;
            }

            case R.id.btnMenu:
                drawerLayout.openDrawer(lvLeftmenu);
                break;
        }
    }

    void showDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_place_picker);
        dialog.setTitle("Chọn địa điểm");

        final AutoCompleteTextView autoCompleteSearch = (AutoCompleteTextView) dialog.findViewById(R.id.auto_complete_search);
        String[] places = {"Accounting", "Airport", "Amusement Park", "Aquarium", "Art Gallery", "Atm", "Bakery", "Bank", "Bar", "Beauty Salon", "Bicycle Store", "Book Store", "Bus Station", "Cafe", "Campground", "Car Dealer", "Car Rental", "Car Repair", "Car Wash", "Casino", "Cemetery", "Church", "City Hall", "Clothing Store", "Convenience Store", "Courthouse", "Dentist", "Department Store", "Doctor", "Electrician", "Electronics Store", "Embassy", "Finance", "Fire Station", "Florist", "Food", "Funeral Home", "Furniture Store", "Gas Station", "Grocery Or Supermarket", "Gym", "Hair Care", "Hardware Store", "Health", "Home Goods Store", "Hospital", "Insurance Agency", "Jewelry Store", "Laundry", "Lawyer", "Library", "Liquor Store", "Local Government Office", "Locksmith", "Lodging", "Meal Delivery", "Meal Takeaway", "Mosque", "Movie Rental", "Movie Theater", "Moving Company", "Museum", "Night Club", "Painter", "Park", "Parking", "Pet Store", "Pharmacy", "Plumber", "Police", "Post Office", "Real Estate Agency", "Restaurant", "School", "Shoe Store", "Shopping Mall", "Spa", "Stadium", "Storage", "Store", "Taxi Stand", "Train Station", "Travel Agency", "University", "Zoo"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, places);
        autoCompleteSearch.setAdapter(adapter);
        autoCompleteSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String place = adapter.getItem(position).toLowerCase().replace(" ", "_");
                FindPlaceAst asyncTask = new FindPlaceAst(getApplicationContext(), place);
                asyncTask.setOnLoadListener(new OnLoadListener()
                {
                    @Override
                    public void onLoaded(Object obj)
                    {
                        map.clear();
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                        MarkerOptions options = new MarkerOptions().icon(icon);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        ArrayList<Place> list = (ArrayList<Place>) obj;
                        for (int i = 0; i < list.size(); ++i)
                        {
                            Place place = list.get(i);
                            LatLng position = new LatLng(place.getLat(), place.getLng());
                            map.addMarker(options.position(position).title(place.getName()));
                            builder.include(position);
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
                    }
                });
                asyncTask.execute(myLocation.latitude, myLocation.longitude);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onLongClick(View v)
    {
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(), map.getCameraPosition().zoom));

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
    {
        switch (groupPosition)
        {
            case 0:
            {
                switch (childPosition)
                {
                    case 0:
                    {
                        Intent i = new Intent(this, PlaceActivity.class);
                        startActivity(i);
                        break;
                    }
                    case 1:
                        showDialog();
                        break;
                    case 2:
                    {
                        Intent intent = new Intent(this, DirectionActivity.class);
                        intent.putExtra("position", map.getCameraPosition().target);
                        intent.putExtra("zoom", map.getCameraPosition().zoom);
                        startActivity(intent);
                        break;
                    }
                    case 3:
                        loadFirebase();
                        break;

                    case 4:
                    {

                    }
                }
                break;
            }

            case 1:
            {
                switch (childPosition)
                {
                    case 0:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
                break;
            }

            case 2:
            {
                switch (childPosition)
                {
                    case 0:

                        break;
                    case 1:
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Chia sẻ từ Map Assistant");

                        String msg = "message";
                        if (place.length() > 0 && address.length() > 0)
                        {
                            msg = "Tôi đang ở " + place + ".\nĐịa chỉ " + address;
                        }
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);

                        startActivity(Intent.createChooser(sharingIntent, "message"));
                        break;
                }
                break;
            }
        }
        drawerLayout.closeDrawer(lvLeftmenu);
        return false;
    }

    ArrayList<LatLng> list;

    void loadFirebase()
    {
        prbLoading.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
        Firebase ref = new Firebase("https://androidtraffic.firebaseio.com/");
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                try
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    Date date = new Date();
                    int timeNow = 60 * date.getHours() + date.getMinutes();

                    int meta = ((Long) snapshot.child("meta").getValue()).intValue();
                    DataSnapshot traffic = snapshot.child("traffic");
                    for (DataSnapshot item : traffic.getChildren())
                    {
                        ArrayList<Jam> jamList = new ArrayList<>();
                        DataSnapshot jamData = item.child("jam");
                        for (DataSnapshot jam : jamData.getChildren())
                        {
                            String time = (String) jam.child("time").getValue();
                            Date jamTime = formatter.parse(time);
                            int span = timeNow - 60 * jamTime.getHours() - jamTime.getMinutes();
                            if (span > -90 && span < 90)   // timespan between 90 minutes earlier or later
                            {
                                int vote = ((Long) jam.child("vote").getValue()).intValue();
                                if (vote > meta)
                                {
                                    jamList.add(new Jam(time, vote));
                                    break;
                                }
                            }
                        }

                        if (jamList.size() > 0)
                        {
                            //sortDescending(jamList);
                            double lat = (double) item.child("position/lat").getValue();
                            double lng = (double) item.child("position/lng").getValue();
                            list.add(new LatLng(lat, lng));
                            //Traffic traffic = new Traffic(lat, lng, jamList.get(0).getVote());
                        }
                    }

                    markTraffic();
                    prbLoading.setVisibility(View.GONE);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {
                System.out.println("The read failed: " + firebaseError.getMessage());
                prbLoading.setVisibility(View.GONE);
            }
        });
    }

    void markTraffic()
    {
        map.clear();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.traffic);
        MarkerOptions options = new MarkerOptions().icon(icon);
        for (LatLng traffic : list)
        {
            map.addMarker(options.position(traffic));
        }
    }
}


