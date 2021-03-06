package vmwares.in.lequan.gmmap;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import Adapter.RestaurantSectionAdt;
import DTO.RestaurantSection;

public class PlaceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    ArrayList<RestaurantSection> list;
    RestaurantSectionAdt adapter;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ăn gì - Ở đâu");

        list = new ArrayList<>();
        list.add(new RestaurantSection("Mì, bún, phở, cháo", "mi-bun-pho-chao", R.drawable.noodle));
        list.add(new RestaurantSection("Cafe, trà sữa", "tra-sua-tra-chanh", R.drawable.drink));
        list.add(new RestaurantSection("Lẩu", "lau", R.drawable.hotpot));
        list.add(new RestaurantSection("Bánh mì, xôi", "banh-mi-xoi", R.drawable.bread));
        list.add(new RestaurantSection("Hải sản", "hai-san", R.drawable.seafood));
        list.add(new RestaurantSection("Món chay", "mon-chay", R.drawable.salad));
        list.add(new RestaurantSection("Kem, bánh ngọt", "banh-kem", R.drawable.icecream));
        list.add(new RestaurantSection("Đồ nướng", "thit-nuong-quay", R.drawable.bbq));
        list.add(new RestaurantSection("Pizza, hamburger", "pizza-hamburger", R.drawable.pizza));
        list.add(new RestaurantSection("Cơm", "com-ga-com-tam", R.drawable.rice));

        adapter = new RestaurantSectionAdt(getApplicationContext(), R.layout.cell_place, list);
        grid = (GridView) findViewById(R.id.gridPlace);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(this, RestaurantActivity.class);
        intent.putExtra("name", list.get(position).getName());
        intent.putExtra("url", list.get(position).getUrl());
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
