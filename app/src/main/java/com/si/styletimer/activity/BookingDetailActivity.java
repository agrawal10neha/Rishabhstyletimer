package com.si.styletimer.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.ServicesAdapter;
import com.si.styletimer.adapters.SliderViewPagerAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivityBookingDetailBinding;
import com.si.styletimer.databinding.CancelConfirmPopupBinding;
import com.si.styletimer.databinding.DialogConfirmBinding;
import com.si.styletimer.databinding.PopupThankReviewBinding;
import com.si.styletimer.databinding.ServicesCellBinding;
import com.si.styletimer.firebase.NotificationHelper;
import com.si.styletimer.models.CartModel;
import com.si.styletimer.models.bookind_detail_service.ServicesModel;
import com.si.styletimer.models.categoryListing.Sercvice;
import com.si.styletimer.models.salonDetails.SalonDetailModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.MarshmallowPermission;
import com.si.styletimer.utill.Utility;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "BookingDetailActivity";
    private Context context;
    ActivityBookingDetailBinding binding;
    private String id = "", from = "", salonname = "", hr_before_cancel = "";
    //sId = ""   salonId = "",salon_Id = "",

    private Gson gson;
    private Dialog dialog;
    private List<ServicesModel> servicesModelList = new ArrayList<>();
    private List<Sercvice> sercviceList = new ArrayList<>();
    private String reviewFlag = "";
    private float ratingg = 0;
    private Controller controller;
    private boolean isRated = false;
    String sallonId = "";
    private GoogleMap mMap;
    private float latt = 0, langg = 0;
    private List<String> Image = new ArrayList<>();
    private String cbAnonymous = "", employeeId = "0", review_id = "", bookingStatus = "", mobile = "", URL = "", selected = "", addrr = "";
    private MarshmallowPermission marshmallowPermission;
    private RealmController realmController;
    private String bookid = "", res_status = "";
    private String bookingTime;
    private CountDownTimer countDownTimer;
    private String hrBeforeCancel;
    private String bookId = "", invoice_id = "";
    private boolean isFirst = true;
    private AlertDialog dialog123;
    private String fragmentName;
    private String total_booking = "", ask_after_booking = "", app_review_status = "", booking_count = "",cancelBookingAllow = "";
    private boolean asyncRunning = false,iseventnull = true;
    private com.google.api.services.calendar.Calendar client;
    private GoogleAccountCredential credential;
    final NetHttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
    private static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final int REQUEST_ACCOUNT_PICKER = 2;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    DateTime startDateTime,endDateTime;
    private String sDate,eDate,empName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_detail);
        gson = new Gson();
        context = this;
        controller = (Controller) context.getApplicationContext();
        NotificationHelper.clearNotifications(context);
        marshmallowPermission = new MarshmallowPermission(context);
        realmController = new RealmController(context);
        initView();

