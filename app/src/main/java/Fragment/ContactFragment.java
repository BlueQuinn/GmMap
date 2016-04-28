package Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;

import Adapter.ContactAdt;
import DTO.Contact;
import Map.OnPlaceSelectedListener;
import vmwares.in.lequan.gmmap.R;

/**
 * Created by lequan on 4/27/2016.
 */
public class ContactFragment extends Fragment implements AdapterView.OnItemClickListener
{
    ListView lvDestination;
    ArrayList<Contact> list;
    ContactAdt adapter;
	OnPlaceSelectedListener listener;

    public ContactFragment()
    {
		//list = new ArrayList<>();
		//loadContact();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View convertView = inflater.inflate(R.layout.fragment_contact, container, false);

        lvDestination = (ListView) convertView.findViewById(R.id.lvContact);
        adapter = new ContactAdt(getActivity().getApplicationContext(), R.layout.row_contact, list);

        lvDestination.setAdapter(adapter);
        lvDestination.setOnItemClickListener(this);



        return convertView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		list = new ArrayList<>();
		loadContact();
    }

	public void setOnPlaceSelectedListener(OnPlaceSelectedListener listener)
	{
		this.listener = listener;
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
		listener.onSelected(list.get(position).getAddress());
    }

    void loadContact() {
		ContentResolver contentResolver = getActivity().getContentResolver();
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		while (cursor.moveToNext()) {
            String contact_id = cursor.getString(cursor.getColumnIndex(Contacts._ID));
            Contact ct = new Contact(ContactName(contact_id), ContactNumber(contact_id), ContactAddress(contact_id));
			list.add(ct);
		}
	}

	public String ContactName(String contact_id) {
		ContentResolver contentResolver = getActivity().getContentResolver();
		Cursor nameCursor = contentResolver.query(Contacts.CONTENT_URI, null, Contacts._ID + " = ?",
				new String[] { contact_id }, null);
		nameCursor.moveToNext();
		String name = nameCursor.getString(nameCursor.getColumnIndex(Contacts.DISPLAY_NAME));
		nameCursor.close();
		return name;
	}

	// return null if the contact_id don't have any phone number
	// return the first number of contact_id
	public String ContactNumber(String contact_id) {
		ContentResolver contentResolver = getActivity().getContentResolver();
		Cursor phoneCursor = contentResolver.query(Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
				new String[] { contact_id }, null);
		String phoneNumber = null;
		if (phoneCursor.moveToNext()) {
			phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
		}
		phoneCursor.close();
		return phoneNumber;
	}

	// return null if the contact_id don't have any phone number
	// return the first number of contact_id
	public String ContactAddress(String contact_id) {
		ContentResolver contentResolver = getActivity().getContentResolver();
		Cursor addressCursor = getActivity().getContentResolver().query(StructuredPostal.CONTENT_URI, null,
				StructuredPostal.CONTACT_ID + "=?", new String[] { contact_id }, null);
		String address = "";
		if (addressCursor.moveToNext()) {
			String street = addressCursor.getString(addressCursor.getColumnIndex(StructuredPostal.STREET));
			if (street != null)
				address += street + ", ";

			String city = addressCursor.getString(addressCursor.getColumnIndex(StructuredPostal.CITY));
			if (city != null)
				address += city + ", ";

			String country = addressCursor.getString(addressCursor.getColumnIndex(StructuredPostal.COUNTRY));
			if (country != null)
				address += country + ", ";

			if (address != "")
				address = address.substring(0, address.length() - 2);
		}
		addressCursor.close();
		if (address == "")
			return null;
		return address;

	
    }
}
