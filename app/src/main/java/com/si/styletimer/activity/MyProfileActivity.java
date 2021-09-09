package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Toast;

import com.fxn.pix.Pix;
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityMyProfileBinding;
import com.si.styletimer.databinding.ImageSellectionBinding;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.CameraGallery;
import com.si.styletimer.utill.DatabindingImageAdapter;
import com.si.styletimer.utill.ImageBinding;
import com.si.styletimer.utill.MarshmallowPermission;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;


public class MyProfileActivity extends AppCompatActivity {
    private static final String TAG = "MyProfileActivity";
    private Context context;
    ActivityMyProfileBinding binding;
    private ArrayList<String> city = new ArrayList<>(Arrays.asList("Indore", "Bhopal"));
    private ArrayList<String> gender = new ArrayList<>(Arrays.asList("Geschlecht wählen", "Männlich", "Weiblich", "Andere"));
    private ArrayList<String> country = new ArrayList<>(Arrays.asList("Land wählen", "Deutschland", "Österreich", "Schweiz"));
    private ArrayList<String> countryId = new ArrayList<>(Arrays.asList("0", "1", "2", "3"));
    private String action = "view", Image_Name = "";
    private Boolean Country = false, Gender = false;
    File Image_file;
    private BottomSheetDialog bsd;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private MarshmallowPermission marshmallowPermission;
    private CameraGallery cameraGallery;
    private SupportedDatePickerDialog datePickerDialog;
    String mydate;
    String flag="",news="0";
    private String dateSend = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        context = this;
        marshmallowPermission= new MarshmallowPermission(context);
        marshmallowPermission.check_write_Permission();
        cameraGallery = new CameraGallery(context,MyProfileActivity.this);

