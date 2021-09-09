package com.si.styletimer.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.BookDetailReceiptAdapter;
import com.si.styletimer.adapters.TaxesAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityReceiptBinding;
import com.si.styletimer.models.receiptdetail.ReceiptModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptActivity extends AppCompatActivity {

    private static final String TAG = "ReceiptActivity";
    private ActivityReceiptBinding binding;
    private Context context;
    private String id = "", pdfurl = "";
    private String book_id = "", salonname = "", salonadd = "";
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_receipt);
        context = this;
        gson = new Gson();
        inIt();
    }

    private void inIt() {

        id = getIntent().getStringExtra("id");
        String service = getIntent().getStringExtra("service");
        book_id = getIntent().getStringExtra("book_id");
        salonname = getIntent().getStringExtra("salonname");

        //   binding.tvService.setText(service);
        //  binding.tvBookId.setText(book_id);

        getDetail();

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // new FileDownloader(ReceiptActivity.this,"" )

        binding.ivPrint.setOnClickListener(v -> {
            if (!pdfurl.equals("")) {
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(pdfurl));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
// Chrome is probably not installed
// OR not selected as default browser OR if no Browser is selected as default browser
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfurl));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            } else {
                Toast.makeText(context, "Keine URL definiert!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void getDetail() {

        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));

        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.BOOK_ID, book_id);

        Log.e("-->", "booking detail " + hashMap);

        RetrofitServices.urlServices.booking_receipt(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    pd.dismiss();
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);
                    ReceiptModel receiptModel = gson.fromJson(jsonObject.toString(), ReceiptModel.class);
                    if (receiptModel.getStatus() == 1) {
                        setDataFom(receiptModel);
                    } else if (receiptModel.getStatus() == 0) {
                        Toast.makeText(context, receiptModel.getResponseMessage(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->", "FALURE ");
                pd.dismiss();
            }
        });
    }

    private void setDataFom(ReceiptModel receiptModel) {
        binding.tvInvoiceid.setText("Rechnung #" + receiptModel.getData().getBookId());
        binding.tvTaxNo.setText("Steuernummer: " + receiptModel.getData().getTaxNumber());
        if (receiptModel.getData().getTip() != null && !receiptModel.getData().getTip().equals("") && !receiptModel.getData().getTip().equals("0")) {
            binding.tvTipThank.setText("Vielen Dank für's Trinkgeld, " + Utility.capitalize(receiptModel.getData().getFirstName()) + "!");
            binding.rlTipThank.setVisibility(View.VISIBLE);
            binding.vieww.setVisibility(View.VISIBLE);
        } else {
            binding.rlTipThank.setVisibility(View.GONE);
            binding.vieww.setVisibility(View.GONE);
        }
        pdfurl = receiptModel.getData().getReceiptUrl();
        // String add = receiptModel.getData().getAddress() + "\n" + receiptModel.getData().getZip() + " " + receiptModel.getData().getCity() + "\n" + receiptModel.getData().getCountry();
        //String add = receiptModel.getData().getAddress() + "\n" + receiptModel.getData().getZip() + " " + receiptModel.getData().getCity();
        String add = "";
        if (receiptModel.getData().getZip().equals("") && receiptModel.getData().getCity().equals("")) {
            add = receiptModel.getData().getAddress();

        } else if (receiptModel.getData().getZip().equals("")) {
            add = receiptModel.getData().getAddress() + "\n" + receiptModel.getData().getCity();

        } else if (receiptModel.getData().getCity().equals("")) {
            add = receiptModel.getData().getAddress() + "\n" + receiptModel.getData().getZip();

        } else {
            add = receiptModel.getData().getAddress() + "\n" + receiptModel.getData().getZip() + " " + receiptModel.getData().getCity();

        }
        if (add != null && !add.trim().equals("")) {
            binding.tvAddress.setVisibility(View.VISIBLE);
            binding.tvAddress.setText(add);
        } else {
            binding.tvAddress.setVisibility(View.GONE);
        }
        binding.tvName.setText(Utility.capitalize(receiptModel.getData().getFirstName() + " " + receiptModel.getData().getLastName()));
        binding.tvEmail.setText(receiptModel.getData().getEmail());
        binding.tvDuration.setText(receiptModel.getData().getTotalTime() + " Min.");
        if (receiptModel.getData().getPaymentType() != null && !receiptModel.getData().getPaymentType().equals("")) {
            if (receiptModel.getData().getPaymentType().equals("cash")) {
                binding.tvPayment.setText("Zahlung : Bar");

            } else if (receiptModel.getData().getPaymentType().equals("card")) {
                binding.tvPayment.setText("Zahlung : Karte");

            } else {
                binding.tvPayment.setText("Zahlung : " + Utility.capitalize(receiptModel.getData().getPaymentType()));
            }
        }
        binding.tvBookedId.setText(receiptModel.getData().getBookId());
        binding.tvBookedVia.setText("App");
        String a[] = receiptModel.getData().getBookingTime().split(" ");
        String b[] = a[1].split(":");
        binding.tvBookingDate.setText(Utility.formatDate(a[0]) + ", " + b[0] + ":" + b[1] + " Uhr");
        String a1[] = receiptModel.getData().getUpdatedOn().split(" ");
        String b1[] = a1[1].split(":");
        binding.tvCompletedDate.setText(Utility.formatDate(a1[0]) + ", " + b1[0] + ":" + b1[1] + " Uhr");
        binding.tvDatetime.setText(Utility.formatDateWithDayName(a1[0]));

        binding.tvSalonName.setText(salonname);
        salonadd = receiptModel.getData().getMAddress() + "\n" + receiptModel.getData().getMZip() + " " + receiptModel.getData().getMCity() + "\n" + receiptModel.getData().getmCountry();
        binding.tvSalonAddress.setText(salonadd);
        binding.tvSubTotal.setText(Utility.getPriceReplaceDotWithComma(receiptModel.getData().getSubtotal()) + " " + AppConstants.EURO);


        // binding.tvTotal.setText(receiptModel.getData().getTotal()+AppConstants.EURO);
        binding.tvGrandTotal.setText(Utility.getPriceReplaceDotWithComma(receiptModel.getData().getPaytotal()) + " " + AppConstants.EURO);


        if (receiptModel.getData().getDiscount() != null && !receiptModel.getData().getDiscount().equals("") && !receiptModel.getData().getDiscount().equals("0")) {
            binding.tvDiscount.setText(Utility.getPriceReplaceDotWithComma(receiptModel.getData().getDiscount()) + " " + AppConstants.EURO);
            binding.rlDiscount.setVisibility(View.VISIBLE);
            binding.viewDiscount.setVisibility(View.VISIBLE);
        } else {
            binding.rlDiscount.setVisibility(View.GONE);
            binding.viewDiscount.setVisibility(View.GONE);
        }
        if (receiptModel.getData().getTip() != null && !receiptModel.getData().getTip().equals("") && !receiptModel.getData().getTip().equals("0")) {
            binding.tvTip.setText(Utility.getPriceReplaceDotWithComma(receiptModel.getData().getTip()) + " " + AppConstants.EURO);
            binding.rlTip.setVisibility(View.VISIBLE);
            binding.viewTip.setVisibility(View.VISIBLE);
            //long tot = Long.parseLong(receiptModel.getData().getTotal()) - Long.parseLong(receiptModel.getData().getTip());
            double tot = Double.parseDouble(receiptModel.getData().getTotal()) - Double.parseDouble(receiptModel.getData().getTip());
            if (tot > 0) {
                String dd = String.valueOf(tot);
                binding.tvTotal.setText(Utility.getPriceReplaceDotWithComma(dd) + " " + AppConstants.EURO);
            }
        } else {
            binding.rlTip.setVisibility(View.GONE);
            binding.viewTip.setVisibility(View.GONE);
            binding.tvTotal.setText(Utility.getPriceReplaceDotWithComma(receiptModel.getData().getTotal()) + " " + AppConstants.EURO);
        }

        String a2[] = receiptModel.getData().getCreatedOn().split(" ");
        String b2[] = a2[1].split(":");
        // binding.tvPaymentDate.setText(Utility.formatDateWithDayName(a2[0]) + " um\n" + b2[0] + ":" + b2[1] + " Uhr");

        binding.rvBooking.setLayoutManager(new LinearLayoutManager(context));
        binding.rvBooking.setNestedScrollingEnabled(false);
        binding.rvBooking.setAdapter(new BookDetailReceiptAdapter(context, receiptModel.getData().getBookDetail(), receiptModel.getData().getEmpName()));

        binding.rvTaxes.setLayoutManager(new LinearLayoutManager(context));
        binding.rvTaxes.setNestedScrollingEnabled(false);
        binding.rvTaxes.setAdapter(new TaxesAdapter(context, receiptModel.getData().getTaxesModel()));


    }
}

