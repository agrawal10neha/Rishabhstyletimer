package com.si.styletimer.adapters.salonDetail;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Point;
import android.os.Build;

import androidx.annotation.UiThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.google.gson.Gson;
import com.si.styletimer.R;
import com.si.styletimer.Session.RealmController;
import com.si.styletimer.Session.Session;
import com.si.styletimer.adapters.ReviewdialogAdapter;
import com.si.styletimer.adapters.allservice.SubServiceAdapter;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.controller.Controller;
import com.si.styletimer.databinding.DialogDiscountPriceBindingImpl;
import com.si.styletimer.databinding.DialogShowDetailsBinding;
import com.si.styletimer.databinding.SalonallserviceCellBinding;
import com.si.styletimer.models.salonDetails.SalonAllServiceModel;
import com.si.styletimer.models.salonDetails.SalonDetailModel;
import com.si.styletimer.models.salonDetails.SalonMainCategoryModel;
import com.si.styletimer.models.salonDetails.SalonSubServiceModel;
import com.si.styletimer.models.showdatarating.Data;
import com.si.styletimer.models.showdatarating.RatingREviewDataModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalonAllServiceAdapter extends RecyclerView.Adapter<SalonAllServiceAdapter.ViewHolder> {

    private static final String TAG = "SalonAllServiceAdapter";
    private final Context mContext;
    private List<SalonAllServiceModel> mSalonAllServiceModelList;
    private OnItemClickListener onItemClickListener;
    private RealmController realmController;
    private Controller controller;
    private Gson gson;
    //private SubServiceAdapter subServiceAdapter;
    private int counttt = 0;
    SalonDetailModel salonDetailModel;

    public SalonAllServiceAdapter(Context mContext, List<SalonAllServiceModel> mSalonAllServiceModelList) {
        this.mSalonAllServiceModelList = mSalonAllServiceModelList;
        this.mContext = mContext;
        realmController = new RealmController(mContext);
        controller = (Controller) mContext.getApplicationContext();
        gson = new Gson();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SalonallserviceCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.salonallservice_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SalonAllServiceModel mSalonAllServiceModel = this.mSalonAllServiceModelList.get(position);
        holder.bind(mSalonAllServiceModel);

        SalonSubServiceModel subServiceModel = realmController.getSalonSubServiceModelList(mSalonAllServiceModel.getSubid());
        holder.binding.tvgroupname.setText(mSalonAllServiceModel.getCategoryName().trim() + " (" + mSalonAllServiceModel.getCount() + ")");
        // holder.binding.tvgroupcount.setText("(" + mSalonAllServiceModel.getCount() + ")");

        holder.binding.tvitemmin.setText(mSalonAllServiceModel.getServiceDuration());

        holder.binding.layountservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.binding.layoutsecond.getVisibility() == View.VISIBLE) {
                    // holder.binding.layoutsecond.setVisibility(View.GONE);
                    holder.binding.ivDropdown.setRotation(270);
                    //  holder.binding.layoutsecond.startAnimation(slideExit);
                    collapse(holder.binding.layoutsecond);
                } else {
                    // holder.binding.layoutsecond.setVisibility(View.VISIBLE);
                    holder.binding.ivDropdown.setRotation(0);
                    //    holder.binding.layoutsecond.startAnimation(slideEnter);
                    expand(holder.binding.layoutsecond, position, mContext);
                }

            }
        });

        if (!mSalonAllServiceModel.getMinPrice().equals("")) {
            String aa[] = mSalonAllServiceModel.getMinPrice().split("\\s+");
            if (aa.length>1) {
                String ab = aa[0];
                String pp = aa[1];
                Log.i("value",pp);
                holder.binding.tvitemprice.setText(ab + " " + Utility.getPriceReplaceDotWithComma(pp) + AppConstants.EURO);
            }else {
                String ab = aa[0];
                if (mSalonAllServiceModel.getCount() != null && !mSalonAllServiceModel.getCount().equals("1"))
                {
                    holder.binding.tvitemprice.setText("ab " +" "+Utility.getPriceReplaceDotWithComma(ab) + AppConstants.EURO);
                }else {
                    holder.binding.tvitemprice.setText(Utility.getPriceReplaceDotWithComma(ab) + AppConstants.EURO);
                }
            }
        }

       /* if (!mSalonAllServiceModel.getSelectedService().equals("") && !mSalonAllServiceModel.getSelectedService().equals("0")) {
            holder.binding.tvSelected.setVisibility(View.VISIBLE);
            holder.binding.tvSelected.setText("1 ausgewählt");
        }*/

        if (!mSalonAllServiceModel.getMaxDiscount().equals("")) {
            holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
            holder.binding.tvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to) + " " + Utility.getPriceReplaceDotWithComma(mSalonAllServiceModel.getMaxDiscount()) + "% ");
        } else {
            holder.binding.tvsaveupto.setVisibility(View.GONE);
        }

        holder.binding.tvShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogShowDetails(mSalonAllServiceModel.getServiceDetail(),mSalonAllServiceModel.getCategoryName());
                String mid = mSalonAllServiceModel.getServicedetailid();
                String allids = returnServiceIds(mSalonAllServiceModel.getSubServices());
                getratingdetails(mid, allids);

            }
        });

        holder.binding.tvsaveupto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogShowDetails(mSalonAllServiceModel.getServiceDetail(),mSalonAllServiceModel.getCategoryName());
                String discount= (String) holder.binding.tvsaveupto.getText();
                String discountPrice=discount.replace("spare bis","");

                //String mid = mSalonAllServiceModel.getServicedetailid();
                //String allids = returnServiceIds(mSalonAllServiceModel.getSubServices());
                dialogShowDiscount(discountPrice);

            }
        });


        if (mSalonAllServiceModel.getServiceDetail() != null && !mSalonAllServiceModel.getServiceDetail().equals("")) {
            holder.binding.tvShowDetails.setVisibility(View.VISIBLE);
            // Log.e(TAG, "onBindViewHolder: ifffffffff "+  mSalonAllServiceModel.getServiceDetail());
        } else if (mSalonAllServiceModel.getReviewcount() != null && !mSalonAllServiceModel.getReviewcount().equals("0")) {
            // Log.e(TAG, "onBindViewHolder: else ifffffffff "+  mSalonAllServiceModel.getServiceDetail());
            holder.binding.tvShowDetails.setVisibility(View.VISIBLE);
        } else {
            // Log.e(TAG, "onBindViewHolder: elseeeeeeeee "+  mSalonAllServiceModel.getServiceDetail());
            holder.binding.tvShowDetails.setVisibility(View.GONE);
        }

        if (mSalonAllServiceModel.getTotalInCart() > 0) {
            holder.binding.tvSelected.setVisibility(View.VISIBLE);
            holder.binding.llpriceshowhide.setVisibility(View.GONE);
            holder.binding.tvSelected.setText(mSalonAllServiceModel.getTotalInCart() + " ausgewählt");
        } else {
            holder.binding.tvSelected.setVisibility(View.GONE);
            holder.binding.llpriceshowhide.setVisibility(View.VISIBLE);
        }


        LinearLayoutManager linarLayoutanager = new LinearLayoutManager(mContext);
        linarLayoutanager.setOrientation(LinearLayoutManager.VERTICAL);
        linarLayoutanager.setAutoMeasureEnabled(true);
        holder.binding.recyallservicetwo.setLayoutManager(linarLayoutanager);
        holder.binding.recyallservicetwo.setHasFixedSize(true);
        holder.binding.recyallservicetwo.setNestedScrollingEnabled(false);

      /*  List<SalonSubServiceModel> salonSubServiceModelList = new ArrayList<>();
        salonSubServiceModelList.add(subServiceModel);*/

        List<SalonSubServiceModel> salonSubServiceModel = realmController.getSalonSubServiceModel(mSalonAllServiceModel.getSubid());

        // SubServiceAdapter subServiceAdapter = new SubServiceAdapter(mContext,mSalonAllServiceModel.getSubServices());


        Log.e(TAG, "onBindViewHolder: sizeeeeeeeeeee " + salonSubServiceModel.size());
       // Log.e(TAG, "onBindViewHolder: sizeeeeeeeeeee " + mSalonAllServiceModel.getValueListData(mContext).size());
        SubServiceAdapter subServiceAdapter = new SubServiceAdapter(mContext, salonSubServiceModel);
        holder.binding.recyallservicetwo.setAdapter(subServiceAdapter);
        subServiceAdapter.notifyDataSetChanged();


        // String minutes = mSalonAllServiceModel.getSubServices().get(0).getDuration() + " min";
        // holder.binding.tvitemmin.setText(minutes);