        initView();
        onClick();
        //onTouch();

    }
    private void initView() {
        setSpinners();
        profile("", "");

    }
    private void onClick() {
        binding.etdDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  Bungee.slideRight(context);
            }
        });

        binding.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideSoftKeyboard(MyProfileActivity.this);
            }
        });

        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        binding.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProfile();
            }
        });

    }


    private void openDatePicker(){
        if (mydate!=null){
            String dod = mydate;
            String spilt[] = dod.split("-");
            int years = Integer.parseInt(spilt[0]);
            int months = Integer.parseInt(spilt[1])-1;
            int day = Integer.parseInt(spilt[2]);
            Log.e(TAG, "openDatePicker: "+years+months+day );
            final Calendar newCalendar = Calendar.getInstance();
            datePickerDialog = new SupportedDatePickerDialog(context,R.style.SpinnerDatePickerDialogTheme, (view, year, monthOfYear, dayOfMonth) -> {
                Log.e(TAG, "openDatePicker: "+Utility.dateBuiilder(year,monthOfYear,dayOfMonth) );
                dateSend = Utility.dateBuiilder(year,monthOfYear,dayOfMonth);
                binding.etdDob.setText(Utility.change_format(dateSend,"yyyy-mm-dd","dd.mm.yyyy"));
                binding.tillDob.setError(null);
            },years,months,day);

            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        }


/*
        if (mydate!=null){
            String dod = mydate;
            String spilt[] = dod.split("-");
            int years = Integer.parseInt(spilt[0]);
            int months = Integer.parseInt(spilt[1])-1;
            int day = Integer.parseInt(spilt[2]);
            Log.e(TAG, "openDatePicker: "+years+months+day );
            final Calendar newCalendar = Calendar.getInstance();
            datePickerDialog = new DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, (view, year, monthOfYear, dayOfMonth) -> {
                Log.e(TAG, "openDatePicker: "+Utility.dateBuiilder(year,monthOfYear,dayOfMonth) );
                binding.etdDob.setText(Utility.dateBuiilder(year,monthOfYear,dayOfMonth));
                binding.tillDob.setError(null);
            },years,months,day);

            datePickerDialog.getDatePicker().setSpinnersShown(true);
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        }
*/

    }


    private void validate() {

        if(binding.cbNews.isChecked()){
            news="1";
        }else {
            news="0";
        }

        if (binding.etfirstname.getText().toString().equals("")) {
            binding.tiFirstname.setError(context.getResources().getString(R.string.please_enter_firstname));

        } else if (binding.etLastname.getText().toString().equals("")) {
            binding.tiLastname.setError(context.getResources().getString(R.string.please_enter_lastname));

        } /*else if (binding.etPhone.getText().toString().equals("")) {
            binding.tiPhone.setError("Bitte geben sie ihre Telefonnummer ein");

        } else if (binding.etPhone.getText().toString().length() < 7) {
            binding.tiPhone.setError("Bitte geben Sie eine mindestens 8-stellige Telefonnummer ein");

        } */else if (binding.etEmail.getText().toString().equals("")) {
            binding.tiEmail.setError("Bitte geben Sie ihre E-Mail-Adresse ein");

        } /*else if (binding.etCity.getText().toString().equals("")) {
            binding.tiCity.setError("Bitte geben Sie Ihre Stadt ein");

        } else if (binding.etPost.getText().toString().equals("")) {
            binding.tiPost.setError("Bitte geben Sie Ihre Postleitzahl ein");

        } else if (binding.etPost.getText().toString().length() < 4) {
            binding.tiPost.setError("Bitte geben Sie eine gültige Postleitzahl ein");

        } else if (binding.etStreet.getText().toString().equals("")) {
            binding.tiStreet.setError("Bitte geben Sie Ihre Straße ein");

        }*/ else if (binding.etfirstname.getText().toString().startsWith(" ") || binding.etfirstname.getText().toString().endsWith(" ")) {
            binding.tiFirstname.setError("Bitte Vorname eingeben");

        } else if (binding.etLastname.getText().toString().startsWith(" ") || binding.etLastname.getText().toString().endsWith(" ")) {
            binding.tiLastname.setError("Bitte Nachname eingeben");

        } else {
            action = "edit";

            String countryy = binding.sCountry.getSelectedItem().toString();
            String gender = binding.sGender.getSelectedItem().toString();
            int i = country.indexOf(countryy);

            if (binding.sGender.getSelectedItem().toString().equals("Male") || binding.sGender.getSelectedItem().toString().equals("Männlich")) {
                profile(countryy, "male");

            } else if (binding.sGender.getSelectedItem().toString().equals("Female") || binding.sGender.getSelectedItem().toString().equals("Weiblich")) {
                profile(countryy, "female");

            } else {
                profile(countryy, "other");
            }

           /* if (countryy.equals("Land wählen")) {
                Toast.makeText(context, "Bitte wählen Sie Ihr Land", Toast.LENGTH_LONG).show();

            } else if (gender.equals("Geschlecht wählen")) {

                Toast.makeText(context, "Bitte wählen Sie Ihr Geschlecht", Toast.LENGTH_LONG).show();
            }
            else {

                int i = country.indexOf(countryy);

                if (binding.sGender.getSelectedItem().toString().equals("Male") || binding.sGender.getSelectedItem().toString().equals("Männlich")) {
                    profile(countryy, "male");

                } else if (binding.sGender.getSelectedItem().toString().equals("Female") || binding.sGender.getSelectedItem().toString().equals("Weiblich")) {
                    profile(countryy, "female");

                } else {
                    profile(countryy, "other");
                }

            } */

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraGallery.onActivityResult(requestCode,resultCode,data);

        /*if (resultCode == Activity.RESULT_OK && requestCode == 100) {

            String photo = (String) data.getExtras().get("data");
            Bitmap photoo = (Bitmap) data.getExtras().get("data");

            Log.e(TAG, "onActivityResult: " + photo);
            Log.e(TAG, "onActivityResult-:- " + photoo);


            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            for (String s : returnValue) {
                Log.e("Name--", " ->  " + s.substring(s.lastIndexOf("/") + 1));
                Image_Name = s.substring(s.lastIndexOf("/") + 1);
                Uri myUri = Uri.parse(s);
                try {

                    Image_file = new File(s);

                    DatabindingImageAdapter.loadImageFile(binding.civProfile, Image_file);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Log.e(TAG, "onActivityResult: >>>>>>>>>>>>>>>>>>"+filePath.getPath() );
            Image_file = new File(getPath(filePath));

            *//*DatabindingImageAdapter.loadImageFile(binding.civProfile, Image_file);*//*

            binding.civProfile.setImageBitmap(photo);

        }
        else if(requestCode==1){
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Image_file = new File(getPath(contentURI));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    binding.civProfile.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if(requestCode==2){

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            binding.civProfile.setImageBitmap(thumbnail);
            Image_file = new File(getPath(thumbnail));


        }*/
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void textWatcher() {

        binding.etfirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 0) {

                    if (String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")) {
                        binding.tiFirstname.setError(context.getResources().getString(R.string.please_enter_firstname));
                    } else {

                        binding.tiFirstname.setError(null);
                        binding.tiFirstname.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                        binding.tiFirstname.setHintTextAppearance(R.style.EditTextHintStyle);
                    }


                } else {

                    binding.tiFirstname.setError(context.getResources().getString(R.string.please_enter_firstname));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 0) {

                    if (String.valueOf(s).startsWith(" ") || String.valueOf(s).endsWith(" ")) {
                        binding.tiLastname.setError(context.getResources().getString(R.string.please_enter_lastname));
                    } else {

                        binding.tiLastname.setError(null);
                        binding.tiLastname.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                        binding.tiLastname.setHintTextAppearance(R.style.EditTextHintStyle);
                    }

                } else {

                    binding.tiLastname.setError(context.getResources().getString(R.string.please_enter_lastname));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 7) {

                    binding.tiPhone.setError(null);
                    binding.tiPhone.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiPhone.setHintTextAppearance(R.style.EditTextHintStyle);

                } else {
                    binding.tiPhone.setError(null);
                    // binding.tiPhone.setError("Bitte geben Sie eine mindestens 8-stellige Telefonnummer ein");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (Utility.isValidEmailAddress(String.valueOf(s))) {
                    Log.e("---->", "in");
                    binding.tiEmail.setError(null);
                    binding.tiEmail.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiEmail.setHintTextAppearance(R.style.EditTextHintStyle);
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_yelow, 0);
                } else {
                    Log.e("---->", "out");
                    binding.tiEmail.setError("Bitte gültige E-Mail Adresse eingeben");
                    //binding.ilEmail.setBoxStrokeColor(context.getResources().getColor(R.color.red));
                    //binding.etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tic_red, 0);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 0) {

                    binding.tiCity.setError(null);
                    binding.tiCity.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiCity.setHintTextAppearance(R.style.EditTextHintStyle);

                } else {

                    binding.tiCity.setError(null);
                    //binding.tiCity.setError("Bitte geben Sie Ihre Stadt ein");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        binding.etPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 4) {

                    binding.tiPost.setError(null);
                    binding.tiPost.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiPost.setHintTextAppearance(R.style.EditTextHintStyle);

                } else {
                    binding.tiPost.setError(null);
                    //  binding.tiPost.setError("Bitte geben Sie mindestens 5 Stellen ein");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.etStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("---->", String.valueOf(s));

                if (s.length() > 2) {

                    binding.tiStreet.setError(null);
                    binding.tiStreet.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    binding.tiStreet.setHintTextAppearance(R.style.EditTextHintStyle);

                } else {
                    binding.tiStreet.setError(null);
                    // binding.tiStreet.setError("Bitte geben Sie Ihre Straße ein");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    private void onTouch() {
        binding.etfirstname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiFirstname.setError(null);
                return false;
            }
        });

        binding.etLastname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiLastname.setError(null);
                return false;
            }
        });

        binding.etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiEmail.setError(null);
                return false;
            }
        });

        binding.etPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiPost.setError(null);
                return false;
            }
        });

        binding.etCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiCity.setError(null);
                return false;
            }
        });

        binding.etPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.tiPhone.setError(null);
                return false;
            }
        });


    }
    private void setSpinners() {

        /*ArrayAdapter<String> adapterrr = new ArrayAdapter<String>(context, R.layout.gender_spinner_cell,city);
        binding.sCity.setAdapter(adapterrr);*/

        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(context, R.layout.gender_spinner_cell, gender);
        binding.sGender.setAdapter(adapterr);

        binding.sGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e(TAG, "onItemSelected:- " + String.valueOf(Gender));
                // binding.tvGender.setText(gender.get(position));

                if (Gender) {
                    Log.e(TAG, "onItemSelected: - here - ");
                    binding.tvGender.setText(gender.get(position));
                } else {
                    Gender = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.gender_spinner_cell, country);
        binding.sCountry.setAdapter(adapter);

        binding.sCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected:- " + String.valueOf(Country));
                if (Country) {
                    Log.e(TAG, "onItemSelected: - here - ");
                    binding.tvCountry.setText(country.get(position));
                } else {
                    Country = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void profile(String country, String gen) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        builder.addFormDataPart(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        builder.addFormDataPart(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        builder.addFormDataPart(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        builder.addFormDataPart(AppConstants.UID, Session.getUid(context));
        builder.addFormDataPart(AppConstants.ACTION, action);
        builder.addFormDataPart(AppConstants.GENDER, gen);
        builder.addFormDataPart(AppConstants.POST_CODE, binding.etPost.getText().toString());
        builder.addFormDataPart(AppConstants.CITY, binding.etCity.getText().toString());
        builder.addFormDataPart(AppConstants.COUNTRY, country);
        builder.addFormDataPart(AppConstants.LATTITUDE, "123.123");
        builder.addFormDataPart(AppConstants.LONGITUDE, "123.123");
        builder.addFormDataPart(AppConstants.LOCATION, binding.etStreet.getText().toString());
        builder.addFormDataPart(AppConstants.TELEPHONE, binding.etPhone.getText().toString());
        builder.addFormDataPart(AppConstants.LAST_NAME, binding.etLastname.getText().toString());
        builder.addFormDataPart(AppConstants.FIRST_NAME, binding.etfirstname.getText().toString());
        builder.addFormDataPart(AppConstants.OLD_IMAGE, "abc");
        builder.addFormDataPart("birth_date", dateSend);
        builder.addFormDataPart("newsletter", news);
        if (action.equals("edit")) {
            if (Image_file!=null) {
                builder.addFormDataPart(AppConstants.PROFILE_PIC, Image_file.getName(), RequestBody.create(MediaType.parse("image*//*"), Image_file));

            }
        }
        final MultipartBody requestbody = builder.build();
        Log.e("Request Body", String.valueOf(requestbody));
        RetrofitServices.urlServices.user_profile(requestbody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("profile-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        JSONObject in_data = jsonObject.getJSONObject("data");
                        if (action.equals("edit")) {
                            Session.setProfile(context,in_data.getString("profile_pic"));
                            Intent intent = new Intent(AppConstants.BROADCAST_KEY);
                            intent.putExtra(AppConstants.MAG1, "profile");
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        binding.etfirstname.setText(in_data.getString("first_name"));
                        binding.tvName.setText(in_data.getString("first_name") + " " + in_data.getString("last_name"));

                        binding.tvEmail.setText(in_data.getString("email"));
                        binding.etEmail.setText(in_data.getString("email"));

                        binding.etLastname.setText(in_data.getString("last_name"));
                        binding.etPhone.setText(in_data.getString("mobile"));
                        binding.etCity.setText(in_data.getString("city"));
                        binding.etPost.setText(in_data.getString("zip"));
                        binding.etStreet.setText(in_data.getString("address"));
                        mydate = in_data.getString("dob");
                        dateSend = in_data.getString("dob");
                        binding.etdDob.setText(Utility.change_format(mydate,"yyyy-mm-dd","dd.mm.yyyy"));
                        Session.setImagePath(context,jsonObject.getString("image_path"));

                        String newsletter = in_data.getString("newsletter");
                        if (newsletter.equals("1")){
                            binding.cbNews.setChecked(true);
                        }else {
                            binding.cbNews.setChecked(false);
                        }



                        Log.e(TAG, "onResponse: image url =  " + Session.getImagePath(context) + Session.getUid(context) + "/" + in_data.getString("profile_pic") );
                        String someFilepath = Session.getImagePath(context) + Session.getUid(context) + "/" + in_data.getString("profile_pic");
                        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
                        String url;
                        if (extension.equals(".webp"))
                        {
                            url = Session.getImagePath(context) + Session.getUid(context) + "/" + in_data.getString("profile_pic");
                        }else {
                            url = Session.getImagePath(context) + Session.getUid(context) + "/" + in_data.getString("profile_pic")+".webp";
                        }
                        DatabindingImageAdapter.loadImage(binding.civProfile, url);

                        if(!in_data.getString("profile_pic").equals("")){
                            binding.tvRemove.setVisibility(View.VISIBLE);
                        }else binding.tvRemove.setVisibility(View.GONE);

                        if (!in_data.getString("gender").equals("")) {
                            setGender(Utility.capitalize(in_data.getString("gender")));
                        }

                        if (!in_data.getString("country").equals("")) {
                            setCountry(in_data.getString("country"));
                        }

                        setCity(in_data.getString("city"));

                        action = "view";
                        Image_Name = "";

                        textWatcher();

                        Session.setProfile(context,in_data.getString("profile_pic"));
                        Intent intent = new Intent(AppConstants.BROADCAST_KEY);
                        intent.putExtra(AppConstants.MAG1, "profile");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.e("-->", "FALURE ");
            }
        });
    }

    private void setCity(String cityy) {

        if (city.contains(cityy)) {
            int a = city.indexOf(cityy);
            /*binding.sCity.setSelection(a);*/
        }
    }

    private void setCountry(String cntry) {

        Log.e("country is - ", cntry);

        if (country.contains(cntry)) {
            int i = country.indexOf(cntry);
            binding.sCountry.setSelection(i);
            binding.tvCountry.setText(country.get(i));
        }

    }

    private void setGender(String genderr) {

        Log.e("gender is - ", genderr);

        String gen = "";
        if (genderr != null && (genderr.equals("male") || genderr.equals("Male")))
        {
            gen = "Männlich";
        }else if (genderr != null && (genderr.equals("female") || genderr.equals("Female")))
        {
            gen = "Weiblich";
        }else {
            gen = "Andere";
        }
        if (gender.contains(gen)) {
            int position = gender.indexOf(gen);
            binding.sGender.setSelection(position);
            binding.tvGender.setText(gender.get(position));
        }

    }

    private void bottomSheet() {

        final ImageSellectionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_sellection, null, false);
        bsd = new BottomSheetDialog(context);
        bsd.setContentView(binding.getRoot());
        bsd.setCanceledOnTouchOutside(true);
        bsd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pix.start(MyProfileActivity.this, 100, 1);
                bsd.dismiss();
            }
        });

        binding.llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
                bsd.dismiss();
            }
        });

        bsd.show();
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle(context.getResources().getString(R.string.select_action));
        String[] pictureDialogItems = {
                context.getResources().getString(R.string.select_gallery),
                context.getResources().getString(R.string.select_camara) };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if(marshmallowPermission.check_camera_Permission()){
                                    cameraGallery.pickFromGallery();
                                }else {
                                    dialog.dismiss();
                                    showPictureDialog();
                                }
                                /*choosePhotoFromGallary();*/
                                break;
                            case 1:
                                if(marshmallowPermission.check_read_external_storage_permission()){
                                    cameraGallery.onLaunchCamera();
                                }else {
                                    dialog.dismiss();
                                    showPictureDialog();
                                }
                                /*takePhotoFromCamera();*/
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(cameraGallery.outputImageFile()!=null){
            Image_file=cameraGallery.outputImageFile();
            Log.e(TAG, "onResume: "+ cameraGallery.outputImageFile());
            ImageBinding.loadImage(binding.civProfile,Image_file);
            try {
                Log.e(TAG, "onResume: "+getReadableFileSize(cameraGallery.outputImageFile().length()) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void choosePhotoFromGallary() {
        if(marshmallowPermission.check_read_external_storage_permission()){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 1);
        }
    }

    private void takePhotoFromCamera() {
        if(marshmallowPermission.check_camera_Permission()) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 2);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Bungee.slideRight(context);
    }

    private void removeProfile() {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));


        RetrofitServices.urlServices.remove_profilepic(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("profile-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        binding.tvRemove.setVisibility(View.GONE);
                        binding.civProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_round_black));
                        Image_file=null;
                        Session.setProfile(context,"");
                        Intent intent = new Intent(AppConstants.BROADCAST_KEY);
                        intent.putExtra(AppConstants.MAG1, "profile");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE ");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Log.e(TAG, "onDateSet: " + day+" "+ (month-1)+" "+year );
        }
    }

}
