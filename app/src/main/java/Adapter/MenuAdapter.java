package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import DTO.Menu;
import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 2/1/2016.
 */
public class MenuAdapter extends ArrayAdapter<Menu> {

    Context context;
    int resource;
    List<Menu> list;

    public MenuAdapter(Context context, int resource, List<Menu> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource,parent,false);
        }

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        TextView tvPrice = (TextView)convertView.findViewById(R.id.tvAddress);
        ImageView image = (ImageView)convertView.findViewById(R.id.imvThumbnail);

        tvTitle.setText(list.get(position).getFood());
        tvPrice.setText(list.get(position).getPrice());

        if (list.get(position).getImage().length() > 0)
            UrlImageViewHelper.setUrlDrawable(image, list.get(position).getImage());
        //else
            //Picasso.with(context).load(R.drawable.food).into(image);

        return convertView;
    }

}

