package com.si.styletimer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.CartAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.ActivityCartBinding;
import com.si.styletimer.databinding.DialogConfirmBinding;
import com.si.styletimer.databinding.PopupSuccessAlertBinding;
import com.si.styletimer.databinding.TermAndCondiPopupBinding;
import com.si.styletimer.models.CartModel;
import com.si.styletimer.models.salonDetails.SalonSubServiceModel;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    private Context context;
    private ActivityCartBinding binding;
    private String str_salon_id = "", employeid = "", date = "", time = "";
    private List<CartModel> cartModelList;
    private CartAdapter cartAdapter;
    private Gson gson;
    private Controller controller;
    private String totladur = "";
    private RealmController realmController;
    private Dialog dialog;
    private String cancellationAllowed = "", cancellationHour = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        context = this;
        gson = new Gson();
        controller = (Controller) context.getApplicationContext();
        realmController = new RealmController(context);

        initviews();
    }

    private void initviews() {
        set_adapter();
        Intent intent = getIntent();
        if (intent != null) {
            employeid = intent.getStringExtra(AppConstants.EMPLOYEE_SELECT);
            date = intent.getStringExtra("date");
            time = intent.getStringExtra("time");
            if (!employeid.equals("")) {
                if (Utility.connectionStatus(context)) {
                    get_book_detailforconfirm();
                } else {
                    Utility.nointernettoast(context);
                }
            }
        }

        Log.e(TAG, "initviews: " + controller.detailModel.toString());
        binding.tvsalonname.setText(controller.detailModel.getBusinessName());
        binding.tvsalonaddrsess.setText(controller.detailModel.getAddress() + "\n" + controller.detailModel.getZip() + " " + controller.detailModel.getCity());
        if (Session.getStylername(context).equals("Any Employee")) {
            binding.tvstylername.setText("Nächster freier Mitarbeiter");
        } else {
            binding.tvstylername.setText(Session.getStylername(context));
        }
        String dateee = Utility.change_format(date, "yyyy-MM-dd", "dd.MM.yyyy");
        binding.tvdatetime.setText(dateee);
        binding.tvtime.setText(time);


        String someFilepath = RetrofitServices.BANNERS + controller.detailModel.getId() + "/" + controller.detailModel.getImage();
        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
        String url;
        if (extension.equals(".webp"))
        {
            url = RetrofitServices.BANNERS + controller.detailModel.getId() + "/" + controller.detailModel.getImage();
        }else {
            url = RetrofitServices.BANNERS + controller.detailModel.getId() + "/" + controller.detailModel.getImage()+".webp";
        }
        //String url = RetrofitServices.BANNERS + controller.detailModel.getId() + "/" + controller.detailModel.getImage();
        Picasso.get()
                .load(url)
                .fit()
                .placeholder(R.drawable.no_image_available1)
                .centerCrop()
                .into(binding.ivBanners);

        binding.btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.connectionStatus(context)) {
                    dialogConfirmBooking();
                } else {
                    Toast.makeText(context, "Es besteht keine Internetverbindung", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnaddother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.e(TAG, "onClick: "+ Session.getServiceIds(context) );
                Intent salondetail = new Intent(context, SalonDetailActivity.class);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                salondetail.putExtra(AppConstants.SERVICEIDS, Session.getServiceIds(context));
                startActivity(salondetail);
                // finish();
            }
        });


        Log.e(TAG, "onClick: " + Session.getServiceIds(context));

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnivcloseet.setVisibility(View.VISIBLE);
                binding.etNote.setVisibility(View.VISIBLE);
                binding.etNote.requestFocus();
            }
        });

        binding.btnivcloseet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnivcloseet.setVisibility(View.GONE);
                binding.etNote.setVisibility(View.GONE);
            }
        });

        binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideSoftKeyboard(CartActivity.this);
            }
        });

