package com.example.homeworkday2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText inputName;
    EditText inputDob;
    EditText inputBloodGroup;
    EditText inputAddress;
    EditText inputMobile;
    EditText inputWebsite;
    CheckBox checkboxTerms;
    RadioGroup radioGroupGender;
    Calendar dobCalendar;
    SharedPreferences sharedPreferences;
    ImageView avatar;
    Bitmap avatarData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputName       = findViewById(R.id.input_name);
        inputDob        = findViewById(R.id.date_dob);
        inputBloodGroup = findViewById(R.id.input_blood_group);
        inputAddress    = findViewById(R.id.input_address);
        inputMobile     = findViewById(R.id.input_mobile);
        inputWebsite    = findViewById(R.id.input_website);
        checkboxTerms   = findViewById(R.id.checkbox_terms);
        radioGroupGender = findViewById(R.id.radio_group_gender);
        dobCalendar     = Calendar.getInstance();
        avatar          = findViewById(R.id.image_avatar);
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dobCalendar.set(Calendar.YEAR, year);
                dobCalendar.set(Calendar.MONTH, month);
                dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDobInput();
            }
        };

        inputDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date,
                        dobCalendar.get(Calendar.YEAR),
                        dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH)
                        ).show();
            }
        });

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        inputName.setText(sharedPreferences.getString("name", ""));
        inputDob.setText(sharedPreferences.getString("dob", ""));
        inputBloodGroup.setText(sharedPreferences.getString("blood_group", ""));
        inputAddress.setText(sharedPreferences.getString("address", ""));
        inputMobile.setText(sharedPreferences.getString("mobile", ""));
        inputWebsite.setText(sharedPreferences.getString("website", ""));
        String termsAgreed = sharedPreferences.getString("terms", "");
        String gender = sharedPreferences.getString("gender", "");

        if (termsAgreed.equals("checked"))
            checkboxTerms.setChecked(true);

        if (gender.equals("Male"))
            radioGroupGender.check(R.id.radio_button_gender_male);
        else if (gender.equals("Female"))
            radioGroupGender.check(R.id.radio_button_gender_female);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    private void updateDobInput() {
        String dobDateFormat = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dobDateFormat, Locale.US);
        inputDob.setText(dateFormat.format(dobCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void aboutClickHandler(MenuItem item) {
        Intent websiteActivity = new Intent(MainActivity.this, WebsiteView.class);
        startActivity(websiteActivity);
    }

    public void helpClickHandler(MenuItem item) {

        String phone = "+1929929333";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    public void contactClickHandler(MenuItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Thanks For Contacting!");
        dialog.setMessage("Our representatives will respond back in a due time!");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Thanks!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "You are so Welcome!", Toast.LENGTH_LONG).show();
            }
        });

        dialog.setNeutralButton("Fine", null);
        dialog.setNegativeButton("No!", null);

        dialog.show();
    }

    public void submitClickHandler(View view) {
        if (!checkboxTerms.isChecked()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Accept Terms");
            alert.setMessage("Please accept terms of use!");
            alert.setIcon(android.R.drawable.stat_sys_warning);
            alert.show();
            return;
        }

        Integer selectedGenderRadioButtonId = radioGroupGender.getCheckedRadioButtonId();
        String gender = "Unknown";
        if (selectedGenderRadioButtonId != -1)
        {
            RadioButton rb = findViewById(radioGroupGender.getCheckedRadioButtonId());
            gender = rb.getText().toString();
        }


        Intent dataViewScreen = new Intent(MainActivity.this, DataListView.class);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();

        sharedPrefEditor.putString("name", inputName.getText().toString());
        sharedPrefEditor.putString("dob", inputDob.getText().toString());
        sharedPrefEditor.putString("blood_group", inputBloodGroup.getText().toString());
        sharedPrefEditor.putString("address", inputAddress.getText().toString());
        sharedPrefEditor.putString("mobile", inputMobile.getText().toString());
        sharedPrefEditor.putString("website", inputWebsite.getText().toString());
        sharedPrefEditor.putString("gender", gender);
        sharedPrefEditor.putString("terms", "checked");

        sharedPrefEditor.apply();
        startActivity(dataViewScreen);
    }

    public void avatarClickHandler(View view) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivity(camera);
        startActivityForResult(camera, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1) {
            avatarData = (Bitmap) data.getExtras().get("data");
            avatar.setImageBitmap(avatarData);

            Log.i("zdf",avatarData+"");
        }
    }

}