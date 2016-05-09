package vmwares.in.lequan.gmmap;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import AsyncTask.NavigateAst;
import Map.OnLoadListener;
import Map.OnNavigationListener;

public class DirectionActivity extends AppCompatActivity
        implements View.OnClickListener, OnMapReadyCallback, OnNavigationListener
{
    GoogleMap map;
    LatLng position;
    Polyline route;
    float zoom;
    private int width;
    private int height;
    String place;

    ImageButton btnBack;
    ImageButton btnReverse;
    TextView[] textView;

    LatLng[] latLng = new LatLng[2];
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        getSreenDimanstions();

        textView = new TextView[2];
        textView[0] = (TextView) findViewById(R.id.txtFrom);
        textView[1] = (TextView) findViewById(R.id.txtTo);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnReverse = (ImageButton) findViewById(R.id.btnReverse);

        setListener();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        position = intent.getParcelableExtra("position");
        zoom = intent.getFloatExtra("zoom", 16);
        place = intent.getStringExtra("address");
    }

    void setListener()
    {
        btnBack.setOnClickListener(this);
        btnReverse.setOnClickListener(this);

        textView[0].setOnClickListener(this);
        textView[1].setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        if (place == null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        }
        else
        {
            try
            {
                Intent var2 = (new PlaceAutocomplete.IntentBuilder(2)).zzeq(place).zzig(1).build(this);
                startActivityForResult(var2, 3);
            }
            catch (GooglePlayServicesRepairableException e)
            {
                e.printStackTrace();
            }
            catch (GooglePlayServicesNotAvailableException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == 0 || requestCode == 1) && data != null)
        {
            String place = data.getStringExtra("place");
            textView[requestCode].setText(place);

            latLng[requestCode] = data.getParcelableExtra("position");
            if (requestCode == 1)
            {
                if (marker == null)
                {
                    marker = map.addMarker(new MarkerOptions().position(latLng[requestCode]));
                }
                else
                {
                    marker.setPosition(latLng[requestCode]);
                }
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[requestCode], zoom));

            if (textView[0].getText().length() > 0 && textView[1].getText().length() > 0)
            {
                navigate(latLng[0], latLng[1]);
            }
        }
        if (requestCode == 3)
        {
            if(resultCode == -1) {
                Place var4 = PlaceAutocomplete.getPlace(this, data);
                textView[0].setText("my location");
                textView[1].setText(var4.getName());
                latLng[0] = new LatLng(10.762689, 106.68233989999999);
                latLng[1] = var4.getLatLng();
                navigate(latLng[0], latLng[1]);
            } else if(resultCode == 2) {
                Status var5 = PlaceAutocomplete.getStatus(this, data);

            }
        }
    }

    void navigate(LatLng start, LatLng end)
    {
        NavigateAst asyncTask = new NavigateAst();
        asyncTask.execute(start, end);
        asyncTask.setOnLoadListener(new OnLoadListener<ArrayList<LatLng>>()
        {
            @Override
            public void onLoaded(ArrayList<LatLng> directionPoints)
            {
                PolylineOptions line = new PolylineOptions().width(10).color(getResources().getColor(R.color.colorPrimary));

                for (int i = 0; i < directionPoints.size(); i++)
                {
                    line.add(directionPoints.get(i));
                }
                if (route != null)
                {
                    route.remove();
                }
                route = map.addPolyline(line);
                LatLngBounds latlngBounds = createLatLngBoundsObject(latLng[0], latLng[1]);
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.txtFrom:
                startActivityForResult(new Intent(this, DestinationActivity.class).putExtra("address", textView[0].getText().toString()), 0);
                break;
            case R.id.txtTo:
                startActivityForResult(new Intent(this, DestinationActivity.class).putExtra("address", textView[1].getText().toString()), 1);
                break;
            case R.id.btnBack:
                finish();
            case R.id.btnReverse:
                if (textView[0].getText().length() > 0 && textView[1].getText().length() > 0)
                {
                    String tmp = textView[0].getText().toString();
                    textView[0].setText(textView[1].getText());
                    textView[1].setText(tmp);
                    navigate(latLng[1], latLng[0]);
                }
        }
    }

    @Override
    public void onNavigation(Place startLocation, Place endLocation)
    {
        final LatLng start = startLocation.getLatLng();
        final LatLng end = endLocation.getLatLng();

        map.addMarker(new MarkerOptions().position(start));
        map.addMarker(new MarkerOptions().position(end));

        NavigateAst asyncTask = new NavigateAst();
        asyncTask.execute(start, end);
        asyncTask.setOnLoadListener(new OnLoadListener<ArrayList<LatLng>>()
        {
            @Override
            public void onLoaded(ArrayList<LatLng> directionPoints)
            {
                PolylineOptions line = new PolylineOptions().width(10).color(getResources().getColor(R.color.colorPrimary));
                for (int i = 0; i < directionPoints.size(); i++)
                {
                    line.add(directionPoints.get(i));
                }
                if (route != null)
                {
                    route.remove();
                }
                route = map.addPolyline(line);
                LatLngBounds latlngBounds = createLatLngBoundsObject(start, end);
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
            }
        });
    }

    private void getSreenDimanstions()
    {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
    {
        if (firstLocation != null && secondLocation != null)
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }

}
