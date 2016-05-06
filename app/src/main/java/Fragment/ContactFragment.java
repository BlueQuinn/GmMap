package Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import DTO.Destination;

/**
 * Created by lequan on 4/28/2016.
 */
public class ContactFragment extends DestinationFragment
{
    @Override
    void loadDestination() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Destination ct = new Destination(ContactName(contact_id), ContactNumber(contact_id), ContactAddress(contact_id));
            list.add(ct);
        }
    }

    public String ContactName(String contact_id) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor nameCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + " = ?",
                new String[] { contact_id }, null);
        nameCursor.moveToNext();
        String name = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        nameCursor.close();
        return name;
    }

    // return null if the contact_id don't have any phone number
    // return the first number of contact_id
    public String ContactNumber(String contact_id) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[] { contact_id }, null);
        String phoneNumber = null;
        if (phoneCursor.moveToNext()) {
            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        phoneCursor.close();
        return phoneNumber;
    }

    // return null if the contact_id don't have any phone number
    // return the first number of contact_id
    public String ContactAddress(String contact_id) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor addressCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + "=?", new String[] { contact_id }, null);
        String address = "";
        if (addressCursor.moveToNext()) {
            String street = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            if (street != null)
                address += street + ", ";

            String city = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            if (city != null)
                address += city + ", ";

            String country = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
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
