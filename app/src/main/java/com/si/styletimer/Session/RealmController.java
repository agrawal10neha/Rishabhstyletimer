package com.si.styletimer.Session;

import android.content.Context;
import android.util.Log;

import com.si.styletimer.models.salonDetails.SalonSubServiceModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    private static final String TAG = "RealmController";
    private final Realm realm;

    public RealmController(Context application) {
        realm = Realm.getDefaultInstance();
    }

    public SalonSubServiceModel getSalonSubServiceModelList(String id){
     //   Log.e(TAG, "getSalonSubServiceModelList: "+id );
        RealmResults<SalonSubServiceModel> results = realm.where(SalonSubServiceModel.class).findAll();
      //  SalonSubServiceModel  subServiceModel  = results.where() .equalTo("mId",id).findFirst();
        SalonSubServiceModel  subServiceModel  = results.where() .equalTo("mSubcategoryId",id).findFirst();
        return subServiceModel;
    }

    public List<SalonSubServiceModel> getSalonSubServiceModel(String id){
        RealmResults<SalonSubServiceModel> results = realm.where(SalonSubServiceModel.class).findAll();
        //  SalonSubServiceModel  subServiceModel  = results.where() .equalTo("mId",id).findFirst();
        List<SalonSubServiceModel>  subServiceModel  = results.where().equalTo("mSubcategoryId",id).findAll();
        return subServiceModel;
    }

    public SalonSubServiceModel getSalonSubServiceModelmId(String id){
        //  Log.e(TAG, "getSalonSubServiceModelList: "+id );
        RealmResults<SalonSubServiceModel> results = realm.where(SalonSubServiceModel.class).findAll();
        //  SalonSubServiceModel  subServiceModel  = results.where() .equalTo("mId",id).findFirst();
        List<SalonSubServiceModel>  subServiceModel  = results.where() .equalTo("mId",id).findAll();
        if (subServiceModel.size()>0) {
            return subServiceModel.get(0);
        }else {
            return null;
        }
    }


    public List<SalonSubServiceModel> getSelectedInCartModel(String id){
        RealmResults<SalonSubServiceModel> results = realm.where(SalonSubServiceModel.class).findAll();
        List<SalonSubServiceModel>  subServiceModel  = results.where().equalTo("mSubcategoryId",id).equalTo("mInCart", "1").findAll();
        return subServiceModel;
    }

    public List<SalonSubServiceModel> getSelectedInCartModelMainCategoryid(String id){
        RealmResults<SalonSubServiceModel> results = realm.where(SalonSubServiceModel.class).findAll();
        List<SalonSubServiceModel>  subServiceModel  = results.where().equalTo("mainCategoryid",id).equalTo("mInCart", "1").findAll();
        return subServiceModel;
    }


    public Realm getRealm() {
        return realm;
    }
    public void closeRealm() {
        realm.close();
    }

    public void clearServices(){
        Log.e(TAG, "clearServices: ----------------- " );
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<SalonSubServiceModel> results = realm.where(SalonSubServiceModel.class).findAll();
                results.deleteAllFromRealm();
                Log.e(TAG, "clearServices:=============== ");
            }
        });
    }

    public void clearDatabase(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

}