/*
        binding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
            }
        });
*/
    }

    private void initView() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getIntentData();
        // getDetail();
        onClick();

    }

    private void onClick() {
        setupCalendarClient();
        binding.tvAddMyCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getPreferenceName(context).equals("")){
                    Log.e(TAG, "onClick: >>>>>>>>>>>>>>>>>>>" );
                    if (checkGooglePlayServicesAvailable()) {
                        haveGooglePlayServices();
                    }
                }else {
                    Log.e(TAG, "onClick: ??????????????????????" );
                    logutfromcalendaraccount();
                }
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("main")) {
                    Log.e(TAG, "onClick: here");
                    Intent in = new Intent(getApplicationContext(), MyBookingActivity.class);
                    in.putExtra("selected", selected);
                    startActivity(in);
                    finish();
                    // Bungee.slideRight(context);
                } else {
                    Log.e(TAG, "onClick: there");
                    Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                    // Bungee.slideLeft(context);
                }
            }
        });

        binding.rlstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  popupRating(id, sId);
                Intent intent = new Intent(context, WriteReviewActivity.class);
                intent.putExtra(AppConstants.BOOK_ID, bookid);
                intent.putExtra(AppConstants.SALON_ID, sallonId);
                intent.putExtra(AppConstants.EMPLOYEEID, employeeId);
                startActivity(intent);

            }
        });

       /* binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btLogin.getText().toString().equals("CANCEL BOOKING")) {
                    popup("c");

                } else if (binding.btLogin.getText().toString().equals("REBOOK")) {
                    popup("r");

                }
            }
        });*/

        binding.btnDetailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup("c");
            }
        });
        binding.btnReebok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popup("r");
                Session.setSalon_id(context, sallonId);
                rebook(id, sallonId);
            }
        });

        binding.btnDetailRescheGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellation_policy_popup("reschedule");
            }
        });
        binding.btnDetailCancelGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellation_policy_popup("cancel");
            }
        });

       /* binding.llReviewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ReviewDetailActivity.class).putExtra(AppConstants.Review_id, review_id));
            }
        });*/

        binding.rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Session.setSalon_id(context,sId);
                controller.sercviceList = sercviceList;
                startActivity(new Intent(context,SalonDetailActivity.class));*/

                realmController.clearServices();
                Session.setSalon_id(context, sallonId);
                Session.setServiceIds(context, returnServiceIds(sercviceList));
                Intent intent = new Intent(context, SalonDetailActivity.class);
                intent.putExtra(AppConstants.SERVICEIDS, returnServiceIds(sercviceList));
                startActivity(intent);
                //    Bungee.slideLeft(context);

            }
        });

        binding.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latt != 0 && langg != 0) {

                    //String a="google.navigation:q="+String.valueOf(latt)+","+String.valueOf(langg)+"&mode=d";
                    String a="http://maps.google.com/maps?q=loc:"+String.valueOf(latt)+","+String.valueOf(langg);
                    Log.e(TAG, "onClick: "+a );
                  /*  Uri gmmIntentUri = Uri.parse(a);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);*/

                    /*    String strUri = "http://maps.google.com/maps?q=loc:" + latt + "," + langg + " (" + addrr + ")";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(Intent.createChooser(intent, "Select an application"));*/
                    //  startActivity(intent);

                  /*  String uri = "http://maps.google.com/maps?f=d&hl=en&daddr=" + latt+ "," + langg+ " (" + addrr + ")";;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(Intent.createChooser(intent, "Select an application"));*/

                    Uri mapUri = Uri.parse("geo:" + latt + "," + langg + "?q=" + Uri.encode(addrr));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

        binding.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latt != 0 && langg != 0) {
                    String a="http://maps.google.com/maps?q=loc:"+String.valueOf(latt)+","+String.valueOf(langg);
                    Log.e(TAG, "onClick: "+a );
                    Uri mapUri = Uri.parse("geo:" + latt + "," + langg + "?q=" + Uri.encode(addrr));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

        binding.rlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile != null && !mobile.equals("")) {
                    phoneCall();
                }
            }
        });

        binding.rlReceipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReceiptActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("service", getServices());
                intent.putExtra("book_id", invoice_id);
                intent.putExtra("salonname", salonname);
                startActivity(intent);
            }
        });

        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: URL " + URL);
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = URL;
                String shareSub = URL;
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share using"));
            }
        });

        binding.btnDetailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupcancel();
            }
        });

        binding.btnDetailResche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datee = Utility.change_format(bookingTime, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
                String timeee = Utility.change_format(bookingTime, "yyyy-MM-dd HH:mm:ss", "HH:mm");
                Session.setSalon_id(context, sallonId);
                Intent intent = new Intent(context, SelectDateActivity.class);
                intent.putExtra(AppConstants.SALON_ID, sallonId);
                intent.putExtra(AppConstants.FLAG, employeeId);
                //  intent.putExtra(AppConstants.DATE, Utility.getCurrentDateYY());
                intent.putExtra(AppConstants.DATE, datee);
                intent.putExtra(AppConstants.TIME, timeee);
                intent.putExtra(AppConstants.FLAGTWO, AppConstants.RESCHEDULE);
                intent.putExtra(AppConstants.BOOK_ID, bookid);
                startActivity(intent);
            }
        });
    }

    private String getServices() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < servicesModelList.size(); i++) {
            stringBuilder.append(servicesModelList.get(i).getServiceName() + ",");
        }

        return removelastcomma(stringBuilder.toString());
    }

    public static String removelastcomma(String s) {
        if (s.endsWith(",")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private void cancellation_policy_popup(String x) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.cancellation_policy_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);

        if (cancelBookingAllow.equals("no"))
        {
            head.setText("Deine Buchung bei " + salonname + " kann leider nicht durch styletimer verlegt oder storniert werden, da diese Option vom Salon deaktiviert wurde.");
            sHead.setText("Bitte kontaktiere den Salon direkt unter: " + mobile);
        }else {
            head.setText("Deine Buchung bei " + salonname + " kann leider nicht mehr durch styletimer verlegt oder storniert werden, da dein Termin in weniger als "+ hr_before_cancel +" Stunden beginnt.");
            sHead.setText("Bitte kontaktiere den Salon direkt unter: " + mobile);
        }
       //head.setText("Deine Buchung bei " + salonname + " kann leider nicht durch styletimer verlegt oder storniert werden, da diese "+ hr_before_cancel +" vom Salon deaktiviert wurde.");
       //sHead.setText("Bitte kontaktiere den Salon direkt unter: " + mobile);
        yes.setText("Salon anrufen");
        no.setText("Schliessen");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                phoneCall();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void phoneCall() {
        if (marshmallowPermission.check__call_phone_permission()) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            String a = "tel:" + mobile;
            callIntent.setData(Uri.parse(a));
            startActivity(callIntent);

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }


        } else {
            //phoneCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    //denied
                    Log.e("denied", permission);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage(context.getString(R.string.permission_message))

                        .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // CameraIntentTestActivity.this.finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {

            switch (1) {
                case MarshmallowPermission.REQUEST_CALL_PHONE_PERMISSION:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        String a = "tel:" + mobile;
                        callIntent.setData(Uri.parse(a));
                        startActivity(callIntent);
                    }
                    break;

            }
        }

    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            fragmentName = intent.getStringExtra("Fragment");
            id = intent.getStringExtra("id");
            from = intent.getStringExtra("from");
