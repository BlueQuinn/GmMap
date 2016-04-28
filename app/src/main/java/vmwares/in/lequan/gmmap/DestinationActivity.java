package vmwares.in.lequan.gmmap;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;

import Adapter.ContactAdt;
import Adapter.ViewPagerAdapter;
import DTO.Contact;
import Fragment.ContactFragment;
import Fragment.PlacePickerFragment;
import Map.OnCloseListener;
import Map.OnPlaceSelectedListener;

public class DestinationActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, OnPlaceSelectedListener, OnCloseListener
{

    private TabLayout tabLayout;
    PlacePickerFragment fragmentPlace;
    private ViewPager viewPager;
    ContactAdt adapter;
ListView lvDestination;

    ArrayList<Contact> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);



        /*lvDestination = (ListView)findViewById(R.id.lvDestination);
        adapter = new ContactAdt(getApplicationContext(),R.layout.row_contact, list);

        lvDestination.setAdapter(adapter);
        lvDestination.setOnItemClickListener(this);*/

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fragmentPlace = (PlacePickerFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        fragmentPlace.setOnCloseListener(this);
        fragmentPlace.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {
            @Override
            public void onPlaceSelected(Place place)
            { // Handle the selected Place
                /*LatLng point = place.getLatLng();
                marker.setPosition(point);
                marker.setVisible(true);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17));*/
                Intent intent = new Intent();
                intent.putExtra("address", place.getAddress().toString());
                intent.putExtra("position", place.getLatLng());
                setResult(1, intent);
                finish();
            }

            @Override
            public void onError(Status status)
            { // Handle the error
            }
        });

        Intent intent = getIntent();
        fragmentPlace.setText(intent.getStringExtra("address"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        /*Intent intent = new Intent();
        intent.putExtra("destination", list.get(position).getAddress());
        setResult(1, intent);
        finish();*/
    }

    ContactFragment contactFragment;
    void setupViewPager(ViewPager viewPager) {
        contactFragment = new ContactFragment();
        contactFragment.setOnPlaceSelectedListener(this);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(contactFragment, "Contact");
        adapter.addFragment(new ContactFragment(), "History");
        adapter.addFragment(new ContactFragment(), "Favourite");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onSelected(String address)
    {
        fragmentPlace.findPlace(address);
    }

    @Override
    public void onClose()
    {
        finish();
    }
}
