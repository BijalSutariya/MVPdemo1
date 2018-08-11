package com.example.matrixhive.mvpdemo1.profile;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.matrixhive.mvpdemo1.R;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String SHARED_PREF_NAME = "demo.db";

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        EditText name = view.findViewById(R.id.etProfileName);
        EditText email = view.findViewById(R.id.etProfileEmail);
        EditText contact = view.findViewById(R.id.etProfileContact);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String strName = sharedPreferences.getString("name", null);
        String strEmail = sharedPreferences.getString("email", null);
        String strContact = sharedPreferences.getString("contact", null);

        Log.d(TAG, "onCreateView: " + strEmail + "\n" + strName + "\n" + strContact);

        name.setText(strName);
        email.setText(strEmail);
        contact.setText(strContact);
        return view;
    }
}
