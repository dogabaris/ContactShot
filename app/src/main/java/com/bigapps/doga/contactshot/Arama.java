package com.bigapps.doga.contactshot;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;


/**
 * Created by shadyfade on 09.07.2016.
 */
public class Arama extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private FButton fButton = null;
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

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    void getPermission() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.randomara_btn, Arama.newInstance())
                .addToBackStack("camera")
                .commitAllowingStateLoss();
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForCamera(PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.permission_contacts_rationale)
                .setPositiveButton(R.string.button_allow, (dialogInterface, i) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialogInterface, i) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForCamera() {
        Toast.makeText(this.getContext(), R.string.permission_contacts_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showNeverAskForCamera() {
        Toast.makeText(this.getContext(), R.string.permission_contacts_never_askagain, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.arama_fragment, container, false);


        fButton = (FButton) view.findViewById(R.id.randomara_btn);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermission();
                //loadContact();
            }
        });


        return view;
    }

    private void loadContact() {
        getLoaderManager().restartLoader(0, null, this);
    }

    /**
     * Initialises a new {@link CursorLoader} that queries the {@link ContactsContract}.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI, PROJECTION,
                null, null, ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null) {
            final int totalCount = cursor.getCount();
            if (totalCount > 0) {
                cursor.moveToFirst();
                String name = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Toast.makeText(this.getContext(),
                        getResources().getString(R.string.contacts_string, totalCount, name),Toast.LENGTH_SHORT).show();
                Log.d(TAG, "First contact loaded: " + name);
                Log.d(TAG, "Total number of contacts: " + totalCount);
                Log.d(TAG, "Total number of contacts: " + totalCount);
            } else {
                Log.d(TAG, "List of contacts is empty.");
                Toast.makeText(this.getContext(), R.string.contacts_empty, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Toast.makeText(this.getContext(), R.string.contacts_empty, Toast.LENGTH_SHORT).show();
    }


    /**
     * Accesses the Contacts content provider directly to insert a new contact.
     * <p>
     * The contact is called "__DUMMY ENTRY" and only contains a name.
     */
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
                        DUMMY_CONTACT_NAME);
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

}




