package com.bigapps.doga.contactshot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.hoang8f.widget.FButton;


/**
 * Created by shadyfade on 09.07.2016.
 */
public class Arama extends Fragment{
    private FButton fButton = null;

    public Arama() {
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
        View view = inflater.inflate(R.layout.arama_fragment,container,false);

        //movingButton = (MovingButton) view.findViewById(R.id.moving_button);

        //movingButton.getMoveDirection();
        //movingButton.setMoveDirection(MoveDirection.ALL);

        return view;
    }





}
