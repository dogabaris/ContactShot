package com.bigapps.doga.contactshot;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;


/**
 * Created by shadyfade on 09.07.2016.
 */
public class Arama extends Fragment{
    private static final String TAG = "Contacts";
    private static final String ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
    private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";

    public Arama() {
        // Required empty public constructor

    }

    public static Arama newInstance(){
        return new Arama();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Contacts.initialize(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.arama_fragment, container, false);


        FButton fButton = (FButton) view.findViewById(R.id.randomara_btn);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Contact> contacts = Contacts.getQuery().find();
                //onRequestPermissionsResult();
            }
        });


        return view;
    }


    private void insertDummyContact() {
        // Two operations are needed to insert a new contact.
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

        // First, set up a new raw contact.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // Next, set the name for the contact.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        "__DUMMY CONTACT from runtime permissions sample");
        operations.add(op.build());

        // Apply the operations.
        ContentResolver resolver = getActivity().getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        } catch (OperationApplicationException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        }
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
        }
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        insertDummyContact();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    insertDummyContact();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}




