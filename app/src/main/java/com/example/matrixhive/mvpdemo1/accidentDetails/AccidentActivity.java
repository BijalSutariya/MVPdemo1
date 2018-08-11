package com.example.matrixhive.mvpdemo1.accidentDetails;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.matrixhive.mvpdemo1.R;
import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.AccidentImageAdapter;
import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.AccidentImageModel;
import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.OnImageAddClick;
import com.example.matrixhive.mvpdemo1.accidentDetails.locationDetails.PlaceArrayAdapter;
import com.example.matrixhive.mvpdemo1.login.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccidentActivity extends AppCompatActivity implements AccidentConteract.view, OnImageAddClick, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private AccidentPresenter presenter;
    private AccidentImageAdapter adapter;
    private List<AccidentImageModel> photoList = new ArrayList<>();
    private static final int REQUEST_CAMERA = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int CAMERA_REQUEST = 3;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(23.022505, 72.571362), new LatLng(37.430610, 72.571362));
    private File file;

    private View view;
    private static final String SHARED_PREF_NAME = "demo.db";
    private SharedPreferences preferences;
    private boolean logged;
    private RadioGroup rgAccidentType;
    private EditText dateTime, title, name, phoneNo, email, description;
    private AutoCompleteTextView location;
    private Calendar myCalendar = Calendar.getInstance();
    private GoogleApiClient mGoogleApiClient = null;
    private PlaceArrayAdapter placeAdapter;
    private ProgressBar progressBar;

    /**
     * onCreate view initialize
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        logged = preferences.getBoolean("logged", false);

        view = findViewById(R.id.accidentActivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.accident_details);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        presenter = new AccidentPresenter(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHome);

        adapter = new AccidentImageAdapter(photoList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        presenter.getImage();

        rgAccidentType = findViewById(R.id.rgHomeAccidentType);
        dateTime = findViewById(R.id.etHomeDateTime);
        dateTime.setInputType(InputType.TYPE_NULL);
        title = findViewById(R.id.etHomeTitle);
        name = findViewById(R.id.etHomeName);
        phoneNo = findViewById(R.id.etHomeContact);
        email = findViewById(R.id.etHomeEmail);
        location = findViewById(R.id.etHomeLocation);
        location.setThreshold(3);
        description = findViewById(R.id.etHomeDescription);
        Button btnProfile = findViewById(R.id.btnAddProfile);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        dateTime.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        location.setOnItemClickListener(mAutocompleteClickListener);
        placeAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        location.setAdapter(placeAdapter);


    }

    /**
     * getImage set into recyclerview
     */
    @Override
    public void getImage() {
        AccidentImageModel model = new AccidentImageModel();
        photoList.add(model);
        adapter.notifyDataSetChanged();
    }

    /**
     * onCreateOptionsMenu option menu
     *
     * @param menu inflate menu layout
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * onOptionsItemSelected
     *
     * @param item menu item
     * @return menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("TAG", "onOptionsItemSelected: " + item);
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences.Editor editor = preferences.edit();
                if (logged) {
                    editor.clear();
                    editor.apply();
                  //  presenter.loginIntent();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * loginIntent goto login activity
     */
   /* @Override
    public void loginIntent() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }*/

    /**
     * selectPhoto on add image click
     */
    @Override
    public void selectPhoto() {
        final CharSequence[] items = {"Take Photo", "Choose photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AccidentActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    presenter.onItemCLick("Take Photo");
                } else if (items[item].equals("Choose photo")) {
                    presenter.onItemCLick("Choose photo");
                }
            }
        });
        builder.show();
    }

    /**
     * requestPermission camera permission
     */
    @Override
    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            presenter.onPermissionsResult("camera");
        } else {
            String[] permossionRequested = {Manifest.permission.CAMERA};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permossionRequested, REQUEST_CAMERA);
            }
        }
    }

    /**
     * displayImage gallery permission
     */
    @Override
    public void displayImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            presenter.onPermissionsResult("gallery");
        } else {
            String[] permossionRequested = new String[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                permossionRequested = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permossionRequested, GALLERY_REQUEST);
            }
        }
    }

    /**
     * onRequestPermissionsResult
     * @param requestCode camera and gallery
     * @param permissions camera and gallery
     * @param grantResults result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onPermissionsResult("camera");
            } else {
                Toast.makeText(this, "Unable to invoke camera without permossion", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GALLERY_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onPermissionsResult("gallery");
            } else {
                Toast.makeText(this, "cannot take photo", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * openImage gallery intent
     */
    @Override
    public void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    /**
     * intentCamera camera intent
     */
    @Override
    public void intentCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            file = getOutputMediaFile();
            Log.d("TAG", "intentCamera: "+file);
            if (file != null) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }
    }

    /**
     * getOutputMediaFile
     * @return image path
     */
    @Override
    public File getOutputMediaFile() {
        File mediaStorageDir = new File(getExternalCacheDir(), "CaptureImage");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    /**
     * onActivityResult
     * @param requestCode camera and gallery request
     * @param resultCode camera and gallery result
     * @param data intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Log.d("TAG", "onActivityResult: "+file);
            if (file != null) {
                presenter.addImage(file.getAbsolutePath());
            }
        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                presenter.addImage(getRealPathFromURI(uri));
            }
        }
    }

    /**
     * addImage set into model
     * @param realPathFromURI file path
     */
    public void addImage(String realPathFromURI) {
        AccidentImageModel model = new AccidentImageModel();
        model.setImages(realPathFromURI);
        photoList.add(model);
        adapter.notifyDataSetChanged();
    }

    /**
     * getRealPathFromURI
     * @param uri file uri
     * @return file path
     */
    @Override
    public String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(
                this,
                uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * onMyClick interface
     * @param view row activity view
     * @param position item position
     */
    @Override
    public void onMyClick(View view, int position) {
        switch (view.getId()) {
            case R.id.ivImageProof:
                presenter.selectPhoto();
                break;
            case R.id.ivDelete:
                if (!TextUtils.isEmpty(photoList.get(position).getImages())) {
                    photoList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


    /**
     * onClick
     * @param v accidentActivity view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etHomeDateTime:
                presenter.selectDate();
                break;
            case R.id.btnAddProfile:
                int selectedId = rgAccidentType.getCheckedRadioButtonId();
                RadioButton rbAccidentType = findViewById(selectedId);

                presenter.onProfileAddClick(photoList, rbAccidentType.getText().toString(), dateTime.getText().toString(),
                        title.getText().toString(), name.getText().toString(), phoneNo.getText().toString(), email.getText().toString(),
                        location.getText().toString(), description.getText().toString());

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * selectDate datePickerDialog
     */
    @Override
    public void selectDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateTime.setText(sdf.format(myCalendar.getTime()));
            }
        };
        new DatePickerDialog(AccidentActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * showMsg error or success message
     * @param message string
     */
    @Override
    public void showMsg(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();

        Log.d("TAG", "showMsg: " + message);
    }

    /**
     * hideProgress progressBar hide
     */
    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * showProgress progressBar visible
     */
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void focusDate() {
        dateTime.setFocusableInTouchMode(true);
        dateTime.requestFocus();
    }

    @Override
    public void focusTitle() {
        title.setFocusableInTouchMode(true);
        title.requestFocus();
    }

    @Override
    public void focusName() {
        name.setFocusableInTouchMode(true);
        name.requestFocus();
    }

    @Override
    public void focusPhoneNo() {
        phoneNo.setFocusableInTouchMode(true);
        phoneNo.requestFocus();
    }

    @Override
    public void focusEmail() {
        email.setFocusableInTouchMode(true);
        email.requestFocus();
    }

    @Override
    public void focusDescription() {
        description.setFocusableInTouchMode(true);
        description.requestFocus();
    }

    @Override
    public void focusLocation() {
        location.setFocusableInTouchMode(true);
        location.requestFocus();
    }

    /**
     * mAutocompleteClickListener location editText click
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = placeAdapter.getItem(position);
            if (item != null) {
                final String placeId = String.valueOf(item.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                Log.i("TAG", "Fetching details for ID: " + item.placeId);
            }
        }
    };

    /**
     * mUpdatePlaceDetailsCallback place details
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            CharSequence attributions = places.getAttributions();
            if (attributions != null) {
                location.setText(attributions.toString());
            }
        }
    };


    /**
     * onConnectionFailed googleApiClient not connected
     * @param connectionResult result
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * onConnected googleApiClient connected
     * @param bundle Bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        placeAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i("TAG", "Google Places API connected.");
    }

    /**
     * onConnectionSuspended googleApiClient suspended
     * @param i int
     */
    @Override
    public void onConnectionSuspended(int i) {

    }
}
