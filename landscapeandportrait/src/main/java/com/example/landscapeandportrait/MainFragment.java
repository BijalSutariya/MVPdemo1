package com.example.landscapeandportrait;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {


    private View view;
    private TextView tvInt;
    private int count = 0;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);

        tvInt = view.findViewById(R.id.tvInt);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnMinus = view.findViewById(R.id.btnMinus);
        tvInt.setText("0");
        btnAdd.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

        setRetainInstance(true);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                count = count + 1;
                displayResult(count);
                break;
            case R.id.btnMinus:
                count = count - 1;
                displayResult(count);
                break;
        }
    }

    private void displayResult(int count) {
        tvInt.setText(String.valueOf(count));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //probably orientation change
            count = savedInstanceState.getInt("count");
            Log.d(TAG, "onViewStateRestored: "+count);
            displayResult(count);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
