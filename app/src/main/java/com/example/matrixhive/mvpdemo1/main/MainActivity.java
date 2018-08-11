package com.example.matrixhive.mvpdemo1.main;

import android.Manifest;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.matrixhive.mvpdemo1.R;
import com.example.matrixhive.mvpdemo1.login.LoginActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Displaying the main Screen
 */
public class MainActivity extends AppCompatActivity implements MainActivityContract.view {

    private static final int REQUEST_CAMERA = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int CAMERA_REQUEST = 3;
    private CircleImageView ivProfile;
    private EditText name, email, contact;
    private MainActivityPresenter presenter;
    private View view;
    private ProgressBar progressBar;
    private File file;
    private String image;
    private static final String SHARED_PREF_NAME = "demo.db";
    private SharedPreferences preferences;
    private boolean logged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        logged = preferences.getBoolean("logged", false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ivProfile = findViewById(R.id.ivProfile);
        CircleImageView ivEditProfile = findViewById(R.id.ivEditProfile);
        progressBar = findViewById(R.id.progressBar);
        view = findViewById(R.id.layoutConstraint);

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        contact = findViewById(R.id.etContact);
        Button btnSubmite = findViewById(R.id.btnSubmit);

        presenter = new MainActivityPresenter(this);

        btnSubmite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonClick(name.getText().toString(), email.getText().toString(), contact.getText().toString(),image);
            }
        });

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onEditClick();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                SharedPreferences.Editor editor = preferences.edit();
                if (logged){
                    editor.clear();
                    editor.apply();
                    presenter.loginIntent();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setQuote(String name, String email, String contact,String image) {
        MainActivityInteractor model = new MainActivityInteractor(name, email, contact,image);
        Toast.makeText(this, "name: " + model.getName() + "\nemail: " + model.getEmail() + "\ncontact: " + model.getContact() + "\n" +model.getImage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginIntent() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void selectPhoto() {
        final CharSequence[] items = {"Take Photo", "Choose photo"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    @Override
    public void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    public void intentCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            file = getOutputMediaFile();
            if (file != null) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }
    }

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            image = file.getAbsolutePath();
            presenter.showImage(file.getAbsolutePath());
        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            image = getRealPathFromURI(uri);
            presenter.showImage(getRealPathFromURI(uri));
        }
    }

    @Override
    public void showImage(String uri) {
        Log.d("TAG", "showImage: "+uri);
        Glide.with(this)
                .load(new File(String.valueOf(uri)))
                .into(ivProfile);
    }

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

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onSuccess(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
