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
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import Adapter.ContactAdt;
import Adapter.ViewPagerAdapter;
import DTO.Destination;
import Fragment.ContactFragment;
import Fragment.DestinationFragment;
import Fragment.FavouriteFragment;
import Fragment.HistoryFragment;
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
    /*OnPositionSelectedListener listener;

    public void setListener(OnPositionSelectedListener listener)
    {
        this.listener = listener;
    }
*/
    ArrayList<Destination> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        /*lvDestination = (ListView)findViewById(R.id.lvDestination);
        adapter = new ContactAdt(getApplicationContext(),R.layout.row_destination, list);

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
            {

                LatLng position = place.getLatLng();

                //historyFragment.save(position.latitude, position.longitude);
                historyFragment.save(place.getName().toString(), place.getAddress().toString());
                //listener.onSelected(position.latitude, position.longitude);

                Intent intent = new Intent();
                intent.putExtra("place", place.getName().toString());
                intent.putExtra("address", place.getAddress().toString());
                intent.putExtra("position", position);
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
    HistoryFragment historyFragment;
    FavouriteFragment favouriteFragment;
    void setupViewPager(ViewPager viewPager) {
        contactFragment = new ContactFragment();
        contactFragment.setOnPlaceSelectedListener(this);

        historyFragment = new HistoryFragment();
        historyFragment.setOnPlaceSelectedListener(this);

        favouriteFragment = new FavouriteFragment();
        favouriteFragment.setOnPlaceSelectedListener(this);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(contactFragment, "Contact");
        adapter.addFragment(historyFragment, "History");
        adapter.addFragment(favouriteFragment, "Favourite");
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
