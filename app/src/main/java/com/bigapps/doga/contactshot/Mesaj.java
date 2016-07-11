package com.bigapps.doga.contactshot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.util.List;
import java.util.Random;

import info.hoang8f.widget.FButton;

/**
 * Created by shadyfade on 09.07.2016.
 */
public class Mesaj extends Fragment {
    private String numara,isim;

    public Mesaj() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mesaj_fragment, container, false);

        final TextView kisi_tv = (TextView) view.findViewById(R.id.mcontact_tv);
        final TextView numara_tv = (TextView) view.findViewById(R.id.mnumber_tv);
        final EditText mesaj_et = (EditText) view.findViewById(R.id.msg_et);

        FButton randomAt = (FButton) view.findViewById(R.id.mrandomara_btn);
        FButton gonder = (FButton) view.findViewById(R.id.gonder_btn);

        randomAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Contact> contacts = Contacts.getQuery().find();
                Contact random = contacts.get(new Random().nextInt(contacts.size()));
                while(true){
                    if(random.getBestPhoneNumber()!=null && random.getBestDisplayName()!=null){
                        try{
                            isim = random.getBestDisplayName();
                            numara = random.getBestPhoneNumber().getNormalizedNumber();
                            break;
                        }catch(NullPointerException ignored){
                        }
                    }else{
                        random = contacts.get(new Random().nextInt(contacts.size()));
                    }
                }
                numara_tv.setVisibility(View.VISIBLE);
                numara_tv.setText(numara);
                kisi_tv.setVisibility(View.VISIBLE);
                kisi_tv.setText(isim);

            }
        });

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager =     SmsManager.getDefault();
                smsManager.sendTextMessage(numara, null, mesaj_et.getText().toString(), null, null);
                Toast.makeText(getContext(),"Message Sent!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
