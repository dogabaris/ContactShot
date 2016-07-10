package com.bigapps.doga.contactshot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.util.List;
import java.util.Random;

import info.hoang8f.widget.FButton;


/**
 * Created by shadyfade on 09.07.2016.
 */
public class Arama extends Fragment{
    private String numara,isim;

    public Arama() {
        // Required empty public constructor

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



        final TextView kisi_tv = (TextView) view.findViewById(R.id.contact_tv);
        final TextView numara_tv = (TextView) view.findViewById(R.id.number_tv);

        FButton randomAt = (FButton) view.findViewById(R.id.randomara_btn);
        FButton ara = (FButton) view.findViewById(R.id.ara_btn);

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

        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+numara));
                startActivity(callIntent);
            }
        });

        return view;
    }


}




