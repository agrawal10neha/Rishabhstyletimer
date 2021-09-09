package com.si.styletimer.controller;

import android.content.Context;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.si.styletimer.models.categoryListing.Sercvice;
import com.si.styletimer.models.salonDetails.SalonAllServiceModel;
import com.si.styletimer.models.salonDetails.SalonDetailModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Controller extends MultiDexApplication {

    public List<Sercvice> sercviceList;
   // public List<AllServiceTwoModel> allServiceTwoModelList;
    public List<SalonAllServiceModel> salonAllServiceModelList;
    public String startTime="",endTime="",day="",anyTime="",datecalendar = "";
    public SalonDetailModel detailModel;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        setupRealmDatabase();
        sercviceList = new ArrayList<>();
      //allServiceTwoModelList = new ArrayList<>();
        detailModel = new SalonDetailModel();
        salonAllServiceModelList = new ArrayList<>();
    }


    private void setupRealmDatabase(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("styletimerdb.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

}
