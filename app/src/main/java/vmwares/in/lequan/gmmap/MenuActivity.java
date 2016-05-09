package vmwares.in.lequan.gmmap;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapter.MenuAdapter;
import AsyncTask.MenuAst;
import DTO.Menu;
import DTO.Restaurant;
import Map.OnLoadListener;

public class MenuActivity extends AppCompatActivity implements OnLoadListener, OnClickListener
{
    GridView gridMenu;
    TextView tvEmpty;
    ArrayList<Menu> listMenu;
    MenuAdapter adapter;
    MenuAst asyncTask;
    Restaurant restaurant;
    FloatingActionButton btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        getSupportActionBar().setTitle(restaurant.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnGo = (FloatingActionButton) findViewById(R.id.btnGo);
        gridMenu = (GridView) findViewById(R.id.lvArticle);
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);


        listMenu = new ArrayList<>();

        adapter = new MenuAdapter(getApplicationContext(), R.layout.cell_menu, listMenu);
        gridMenu.setAdapter(adapter);


        asyncTask = new MenuAst(this.findViewById(android.R.id.content));
        asyncTask.setOnLoaded(this);
        asyncTask.execute("http://www.deliverynow.vn" + restaurant.getUrl());

        btnGo.setOnClickListener(this);
    }

    @Override
    public void onLoaded(Object result)
    {
        ArrayList<Menu> list = (ArrayList<Menu>) result;
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                listMenu.add(list.get(i));
            }
        }
        adapter.notifyDataSetChanged();
        gridMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(this, DirectionActivity.class);
        intent.putExtra("address", restaurant.getTitle());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
