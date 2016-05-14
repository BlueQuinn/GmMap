package Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapter.ContactAdt;
import DTO.Destination;
import Listener.OnPlaceSelectedListener;
import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 4/27/2016.
 */
public class DestinationFragment extends Fragment implements AdapterView.OnItemClickListener
{
    ListView lvDestination;
    ArrayList<Destination> list;
    ContactAdt adapter;
	OnPlaceSelectedListener listener;

    public DestinationFragment()
    {
		//list = new ArrayList<>();
		//loadDestination();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init();

        list = new ArrayList<>();
        loadDestination();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View convertView = inflater.inflate(R.layout.fragment_destination, container, false);

        lvDestination = (ListView) convertView.findViewById(R.id.lvDestination);
        adapter = new ContactAdt(getActivity().getApplicationContext(), R.layout.row_destination, list);

        lvDestination.setAdapter(adapter);
        lvDestination.setOnItemClickListener(this);

        return convertView;
    }



    void init()
    {

    }

	public void setOnPlaceSelectedListener(OnPlaceSelectedListener listener)
	{
		this.listener = listener;
	}

    void loadDestination() {

	}

    void action(int position)
    {
        listener.onSelected(list.get(position).getAddress());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        action(position);
    }
}
