package com.si.styletimer.activity;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ActivityWriteReviewBinding;
import com.si.styletimer.databinding.PopupThankReviewBinding;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Animatoo;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteReviewActivity extends AppCompatActivity {
    private static final String TAG = "WriteReviewActivity";
    private Context context;
    private ActivityWriteReviewBinding binding;
    private String bookId, salonId, employeeId,flag = "",res_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write_review);
        context = this;
        hideKeyboardNew(WriteReviewActivity.this);
        initViews();
    }

    private void initViews() {

        bookId = getIntent().getStringExtra(AppConstants.BOOK_ID);
        salonId = getIntent().getStringExtra(AppConstants.SALON_ID);
        employeeId = getIntent().getStringExtra(AppConstants.EMPLOYEEID);
        res_status = getIntent().getStringExtra(AppConstants.RES_STATUS);
        flag = getIntent().getStringExtra(AppConstants.FLAG);

        binding.btNext.setOnClickListener(view -> validation());
        binding.ivClose.setOnClickListener(view -> finish());

        binding.etReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int ratonng = (int) binding.ratingbar.getRating();
                if (ratonng < 4) {
                    if (s.toString().length()>0) {
                        binding.ilReview.setError(null);
                    } else {
                        binding.ilReview.setError("Bei einer Bewertung von weniger als 3 Sternen musst du eine Bewertung verfassen");
                    }
                } else {
                    binding.ilReview.setError(null);
                }
            }
        });

    }

    private void validation() {
        String review = binding.etReview.getText().toString();
        int ratonng = (int) binding.ratingbar.getRating();
        String anony = "";

        if (binding.cbanonymous.isChecked()) {
            anony = "1";
        } else {
            anony = "0";
        }

        if (ratonng == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.please_select_your_rating), Toast.LENGTH_SHORT).show();
            return;
        }

        if (ratonng < 4) {
            if (TextUtils.isEmpty(review)) {
                binding.ilReview.setError("Bei einer Bewertung von weniger als 3 Sternen musst du eine Bewertung verfassen");
                //    binding.etReview.setBoxStrokeColor(context.getResources().getColor(R.color.blue));
                // binding.etReview.setHintTextAppearance(R.style.EditTextHintStyle);
            } else {
                binding.ilReview.setError(null);
                review = "";
                postReview(ratonng, review, anony);
            }
        } else {
            postReview(ratonng, review, anony);
        }
    }


    private void postReview(int ratonng, String review, String anony) {

        if (salonId == null && employeeId == null && employeeId == null) {
            return;
        }

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
        hashMap.put(AppConstants.EMPLOYEEID, employeeId);
        hashMap.put(AppConstants.ANONYMOUS, anony);
        hashMap.put(AppConstants.RATING, ratonng + "");
        hashMap.put(AppConstants.REVIEW, review);


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

    public static void hideKeyboardNew(AppCompatActivity activity) {
        setupParent(activity.getWindow().getDecorView().findViewById(android.R.id.content), activity);
    }

    //Hide Keyboard
    public static void setupParent(View view, Context context) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                Utility.hideKeyboard(context);
                //view.performClick();
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView, context);
            }
        }
    }

    private void popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PopupThankReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_thank_review, null, false);
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        binding.tvOk.setText("Zurück zur Buchung");
        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (flag != null && flag.equals("list"))
                {
                    Intent intent = new Intent(context, BookingDetailActivity.class);
                    intent.putExtra("Fragment","CompletedFragment");
                    intent.putExtra("id",bookId);
                    intent.putExtra("from","main");
                    intent.putExtra("sId",salonId);
                    intent.putExtra("selected","1");
                    intent.putExtra(AppConstants.RES_STATUS,res_status);
                    startActivity(intent);
                    finish();
                    Animatoo.animateSlideRight(context);
                }else {
                    finish();
                }

            }
        });
        binding.btnivclosedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (flag != null && flag.equals("list"))
                {
                    Intent intent = new Intent(context, BookingDetailActivity.class);
                    intent.putExtra("Fragment","CompletedFragment");
                    intent.putExtra("id",bookId);
                    intent.putExtra("from","main");
                    intent.putExtra("sId",salonId);
                    intent.putExtra("selected","1");
                    intent.putExtra(AppConstants.RES_STATUS,res_status);
                    startActivity(intent);
                    finish();
                    Animatoo.animateSlideRight(context);
                }else {
                    finish();
                }
            }
        });

    }

}
