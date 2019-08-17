package com.example.backingapp.Fragments;

import android.content.Context;
import android.net.Uri;
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
    private String mStepDescription;
    private TextView mDescription;



    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mStepDescription = savedInstanceState.getString(ARG_PARAM1);
        }
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_step, container, false);

        mDescription = (TextView) rootView.findViewById(R.id.step_info);
        mDescription.setText(mStepDescription);

        return rootView;
    }

    public void setmStepDescription(String mStepDescription) {
        this.mStepDescription = mStepDescription;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(mStepDescription, ARG_PARAM1);
        super.onSaveInstanceState(outState);
    }
}
