package Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLngBounds;

import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 4/19/2016.
 */
public class PlacePickerFragment extends Fragment {

    private View zzaRh;
    //private View zzaRi;
    private TextView zzaRj;
    @Nullable
    private LatLngBounds zzaRk;
    @Nullable
    private AutocompleteFilter zzaRl;
    @Nullable
    private PlaceSelectionListener zzaRm;

    public PlacePickerFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View var4 = inflater.inflate(R.layout.fragment_place_picker, container, false);
        zzaRh = var4.findViewById(R.id.imvMenu);
        //zzaRi = var4.findViewById(R.id.imvClose);
        zzaRj = (TextView)var4.findViewById(R.id.txtSearch);
        View.OnClickListener var5 = new View.OnClickListener() {
            public void onClick(View view) {
                zzzG();
            }
        };
        //zzaRh.setOnClickListener(var5);
        zzaRj.setOnClickListener(var5);
        //zzaRi.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View view) {
        //        PlacePickerFragment.setText("");
        //    }
        //});
        zzzF();
        return var4;
    }

    public void onDestroyView() {
        zzaRh = null;
        //zzaRi = null;
        zzaRj = null;
        super.onDestroyView();
    }

    public void setBoundsBias(@Nullable LatLngBounds bounds) {
        zzaRk = bounds;
    }

    public void setFilter(@Nullable AutocompleteFilter filter) {
        zzaRl = filter;
    }

    public void setText(CharSequence text) {
        zzaRj.setText(text);
        zzzF();
    }

    public void setHint(CharSequence hint) {
        zzaRj.setHint(hint);
        zzaRh.setContentDescription(hint);
    }

    public void setOnPlaceSelectedListener(PlaceSelectionListener listener) {
        zzaRm = listener;
    }

    private void zzzF() {
        boolean var1 = !zzaRj.getText().toString().isEmpty();
        //zzaRi.setVisibility(var1 ? View.VISIBLE : View.GONE);

    }

    private void zzzG() {
        int var1 = -1;

        try {
            Intent var2 = (new PlaceAutocomplete.IntentBuilder(2)).setBoundsBias(zzaRk).setFilter(zzaRl).zzeq(zzaRj.getText().toString()).zzig(1).build(getActivity());
            startActivityForResult(var2, 1);
        } catch (GooglePlayServicesRepairableException var3) {
            var1 = var3.getConnectionStatusCode();
            Log.e("Places", "Could not open autocomplete activity", var3);
        } catch (GooglePlayServicesNotAvailableException var4) {
            var1 = var4.errorCode;
            Log.e("Places", "Could not open autocomplete activity", var4);
        }

        if(var1 != -1) {
            GoogleApiAvailability var5 = GoogleApiAvailability.getInstance();
            var5.showErrorDialogFragment(getActivity(), var1, 2);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == -1) {
                Place var4 = PlaceAutocomplete.getPlace(getActivity(), data);
                if(zzaRm != null) {
                    zzaRm.onPlaceSelected(var4);
                }

                setText(var4.getName().toString());
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
