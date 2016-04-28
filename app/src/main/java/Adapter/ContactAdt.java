package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import DTO.Contact;
import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 4/23/2016.
 */
public class ContactAdt extends ArrayAdapter<Contact>
{
    ArrayList<Contact> list;
    Context context;
    int resource;
    public ContactAdt(Context context, int resource, ArrayList<Contact> listContact)
    {
        super(context, resource);


        list = listContact;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvAddress.setText(list.get(position).getAddress());
        tvName.setText(list.get(position).getName());

        return convertView;
    }
}
