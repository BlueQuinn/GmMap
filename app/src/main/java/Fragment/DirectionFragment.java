package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLngBounds;

import org.w3c.dom.Text;

import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 4/5/2016.
 */
public class DirectionFragment extends Fragment implements View.OnClickListener{
    ImageButton btnBack;
    ImageButton btnReverse;
    TextView[] textView;

    PlaceSelectionListener zzaRm;
    LatLngBounds zzaRk;
    public DirectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View var4 = inflater.inflate(R.layout.fragment_direction, container, false);

        textView = new TextView[2];
        textView[0] = (TextView) var4.findViewById(R.id.txtFrom);
        textView[1] = (TextView) var4.findViewById(R.id.txtTo);

        btnBack = (ImageButton) var4.findViewById(R.id.btnBack);
        btnReverse = (ImageButton) var4.findViewById(R.id.btnReverse);

        setListener();
        return var4;
}

    void setListener()
    {
        btnBack.setOnClickListener(this);
        btnReverse.setOnClickListener(this);

        View.OnClickListener var5 = new View.OnClickListener() {
            public void onClick(View view) {
                int i = 0;
                if (view.getId() == R.id.txtFrom)
                    i = 1;
                    zzzG(textView[i].getText().toString(), i);
            }
        };
        textView[0].setOnClickListener(var5);
        textView[1].setOnClickListener(var5);
    }


    public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
        this.zzaRm = listener;
    }

    public void setText(CharSequence text, int i) {
        textView[i].setText(text);
       // zzzF();
    }

    @Override
    public void onClick(View v) {

    }

    void zzzG(String text, int i) {
        int var1 = -1;

        try {
            Intent var2 = (new PlaceAutocomplete.IntentBuilder(2)).zzeq(text).zzig(1).build(this.getActivity());
            this.startActivityForResult(var2, i);
        } catch (GooglePlayServicesRepairableException var3) {
            var1 = var3.getConnectionStatusCode();
            Log.e("Places", "Could not open autocomplete activity", var3);
        } catch (GooglePlayServicesNotAvailableException var4) {
            var1 = var4.errorCode;
            Log.e("Places", "Could not open autocomplete activity", var4);
        }

        if(var1 != -1) {
            GoogleApiAvailability var5 = GoogleApiAvailability.getInstance();
            var5.showErrorDialogFragment(this.getActivity(), var1, 2);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 || requestCode == 1) {
            if(resultCode == -1) {
                Place var4 = PlaceAutocomplete.getPlace(getActivity(), data);
                if(zzaRm != null) {
                    zzaRm.onPlaceSelected(var4);
                }

                setText(var4.getName().toString(), requestCode);
            } else if(resultCode == 2) {
                Status var5 = PlaceAutocomplete.getStatus(getActivity(), data);
                if(zzaRm != null) {
                    zzaRm.onError(var5);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
