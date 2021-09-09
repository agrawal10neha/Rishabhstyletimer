package com.si.styletimer.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.BookingDetailActivity;
import com.si.styletimer.adapters.CancelBookinbgAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.FragmentCancelledBinding;
import com.si.styletimer.models.mybooking_list_model.cancelled_booking_model.CancelModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CancelledFragment extends Fragment {
    private static final String TAG = "CancelledFragment";
    FragmentCancelledBinding binding;
    private Context context;
    private View view;
    private List<CancelModel> cancelList = new ArrayList<>();
    private Gson gson;
    private Dialog dialog;
    private CancelBookinbgAdapter adapter;

    private int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean paginationCheck = false;
    private int page=1;

    public CancelledFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cancelled, container, false);
        gson = new Gson();
        context = getActivity();

        initView();
        Log.e(TAG, "onCreateView:--> Cancelled " );

        view = binding.getRoot();
        return view;
    }

    private void initView() {

        getCancelled();

        onClick();
        setupRV();
    }

    private void setupRV() {

        LinearLayoutManager m =new LinearLayoutManager(context);
        binding.rvCancel.setLayoutManager(m);
        binding.rvCancel.setNestedScrollingEnabled(false);

        adapter =new CancelBookinbgAdapter(context,cancelList);
        binding.rvCancel.setAdapter(adapter);

        binding.rvCancel.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0)
                {
                    visibleItemCount = m.getChildCount();
                    totalItemCount =m.getItemCount();
                    pastVisiblesItems = m.findFirstVisibleItemPosition();

                    if (!loadingMore)
                    {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            viewMore = true;
                        }
                        if (viewMore)
                        {
                            loadingMore = true;

                            page++;
                            paginationCheck=true;
                            getCancelled();
                            Log.e(TAG, "onScrolled: ?>:<{}+_DAFCBHBCBHCB---HERE" );

                        }
                    }
                }
            }
        });

    }

    private void onClick() {
//        binding.lldetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, BookingDetailActivity.class);
//                intent.putExtra("Fragment","CancelledFragment");
//                startActivity(intent);
//            }
//        });
    }

    private void getCancelled(){

        final ProgressDialog pd =new ProgressDialog(context);
        pd.setTitle(context.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(context));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);

        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(context));
        hashMap.put(AppConstants.UID, Session.getUid(context));
        hashMap.put(AppConstants.ACTION, "cancelled");
        hashMap.put(AppConstants.PAGE, String.valueOf(page));


        Log.e("-->", "cancelled booking list " + hashMap);

        RetrofitServices.urlServices.booking_listing(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("cancelled booking-->", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        if(page==1){

                            cancelList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CancelModel>>() {}.getType());
                            adapter.setCancelModelList(cancelList);
                            adapter.notifyDataSetChanged();

                        }else if(page>1){

                            List<CancelModel> cancelList_2 = new ArrayList<>();
                            cancelList_2 = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<CancelModel>>() {}.getType());
                            cancelList.addAll(cancelList_2);
                            adapter.notifyDataSetChanged();

                        }


                        adapter.setOnItemClickListener(new CancelBookinbgAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(view.getId()==R.id.llDetail){
                                    Intent intent = new Intent(context, BookingDetailActivity.class);
                                    intent.putExtra("Fragment","CancelledFragment");
                                    intent.putExtra("id",cancelList.get(position).getId());
                                    intent.putExtra("sId",cancelList.get(position).getMerchantId());
                                    intent.putExtra("from","main");
                                    startActivity(intent);

                                }else if(view.getId()==R.id.tvRebook){
                                    popup(cancelList.get(position).getId());
                                }
                            }
                        });

                    }else if(jsonObject.getInt("status")==0){

                        if(page==1){
                            //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                            binding.rvCancel.setVisibility(View.GONE);
                            binding.ivNoData.setVisibility(View.VISIBLE);

                        }else if(page>1){

                            binding.rvCancel.setVisibility(View.VISIBLE);
                            binding.ivNoData.setVisibility(View.GONE);
                        }

                    }

                }
                catch (Exception e){
                    pd.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("-->","FALURE ");
            }
        });
    }

    private void popup(String x){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.booking_cancel_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);

        yes.setText("REBOOK");
        no.setText("CLOSE");
        head.setText("Rebook");
        sHead.setText("Are you sure you want to rebook this booking?");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                rebook(x);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    } // todo call Rebook Api----

    private void rebook(String bookingId){

        final ProgressDialog pd =new ProgressDialog(context);
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

        Log.e("-->", "Rebook-->" + hashMap);

        RetrofitServices.urlServices.rebook_service(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();

                    String respo = response.body().string().trim();
                    Log.e("Rebook --> ", "onResponse:- " + respo);
                    JSONObject jsonObject = new JSONObject(respo);

                    if(jsonObject.getInt("status")==1){

                        Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();

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
                Log.e("-->","FALURE ");
            }

        });
    }

}