/*
        for (int i = 0; i < salonSubServiceModel.size(); i++) {
            if (salonSubServiceModel.get(i).getDiscountPrice().equals("0")) {
                holder.binding.tvitemprice.setText(AppConstants.EURO + salonSubServiceModel.get(i).getPrice());
            } else {
                List<Integer> priceList = new ArrayList<>();
                List<Integer> disCountMaxList = new ArrayList<>();
                holder.binding.tvsaveupto.setVisibility(View.VISIBLE);
                priceList.add(Integer.valueOf(salonSubServiceModel.get(i).getDiscountPrice()));
                disCountMaxList.add(Integer.valueOf(discount_per(salonSubServiceModel.get(i).getPrice(), salonSubServiceModel.get(i).getDiscountPrice())));
                Log.e(TAG, "onBindViewHolder: elseeeeeeeee  " + discount_per(salonSubServiceModel.get(i).getPrice(), salonSubServiceModel.get(i).getDiscountPrice()));
                Log.e(TAG, "onBindViewHolder: elseeeeeeeee max " + Collections.max(disCountMaxList));
                holder.binding.tvitemprice.setText(mContext.getResources().getString(R.string.from) + " " + AppConstants.EURO + Collections.min(priceList));
                holder.binding.tvsaveupto.setText(mContext.getResources().getString(R.string.save_up_to) + " " + Collections.max(disCountMaxList) + "%");

            }
        }
*/

        for (int i = 0; i < salonSubServiceModel.size(); i++) {
            if (salonSubServiceModel != null) {
                if (salonSubServiceModel.get(i).getInCart().equals("1")) {
                    holder.binding.layoutsecond.setVisibility(View.VISIBLE);
                    holder.binding.ivDropdown.setRotation(0);
                    holder.binding.tvSelected.setVisibility(View.VISIBLE);
                    List<SalonSubServiceModel> selectedList = realmController.getSelectedInCartModel(salonSubServiceModel.get(i).getSubcatid());
                    //Log.e("Kamalllll ", "onBindViewHolder: iffff size = " +selectedList.size()+ "  idddddd  "+ salonSubServiceModel.get(i).getSubcatid());
                    holder.binding.tvSelected.setText(selectedList.size() + " ausgewählt");
                    holder.binding.llpriceshowhide.setVisibility(View.GONE);
                    return;
                } else {
                    //Log.e(TAG, "onBindViewHolder: elseeeeeee ");
                    holder.binding.layoutsecond.setVisibility(View.GONE);
                    holder.binding.ivDropdown.setRotation(270);
                    holder.binding.tvSelected.setVisibility(View.GONE);
                    holder.binding.llpriceshowhide.setVisibility(View.VISIBLE);

                }
            }
        }


    }

    private String returnServiceIds(List<SalonSubServiceModel> sercviceList) {
        // Log.e(TAG, "returnServiceIds: " + sercviceList.toString());
        String ids = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (sercviceList.size() > 0) {
            for (int i = 0; i < sercviceList.size(); i++) {
                stringBuilder.append(sercviceList.get(i).getId() + ",");
            }
            ids = stringBuilder.toString().substring(0, stringBuilder.length() - ",".length());
            return ids;
        }
        return ids;
    }

    @Override
    public int getItemCount() {
        //return  2;
        return mSalonAllServiceModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<SalonAllServiceModel> getSalonAllServiceModel() {
        return mSalonAllServiceModelList;
    }

    public void addChatMassgeModel(SalonAllServiceModel mSalonAllServiceModel) {
        try {
            this.mSalonAllServiceModelList.add(mSalonAllServiceModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSalonAllServiceModelList(List<SalonAllServiceModel> mSalonAllServiceModelList) {
        this.mSalonAllServiceModelList = mSalonAllServiceModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SalonallserviceCellBinding binding;

        public ViewHolder(final View view, final SalonallserviceCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final SalonAllServiceModel mSalonAllServiceModel) {
            this.binding.setSalonAllServiceModel(mSalonAllServiceModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mSalonAllServiceModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mSalonAllServiceModelList.size());
    }

    private void dialogShowDetails(Data data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        DialogShowDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_show_details, null, false);
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        //binding.nesstedddd.fullScroll(View.FOCUS_UP);
        if (data.getReviews() != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            binding.recysdfd.setLayoutManager(linearLayoutManager);
            binding.recysdfd.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            ReviewdialogAdapter reviewdialogAdapter = new ReviewdialogAdapter(mContext, data.getReviews());
            binding.recysdfd.setAdapter(reviewdialogAdapter);
            reviewdialogAdapter.notifyDataSetChanged();

            binding.btnshowmore.setOnClickListener(view -> {
                // Log.e(TAG, "dialogShowDetails: clickeeeeeeeeeeeeeee ");
                // binding.nesstedddd.fullScroll(View.FOCUS_DOWN);
                // int pp = data.getReviews().size()-1;
                // binding.recysdfd.scrollToPosition(0);
                scrollToView(binding.nesstedddd, binding.tvssssss);
            });

            binding.tvServiceName.setText(data.getCatName());
            if (data.getServiceDetail() != null && !data.getServiceDetail().equals("")) {
                binding.tvMessage.setVisibility(View.VISIBLE);
                binding.tvitemname.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvMessage.setText(Html.fromHtml(data.getServiceDetail(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvMessage.setText(Html.fromHtml(data.getServiceDetail()));
                }
                // binding.tvMessage.setText(Html.fromHtml(data.getServiceDetail()));
                //  Log.e(TAG, "dialogShowDetails: " + Html.fromHtml(data.getServiceDetail()));
            } else {
                binding.tvMessage.setVisibility(View.GONE);
                binding.tvitemname.setVisibility(View.GONE);
            }


            if (data.getReviews().size() == 0) {
                binding.btnshowmore.setVisibility(View.GONE);
                binding.tvssssss.setVisibility(View.GONE);
            } else {
                binding.btnshowmore.setVisibility(View.VISIBLE);
                binding.tvssssss.setVisibility(View.VISIBLE);
                binding.btnshowmore.setText(mContext.getResources().getString(R.string.show) + " " + data.getReviews().size() + " " + mContext.getResources().getString(R.string.show_reviews));
            }
        }

       /* binding.btnshowmore.setOnClickListener(view -> {
            if (binding.recysdfd.getVisibility() == View.VISIBLE){
                binding.recysdfd.setVisibility(View.GONE);
            }else{
                binding.recysdfd.setVisibility(View.VISIBLE);
            }
        });*/


        if (data.getAvgrateCount() != null) {
            float daafa = Float.parseFloat(data.getAvgrateCount().getAvgrage());
            binding.ratingbar.setRating(daafa);
            binding.tvreviewtotal.setText(data.getAvgrateCount().getTotalcount() + " Bewertungen");
            float myra = Float.parseFloat(data.getAvgrateCount().getAvgrage());
            if (myra < 5) {
                binding.tvRating.setText(Utility.round_off(myra + "").substring(0, 3));
            } else {
                String dfdfd = String.valueOf(myra);
                binding.tvRating.setText(dfdfd.substring(0, 1));

            }
        }
        dialog.show();

        binding.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        binding.ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }



    private void dialogShowDiscount(String discount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        DialogDiscountPriceBindingImpl binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.dialog_discount_price, null, false);
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        //binding.nesstedddd.fullScroll(View.FOCUS_UP);

        // String Business=salonDetailModel.getBusinessName();
       // Log.e("Business Name", controller.detailModel.getBusinessName());
        binding.tvServiceName.setText("Dieser Salon bietet für diesen Service Rabattzeiten an. Dies bedeutet, dass du auf diese Behandlung an bestimmten Tagen, oder zu bestimmten Zeiten einen Rabatt von bis zu"+discount+" bekommen kannst. Um zu sehen, zu welchen Zeiten der Rabatt verfügbar ist durchsuche einfach den Kaldender von "+ controller.detailModel.getBusinessName()+".");
        dialog.show();

        binding.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        binding.ivclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void scrollToView(final NestedScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    private void getratingdetails(String idd, String mainid) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(AppConstants.DEVICE_ID, Utility.getDeviceId(mContext));
        hashMap.put(AppConstants.DEVICE_TYPE, AppConstants.DEVICE_TYPE_VALUE);
        hashMap.put(AppConstants.API_KEY, AppConstants.API_KEY_VALUE);
        hashMap.put(AppConstants.ACCECC_TOKEN, Session.getAccessToken(mContext));
        hashMap.put(AppConstants.UID, Session.getUid(mContext));
        hashMap.put("id", idd);
        hashMap.put("allid", mainid);

        Log.e(TAG, "getratingdetails: " + hashMap);

        RetrofitServices.urlServi.getsalon_servicedetail(hashMap).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String rees = response.body().string().trim();
                    RatingREviewDataModel ratingREviewDataModel = gson.fromJson(rees, RatingREviewDataModel.class);
                    if (ratingREviewDataModel.getStatus() == 1) {
                        dialogShowDetails(ratingREviewDataModel.getData());
                    }
                    //  Log.e(TAG, "onResponse:=======" + rees);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public static void expand(final View v, int position, Context context) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration(250);
        v.startAnimation(a);

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(AppConstants.BROADCAST_KEY);
                intent.putExtra(AppConstants.MAG1, "abc");
                intent.putExtra(AppConstants.MAG2, position + "");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        // Log.e(TAG, "collapse: --- " + (int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(250);
        v.startAnimation(a);
    }

    private String discount_per(String price, String disprice) {
        Float intprice = Float.valueOf(price);
        Float intdiscprcie = Float.valueOf(disprice);
        Float per = ((intprice - intdiscprcie) * 100 / intprice);
        int result2 = Math.round(per);
        String disc = "0";
        return disc = String.valueOf(result2);
    }

}