/*
        binding.tvsalonname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmBooking();
            }
        });
*/
    }


    private void dialogConfirmBooking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PopupSuccessAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.popup_success_alert, null, false);
        builder.setView(binding.getRoot());
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        binding.btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirm_booking();
            }
        });

        binding.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term_and_condi_popup("terms");
                //Intent intent = new Intent(context, WebViewActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void term_and_condi_popup(String lastname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        TermAndCondiPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.term_and_condi_popup, null, false);
        builder.setView(binding.getRoot());

        AlertDialog dialog = builder.create();

        String url = Environment.getBaseUrl() + "user/get_staticpage/" + lastname;
        Log.e(TAG, "term_and_condi_popup: " + url);

        Utility.showProgressHUD(context);
        binding.webview.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView1, int newProgress) {
                Utility.hideProgressHud();
            }
        });
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.loadUrl(url);
        binding.webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Utility.hideProgressHud();
                return true;
            }
        });


        binding.ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    private void set_adapter() {
        cartModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycart.setLayoutManager(linearLayoutManager);
        binding.recycart.setHasFixedSize(true);
        cartAdapter = new CartAdapter(context, cartModelList);
        binding.recycart.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.btnivclose) {
                    String carrtid = cartAdapter.getCartModel().get(position).getId();
                    String price = cartAdapter.getCartModel().get(position).getPrice();
                    String disPrice = cartAdapter.getCartModel().get(position).getDiscountPrice();
                    String serviceId = cartAdapter.getCartModel().get(position).getServiceId();
                    remove_item_From_Cart(carrtid, position, price, disPrice);
                    removeCheckFromItem(serviceId);
                }
            }
        });
    }

    private void removeCheckFromItem(String id) {
        SalonSubServiceModel subServiceModel = realmController.getRealm()
                .where(SalonSubServiceModel.class)
                .equalTo("mId", id)
                .findFirst();

        if (subServiceModel == null) {
            return;
        }
        String incar = subServiceModel.getInCart();

        String value = "";
        if (incar.equals("1")) {
            value = "0";
        } else {
            value = "1";
        }


        realmController.getRealm().beginTransaction();
        subServiceModel.setInCart(value);
        realmController.getRealm().copyToRealmOrUpdate(subServiceModel);
        realmController.getRealm().commitTransaction();
    }

    private void get_book_detailforconfirm() {
        show_loader();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.EMPLOYEE_SELECT, this.employeid);
        hashMap.put(AppConstants.DATE, this.date);
        hashMap.put(AppConstants.TIME, this.time);

        Log.e(TAG, "get_book_detailforconfirm: " + hashMap);

        RetrofitServices.urlServices.book_detailforconfirm(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hide_loader();
                    String res = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status") == 1) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray cart = data.getJSONArray("cart");

                        totladur = String.valueOf(data.getInt("totalDuration"));
                        if (data.getString("tot_disc") != null && !data.getString("tot_disc").equals("") && !data.getString("tot_disc").equals("0")) {
                            binding.rlDiscount.setVisibility(View.VISIBLE);
                            binding.tvdis.setText(Utility.getPriceReplaceDotWithComma(data.getString("tot_disc"))+" " + AppConstants.EURO);
                        } else {
                            binding.rlDiscount.setVisibility(View.GONE);
                        }

                        binding.tvtotoalamt.setText(Utility.getPriceReplaceDotWithComma(data.getString("total"))+" " + AppConstants.EURO);
                        cartModelList = gson.fromJson(cart.toString(), new TypeToken<ArrayList<CartModel>>() {
                        }.getType());

                        String sbb = "";
                        for (int i = 0;i<cartModelList.size();i++)
                        {
                            if (cartModelList.get(i).getPriceStartOption().equals("ab"))
                            {
                                sbb = "ab";
                            }
                        }

                        if (sbb.equals("ab"))
                        {
                            binding.tvAb.setVisibility(View.VISIBLE);
                        }else {
                            binding.tvAb.setVisibility(View.GONE);
                        }



                        if (cartModelList.size() > 0) {
                            cancellationAllowed = cartModelList.get(0).getCancelBookingAllow();
                            cancellationHour = cartModelList.get(0).getHrBeforeCancel();
                            if (cancellationAllowed.equals("no")) {
                                binding.tvCanceltime.setText("Bitte kontaktiere den Salon für eine Verlegung oder Stornierung deines Termins");
                            } else {
                                binding.tvCanceltime.setText("Du kannst deine Buchung bis zu " + cancellationHour + " Stunden vor deinem Termin über styletimer stornieren oder verlegen");

                            }
                        }

                        cartAdapter.setCartModelList(cartModelList);
                        cartAdapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_loader();
            }
        });
    }

    private void confirm_booking() {
        binding.btnconfirm.setVisibility(View.GONE);
        show_loader();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.EMPLOYEE_SELECT, this.employeid);
        hashMap.put(AppConstants.DATE, this.date);
        hashMap.put(AppConstants.TIME, this.time + ":00");
        hashMap.put(AppConstants.NOTE, binding.etNote.getText().toString());
        hashMap.put("totalDuration", totladur);
        hashMap.put("salon_id", Session.getSalon_id(context));

        Log.e(TAG, "get_employee_time: " + hashMap);

        RetrofitServices.urlServices.booking_confirm(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hide_loader();
                    String res = response.body().string().trim();

                    Log.e(TAG, "onResponse:-----> rish " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status") == 1) {
                        Session.setIsadded(context, "0");

                        String total_booking = jsonObject.getString("total_booking");
                        String ask_after_booking = jsonObject.getString("ask_after_booking");
                        String app_review_status = jsonObject.getString("app_review_status");
                        String booking_count = jsonObject.getString("booking_count");

                        dialog_confirm(total_booking,ask_after_booking,app_review_status,booking_count,jsonObject.getString("response_message"),jsonObject.getString("book_id"),jsonObject.getString("booking_status"));
                    } else {
                        binding.btnconfirm.setVisibility(View.VISIBLE);
                        Toast.makeText(context, binding.tvsalonname.getText()+"nimmt aktuell keine neuen Buchungen entgegen", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_loader();
            }
        });
    }

    private void dialog_confirm(String total_booking, String ask_after_booking, String app_review_status, String booking_count, String des, String book_id, String booking_status) {
        DialogConfirmBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_confirm, null , false);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(binding1.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        binding1.btngohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(context, BookingDetailActivity.class);
                intent.putExtra("Fragment","ConfirmedFragment");
                intent.putExtra("id",book_id);
                intent.putExtra("sId",Session.getSalon_id(context));
                intent.putExtra("from","main");
                intent.putExtra("selected","0");
                //intent.putExtra(AppConstants.RES_STATUS,booking_status);
                intent.putExtra(AppConstants.RES_STATUS,"0");
                intent.putExtra("total_booking",total_booking);
                intent.putExtra("ask_after_booking",ask_after_booking);
                intent.putExtra("app_review_status",app_review_status);
                intent.putExtra("booking_count",booking_count);
                startActivity(intent);
                finishAffinity();
             /*   Intent intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
            }
        });
        binding1.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, BookingDetailActivity.class);
                intent.putExtra("Fragment","ConfirmedFragment");
                intent.putExtra("id",book_id);
                intent.putExtra("sId",Session.getSalon_id(context));
                intent.putExtra("from","main");
                intent.putExtra("selected","0");
                //intent.putExtra(AppConstants.RES_STATUS,booking_status);
                intent.putExtra(AppConstants.RES_STATUS,"0");
                intent.putExtra("total_booking",total_booking);
                intent.putExtra("ask_after_booking",ask_after_booking);
                intent.putExtra("app_review_status",app_review_status);
                intent.putExtra("booking_count",booking_count);
                startActivity(intent);
                finishAffinity();
             /*   Intent intent = new Intent(context, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
            }
        });
       // binding1.tvsub.setText(des);
        binding1.tvsub.setText("Deine Buchung war erfolgreich. Eine Bestätigung deiner Buchung wurde an deine E-Mail Adresse gesendet.");
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void show_loader() {
        binding.loader.setVisibility(View.VISIBLE);
    }

    private void hide_loader() {
        binding.loader.setVisibility(View.GONE);
    }

    private void remove_item_From_Cart(String carrtid, int pos, String price, String disPrice) {
        show_loader();
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.CART_ID, carrtid);

        hashMap.put(AppConstants.EMPLOYEE_SELECT, this.employeid);
        hashMap.put(AppConstants.DATE, this.date);
        hashMap.put(AppConstants.TIME, this.time);

        Log.e(TAG, "remove_item_From_Cart: " + hashMap);

        RetrofitServices.urlServices.remove_service(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hide_loader();
                    String res = response.body().string().trim();
                    Log.e(TAG, "onResponse: " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getInt("status") == 1) {

                       // Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        cartAdapter.removeItem(pos);

                        JSONObject data = jsonObject.getJSONObject("data");
                        binding.tvdis.setText(AppConstants.EURO + data.getString("tot_disc"));
                        binding.tvtotoalamt.setText(AppConstants.EURO + data.getString("total"));

                        if (cartAdapter.getCartModel().size() == 0) {
//todo --- Modified Code --->
                           // Toast.makeText(context, "Keine Services im Warenkorb", Toast.LENGTH_SHORT).show();
                            binding.btnaddother.performClick();
                            finish();
                          //  Intent intent = new Intent(context,DashboardActivity.class);
                          //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          //  startActivity(intent);

//todo --- Original code --->
                            /*Intent intent = new Intent(context,DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/
                        }
                    } else {
                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_loader();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
