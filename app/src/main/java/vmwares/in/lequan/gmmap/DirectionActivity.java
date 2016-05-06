package vmwares.in.lequan.gmmap;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
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

import java.util.ArrayList;

import AsyncTask.NavigateAst;
import Fragment.DirectionFragment;
import Map.OnCloseListener;
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

    ImageButton btnBack;
    ImageButton btnReverse;
    TextView[] textView;

    LatLng[]  latLng = new LatLng[2];
    Marker[] marker = new Marker[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        getSreenDimanstions();
        Intent intent = getIntent();
        position = intent.getParcelableExtra("position");
        zoom = intent.getFloatExtra("zoom", 16);


        textView = new TextView[2];
        textView[0] = (TextView) findViewById(R.id.txtFrom);
        textView[1] = (TextView) findViewById(R.id.txtTo);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnReverse = (ImageButton) findViewById(R.id.btnReverse);

        setListener();
        //fragmentDirection = (DirectionFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        //fragmentDirection.setOnNavigationListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    void setListener()
    {
        btnBack.setOnClickListener(this);
        btnReverse.setOnClickListener(this);

        textView[0].setOnClickListener(this);
        textView[1].setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ((requestCode == 0 || requestCode == 1) && data != null)
        {
            String place = data.getStringExtra("place");
            textView[requestCode].setText(place);

            latLng[requestCode] = data.getParcelableExtra("position");
            if (marker[requestCode] == null)
                marker[requestCode] = map.addMarker(new MarkerOptions().position(latLng[requestCode]));
            else
                marker[requestCode].setPosition(latLng[requestCode]);

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng[requestCode], zoom));

            if (textView[0].getText().length() > 0 && textView[1].getText().length() > 0)
            {
                NavigateAst asyncTask = new NavigateAst();
                asyncTask.execute(latLng[0], latLng[1]);
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
        }
    }

    @Override
    public void onClick(View v) {
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
                for(int i = 0 ; i < directionPoints.size() ; i++)
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
