package com.example.backingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.backingapp.R;

/**
 * Fragment to populate the Baking activity with the Description of a step
 */
public class StepFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private String mDescription;
    private TextView mDescriptionView;



    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        if (savedInstanceState != null){
            mDescription = savedInstanceState.getString(ARG_PARAM1);
        }
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_step, container, false);

        mDescriptionView = (TextView) rootView.findViewById(R.id.step_info);
        mDescriptionView.setText(mDescription);


        return rootView;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(ARG_PARAM1, mDescription);
    }
}
