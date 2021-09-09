package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.databinding.DataBindingUtil;

import android.graphics.PointF;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.SelectTimeAdapter;
import com.si.styletimer.adapters.WeekCalendarAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivitySelectDateBinding;
import com.si.styletimer.databinding.DialogThankRescheduleBinding;

import com.si.styletimer.databinding.RescheduleConfirmPopupBinding;
import com.si.styletimer.models.SelectTimeModel;
import com.si.styletimer.models.WeekCalendarModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.SnapToBlock;
import com.si.styletimer.utill.Utility;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import noman.weekcalendar.listener.OnDateClickListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SelectDateActivity extends AppCompatActivity {
    private static final String TAG = "SelectDateActivity";
    private Context context;
    private ActivitySelectDateBinding binding;
    private String str_salon_id = "", employeid = "", date = "", time = "", reschedule, bookId = "", dateSelected = "";
    private List<SelectTimeModel> selectTimeModelList;
    private SelectTimeAdapter selectTimeAdapter;
    private Gson gson;
    private Controller controller;
    private List<String> nationalDaysList = new ArrayList<>();
    private List<String> daysList = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private List<WeekCalendarModel> weekCalendarModelList;
    private WeekCalendarAdapter weekCalendarAdapter;
    public GifDrawable gifFromResource;
    private AlertDialog dialog123;
    private AlertDialog dialog12;
    int posss = 0;
    int targetPosition;
    private JSONArray available_employees = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_date);
        context = this;
        controller = (Controller) context.getApplicationContext();
        gson = new Gson();
        initviews();

        Log.e(TAG, "onCreate:------------ " + Utility.days());
    }

    private void initviews() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String mmmm = intent.getStringExtra(AppConstants.MAG1);
                    binding.tvheadname.setText(mmmm);
                }
            }
        };

        String monthname = (String) android.text.format.DateFormat.format("MMMM", new Date());
        binding.tvheadname.setText(monthname);
        Intent intent22 = getIntent();
        date = intent22.getStringExtra(AppConstants.DATE);
        dateSelected = intent22.getStringExtra(AppConstants.DATE);

        Log.e(TAG, "initviews:::::::::::::::::::::::::::::: " + date + "   " + dateSelected);

        Intent intent = getIntent();
        if (intent != null) {
            employeid = intent.getStringExtra(AppConstants.FLAG);
            String availableEmployees = intent.getStringExtra(AppConstants.AVAILABLE_EMPLOYEE);
            try {
                if (availableEmployees!= null && !availableEmployees.equals("")) {
                    available_employees = new JSONArray(availableEmployees);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        set_adapter();



        if (intent != null) {
            str_salon_id = intent.getStringExtra(AppConstants.SALON_ID);
            employeid = intent.getStringExtra(AppConstants.FLAG);
            //date = intent.getStringExtra(AppConstants.DATE);
            reschedule = intent.getStringExtra(AppConstants.FLAGTWO);
            bookId = intent.getStringExtra(AppConstants.BOOK_ID);
            time = intent.getStringExtra(AppConstants.TIME);
            Log.e(TAG, "initviews: " + controller.startTime);

            // Log.e(TAG, "initviews: " + str_salon_id );
            if (!str_salon_id.equals("")) {
                if (reschedule.equals(AppConstants.RESCHEDULE)) {
                    binding.btncontinue.setText(context.getResources().getString(R.string.reschedule));
                    if (Utility.connectionStatus(context)) {
                        getSingleEmployeeTime();
                    } else {
                        Utility.nointernettoast(context);
                    }
                } else {
                    binding.btncontinue.setText(context.getResources().getString(R.string.continue1));
                    if (Utility.connectionStatus(context)) {
                        get_employee_time();
                    } else {
                        Utility.nointernettoast(context);
                    }

                }

            }

        }

        if (!date.equals("")) {
            Log.e(TAG, "initviewssssssssssss ifff  : " + date);
            String ddat[] = date.split("-");
            int year = Integer.parseInt(ddat[0]);
            int month = Integer.parseInt(ddat[1]);
            int day = Integer.parseInt(ddat[2]);

            Log.e(TAG, "initviews: " + year + month + day);

            binding.weekCalendar.setSelectedDate(new DateTime().withDate(year, month, day));

        } else {
            String ddat[] = Utility.getCurrentDateYY().split("-");
            int year = Integer.parseInt(ddat[0]);
            int month = Integer.parseInt(ddat[1]);
            int day = Integer.parseInt(ddat[2]);
            binding.weekCalendar.setSelectedDate(new DateTime().withDate(year, month, day));
        }

        binding.weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {

                String year = String.valueOf(dateTime.getYear());
                String day = String.valueOf(dateTime.getDayOfMonth());
                StringBuilder monthee = new StringBuilder();
                if (dateTime.getMonthOfYear() < 10) {
                    monthee.append("0" + dateTime.getMonthOfYear());
                } else {
                    monthee.append(dateTime.getMonthOfYear());
                }
                if (Integer.valueOf(day) > 9) {
                    date = year + "-" + monthee.toString() + "-" + day;
                } else {
                    date = year + "-" + monthee.toString() + "-" + "0" + day;
                }

                String mname = Utility.change_format(date, "yyyy-MM-dd", "MMMM");
                binding.tvheadname.setText(mname);

                if (Utility.comparedates(Utility.getCurrentDateYY(), date).equals(Utility.DATEAFTER)) {
                    Toast.makeText(context, "Bitte wählen Sie das richtige Datum", Toast.LENGTH_SHORT).show();
                } else {
//                     if (Utility.connectionStatus(context)) {
//                         //selectTimeModelList.clear();
//                         get_employee_time();
//                     } else {
//                         Utility.nointernettoast(context);
//                     }
                    if (reschedule.equals(AppConstants.RESCHEDULE)) {
                        if (Utility.connectionStatus(context)) {
                            getSingleEmployeeTime();
                        } else {
                            Utility.nointernettoast(context);
                        }
                    } else {
                        if (Utility.connectionStatus(context)) {
                            get_employee_time();
                        } else {
                            Utility.nointernettoast(context);
                        }
                    }
                }
            }
        });

        binding.btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reschedule.equals(AppConstants.RESCHEDULE)) {
                    if (selectTimeAdapter.getmSelectedItem() == -1) {
                        Toast.makeText(context, "Bitte wähle eine Uhrzeit ", Toast.LENGTH_SHORT).show();
                    } else {
                        time = selectTimeAdapter.getSelectTimeModel().get(selectTimeAdapter.getmSelectedItem()).getTime();
                        dialogConfirmReschedule();
                    }
                } else {
                    if (selectTimeAdapter.getmSelectedItem() == -1) {
                        Toast.makeText(context, "Bitte wähle eine Uhrzeit ", Toast.LENGTH_SHORT).show();
                    } else {
                        time = selectTimeAdapter.getSelectTimeModel().get(selectTimeAdapter.getmSelectedItem()).getTime();
                        if (!time.equals("")) {
                            Intent intent1 = new Intent(context, CartActivity.class);
                            intent1.putExtra(AppConstants.EMPLOYEE_SELECT, employeid);
                            intent1.putExtra("date", date);
                            intent1.putExtra("time", time);
                            startActivity(intent1);
                        } else {
                            Toast.makeText(context, "Bitte wähle eine Uhrzeit ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void set_adapter() {
        salon_calendar_slots();
        selectTimeModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyemployetime.setLayoutManager(linearLayoutManager);
        binding.recyemployetime.setHasFixedSize(true);
        selectTimeAdapter = new SelectTimeAdapter(context, selectTimeModelList);
        binding.recyemployetime.setAdapter(selectTimeAdapter);
//        selectTimeAdapter.notifyDataSetChanged();

        LinearLayoutManager weeklayoutmanager = new LinearLayoutManager(context);
        weeklayoutmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        MyCustomLayoutManager myCustomLayoutManager = new MyCustomLayoutManager(context);
        myCustomLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        weekCalendarModelList = new ArrayList<>();
        binding.rvWeekCalendat.setLayoutManager(myCustomLayoutManager);
        binding.rvWeekCalendat.setHasFixedSize(true);
        weekCalendarAdapter = new WeekCalendarAdapter(context, weekCalendarModelList);
        binding.rvWeekCalendat.setAdapter(weekCalendarAdapter);
        binding.rvWeekCalendat.scrollToPosition(0);
        weekCalendarAdapter.notifyDataSetChanged();


        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return binding.rvWeekCalendat.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                 targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
      //  SnapToBlock snapToBlock = new SnapToBlock(3);
      //  snapToBlock.attachToRecyclerView(binding.rvWeekCalendat);
        snapHelper.attachToRecyclerView(binding.rvWeekCalendat);
       // binding.rvWeekCalendat.setOnFlingListener(snapHelper);
      //  SnapToBlock snapToBlock = new SnapToBlock(3);
       // snapToBlock.attachToRecyclerView(binding.rvWeekCalendat);

        List<Date> dates = Utility.getDates();
        for (Date date : dates) {
            String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            WeekCalendarModel weekCalendarModel = new WeekCalendarModel();
            weekCalendarModel.setDates(date.toString());
            weekCalendarModel.setmDate(formateDate);
            weekCalendarModelList.add(weekCalendarModel);
        }

        if (!date.equals("")) {
            Log.e(TAG, "initviewssssssssssss ::::::::::::::::::::::::: " + date);
            String ddat[] = date.split("-");
            int year = Integer.parseInt(ddat[0]);
            int month = Integer.parseInt(ddat[1]);
            int day = Integer.parseInt(ddat[2]);

            for (int i = 0; i < weekCalendarModelList.size(); i++) {
                String datesss = weekCalendarModelList.get(i).getmDate();
                if (datesss.equals(date)) {
                    Log.e(TAG, "set_adapter: if  " + datesss + "  " + date);
                    weekCalendarAdapter.setCurrentPos(i);

                    binding.rvWeekCalendat.scrollToPosition(i);

                }

            }
        } else {
            weekCalendarAdapter.setCurrentPos(Utility.days());

        }


        weekCalendarAdapter.setWeekCalendarModelList(weekCalendarModelList);
        weekCalendarAdapter.notifyDataSetChanged();

        weekCalendarAdapter.setOnItemClickListener(new WeekCalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String mydate = weekCalendarAdapter.getWeekCalendarModel().get(position).getDates();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String datestr = df.format(Date.parse(mydate));
                //Log.e(TAG, "onItemClick: "+date );
                date = datestr;
                String mname = Utility.change_format(date, "yyyy-MM-dd", "MMMM");
                binding.tvheadname.setText(mname);

                int d= weekCalendarAdapter.getScreenWidth();
                d=d/2;
                Log.e("Screen Width", String.valueOf(d));

//                if (Utility.connectionStatus(context)) {
//                    get_employee_time();
//                } else {
//                    Utility.nointernettoast(context);
//                }

                if (reschedule.equals(AppConstants.RESCHEDULE)) {
                    if (Utility.connectionStatus(context)) {
                        getSingleEmployeeTime();
                    } else {
                        Utility.nointernettoast(context);
                    }
                } else {
                    if (Utility.connectionStatus(context)) {
                        get_employee_time();
                    } else {
                        Utility.nointernettoast(context);
                    }

                }
            }

        });
        //  Log.e(TAG, "set_adapter: "+weekCalendarModelList.toString() );
    }

    private void get_employee_time() {
        selectTimeModelList.clear();
        selectTimeAdapter.setmSelectedItem(-1);
        binding.loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, this.str_salon_id);
        hashMap.put(AppConstants.EMPLOYEE_SELECT, this.employeid);
        hashMap.put(AppConstants.DATE, this.date);
        hashMap.put(AppConstants.AVAILABLE_EMPLOYEE, available_employees.toString());

        Log.e(TAG, "get_employee_time: " + hashMap);

        RetrofitServices.urlServices.employee_time(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    binding.loader.setVisibility(View.GONE);
                    String res = response.body().string().trim();
                     Log.e(TAG, "onResponse: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status") == 1) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        selectTimeModelList = gson.fromJson(data.toString(), new TypeToken<ArrayList<SelectTimeModel>>() {
                        }.getType());
                        selectTimeAdapter.setSelectTimeModelList(selectTimeModelList);
                        selectTimeAdapter.notifyDataSetChanged();

                    }
                    String d = Utility.change_format(date, "yyyy-MM-dd", "dd");
                    String dd = Utility.change_format(date, "yyyy-MM-dd", "EEEE");
                    binding.tvnolist.setText("Leider sind am ausgewählten Tag keine freien Termine verfügbar.");
                    if (selectTimeModelList.size() == 0) {
                        binding.tvnolist.setVisibility(View.VISIBLE);
                        binding.tvnolistPlease.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvnolist.setVisibility(View.GONE);
                        binding.tvnolistPlease.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);
            }
        });
    }

    private void getSingleEmployeeTime() {
        selectTimeModelList.clear();
        selectTimeAdapter.setmSelectedItem(-1);
        binding.loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, this.str_salon_id);
        hashMap.put("eid", this.employeid);
        hashMap.put("bookid", this.bookId);
        hashMap.put(AppConstants.DATE, this.date);

        Log.e(TAG, "employee_reschedule_time: " + hashMap);

        RetrofitServices.urlServices.employee_reschedule_time(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    binding.loader.setVisibility(View.GONE);
                    String res = response.body().string().trim();
                    // Log.e(TAG, "onResponse: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status") == 1) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        selectTimeModelList = gson.fromJson(data.toString(), new TypeToken<ArrayList<SelectTimeModel>>() {
                        }.getType());
                        if (dateSelected.equals(date)) {
                            for (int i = 0; i < selectTimeModelList.size(); i++) {
                                if (time.equals(selectTimeModelList.get(i).getTime())) {
                                    selectTimeAdapter.setmSelectedItem(i);
                                    binding.recyemployetime.scrollToPosition(i);
                                }
                            }
                        } else {
                            binding.recyemployetime.scrollToPosition(0);
                            selectTimeAdapter.setmSelectedItem(-1);
                        }
                        selectTimeAdapter.setSelectTimeModelList(selectTimeModelList);
                        selectTimeAdapter.notifyDataSetChanged();
                    }

                    String d = Utility.change_format(date, "yyyy-MM-dd", "dd");
                    String dd = Utility.change_format(date, "yyyy-MM-dd", "EEEE");
                    //binding.tvnolist.setText("Leider sind am ausgewählten Tag keine freien Termine verfügbar " + d + " of " + dd + ".");

                    binding.tvnolist.setText("Leider sind am ausgewählten Tag keine freien Termine verfügbar.");
                    if (selectTimeModelList.size() == 0) {
                        binding.tvnolist.setVisibility(View.VISIBLE);
                        binding.tvnolistPlease.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvnolist.setVisibility(View.GONE);
                        binding.tvnolistPlease.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);
            }
        });
    }


    private void rescheduleBooking() {
        binding.loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, this.str_salon_id);
        hashMap.put("eid", this.employeid);
        hashMap.put("bookid", this.bookId);
        hashMap.put("chg_time", this.time);
        hashMap.put(AppConstants.DATE, this.date);

        Log.e(TAG, "rescheduleBooking: " + hashMap);

        RetrofitServices.urlServices.booking_reshedule(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    binding.loader.setVisibility(View.GONE);
                    String res = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status") == 1) {
                        popupresched(str_salon_id, bookId, jsonObject.getString("response_message"));
                    } else {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);
            }
        });
    }

    private void popupresched(String str_salon_id, String bookId, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogThankRescheduleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_thank_reschedule, null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        dialog123 = builder.create();

        if (binding.ivmaingiftwo.getDrawable() != null) {
            pl.droidsonroids.gif.GifDrawable gifDrawable = (pl.droidsonroids.gif.GifDrawable) binding.ivmaingiftwo.getDrawable();
            gifDrawable.setLoopCount(1);
        }
        binding.tvtit.setVisibility(View.GONE);
        binding.tvtitle.setText(msg);
        binding.tvOk.setText("Zu deiner Buchung");
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog123.dismiss();
                Intent intent = new Intent(context, BookingDetailActivity.class);
                intent.putExtra("Fragment", "ConfirmedFragment");
                intent.putExtra("id", bookId);
                intent.putExtra("sId", str_salon_id);
                intent.putExtra("from", "main");
                intent.putExtra("selected", "0");
                intent.putExtra(AppConstants.RES_STATUS, "");
                startActivity(intent);
                finishAffinity();

              /*  Intent intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
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
    protected void onDestroy() {
        super.onDestroy();
        if (dialog123 != null) {
            dialog123.dismiss();
        }
        if (dialog12 != null) {
            dialog12.dismiss();
        }
    }

    /*
    private void dialogConfirmReschedule() {
        new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setMessage("Are you sure you want to Reschedule this booking ?")
                .setPositiveButton("Reschedule", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if (Utility.connectionStatus(context)) {
                        rescheduleBooking();
                    } else {
                        Utility.nointernettoast(context);
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .create()
                .show();
    }
*/

    private void dialogConfirmReschedule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        RescheduleConfirmPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.reschedule_confirm_popup, null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        dialog12 = builder.create();

        binding.btCancel12.setText("ABBRECHEN");
        binding.tvReschedule.setText("UMBUCHEN");

        binding.tvReschedule.setOnClickListener(v -> {
            dialog12.dismiss();
            if (Utility.connectionStatus(context)) {
                rescheduleBooking();
            } else {
                Utility.nointernettoast(context);
            }
        });
        binding.btCancel12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog12.dismiss();
            }
        });

        binding.btnivclosedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog12.dismiss();
            }
        });
        dialog12.show();
    }

    private void salon_calendar_slots() {
        // binding.loader.setVisibility(View.VISIBLE);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.SALON_ID, Session.getSalon_id(context));
        hashMap.put(AppConstants.EMPLOYEEID, employeid);

        Log.e(TAG, "salon_calendar_slots: " + hashMap);

        RetrofitServices.urlServi.salon_calendar_slots(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    binding.loader.setVisibility(View.GONE);
                    String res = response.body().string().trim();
                    Log.e(TAG, "onResponseeeeeeeeeeeeeeeeeeeeeeeeeeeeeee: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status") == 1) {
                        nationalDaysList.clear();
                        nationalDaysList = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("nationalDays").toString(), new TypeToken<ArrayList<String>>() {
                        }.getType());

                        for (int i = 0; i < weekCalendarModelList.size(); i++) {
                            if (nationalDaysList != null && nationalDaysList.size() > 0) {
                                for (int j = 0; j < nationalDaysList.size(); j++) {
                                    if (weekCalendarModelList.get(i).getmDate().equals(nationalDaysList.get(j))) {
                                        //Log.e(TAG, "set_adapter: national " + weekCalendarModelList.get(i).getmDate() + " = " + nationalDaysList.get(j));
                                        weekCalendarModelList.get(i).setClosed(true);
                                    }
                                }
                            }
                        }

                        daysList.clear();
                        daysList = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("days").toString(), new TypeToken<ArrayList<String>>() {
                        }.getType());

                        for (int i = 0; i < weekCalendarModelList.size(); i++) {
                            if (daysList != null && daysList.size() > 0) {
                                for (int j = 0; j < daysList.size(); j++) {
                                    if (weekCalendarModelList.get(i).getDay().equals(daysList.get(j))) {
                                        //Log.e(TAG, "set_adapter: daysss " + weekCalendarModelList.get(i).getDay() + " = " + daysList.get(j));
                                        weekCalendarModelList.get(i).setClosed(true);
                                    }
                                }
                            }
                        }

                        weekCalendarAdapter.setWeekCalendarModelList(weekCalendarModelList);
                        weekCalendarAdapter.notifyDataSetChanged();
                    } else {
                        //  Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);
                Log.e(TAG, "onFailureeeeeeeeeeeeeeeee: " + t.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.BROADCAST_KEY));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }


    public class MyCustomLayoutManager extends LinearLayoutManager {
        private static final float MILLISECONDS_PER_INCH = 5f;
        private Context mContext;

        public MyCustomLayoutManager(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state, final int position) {

            LinearSmoothScroller smoothScroller =
                    new LinearSmoothScroller(mContext) {

                        //This controls the direction in which smoothScroll looks
                        //for your view
                        @Override
                        public PointF computeScrollVectorForPosition
                        (int targetPosition) {
                            return MyCustomLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to
                        //scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel
                        (DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                        }
                    };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }

}
