package com.si.styletimer.fragment;


import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.BookingDetailActivity;
import com.si.styletimer.activity.MyBookingActivity;
import com.si.styletimer.adapters.ConfirmBookingAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.CancelConfirmPopupBinding;
import com.si.styletimer.databinding.FragmentConfirmedBinding;
import com.si.styletimer.models.mybooking_list_model.confirm_booking_model.ConfirmModel;
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
public class ConfirmedFragment extends Fragment {
    private static final String TAG = "ConfirmedFragment";
    FragmentConfirmedBinding binding;
    private Context context;
    private View view;
    private List<ConfirmModel> confirmList = new ArrayList<>();
    private Gson gson;
    private Dialog dialog;
    private ConfirmBookingAdapter adapter;

    private int pastVisiblesItems = 0, visibleItemCount = 0, totalItemCount = 0;
    private Boolean loadingMore = false;
    private Boolean viewMore = false;
    private Boolean paginationCheck = false;
    private int page=1;
    private boolean mLoading = false;
    private AlertDialog dialog12;

    public ConfirmedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmed, container, false);
        gson=new Gson();
        context = getActivity();

        initView();

        Log.e(TAG, "onCreateView:--> Confirmed " );


        view = binding.getRoot();
        return view;
    }

    private void initView() {
        if (Utility.connectionStatus(context)) {
            getConfirmList();
        } else {
            Utility.nointernettoast(context);
        }


        onClick();
        setUpRV();

    }

    private void setUpRV() {
        LinearLayoutManager m = new LinearLayoutManager(context);
        binding.rvConfirm.setLayoutManager(m);
        binding.rvConfirm.setNestedScrollingEnabled(false);

        adapter=new ConfirmBookingAdapter(context, confirmList);
        binding.rvConfirm.setAdapter(adapter);


        binding.rvConfirm.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((m.getChildCount() + m.findFirstVisibleItemPosition()) >= m.getItemCount()) {
                        if (!mLoading ){
                            Log.e(TAG, "onLoadMore: ----------------------" );
                            // Scrolled to bottom. Do something here.
                            if (Utility.connectionStatus(context)) {
                                page++;
                                getConfirmList();
                                mLoading = true;
                            } else {
                                Utility.nointernettoast(context);
                            }

                        }
                    }
                }
            }
        });


        /*binding.rvConfirm.addOnScrollListener(new RecyclerView.OnScrollListener()
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

                            paginationCheck=true;
                            page++;
                            getConfirmList();
                            Log.e(TAG, "onScrolled: ?>:<{}+_DAFCBHBCBHCB---HERE" );

                        }
                    }
                }
            }
        });*/


        adapter.setOnItemClickListener(new ConfirmBookingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(view.getId()==R.id.llDetail){
                    Intent intent = new Intent(context, BookingDetailActivity.class);
                    intent.putExtra("Fragment","ConfirmedFragment");
                    intent.putExtra("id",confirmList.get(position).getId());
                    intent.putExtra("sId",confirmList.get(position).getMerchantId());
                    intent.putExtra("from","main");
                    intent.putExtra("selected","0");
                    intent.putExtra(AppConstants.RES_STATUS,confirmList.get(position).getResheduleStatus());
                    startActivity(intent);
                    getActivity().finish();

                }else if(view.getId()==R.id.tvCancel){
                    popup(confirmList.get(position).getId());
                }
            }
        });

        binding.swipyrefreshlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                try {

                    if (Utility.connectionStatus(context)) {
                        page++;
                        getConfirmList();
                    } else {
                        Utility.nointernettoast(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void onClick() {

//        binding.lldetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, BookingDetailActivity.class);
//                intent.putExtra("Fragment","ConfirmedFragment");
//                startActivity(intent);
//            }
//        });

    }

    private void getConfirmList(){

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
        hashMap.put(AppConstants.ACTION, "confirmed");
        hashMap.put(AppConstants.PAGE, String.valueOf(page));

        Log.e(TAG, "getConfirmList: "+hashMap );

        RetrofitServices.urlServices.booking_listing(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    pd.dismiss();
                    binding.swipyrefreshlayout.setRefreshing(false);
                    String respo = response.body().string().trim();
                    Log.e(TAG, "onResponse: "+respo );
                    JSONObject jsonObject = new JSONObject(respo);
                    
                    if(jsonObject.getInt("status")==1){

                        if(page==1){

                            confirmList = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<ConfirmModel>>() {}.getType());
                            adapter.setConfirmModelList(confirmList);
                            adapter.notifyDataSetChanged();

                            if(confirmList.size()<1){
                                binding.rvConfirm.setVisibility(View.GONE);
                                binding.ivNoData.setVisibility(View.VISIBLE);
                            }else {
                                binding.rvConfirm.setVisibility(View.VISIBLE);
                                binding.ivNoData.setVisibility(View.GONE);
                            }

                        }else if (page>1){
                            List<ConfirmModel> confirmList_2 = new ArrayList<>();
                            confirmList_2 = gson.fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<ConfirmModel>>() {}.getType());
                            confirmList.addAll(confirmList_2);
                            adapter.notifyDataSetChanged();
                            mLoading = false;
                        }

                    }else {
                        
                        if(page==1){
                            Log.e(TAG, "onResponse: there "+page );
                            //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                            binding.swipyrefreshlayout.setVisibility(View.GONE);
                            binding.ivNoData.setVisibility(View.VISIBLE);

                        }else if(page>1){
                            binding.swipyrefreshlayout.setVisibility(View.VISIBLE);
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
                pd.dismiss();
                Log.e("-->","FALURE ");
            }
        });
    }

    private void popup(final String x){
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

        Log.e(TAG, "popup: here we t" );

        yes.setText(context.getResources().getString(R.string.yes));
        no.setText(context.getResources().getString(R.string.no));
        head.setText(context.getResources().getString(R.string.cancel_booking));
        sHead.setText(context.getResources().getString(R.string.are_you_sure_cancel_booking));
        yes.setVisibility(View.GONE);
        no.setVisibility(View.GONE);
        btCancel.setVisibility(View.VISIBLE);
        icClose.setVisibility(View.VISIBLE);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                int a = rg.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(a);
                cancelBook(x,rb.getText().toString());
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
                Log.e(TAG, "onClick: __== " );
                dialog.dismiss();
                int a = rg.getCheckedRadioButtonId();
                RadioButton rb = dialog.findViewById(a);

                cancelBook(x,rb.getText().toString());
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

    private void cancelBook(String id,String reason){
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

                    if(jsonObject.getInt("status")==1){

                        //Toast.makeText(context, jsonObject.getString("response_message"), Toast.LENGTH_SHORT).show();
                        cancel_confirm_popup(jsonObject.getString("response_message"));
                      //  startActivity(new Intent(context, MyBookingActivity.class).putExtra("selected","0"));
                       // getActivity().finish();

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
    private void cancel_confirm_popup(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        CancelConfirmPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.cancel_confirm_popup, null, false);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        dialog12 = builder.create();

        if (binding.ivmaingiftwo.getDrawable() != null) {
            pl.droidsonroids.gif.GifDrawable gifDrawable = (pl.droidsonroids.gif.GifDrawable) binding.ivmaingiftwo.getDrawable();
            gifDrawable.setLoopCount(1);
        }

       // binding.tvtitle.setText(msg);
        binding.tvtitle.setText("Deine Buchung wurde erfolgreich storniert");

        binding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog12.dismiss();
                startActivity(new Intent(context, MyBookingActivity.class).putExtra("selected","0"));
                getActivity().finish();
            }
        });

        binding.btnivclosedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog12.dismiss();
                startActivity(new Intent(context, MyBookingActivity.class).putExtra("selected","0"));
                getActivity().finish();
            }
        });
        dialog12.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog12 != null)
        {
            dialog12.dismiss();
        }
        if (dialog != null)
        {
            dialog.dismiss();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //getConfirmList();
    }

}
