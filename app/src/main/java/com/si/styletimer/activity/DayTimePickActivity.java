package com.si.styletimer.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivityDayTimePickBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayTimePickActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityDayTimePickBinding binding;
    private static final String TAG = "DayTimePickActivity";
    private Calendar lastSelectedCalendar = null;
    private Context context;
    private Controller controller;
    private String date="",st="",et="",calenddate = "";
    List<String> endTimelist = new ArrayList<String>();
    AlertDialog dialog ;
    private String endtimeindex = "",starttimeindex = "",tempStart = "",tempEnd = "";
    private int start, end;
    String tes="",tes1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_day_time_pick);
        context = this;
        controller = (Controller)context.getApplicationContext();
        setUpNumberPicker();
        inIt();
        getIntentData();

    }

    private void getIntentData() {
        date= getIntent().getStringExtra("date");
        st = getIntent().getStringExtra("st");
        et = getIntent().getStringExtra("et");
        Log.e(TAG, "getIntentData:------------------------------- "+date );

        if(date.equals("Heute")){

            Log.e(TAG, "getIntentData: Today - " +date );
            setColor(1);
            calenddate = getCurrentDate();
            Log.e("cureent_date ------->",calenddate);

        }else if(date.equals("Morgen")){

            Log.e(TAG, "getIntentData: Tom - " +date );
            setColor(2);
            calenddate = getTomorrow();
            Log.e("cureent_date ------->",calenddate);
        }else if(date.equals("")){

        }else {

            calenddate = date;
            Log.e("cureent_date ------->",calenddate);
            setColor(3);
        }

        if(!st.equals("") && !et.equals("")){

            String s[]=st.split(":");
            String e[]=et.split(":");

            Log.e(TAG, "getIntentData: "+s[0]+" - "+e[0]);

            String start = s[0]+":00 Uhr";
            String end  = e[0]+":00 Uhr";
            endtimeindex = e[0];
            starttimeindex = s[0];

            binding.btnDialogStartTime.setText(start);
            binding.btnDialogEndTime.setText(end);

            tempStart = start;
            tempEnd = end;

            binding.npStart.setValue(Integer.valueOf(s[0]));
            binding.npEnd.setValue(Integer.valueOf(e[0]));

            binding.tvStart.setText(s[0]+":00 Uhr");
            binding.tvEnd.setText(e[0]+":00 Uhr");
            binding.tvTo.setText(" bis ");

            binding.ivCloseTime.setVisibility(View.VISIBLE);

        }else {

            binding.npStart.setValue(0);
            binding.npEnd.setValue(1);
        }
        
    }

    @SuppressLint("LongLogTag")
    private void inIt() {
        lastSelectedCalendar = Calendar.getInstance();
        binding.calendarView.setMinDate(System.currentTimeMillis()/*+86400000*/);
        binding.calendarView.setMaxDate(System.currentTimeMillis()+TimeUnit.DAYS.toMillis(60));
      //  binding.calendarView.setMinDate(System.currentTimeMillis()+518400000);

        //Log.e("Long date", String.valueOf(s));

      //  binding.calendarView.setDate().setDisabled();
        //setUpNumberPicker();
        getAvailableList();
        onClck();
    }

    private void onClck() {
        binding.ivCloseTime.setOnClickListener(this);
        binding.ivCloseDate.setOnClickListener(this);
        binding.ivClose.setOnClickListener(this);

        binding.btDate.setOnClickListener(this);
        binding.btToday.setOnClickListener(this);
        binding.btTomorrow.setOnClickListener(this);
        binding.btApply.setOnClickListener(this);

        binding.rlDate.setOnClickListener(this);
        binding.rlTime.setOnClickListener(this);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {

             /*   Calendar checkCalendar = Calendar.getInstance();
                checkCalendar.set(i, i1, i2);
                if(checkCalendar.equals(lastSelectedCalendar))
                    return;
                if(checkCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                   binding.calendarView.setDate(lastSelectedCalendar.getTimeInMillis());

                else
                    lastSelectedCalendar = checkCalendar;*/

                String month="";
                String day="";
                if(i1+1<10){
                    month = "0"+String.valueOf(i1+1);
                    day=String.valueOf(i2);
                }else {
                    month = String.valueOf(i1+1);

                }


                tes=Utility.date_format(String.valueOf(i) + "-" + month + "-" + String.valueOf(i2));
                String test=Utility.app_format(String.valueOf(i) + "-" + month + "-" + String.valueOf(i2));
                // String x= Utility.date_format(test);
                String[] a= test.split(",.");
                Log.e("check",a[1]);
                Log.e("check",tes);

            //Sunday Salon Closed
             /*   Date date1 = null;
                String fectchDate=Utility.dateBuiilder(i2,i1,i);
                try {
                    date1=new SimpleDateFormat("dd-MM-yyyy").parse(fectchDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int day1 = date1.getDay();
                if(day1==0)
                {

                    Toast.makeText(context,"Salon ist am Sonntag geschlossen. Bitte wählen Sie ein anderes Datum",Toast.LENGTH_LONG).show();
                }*/
                //Log.e("check's", String.valueOf(date1));

                binding.tvAny.setText(a[1]);


                controller.datecalendar = String.valueOf(i) + "-" + month + "-" + String.valueOf(i2);
            }
        });

        binding.btnDialogStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartTimeDialog();
            }
        });

        binding.btnDialogEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endTimelist.size()>0){
                    openEndTimeDialog();
                }else {
                    Toast.makeText(context, "Bitte Startzeit angeben", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUpNumberPicker() {

        NumberPicker.Formatter formatter = new NumberPicker.Formatter(){
            @Override
            public String format(int i) {
                return i+":00";
            }
        };

        binding.npStart.setFormatter(formatter);
        binding.npStart.setMinValue(0);
        binding.npStart.setMaxValue(23);
        //binding.npStart.setValue(0);
        binding.npStart.setWrapSelectorWheel(false);


        binding.npEnd.setFormatter(formatter);
        binding.npEnd.setMinValue(1);
        binding.npEnd.setMaxValue(24);
        //binding.npEnd.setValue(1);
        binding.npEnd.setWrapSelectorWheel(false);


        try {
            Method method = binding.npStart.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(binding.npStart, true);
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            Method method = binding.npEnd.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(binding.npEnd, true);
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


        binding.npStart.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                binding.ivCloseTime.setVisibility(View.VISIBLE);
                binding.tvStart.setText(String.valueOf(newVal)+":00 Uhr");
                binding.tvTo.setText(" bis ");
                binding.npEnd.setMinValue(newVal+1);
                binding.tvEnd.setText(binding.npEnd.getValue()+":00 Uhr");
            }
        });

        binding.npEnd.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                binding.ivCloseTime.setVisibility(View.VISIBLE);
                binding.tvEnd.setText(String.valueOf(newVal)+":00 Uhr");
                binding.tvTo.setText(" bis ");
            }
        });

    }

    @Override
    public void onClick(View v) {

        if(v.equals(binding.rlTime)){
            binding.cvTime.setVisibility(View.VISIBLE);

        }else if(v.equals(binding.ivCloseTime)){

            //binding.cvTime.setVisibility(View.GONE);
            binding.ivCloseTime.setVisibility(View.GONE);
            binding.tvStart.setText("");
            binding.tvEnd.setText("");
            binding.tvTo.setText("Beliebige Zeit");
            binding.btnDialogStartTime.setText("0:00");
            binding.btnDialogEndTime.setText("1:00");

        }else if(v.equals(binding.btDate)){
            setColor(3);

        }else if(v.equals(binding.btToday)){
            setColor(1);


        }else if(v.equals(binding.btTomorrow)){
            setColor(2);

        }else if(v.equals(binding.btApply)){
            callApply();

        }else if(v.equals(binding.rlDate)){
            /*binding.llDate.setVisibility(View.VISIBLE);
            binding.ivCloseDate.setVisibility(View.VISIBLE);*/

        }else if(v.equals(binding.ivCloseDate)){
            //binding.llDate.setVisibility(View.GONE);
            binding.ivCloseDate.setVisibility(View.GONE);
            setColor(4);

        }else if(v.equals(binding.ivClose)){

            Intent returnIntent = new Intent();
            setResult(3, returnIntent);
          //  Bungee.slideRight(context);
            finish();

        }
    }

    private void callApply() {
        if(!binding.tvStart.getText().toString().equals("") && binding.tvEnd.getText().toString().equals("")){
            binding.tvEnd.setText("23:00 Uhr");
        }
        if(!binding.tvStart.getText().toString().equals("")){
            if(binding.tvEnd.getText().toString().equals("")){
                Toast.makeText(this, "Bitte gib auch eine Endzeit an", Toast.LENGTH_SHORT).show();
            }else {
                //todo... do back intent SUCCESS tor time

//for date----->
                String date = "";

                if(binding.tvAny.getText().toString().equals("Any date") || binding.tvAny.getText().toString().equals("Beliebig Datum")){
                    date="";
                }else{
                    String ll = null;
                    String kk=null;
                    String s=binding.tvAny.getText().toString();
                    System.out.println("chech day---- ---- ---"+s);
                    if(s.equals("Morgen"))
                    {
                        kk=s;
                    }else if(s.equals("Heute"))
                    {
                        kk=s;
                    }
                    else {
                        String[] uu = s.split(" ");
                        String w = uu[0];
                        if (uu[1] != null || uu[1].equals("")) {
                            ll = uu[1];
                        }

                        if (ll.equals("June")) {
                            ll = "Juni";
                        } else if (ll.equals("July")) {
                            ll = "Juli";
                        } else if (ll.equals("Jan.")) {
                            ll = "Jan";
                        } else if (ll.equals("Aug.")) {
                            ll = "Aug";
                        } else if (ll.equals("Sep.")) {
                            ll = "Sep";
                        } else if (ll.equals("Okt.")) {
                            ll = "Oct";
                        } else if (ll.equals("Nov.")) {
                            ll = "Nov";
                        } else if (ll.equals("Dez.")) {
                            ll = "Dec";
                        } else if (ll.equals("Feb.")) {
                            ll = "Feb";
                        } else if (ll.equals("Mär.")) {
                            ll = "Mar";
                        } else if (ll.equals("Apr.")) {
                            ll = "Apr";
                        } else if (ll.equals("Kann")) {
                            ll = "May";
                        }

                        if (ll.equals("null")) {
                            kk = w;
                        } else {
                            kk = w + " " + ll;
                        }
                    }
                   // date=binding.tvAny.getText().toString();
                    date=kk;
                    Log.i("r",binding.tvAny.getText().toString());
                }
//--date--<<

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result_start_time",binding.tvStart.getText().toString().replace("Uhr",""));
                returnIntent.putExtra("result_end_time",binding.tvEnd.getText().toString().replace("Uhr",""));
                returnIntent.putExtra("result_date",date);
                returnIntent.putExtra("result_date1",tes);
                setResult(3,returnIntent);
             //   Bungee.slideRight(context);
                finish();
            }

        }else {

            if(!binding.tvEnd.getText().toString().equals("")){
                Toast.makeText(this, "Bitte gib auch eine Startzeit an", Toast.LENGTH_SHORT).show();
            }else {
                //todo... do back intent FAILURE for time

//for date----->
                String date = "";
                if( binding.tvAny.getText().toString().equals("Any date") || binding.tvAny.getText().toString().equals("Beliebig Datum")){
                    date="";
                }else{
                    date=binding.tvAny.getText().toString();
                }
//--date--<<

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result_start_time","");
                returnIntent.putExtra("result_end_time","");
                returnIntent.putExtra("result_date",date);
                setResult(3,returnIntent);
                finish();
             //   Bungee.slideRight(context);
            }

        }
        //finish();
    }


    @SuppressLint("SimpleDateFormat")
    private void setColor(int i) {
        if (i==1){

            binding.rlCalendar.setVisibility(View.GONE);

            if(binding.tvAny.getText().toString().equals("Heute")){

                binding.tvAny.setText("Beliebig Datum");

                binding.btDate.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btToday.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btTomorrow.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btToday.setTextColor(getResources().getColor(R.color.font_black));
                binding.btDate.setTextColor(getResources().getColor(R.color.font_black));
                binding.btTomorrow.setTextColor(getResources().getColor(R.color.font_black));

                binding.ivCloseDate.setVisibility(View.GONE);

            }else {

                binding.tvAny.setText("Heute");
              /*  Date date1 = null;
                String fectchDate=getCurrentDate();
                String[] Fdate=fectchDate.split(" ");
                if(Fdate[1].equals("Juni"))
                {
                    Fdate[1]="June";
                }
                else if(Fdate[1].equals("Juli"))
                {
                    Fdate[1]="July";
                }
                else if(Fdate[1].equals("Jan"))
                {
                    Fdate[1]="Jan";
                }
                else if(Fdate[1].equals("Aug"))
                {
                    Fdate[1]="Aug";
                }
                else if(Fdate[1].equals("Sept"))
                {
                    Fdate[1]="Sept";
                }
                else if(Fdate[1].equals("Oct"))
                {
                    Fdate[1]="Oct";
                }
                else if(Fdate[1].equals("Nov"))
                {
                    Fdate[1]="Nov";
                }
                else if(Fdate[1].equals("Dez"))
                {
                    Fdate[1]="Dez";
                }
                else if(Fdate[1].equals("Feb"))
                {
                    Fdate[1]="Feb";
                }
                else if(Fdate[1].equals("Mär"))
                {
                    Fdate[1]="Mar";
                }
                else if(Fdate[1].equals("Apr"))
                {
                    Fdate[1]="Apr";
                }
                else if(Fdate[1].equals("Kann"))
                {
                    Fdate[1]="May";
                }

                String FinalDate=Fdate[0]+" "+Fdate[1]+" "+Fdate[2];
                 try {
                    date1=new SimpleDateFormat("dd-MM-yyyy").parse(FinalDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("date----", String.valueOf(date1));*/
              /* int day1 = date1.getDay();
                if(day1==0)
                {
                    Toast.makeText(context,"Salon ist am Sonntag geschlossen. Bitte wählen Sie ein anderes Datum",Toast.LENGTH_LONG).show();
                }*/

                binding.btDate.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btToday.setBackground(getResources().getDrawable(R.drawable.bg_cv_border_dark));
                binding.btTomorrow.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btToday.setTextColor(getResources().getColor(R.color.blue));
                binding.btDate.setTextColor(getResources().getColor(R.color.font_black));
                binding.btTomorrow.setTextColor(getResources().getColor(R.color.font_black));
                binding.ivCloseDate.setVisibility(View.VISIBLE);
            }

        }else if(i==2){

            binding.rlCalendar.setVisibility(View.GONE);

            Log.e(TAG, "setColor: "+ binding.tvAny.getText().toString() );

            if(binding.tvAny.getText().toString().equals("Morgen")){
                binding.tvAny.setText("Beliebig Datum");
                binding.btDate.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btTomorrow.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btToday.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btTomorrow.setTextColor(getResources().getColor(R.color.font_black));
                binding.btDate.setTextColor(getResources().getColor(R.color.font_black));
                binding.btToday.setTextColor(getResources().getColor(R.color.font_black));
                binding.ivCloseDate.setVisibility(View.GONE);

            }else{
                binding.tvAny.setText("Morgen");
                binding.btDate.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btTomorrow.setBackground(getResources().getDrawable(R.drawable.bg_cv_border_dark));
                binding.btToday.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btTomorrow.setTextColor(getResources().getColor(R.color.blue));
                binding.btDate.setTextColor(getResources().getColor(R.color.font_black));
                binding.btToday.setTextColor(getResources().getColor(R.color.font_black));

                binding.ivCloseDate.setVisibility(View.VISIBLE);
            }

        }else if(i==3){

            if(binding.tvAny.getText().toString().equals("Beliebig Datum") || binding.tvAny.getText().toString().equals("Any date") || binding.tvAny.getText().toString().equals("Morgen") || binding.tvAny.getText().toString().equals("Heute")){

                binding.rlCalendar.setVisibility(View.VISIBLE);
                binding.rlCalendar.requestFocus();

                if(calenddate==null ||  calenddate.equals("") || calenddate.equals(getCurrentDate())){
                    Log.e(TAG, "setColor: first" );
                    tes=Utility.date_format(Utility.getCurrentDateYY());
                    Log.e("check------",getCurrentDate());
                    String test=Utility.app_format(Utility.getCurrentDateYY());
                    String[] x= test.split(",.");
                    Log.e("check1",x[1]);
                    binding.tvAny.setText(x[1]);

                } else if(calenddate.equals(getTomorrow())){
                   // String test=Utility.app_format(getTomorrowYY());
                   // String[] x= test.split(",.");
                   // Log.e("check",x[1]);
                    Log.e("check------>",getTomorrow());
                    binding.tvAny.setText(Utility.date_format(getTomorrowYY()));
                    binding.calendarView.setDate(Utility.change_format_long(getTomorrowYY(),"yyyy-MM-dd","yyyy-MM-dd"));
                }else  {
                    Log.e(TAG, "setColor: second - " + calenddate );
                    String[] a=calenddate.split(" ");
                   String nn=a[0];
                    String   ll=a[1];
                    if(ll.equals("June"))
                    {
                        ll="Juni";
                    }
                    else if(ll.equals("July"))
                    {
                        ll="Juli";
                    }
                    else if(ll.equals("Jan"))
                    {
                        ll="Jan.";
                    }
                    else if(ll.equals("Aug"))
                    {
                        ll="Aug.";
                    }
                    else if(ll.equals("Sept"))
                    {
                        ll="Sep.";
                    }
                    else if(ll.equals("Oct"))
                    {
                        ll="Okt.";
                    }
                    else if(ll.equals("Nov"))
                    {
                        ll="Nov.";
                    }
                    else if(ll.equals("Dec"))
                    {
                        ll="Dez.";
                    }
                    else if(ll.equals("Feb"))
                    {
                        ll="Feb.";
                    }
                    else if(ll.equals("Mar"))
                    {
                        ll="Mär.";
                    }
                    else if(ll.equals("Apr"))
                    {
                        ll="Apr.";
                    }
                    else if(ll.equals("May"))
                    {
                        ll="Kann";
                    }

                    String kk= nn+" "+ll;
                    binding.tvAny.setText(kk);

                    binding.calendarView.setDate(Utility.change_format_long(calenddate +" "+Utility.getCurrentYear(),"d MMMM yyyy","yyyy-MM-dd"));
                }

                binding.btToday.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btDate.setBackground(getResources().getDrawable(R.drawable.bg_cv_border_dark));
                binding.btTomorrow.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btDate.setTextColor(getResources().getColor(R.color.blue));
                binding.btToday.setTextColor(getResources().getColor(R.color.font_black));
                binding.btTomorrow.setTextColor(getResources().getColor(R.color.font_black));
                binding.scroll.fullScroll(View.FOCUS_DOWN);
                binding.ivCloseDate.setVisibility(View.VISIBLE);


            }else {

                binding.rlCalendar.setVisibility(View.GONE);

                binding.tvAny.setText("Beliebig Datum");

                binding.btToday.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btDate.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btTomorrow.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
                binding.btDate.setTextColor(getResources().getColor(R.color.font_black));
                binding.btToday.setTextColor(getResources().getColor(R.color.font_black));
                binding.btTomorrow.setTextColor(getResources().getColor(R.color.font_black));

                binding.ivCloseDate.setVisibility(View.GONE);

            }



        }else if(i==4){

            binding.rlCalendar.setVisibility(View.GONE);
            binding.tvAny.setText("Beliebig Datum");

            binding.btToday.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
            binding.btDate.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));
            binding.btTomorrow.setBackground(getResources().getDrawable(R.drawable.bg_cardview_border_blue));

            binding.btDate.setTextColor(getResources().getColor(R.color.font_black));
            binding.btToday.setTextColor(getResources().getColor(R.color.font_black));
            binding.btTomorrow.setTextColor(getResources().getColor(R.color.font_black));

        }
    }

    public static String getTomorrow(){
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        Log.e(TAG, "initviews: "+tomorrow );
        return new SimpleDateFormat("d MMMM yyyy").format(tomorrow);
    }

    public static String getTomorrowYY(){
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        Log.e(TAG, "initviews: "+tomorrow );
        return new SimpleDateFormat("yyyy-MM-dd").format(tomorrow);
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("d MMMM yyyy",Locale.GERMAN).format(new Date());
    }

    private void openStartTimeDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Startzeit eingeben");
       // String[] items = new String[24];
        int startTime = start;
        Log.e(TAG, "openStartTimeDialog: ++ "+start);


        List<String> stringList = new ArrayList<>();
        for (int i=0;i<24;i++){
            if (startTime<=i){
                Log.e(TAG, "openStartTimeDialog: =================== >>>>>>>>>>>>>>" );
                stringList.add(""+i+":00");
               // items[i]= ""+i+":00";
            }
        }

        String[] items = stringList.toArray(new String[0]);

        int startindex = 0;

        if (!starttimeindex.equals("")) {
            startindex = Integer.parseInt(starttimeindex);
            int newstartindex = startindex+1;

            for (int i=newstartindex;i<25;i++){
                endTimelist.add(""+i+":00");
            }

        }else {
            startindex = 0;
        }


        builder.setSingleChoiceItems(items, startindex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        endTimelist.clear();
                        int newtime = which+1;
                        for (int i=newtime;i<25;i++){
                            endTimelist.add(""+i+":00");
                        }

                        binding.tvTo.setText(" bis ");
                        binding.ivCloseTime.setVisibility(View.VISIBLE);
                        binding.btnDialogStartTime.setText(items[which]+" Uhr");
                        binding.tvStart.setText(items[which]+" Uhr");
                        dialog.dismiss();


                        Log.e(TAG, "openStartTimeDialog: "+endTimelist.size() );

                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }

    private void openEndTimeDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Endzeit eingeben");

        String[] items = endTimelist.toArray(new String[0]);

        int index = 0;
        if (!endtimeindex.equals("")){
             index = endTimelist.indexOf(endtimeindex+":00 Uhr");
        }else {
            index = 0;
        }


        builder.setSingleChoiceItems(items, index,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        dialog.dismiss();
                        binding.ivCloseTime.setVisibility(View.VISIBLE);
                        binding.btnDialogEndTime.setText(items[which]+" Uhr");
                        binding.tvEnd.setText(items[which]+" Uhr");
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }

    private void getAvailableList(){
        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));

        Log.e("-->", "available time" + hashMap);

        RetrofitServices.urlServices.allsalon_opentime(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("available list-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        JSONObject data = jsonObject.getJSONObject("data");

                        start=Integer.valueOf(data.getString("mintime").substring(0,2));
                        end=Integer.valueOf(data.getString("maxtime").substring(0,2));

                        String endTime= data.getString("maxtime");
                        endTime = endTime.substring(0,5);

                        String startTime=data.getString("mintime");
                        startTime = startTime.substring(0,5);

                        if (tempStart != null && !tempStart.equals(""))
                        {
                            binding.btnDialogStartTime.setText(tempStart);
                        }else {
                            binding.btnDialogStartTime.setText(startTime);
                        }
                        if (tempEnd != null && !tempEnd.equals(""))
                        {
                            binding.btnDialogEndTime.setText(tempEnd);
                        }else {
                            binding.btnDialogEndTime.setText(endTime);
                        }


                    }else if(jsonObject.getInt("status")==0){
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e){
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                pd.dismiss();
                Log.e("-->","FALURE ");
            }
        });
    }

}
