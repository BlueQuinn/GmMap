package vmwares.in.lequan.gmmap;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import Adapter.ViewPagerAdapter;
import Fragment.ContactFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

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
                Log.d("123", "" + position.latitude + " " + position.longitude);

                historyFragment.save(place.getName().toString(), place.getAddress().toString());

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
