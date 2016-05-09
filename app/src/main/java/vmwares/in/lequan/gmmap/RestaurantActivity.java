package vmwares.in.lequan.gmmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapter.RestaurantAdt;
import AsyncTask.RestaurantAst;
import DTO.Restaurant;
import Map.OnLoadListener;

public class RestaurantActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, OnLoadListener
{
    GridView lvRestaurant;
    TextView tvEmpty;
    ArrayList<Restaurant> listRestaurant;
    RestaurantAdt adapter;
    RestaurantAst asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        lvRestaurant = (GridView) findViewById(R.id.lvArticle);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);

        listRestaurant = new ArrayList<>();

        adapter = new RestaurantAdt(getApplicationContext(), R.layout.cell_restaurant, listRestaurant);
        lvRestaurant.setAdapter(adapter);
        lvRestaurant.setOnItemClickListener(this);


        Intent intent = getIntent();

        String url = "http://www.deliverynow.vn/ho-chi-minh/danh-sach-dia-diem-phuc-vu-" + intent.getStringExtra("url") + "-giao-tan-noi";
        asyncTask = new RestaurantAst(this.findViewById(android.R.id.content));
        asyncTask.setOnLoaded(this);
        asyncTask.execute(url);

        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("restaurant", adapter.getItem(position));
        startActivity(i);
    }

    @Override
    public void onLoaded(Object result)
    {
        ArrayList<Restaurant> list = (ArrayList<Restaurant>) result;
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                listRestaurant.add(list.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        lvRestaurant.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