//            sId = intent.getStringExtra("sId");
//            salonId = intent.getStringExtra("sId");
            bookingStatus = intent.getStringExtra("status");
            selected = intent.getStringExtra("selected");
            res_status = intent.getStringExtra(AppConstants.RES_STATUS);
            Log.e(TAG, "getIntentData: - " + res_status);


            Bundle bundle = getIntent().getExtras();
            Log.e(TAG, "getIntentData:------->>>>>>> " + bundle.toString());
            if (id == null) {
                id = bundle.getString("book_id");
            }

            total_booking = intent.getStringExtra("total_booking");
            ask_after_booking = intent.getStringExtra("ask_after_booking");
            app_review_status = intent.getStringExtra("app_review_status");
            booking_count = intent.getStringExtra("booking_count");

        }
    }

    private void setupStatus(String status, String enddate,String onlineBooking) {
        binding.cvBtns.setVisibility(View.VISIBLE);
        Log.i("online booking", onlineBooking);
        if (status.equals("confirmed")) {
            binding.cvBtns.setVisibility(View.VISIBLE);
            if (fragmentName != null && fragmentName.equals("CompletedFragment")) {
                if (onlineBooking.equals("0")) {
                    binding.btnReebok.setVisibility(View.GONE);
                    binding.cvBtns.setVisibility(View.GONE);

                } else {
                    binding.btnReebok.setVisibility(View.VISIBLE);
                    binding.cvBtns.setVisibility(View.VISIBLE);

                }

              //  binding.btnReebok.setVisibility(View.VISIBLE);
                binding.tvAddMyCalendar.setVisibility(View.GONE);
            } else {
                binding.btnReebok.setVisibility(View.GONE);
                binding.tvAddMyCalendar.setVisibility(View.VISIBLE);
            }

            reviewFlag = "";
            binding.tvstatus.setText(context.getResources().getString(R.string.confirmed));
            binding.llStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_confirmed_status));
            binding.tvstatus.setTextColor(Color.parseColor("#6ac369"));
            binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_comfirm_booking));
            //  binding.btLogin.setText("CANCEL BOOKING");

            // binding.cvButton.setVisibility(View.VISIBLE);
            binding.llReason.setVisibility(View.GONE);

            binding.viewstar.setVisibility(View.GONE);
            binding.rlstar.setVisibility(View.GONE);

            //binding.viewReview.setVisibility(View.GONE);
            binding.llReviewed.setVisibility(View.GONE);
            binding.tvYourReview.setVisibility(View.GONE);
            binding.View1.setVisibility(View.GONE);
            binding.View2.setVisibility(View.GONE);


          /*  if (timeleft(bookingTime) > 86400000){
                binding.btLogin.setVisibility(View.GONE);
                if (!res_status.equals("0")){
                    binding.cvResheule.setVisibility(View.GONE);
                    binding.btLogin.setVisibility(View.VISIBLE);
                }else {

                    binding.cvResheule.setVisibility(View.VISIBLE);

                    if (cancelBookingAllow.equals("yes")){
                        long valueforCancel = getMilisecFromHours(hrBeforeCancel);
                        if (timeleft(bookingTime) > valueforCancel){
                            binding.btnDetailCancel.setVisibility(View.VISIBLE);
                        }else {
                            binding.btnDetailCancel.setVisibility(View.GONE);
                        }
                    }else {
                        binding.btnDetailCancel.setVisibility(View.GONE);
                    }
                }

            }else {
                binding.btLogin.setVisibility(View.GONE);
                binding.cvResheule.setVisibility(View.GONE);
            }*/

            if (cancelBookingAllow.equals("yes")) {
                long valueforCancel = getMilisecFromHours(hrBeforeCancel);
                if (Utility.timeleft(bookingTime) > valueforCancel) {
                    binding.btnDetailCancel.setVisibility(View.VISIBLE);
                    binding.btnDetailCancelGray.setVisibility(View.GONE);
                } else {
                    binding.btnDetailCancel.setVisibility(View.GONE);
                    binding.btnDetailCancelGray.setVisibility(View.VISIBLE);
                }

                // chek for reschedule btn ot hide
                if (Utility.timeleft(bookingTime) > valueforCancel) {
                    Log.e(TAG, "setupStatus: " +Utility.timeleft(bookingTime)+" "+valueforCancel+"  "+res_status  );
                    if (res_status != null && !res_status.equals("0")) {
                        binding.btnDetailResche.setVisibility(View.GONE);
                        binding.btnDetailRescheGray.setVisibility(View.GONE);
                    } else {
                        Log.e(TAG, "setupStatus: elsee " );
                        binding.btnDetailResche.setVisibility(View.VISIBLE);
                        binding.btnDetailRescheGray.setVisibility(View.GONE);
                    }
                } else {
                    binding.btnDetailResche.setVisibility(View.GONE);
                    binding.btnDetailRescheGray.setVisibility(View.VISIBLE);
                }
            } else {
                binding.btnDetailCancel.setVisibility(View.GONE);
                binding.btnDetailCancelGray.setVisibility(View.VISIBLE);
            }


            if (fragmentName != null && fragmentName.equals("CompletedFragment")) {
                binding.btnDetailCancelGray.setVisibility(View.GONE);
                binding.btnDetailRescheGray.setVisibility(View.GONE);
            }


            //  cancleVisibilty(bookingTime);

        } else if (status.equals("cancelled")) {

            reviewFlag = "";
            binding.tvstatus.setText(context.getResources().getString(R.string.cancelled));
            binding.tvstatus.setTextColor(Color.parseColor("#FC6076"));
            binding.llStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_cancelled_status));
            binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_booking_cancel));

            // binding.btLogin.setText("REBOOK");

            //  binding.cvButton.setVisibility(View.GONE);
            binding.cvBtns.setVisibility(View.VISIBLE);

            if (onlineBooking.equals("0")) {
                binding.btnReebok.setVisibility(View.GONE);
                binding.cvBtns.setVisibility(View.GONE);
            } else {
                binding.btnReebok.setVisibility(View.VISIBLE);
                binding.cvBtns.setVisibility(View.VISIBLE);
            }
            //  binding.btnReebok.setVisibility(View.VISIBLE);
            binding.llReason.setVisibility(View.VISIBLE);

            binding.viewstar.setVisibility(View.GONE);
            binding.rlstar.setVisibility(View.GONE);

            //binding.viewReview.setVisibility(View.GONE);
            binding.llReviewed.setVisibility(View.GONE);
            binding.tvYourReview.setVisibility(View.GONE);

            binding.View1.setVisibility(View.GONE);
            binding.View2.setVisibility(View.GONE);

        } else if (status.equals("no show")) {

            binding.tvstatus.setText(context.getResources().getString(R.string.no_show));
            binding.tvstatus.setTextColor(Color.parseColor("#FC6076"));
            binding.llStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_cancelled_status));
            binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_booking_cancel));
            String date[] = enddate.split(" ");

            if (Utility.comparedates(Utility.getCurrentDateYY(), date[0]).equals("before")) {
                binding.cvBtns.setVisibility(View.VISIBLE);
                if (onlineBooking.equals("0")) {
                    binding.btnReebok.setVisibility(View.GONE);
                    binding.cvBtns.setVisibility(View.GONE);

                } else {
                    binding.btnReebok.setVisibility(View.VISIBLE);
                    binding.cvBtns.setVisibility(View.VISIBLE);

                }
              //  binding.btnReebok.setVisibility(View.VISIBLE);

            } else if (Utility.comparedates(Utility.getCurrentDateYY(), date[0]).equals("after")) {
                binding.cvBtns.setVisibility(View.GONE);
                if (onlineBooking.equals("0")) {
                    binding.btnReebok.setVisibility(View.GONE);
                    binding.cvBtns.setVisibility(View.GONE);

                } else {
                    binding.btnReebok.setVisibility(View.VISIBLE);
                    binding.cvBtns.setVisibility(View.VISIBLE);

                }
                //binding.btnReebok.setVisibility(View.GONE);

            } else if (Utility.comparedates(Utility.getCurrentDateYY(), date[0]).equals("equal")) {
                binding.cvBtns.setVisibility(View.VISIBLE);
                if (onlineBooking.equals("0")) {
                    binding.btnReebok.setVisibility(View.GONE);
                    binding.cvBtns.setVisibility(View.GONE);

                } else {
                    binding.btnReebok.setVisibility(View.VISIBLE);
                    binding.cvBtns.setVisibility(View.VISIBLE);

                }
              //  binding.btnReebok.setVisibility(View.VISIBLE);
            }

            binding.llReason.setVisibility(View.GONE);
            binding.viewstar.setVisibility(View.GONE);
            binding.rlstar.setVisibility(View.GONE);
            binding.llReviewed.setVisibility(View.GONE);
            binding.tvYourReview.setVisibility(View.GONE);
            binding.View1.setVisibility(View.GONE);
            binding.View2.setVisibility(View.GONE);
        } else {
            reviewFlag = "completed";
            binding.tvstatus.setText(context.getResources().getString(R.string.completed));
            binding.tvstatus.setTextColor(Color.parseColor("#4BB543"));
            binding.llStatus.setBackground(context.getResources().getDrawable(R.drawable.bg_completed_status));
            binding.ivStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_completed_booking));

            //  binding.btLogin.setText("REBOOK");
            binding.cvBtns.setVisibility(View.VISIBLE);
            binding.llReason.setVisibility(View.GONE);
            binding.viewstar.setVisibility(View.GONE);
            binding.rlstar.setVisibility(View.VISIBLE);

            //binding.viewReview.setVisibility(View.VISIBLE);
            binding.llReviewed.setVisibility(View.VISIBLE);
            binding.tvYourReview.setVisibility(View.VISIBLE);

            if (onlineBooking.equals("0")) {
                binding.btnReebok.setVisibility(View.GONE);
                binding.cvBtns.setVisibility(View.GONE);

            } else {
                binding.btnReebok.setVisibility(View.VISIBLE);
                binding.cvBtns.setVisibility(View.VISIBLE);

            }
           // binding.btnReebok.setVisibility(View.VISIBLE);
        }
    }

    private long getMilisecFromHours(String time) {
        int acttime = Integer.parseInt(time);
        return (acttime * 3600000);
    }

    private void popup(String x) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.booking_cancel_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);

        Button btCancel = dialog.findViewById(R.id.btCancel);
        ImageView icClose = dialog.findViewById(R.id.icClose);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);

        RadioGroup rg = dialog.findViewById(R.id.rg);

        if (x.equals("c")) {

            yes.setText(context.getResources().getString(R.string.yes));
            no.setText(context.getResources().getString(R.string.no));
            yes.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            btCancel.setVisibility(View.VISIBLE);
            icClose.setVisibility(View.VISIBLE);

            head.setText(context.getResources().getString(R.string.cancel_booking));
            sHead.setText(context.getResources().getString(R.string.are_you_sure_cancel_booking));

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    int a = rg.getCheckedRadioButtonId();
                    RadioButton rb = dialog.findViewById(a);

                    cancelBook(rb.getText().toString());
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    int a = rg.getCheckedRadioButtonId();
                    RadioButton rb = dialog.findViewById(a);

                    cancelBook(rb.getText().toString());
                }
            });
            icClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        } else if (x.equals("r")) {

            yes.setText("NOCHMAL BUCHEN");
            no.setText("SCHLIESSEN");
            head.setText(context.getResources().getString(R.string.rebook));
            sHead.setText("Bist du sicher, dass du diese Services erneut buchen möchtest?");

            rg.setVisibility(View.GONE);

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Session.setSalon_id(context, sallonId);
                    rebook(id, sallonId);
                    dialog.dismiss();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            icClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();
    }

    private void getDetail()
    {

        binding.loader.setVisibility(View.VISIBLE);

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.BOOK_ID, id);

        Log.e(TAG, "booking detail " + hashMap);

        RetrofitServices.urlServices.booking_detail(hashMap).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    binding.loader.setVisibility(View.GONE);
                    String respo = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(respo);
                    Log.e("respo   ----",respo );
                    if (jsonObject.getInt("status") == 1) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONObject inData = data.getJSONObject("detail");

                        bookid = inData.getString("id");

                        salonname = inData.getString("business_name");
                        binding.tvName.setText(inData.getString("business_name"));
                        binding.tvAddress.setText(inData.getString("address") + "\n" +
                                inData.getString("zip") + " " + inData.getString("city"));


                        addrr = inData.getString("address") + " " + inData.getString("zip") + " " + inData.getString("city");

                        binding.tvStylist.setText(inData.getString("emp_first_name")/*+" "+inData.getString("emp_last_name")*/);
                        //binding.tvDate.setText(inData.getString("booking_time"));
                        servicesModelList = gson.fromJson(data.getJSONArray("all_services").toString(), new TypeToken<ArrayList<ServicesModel>>() {
                        }.getType());
                        String sbb = "";
                        for (int i = 0;i<servicesModelList.size();i++)
                        {
                            if (servicesModelList.get(i).getPriceStartOption().equals("ab"))
                            {
                                sbb = "ab";
                            }
                        }
                        
                       // binding.tvAmount.setText(Utility.getPriceReplaceDotWithComma(inData.getString("tot_price")) + "€");

                        employeeId = inData.getString("employee_id");
                        bookingTime = inData.getString("booking_time");
                        review_id = inData.getString("review_id");
                        cancelBookingAllow = inData.getString("cancel_booking_allow");
                        hrBeforeCancel = inData.getString("hr_before_cancel");
                        sallonId = inData.getString("merchant_id");
                        String email_text = inData.getString("email_text");
                        if (email_text != null && !email_text.equals("")) {
                            binding.rlemailText.setVisibility(View.VISIBLE);
                            binding.emailText.setText("Zusätzliche Infos von " + salonname.toString().trim() + ":");
                            binding.tvemailText.setText(email_text);
                        } else {
                            binding.rlemailText.setVisibility(View.GONE);
                        }

                        if (inData.getString("reason").equals("Cancelled by Salon")) {
                            binding.tvCancelReason.setText("Wurde vom Salon storniert");
                        } else {
                            binding.tvCancelReason.setText(inData.getString("reason"));
                        }



                        mobile = inData.getString("mobile");
                        hr_before_cancel = inData.getString("hr_before_cancel");
                        binding.tvNumber.setText(mobile);

                        bookId = inData.getString("book_id");
                        invoice_id = inData.getString("invoice_id");
                        if (inData.getString("status").equals("no show")) {
                            binding.tvstatus.setText(context.getResources().getString(R.string.no_show));
                        }

                        if (!inData.getString("tot_discount").equals("0")) {
                            binding.rlDiscount.setVisibility(View.VISIBLE);
                            binding.tvDiscount.setText(Utility.getPriceReplaceDotWithComma(inData.getString("tot_discount")) + "€");
                        } else {
                            binding.rlDiscount.setVisibility(View.GONE);
                        }

                        servicesModelList = gson.fromJson(data.getJSONArray("all_services").toString(), new TypeToken<ArrayList<ServicesModel>>() {
                        }.getType());

                        binding.rvServices.setLayoutManager(new LinearLayoutManager(context));
                        binding.rvServices.setNestedScrollingEnabled(false);
                        binding.rvServices.setAdapter(new ServicesAdapter(context, servicesModelList));

                        setupStatus(inData.getString("status"), inData.getString("end_date"),inData.getString("online_booking"));    // todo--> to set rating/review view and status and its color
                        // salon_Id = inData.getString("id");

                        Double tot_price= Double.valueOf(inData.getString("tot_price"));
                        Double tot_discount= Double.valueOf(inData.getString("tot_discount"));
                        String final_price= String.valueOf(tot_price - tot_discount);

                        if (inData.getString("status").equals("completed")) {
                            if (sbb.equals("ab"))
                            {
                                binding.tvAmount.setText("ab "+Utility.getPriceReplaceDotWithComma(final_price) + "€");
                               // binding.tvAmount.setText(Utility.getPriceReplaceDotWithComma(inData.getString("tot_price")) + "€");
                            }else {
                                binding.tvAmount.setText(Utility.getPriceReplaceDotWithComma(final_price) + "€");
                               // binding.tvAmount.setText(Utility.getPriceReplaceDotWithComma(inData.getString("tot_price")) + "€");
                            }
                            if (inData.getString("is_review").equals("1")) {
                                binding.llReviewed.setVisibility(View.VISIBLE);
                                binding.tvYourReview.setText(context.getResources().getString(R.string.your_rating_amp_review_after));
                                binding.ratingbar.setRating(Float.valueOf(inData.getString("rating")));
                                binding.tvReview.setText(inData.getString("review"));
                                binding.rlstar.setVisibility(View.GONE);
                                isRated = true;
                            } else {
                                binding.tvYourReview.setText(context.getResources().getString(R.string.your_rating_amp_review));
                                binding.llReviewed.setVisibility(View.GONE);
                                binding.tvYourReview.setVisibility(View.GONE);
                                //binding.viewReview.setVisibility(View.GONE);
                                isRated = false;

                                if (isFirst) {
                                    Intent intent = new Intent(context, WriteReviewActivity.class);
                                    intent.putExtra(AppConstants.BOOK_ID, bookid);
                                    intent.putExtra(AppConstants.SALON_ID, sallonId);
                                    intent.putExtra(AppConstants.EMPLOYEEID, employeeId);
                                    intent.putExtra(AppConstants.RES_STATUS, res_status);
                                    intent.putExtra(AppConstants.FLAG, "detail");
                                    startActivity(intent);
                                    isFirst = false;
                                }
                                // popupRating(id, sId);
                            }

                        } else {
                            if (sbb.equals("ab"))
                            {
                                binding.tvAmount.setText("ab "+Utility.getPriceReplaceDotWithComma(final_price) + "€");
                               // binding.tvAmount.setText("ab "+Utility.getPriceReplaceDotWithComma(inData.getString("tot_price")) + "€");
                            }else {
                                binding.tvAmount.setText(Utility.getPriceReplaceDotWithComma(final_price) + "€");
                                //binding.tvAmount.setText(Utility.getPriceReplaceDotWithComma(inData.getString("tot_price")) + "€");
                            }
                            binding.llReviewed.setVisibility(View.GONE);
                            binding.tvYourReview.setVisibility(View.GONE);
                            //binding.viewReview.setVisibility(View.GONE);
                            binding.rlstar.setVisibility(View.GONE);
                            binding.rlReceipe.setVisibility(View.GONE);
                        }

                        String a[] = inData.getString("booking_time").split(" ");
                        String b[] = a[1].split(":");
                        String c[] = a[0].split("-");

                        binding.tvBookTime.setText(b[0] + ":" + b[1]);
                        binding.tvBookDate.setText(c[2] + "." + c[1] + "." + c[0]);

                        Log.e(TAG, "onResponse: +=+ " + data.getJSONArray("sercvices").toString());
                        sercviceList = gson.fromJson(data.getJSONArray("sercvices").toString(), new TypeToken<ArrayList<Sercvice>>() {
                        }.getType());
                        sercviceList.toString();

                        setMarker(Float.valueOf(inData.getString("latitude")), Float.valueOf(inData.getString("longitude")));

                        JSONObject images = jsonObject.getJSONObject("images");

                        String id = "";

                        if (!images.equals("")) {
                            binding.viewPager.setVisibility(View.VISIBLE);
                            id = images.getString("user_id");

                            if (!images.getString("image").equals(""))
                                Image.add(images.getString("image"));
                            if (!images.getString("image1").equals(""))
                                Image.add(images.getString("image1"));
                            if (!images.getString("image2").equals(""))
                                Image.add(images.getString("image2"));
                            if (!images.getString("image3").equals(""))
                                Image.add(images.getString("image3"));
                            if (!images.getString("image4").equals(""))
                                Image.add(images.getString("image4"));
                        } else {
                            binding.viewPager.setVisibility(View.GONE);
                        }

                        URL = inData.getString("share_url");
                        SliderViewPagerAdapter adapter = new SliderViewPagerAdapter(context, Image, id);
                        binding.viewPager.setAdapter(adapter);
                        sDate = inData.getString("booking_time");
                        eDate = inData.getString("booking_endtime");
                        empName = inData.getString("emp_first_name")+" "+inData.getString("emp_last_name");
                    } else if (jsonObject.getInt("status") == 0) {

                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
//                    pd.dismiss();
                    binding.loader.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                if (app_review_status != null && !app_review_status.equals("") && app_review_status.equals("ask_me_later")) {
                    int totc = Integer.parseInt(total_booking);
                    int askc = Integer.parseInt(ask_after_booking);
                    if (totc % askc == 0) {
                        app_review_popup();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);
                Log.e(TAG, "FALURE ");
            }
        });
    }


    private void app_review_popup() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.app_review_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                app_review_popup_three();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                app_review_popup_two();
            }
        });
        dialog.show();
    }

    private void app_review_popup_two() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.app_review_popup_two);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                app_review_feedback_popup();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                update_app_review_status("", "leave_me_alone");
            }
        });
        dialog.show();
    }

    private void app_review_feedback_popup() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.app_review_feedback_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView yes = dialog.findViewById(R.id.yes);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        com.google.android.material.textfield.TextInputLayout tlFeedback = dialog.findViewById(R.id.tlFeedback);
        com.google.android.material.textfield.TextInputEditText etFeedback = dialog.findViewById(R.id.etFeedback);
        dialog.setCanceledOnTouchOutside(false);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etFeedback.getText().toString().equals("")) {
                    dialog.dismiss();
                    update_app_review_status(etFeedback.getText().toString(), "rate_now");
                } else {
                    Toast.makeText(context, "Bitte geben Sie Ihr Feedback ein", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void app_review_popup_three() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.app_review_popup_three);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);
        TextView noLeave = dialog.findViewById(R.id.noLeave);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                update_app_review_status("", "rate_now");
                //update_app_review_status("ask_me_later");
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                update_app_review_status("", "ask_me_later");
            }
        });
        noLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                update_app_review_status("", "leave_me_alone");
            }
        });

        dialog.show();
    }

    private void update_app_review_status(String feedback, String actionn) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put("app_review_status", actionn);
        hashMap.put("booking_count", total_booking);
        hashMap.put("feedback", feedback);
        Log.e(TAG, "update_app_review_status " + hashMap);
        RetrofitServices.urlServi.update_app_review_status(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {
                        if (feedback != null && !feedback.equals("")) {
                            Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        }
                    } else if (jsonObject.getInt("status") == 0) {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
                if (feedback != null && feedback.equals("")) {
                    if (actionn != null && actionn.equals("rate_now")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.si.styletimer")));
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")));
                    }
                }
                total_booking = "";
                ask_after_booking = "";
                app_review_status = "";

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Log.e("-->", "FALURE ");
            }
        });
    }


    private void cancleVisibilty(String date) {

        String a[] = date.split(" ");
        String b[] = a[1].split(":");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date());

        Log.e("DATE IS ", currentTime);

        if (Utility.comparedates(String.valueOf(formattedDate), a[0]).equals("after")) {
            binding.btnDetailCancel.setVisibility(View.GONE);

        } else if (Utility.comparedates(String.valueOf(formattedDate), a[0]).equals("equal")) {

            Log.e("DATE IS ", Utility.checktime(currentTime, b[0] + ":" + b[1]));

            if (Utility.checktime(currentTime, b[0] + ":" + b[1]).equals("after")) {
                binding.btnDetailCancel.setVisibility(View.GONE);
            } else {
                binding.cvBtns.setVisibility(View.VISIBLE);
                binding.btnDetailCancel.setVisibility(View.VISIBLE);
            }
        } else {
            binding.cvBtns.setVisibility(View.VISIBLE);
            binding.btnDetailCancel.setVisibility(View.VISIBLE);
        }

    }

    private void cancelBook(String reason) {
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
        hashMap.put(AppConstants.BOOK_ID, id);
        hashMap.put(AppConstants.REASON, reason);

        Log.e("-->", "booking cancel" + hashMap);

        RetrofitServices.urlServices.booking_cancel(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("booking cancel-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        cancel_confirm_popup(jsonObject.getString("response_message"));

                        // finish();

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

    private void cancel_confirm_popup(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        CancelConfirmPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.cancel_confirm_popup, null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        dialog123 = builder.create();

        if (binding.ivmaingiftwo.getDrawable() != null) {
            pl.droidsonroids.gif.GifDrawable gifDrawable = (pl.droidsonroids.gif.GifDrawable) binding.ivmaingiftwo.getDrawable();
            gifDrawable.setLoopCount(1);
        }

        //  binding.tvtitle.setText(msg);
        binding.tvtitle.setText("Deine Buchung wurde erfolgreich storniert");

        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog123.dismiss();
                Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                //finish();
            }
        });

        binding.btnivclosedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog123.dismiss();
                finish();
            }
        });
        dialog123.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog123 != null) {
            dialog123.dismiss();
        }
        if (dialog != null) {
            dialog.dismiss();
        }

    }


    private void rebook(String bookingId, String salonId) {

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
        hashMap.put(AppConstants.BOOK_ID, bookingId);
        hashMap.put(AppConstants.SALON_ID, salonId);

        Log.e("-->", "Rebook-->" + hashMap);

        RetrofitServices.urlServices.rebook_service(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Rebook --> ", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getInt("status") == 1) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        controller.detailModel = gson.fromJson(data.getJSONArray("details").getJSONObject(0).toString(), SalonDetailModel.class);
                        controller.day = "Today";

                        startActivity(new Intent(context, ChooseStylistActivity.class));
                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();

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

    private void popupRating(String bookingId, String salonId) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.booking_cancel_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout llOther = dialog.findViewById(R.id.llOther);
        RelativeLayout rlRating = dialog.findViewById(R.id.rlRating);

        llOther.setVisibility(View.GONE);
        rlRating.setVisibility(View.VISIBLE);

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);

        RatingBar RatingBar = dialog.findViewById(R.id.ratingbar);
        TextInputLayout ilReview = dialog.findViewById(R.id.ilReview);
        TextInputEditText etReview = dialog.findViewById(R.id.etReview);
        Button btSubmit = dialog.findViewById(R.id.btSubmit);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        CheckBox cbanonymous = dialog.findViewById(R.id.cbanonymous);

        cbanonymous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbAnonymous = "1";
                } else {
                    cbAnonymous = "0";
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        etReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ilReview.setError(null);
                    ilReview.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                    ilReview.setHintTextAppearance(R.style.EditTextHintStyle);
                } else {
                    ilReview.setError("Bitte schreibe eine Bewertung");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingg = rating;
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = etReview.getText().toString();
                int ratonng = (int) ratingg;
                Log.e(TAG, "onClick: " + ratonng);

                if (ratonng == 0) {
                    Toast.makeText(context, "Bitte wähle eine Bewertung", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ratonng < 4) {
                    if (TextUtils.isEmpty(review)) {
                        ilReview.setError("Bitte schreibe eine Bewertung");
                        ilReview.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                        ilReview.setHintTextAppearance(R.style.EditTextHintStyle);
                    } else {
                        dialog.dismiss();
                        review = "";
                        postReview(bookingId, salonId, String.valueOf(ratingg), review, employeeId);
                    }
                } else {
                    dialog.dismiss();
                    postReview(bookingId, salonId, String.valueOf(ratingg), review, employeeId);
                }
            }
        });

        //--Rating--

        dialog.show();
    }

    private void postReview(String bookId, String salonId, String rating, String review, String employeid) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));

        hashMap.put(AppConstants.BOOK_ID, bookId);
        hashMap.put(AppConstants.SALON_ID, salonId);

        hashMap.put(AppConstants.RATING, rating);
        hashMap.put(AppConstants.REVIEW, review);
        hashMap.put(AppConstants.EMPLOYEEID, employeid);
        hashMap.put(AppConstants.ANONYMOUS, cbAnonymous);


        Log.e("-->", "Review " + hashMap);

        RetrofitServices.urlServices.addreview_service(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Review-->", "onResponse: " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if (jsonObject.getString("status").equals("1")) {

                        popup();

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
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (from.equals("main")) {
            Log.e(TAG, "onClick: here");
            Intent in = new Intent(getApplicationContext(), MyBookingActivity.class);
            in.putExtra("selected", selected);
            startActivity(in);
            finish();
            //  Bungee.slideRight(context);
        } else {
            Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
        }
    }

    private void setMarker(float lat, float lang) {
        Log.e(TAG, "setMarker: " + lat + "   lang   " + lang);
        latt = lat;
        langg = lang;
        LatLng sydney = new LatLng(lat, lang);
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lang)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
        //mMap.getUiSettings().setZoomControlsEnabled(true);
       // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang), 16));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PopupThankReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.popup_thank_review, null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // getDetail();
                Intent in = new Intent(getApplicationContext(), MyBookingActivity.class);
                in.putExtra("selected", selected);
                startActivity(in);
                finish();
            }
        });

        binding.btnivclosedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void axe() {
        DialogConfirmBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popup_thank_review, null, false);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(binding.getRoot());
    }

    private String returnServiceIds(List<Sercvice> sercviceList) {
        Log.e(TAG, "returnServiceIds: " + sercviceList.toString());
        String ids = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (sercviceList.size() > 0) {
            for (int i = 0; i < sercviceList.size(); i++) {
                sercviceList.get(i).getSubcategoryId();
                stringBuilder.append(sercviceList.get(i).getSubcategoryId() + ",");
            }
            ids = stringBuilder.toString().substring(0, stringBuilder.length() - ",".length());
            return ids;
        }
        return ids;
    }


    private void popupcancel() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.booking_cancel_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioGroup rg = dialog.findViewById(R.id.rg);
        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);

        Button btCancel = dialog.findViewById(R.id.btCancel);
        ImageView icClose = dialog.findViewById(R.id.icClose);

        Log.e(TAG, "popup: here we t");

        yes.setVisibility(View.GONE);
        yes.setText(context.getResources().getString(R.string.yes));
        no.setText(context.getResources().getString(R.string.no));
        head.setText(context.getResources().getString(R.string.cancel_booking));
        sHead.setText(context.getResources().getString(R.string.are_you_sure_cancel_booking));

        no.setVisibility(View.GONE);
        btCancel.setVisibility(View.VISIBLE);
        icClose.setVisibility(View.VISIBLE);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                int a = rg.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(a);

                cancelBook(rb.getText().toString());
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: __== ");
                dialog.dismiss();
                int a = rg.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(a);

                cancelBook(rb.getText().toString());
            }
        });
        icClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    } // todo call cancel Api----


    public static long timeleft(String creeteond) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(creeteond);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Date today = new Date();
        long abc = (date1.getTime() - today.getTime());
        //long newtask = (86400000 - abc);
        Log.e(TAG, "timeleft: " + abc + " " + today.getTime() + " " + date1.getTime());
        return abc;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static String getLeftTimeStrforChat(Long millisUntilFinished) {
        long seconds = millisUntilFinished / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (minutes > 0) {
            seconds = seconds % 60;
        }
        if (hours > 0) {
            minutes = minutes % 60;
        }

        StringBuilder strhour = new StringBuilder();
        StringBuilder strminutee = new StringBuilder();
        StringBuilder strsec = new StringBuilder();

        if (hours < 10) {
            strhour.append("0" + hours);
        } else {
            strhour.append(hours);
        }
        if (minutes < 10) {
            strminutee.append("0" + minutes);
        } else if (minutes == 0) {
            strminutee.append("00");
        } else {
            strminutee.append(minutes);
        }

        if (seconds < 10) {
            strsec.append("0" + seconds);
        } else if (seconds == 0) {
            strsec.append("00");
        } else {
            strsec.append(seconds);
        }

        String time = strhour + ":" + strminutee + ":" + strsec;
        Log.e(TAG, "getLeftTimeStrforChat: " + time);
        return time;
    }
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, this, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private void haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            chooseAccount();
        }else {
            AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
            asyncTaskRunner.execute();
        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private void logutfromcalendaraccount(){
        //realmController.deleteAllEvents();
        Session.setPreferenceName(context,"");
        Session.setPreferenceNameTwo(context,"");
        credential.setSelectedAccountName(null);
        credential.setSelectedAccount(null);
        binding.tvAddMyCalendar.setVisibility(View.VISIBLE);
        binding.tvAddMyCalendar.setText(context.getResources().getString(R.string.add_to_my_calendar));
    }

    private void setupCalendarClient(){
        // Google Accounts
        credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(CalendarScopes.CALENDAR));
        credential.setSelectedAccountName(Session.getPreferenceName(context));
        // Calendar client
        client = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("Google-CalendarAndroidSample/1.0").build();

        if (Session.getPreferenceName(context).equals("")){
            binding.tvAddMyCalendar.setText(context.getResources().getString(R.string.add_to_my_calendar));
            Log.e(TAG, "setupCalendarClient: >>>>>>>>>>>>>>>>>>>> no account" );
        }else {
            binding.tvAddMyCalendar.setText(Session.getPreferenceName(context));
            Log.e(TAG, "setupCalendarClient: >>>>>>>>>>>>>>>>>>>> acccontttttt" );
           // getEventListing();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        Log.e(TAG, "onActivityResult: " + accountName);
                        credential.setSelectedAccountName(accountName);
                        Session.setPreferenceName(context, accountName);
                        Session.setPreferenceNameTwo(context, accountName);
                      //  getEventListing();
                       // createEvent(credential);
                        Log.e(TAG, "onActivityResult: " + sDate +"  "+ eDate );
                        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
                        asyncTaskRunner.execute();
                    }
                }
                break;

        }
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp,eventId = "";
        // ProgressDialog progressDialog;
        int time;
        @Override
        protected String doInBackground(String... params) {
            asyncRunning = true;
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            //   Log.e(TAG, "doInBackground: "+params[0].toString() );
            Log.e(TAG, "doInBackground::::::::::::::::::::::: " + sDate +"  "+ eDate );
            try{
                Event event = new Event()
                        .setSummary("Termin bei "+salonname)
                        .setLocation(addrr)
                        .setDescription("Gebucht für "+empName);

                // DateTime startDateTime = new DateTime(strstartDateTime);

                startDateTime = new DateTime(Utility.getDateTimeFromString(sDate));
                endDateTime = new DateTime(Utility.getDateTimeFromString(eDate));
                EventDateTime start = new EventDateTime()
                        .setDateTime(startDateTime);
                //.setTimeZone(timeZone);
                event.setStart(start);

                //  DateTime endDateTime = new DateTime(strendDateTime);

                EventDateTime end = new EventDateTime()
                        .setDateTime(endDateTime);
                // .setTimeZone(timeZone);
                event.setEnd(end);

            /*    String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
                event.setRecurrence(Arrays.asList(recurrence));*/

               /* EventAttendee[] attendees = new EventAttendee[] {
                        new EventAttendee().setEmail("lpage@example.com"),
                        new EventAttendee().setEmail("sbrin@example.com"),
                };*/
                //  event.setAttendees(Arrays.asList(attendees));

                EventReminder[] reminderOverrides = new EventReminder[] {
                        new EventReminder().setMethod("email").setMinutes(24 * 60),
                        new EventReminder().setMethod("popup").setMinutes(60),
                        new EventReminder().setMethod("popup").setMinutes(24 * 60),
                };
                Event.Reminders reminders = new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(Arrays.asList(reminderOverrides));
                event.setReminders(reminders);

                String calendarId = "primary";
                try {
                    event = client.events().insert(calendarId, event).execute();
                } catch (UserRecoverableAuthIOException e) {
                    startActivityForResult(e.getIntent(), 12);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "doInBackground: "+event.getHtmlLink());
                String[] eventID = event.getHtmlLink().split("\\?");
                String[] newevent = eventID[1].split("=");
                eventId  = newevent[1];
                Log.e(TAG, "doInBackground: "+eventId );

            }catch (Exception e){
                e.printStackTrace();
            }
            return resp;
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            // Log.e(TAG, "onPostExecute: "+result.toString() );

            // progressDialog.dismiss();
            if (!eventId.equals("")){
                Base64.Decoder decoder = Base64.getMimeDecoder();
                // Decoding MIME encoded message
                String dStr = new String(decoder.decode(eventId));
                String corec[] = dStr.split(" ");
                Log.e(TAG, "adddevent: ------- "+dStr+"  "+corec[0]);
                String eventID = corec[0];
                asyncRunning = false;
                Log.e(TAG, "onPostExecute: >>>>>>>>>>>>>>>>>>>>>" );
                Toast.makeText(context, "Der Termin wurde erfolgreich zu deinem Kalender hinzugefügt", Toast.LENGTH_SHORT).show();
              //  createEvent(dataId,bookingId,eventID);

            }else {
                Toast.makeText(context, "Der Zeitbereich ist ungültig", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "onPreExecute: "+"Wait for" );
            // progressDialog = ProgressDialog.show(context, "ProgressDialog", "Wait for");
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
