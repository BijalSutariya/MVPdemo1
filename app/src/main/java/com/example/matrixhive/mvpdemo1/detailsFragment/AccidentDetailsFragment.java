package com.example.matrixhive.mvpdemo1.detailsFragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.matrixhive.mvpdemo1.R;
import com.example.matrixhive.mvpdemo1.accidentDetails.AccidentConteract;
import com.example.matrixhive.mvpdemo1.accidentDetails.AccidentPresenter;
import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.AccidentImageAdapter;
import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.AccidentImageModel;
import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.OnImageAddClick;
import com.example.matrixhive.mvpdemo1.accidentDetails.locationDetails.PlaceArrayAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccidentDetailsFragment extends Fragment implements AccidentConteract.view, OnImageAddClick, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

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
    private RadioGroup rgAccidentType;
    private EditText dateTime, title, name, phoneNo, email, description;
    private AutoCompleteTextView location;
    private Calendar myCalendar = Calendar.getInstance();
    private GoogleApiClient mGoogleApiClient = null;
    private PlaceArrayAdapter placeAdapter;
    private ProgressBar progressBar;

    private InputMethodManager imm;

    public AccidentDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accident_details, container, false);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        presenter = new AccidentPresenter(this);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHome);

        adapter = new AccidentImageAdapter(photoList, getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        presenter.getImage();


        rgAccidentType = view.findViewById(R.id.rgHomeAccidentType);
        dateTime = view.findViewById(R.id.etHomeDateTime);
        dateTime.setInputType(InputType.TYPE_NULL);
        title = view.findViewById(R.id.etHomeTitle);
        name = view.findViewById(R.id.etHomeName);
        phoneNo = view.findViewById(R.id.etHomeContact);
        email = view.findViewById(R.id.etHomeEmail);
        location = view.findViewById(R.id.etHomeLocation);
        location.setThreshold(3);
        description = view.findViewById(R.id.etHomeDescription);
        Button btnProfile = view.findViewById(R.id.btnAddProfile);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        dateTime.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        location.setOnItemClickListener(mAutocompleteClickListener);
        placeAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        location.setAdapter(placeAdapter);
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        mGoogleApiClient.connect();
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
     * selectPhoto on add image click
     */
    @Override
    public void selectPhoto() {
        final CharSequence[] items = {"Take Photo", "Choose photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
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
     *
     * @param requestCode  camera and gallery
     * @param permissions  camera and gallery
     * @param grantResults result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onPermissionsResult("camera");
            } else {
                Toast.makeText(getActivity(), "Unable to invoke camera without permossion", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GALLERY_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onPermissionsResult("gallery");
            } else {
                Toast.makeText(getActivity(), "cannot take photo", Toast.LENGTH_SHORT).show();
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
        file = getOutputMediaFile();
        Uri picUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", file); // create
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    /**
     * getOutputMediaFile
     *
     * @return image path
     */
    @Override
    public File getOutputMediaFile() {
        File mediaStorageDir = new File(getActivity().getExternalCacheDir(), "CaptureImage");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (file != null) {
                Log.d(TAG, "onActivityResult: " + file);
                presenter.addImage(file.getAbsolutePath());
            }
        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                presenter.addImage(getRealPathFromURI(uri));
            }
        }
    }


    /**
     * addImage set into model
     *
     * @param realPathFromURI file path
     */
    public void addImage(String realPathFromURI) {
        Log.d("TAG", "addImage: " + realPathFromURI);
        AccidentImageModel model = new AccidentImageModel();
        model.setImages(realPathFromURI);
        photoList.add(model);
        adapter.notifyDataSetChanged();
    }

    /**
     * getRealPathFromURI
     *
     * @param uri file uri
     * @return file path
     */
    @Override
    public String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        Log.d(TAG, "getRealPathFromURI: " + column_index);
        return cursor.getString(column_index);
    }

    /**
     * onMyClick interface
     *
     * @param view     row activity view
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
     *
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
                RadioButton rbAccidentType = view.findViewById(selectedId);

                presenter.onProfileAddClick(photoList, rbAccidentType.getText().toString(), dateTime.getText().toString(),
                        title.getText().toString(), name.getText().toString(), phoneNo.getText().toString(), email.getText().toString(),
                        location.getText().toString(), description.getText().toString());
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                dateTime.setCursorVisible(false);
                title.setCursorVisible(false);
                name.setCursorVisible(false);
                phoneNo.setCursorVisible(false);
                email.setCursorVisible(false);
                location.setCursorVisible(false);
                description.setCursorVisible(false);
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
                selectTime();

                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateTime.setText(sdf.format(myCalendar.getTime()));
            }
        };
        new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * selectTime
     */
    private void selectTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateTime.setText(MessageFormat.format("{0} - {1}:{2}", dateTime.getText(), hourOfDay, minute));
            }
        }, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }


    /**
     * showMsg error or success message
     *
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
     *
     * @param connectionResult result
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * onConnected googleApiClient connected
     *
     * @param bundle Bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        placeAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i("TAG", "Google Places API connected.");
    }

    /**
     * onConnectionSuspended googleApiClient suspended
     *
     * @param i int
     */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * onSaveInstanceState
     * @param outState save data into bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) photoList);
    }

    /**
     * onActivityCreated get saved instance
     * @param savedInstanceState bundle
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            photoList.remove(0);
            photoList.addAll(Objects.requireNonNull(savedInstanceState.<AccidentImageModel>getParcelableArrayList("key")));
            adapter.notifyDataSetChanged();
            description.setCursorVisible(false);
        }

        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}