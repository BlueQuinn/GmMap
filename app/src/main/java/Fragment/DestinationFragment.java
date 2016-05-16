package Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import Adapter.ContactAdt;
import AsyncTask.DestinationAst;
import DTO.Destination;
import Listener.OnLoadListener;
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
    ProgressBar prbLoading;
    boolean loaded;
    DestinationAst asyncTask;

    public DestinationFragment()
    {
        loaded = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init();
        Log.d("123", "uolo");
        list = new ArrayList<>();
        loadDestination();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View convertView = inflater.inflate(R.layout.fragment_destination, container, false);
        Log.d("123", "" + loaded);
        prbLoading = (ProgressBar) convertView.findViewById(R.id.prbLoading);
        if (loaded)
        {
            prbLoading.setVisibility(View.GONE);
        }
        else
        {
            prbLoading.setVisibility(View.VISIBLE);
        }
        loaded = true;
Log.d("123", "size=" + list.size() + " " + this.getClass());

        Log.d("123", "" + adapter + "   " + "   " + lvDestination);

        adapter = new ContactAdt(getActivity().getApplicationContext(), R.layout.row_destination, list);
        lvDestination = (ListView) convertView.findViewById(R.id.lvDestination);
        lvDestination.setAdapter(adapter);
        lvDestination.setOnItemClickListener(this);



        return convertView;
    }

    void init()
    {

    }

    void initAsyncTask()
    {

    }


    void loadDestination()
    {
        initAsyncTask();
        asyncTask.setOnLoadListener(new OnLoadListener()
        {
            @Override
            public void onLoaded(Object result)
            {

                prbLoading.setVisibility(View.GONE);
                ArrayList<Destination> listDestination = (ArrayList<Destination>) result;
                for (int i = 0; i < listDestination.size(); ++i)
                {
                    list.add(listDestination.get(i));
                }
                adapter.notifyDataSetChanged();
                lvDestination.setVisibility(View.VISIBLE);
            }
        });

        asyncTask.execute();
    }

    public void setOnPlaceSelectedListener(OnPlaceSelectedListener listener)
    {
        this.listener = listener;
    }

    void onSelected(int position)
    {
        listener.onSelected(list.get(position).getAddress());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        onSelected(position);
    }
}
