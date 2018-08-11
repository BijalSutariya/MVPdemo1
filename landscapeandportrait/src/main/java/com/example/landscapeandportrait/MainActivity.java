package com.example.landscapeandportrait;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvInt;
    private int count = 0;
    private MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean addNewFragment = true;

        if(savedInstanceState!=null)
        {
            addNewFragment = false;
        }

        fragment = new MainFragment();

        if (addNewFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.activityMain, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        /*tvInt = findViewById(R.id.tvInt);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnMinus = findViewById(R.id.btnMinus);
        tvInt.setText("0");
        btnAdd.setOnClickListener(this);
        btnMinus.setOnClickListener(this);*/
    }

   /* @Override
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

    private void displayResult(int s) {
        tvInt.setText(String.valueOf(s));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = (int) savedInstanceState.get("count");
        displayResult(count);
    }*/
}
