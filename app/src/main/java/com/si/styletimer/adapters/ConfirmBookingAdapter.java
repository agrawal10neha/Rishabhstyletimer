package com.si.styletimer.adapters;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.activity.SelectDateActivity;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ConfirmBookingCellBinding;
import com.si.styletimer.models.mybooking_list_model.confirm_booking_model.ConfirmModel;
import com.si.styletimer.utill.MarshmallowPermission;
import com.si.styletimer.utill.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ConfirmBookingAdapter extends RecyclerView.Adapter<ConfirmBookingAdapter.ViewHolder> {
    private static final String TAG = "ConfirmBookingAdapter";

    private final Context mContext;
    private List<ConfirmModel> mConfirmModelList=new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Dialog dialog;
    private MarshmallowPermission marshmallowPermission;

    public ConfirmBookingAdapter(Context mContext, List<ConfirmModel> mConfirmModelList) {
        this.mConfirmModelList = mConfirmModelList;
        this.mContext = mContext;
        marshmallowPermission = new MarshmallowPermission(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ConfirmBookingCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.confirm_booking_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //holder.binding.tvDate.setText(mConfirmModelList.get(position).getBookingTime());
        String a[] = mConfirmModelList.get(position).getBookingTime().split(" ");
        String b[] = a[1].split(":");
        holder.binding.tvDate.setText(Utility.app_format(a[0]) + " / " + b[0] + ":" + b[1] + " Uhr");
        if (mConfirmModelList.get(position).getPriceStartOption() != null && mConfirmModelList.get(position).getPriceStartOption().equals("ab")) {
            holder.binding.tvPrice.setText("ab "+ Utility.getPriceReplaceDotWithComma(mConfirmModelList.get(position).getTotalPrice())+"€");
        } else {
         holder.binding.tvPrice.setText(Utility.getPriceReplaceDotWithComma(mConfirmModelList.get(position).getTotalPrice()) + "€");
        }

        holder.binding.rv.setLayoutManager(new LinearLayoutManager(mContext));
       // holder.binding.rv.setAdapter(new ConfirmBookingServiceAdapter(mContext, mConfirmModelList.get(position).getAllCategory()));
        holder.binding.rv.setAdapter(new ConfirmBookingServiceAdapter(mContext, mConfirmModelList.get(position).getAllCategory(),holder.binding.rv,mConfirmModelList.get(position).getId()));


  /*      if (mConfirmModelList.get(position).getServiceName().equals("")) {
            holder.binding.tvName.setText(mConfirmModelList.get(position).getCatName());
        } else {
            holder.binding.tvName.setText(mConfirmModelList.get(position).getCatName() + " - " + mConfirmModelList.get(position).getServiceName());
        }
*/
        holder.binding.tvSalonName.setText(mConfirmModelList.get(position).getBusinessName());
        holder.binding.tvAddress.setText(mConfirmModelList.get(position).getAddress() + "\n"
                + mConfirmModelList.get(position).getZip() + " " + mConfirmModelList.get(position).getCity());


        if (mConfirmModelList.get(position).getStatus().equals("confirmed")) {
            holder.binding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.new_orange));
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.confirmed));
        } else if (mConfirmModelList.get(position).getStatus().equals("cancelled")) {
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.cancelled));
            holder.binding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.new_red));
        } else if (mConfirmModelList.get(position).getStatus().equals("completed")) {
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.completed));
            holder.binding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.new_gren));
        } else if (mConfirmModelList.get(position).getStatus().equals("no show")) {
            holder.binding.tvStatus.setText(mContext.getResources().getString(R.string.no_show));
        } else {
            holder.binding.tvStatus.setText(mConfirmModelList.get(position).getStatus().toUpperCase());
        }


        //holder.binding.tvStatus.setText(mConfirmModelList.get(position).getStatus().toUpperCase());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date());

        Log.e("DATE IS ", currentTime);

        if (Utility.comparedates(String.valueOf(formattedDate), a[0]).equals("after")) {
            holder.binding.tvCancel.setVisibility(View.GONE);
            holder.binding.tvCancelDray.setVisibility(View.VISIBLE);
        } else if (Utility.comparedates(String.valueOf(formattedDate), a[0]).equals("equal")) {
            Log.e("DATE IS ", Utility.checktime(currentTime, b[0] + ":" + b[1]));
            if (Utility.checktime(currentTime, b[0] + ":" + b[1]).equals("after")) {
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvCancelDray.setVisibility(View.VISIBLE);
            } else {
                holder.binding.tvCancel.setVisibility(View.VISIBLE);
                holder.binding.tvCancelDray.setVisibility(View.GONE);
            }
        } else {
            holder.binding.tvCancel.setVisibility(View.VISIBLE);
            holder.binding.tvCancelDray.setVisibility(View.GONE);
        }


        holder.binding.btnReschedule.setOnClickListener(v -> {
            String datee = Utility.change_format(mConfirmModelList.get(position).getBookingTime(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
            String timeee = Utility.change_format(mConfirmModelList.get(position).getBookingTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm");
            Session.setSalon_id(mContext, mConfirmModelList.get(position).getMerchantId());
            Intent intent = new Intent(mContext, SelectDateActivity.class);
            intent.putExtra(AppConstants.SALON_ID, mConfirmModelList.get(position).getMerchantId());
            intent.putExtra(AppConstants.FLAG, mConfirmModelList.get(position).getmEmployeeId());
            //intent.putExtra(AppConstants.DATE,Utility.getCurrentDateYY());
            intent.putExtra(AppConstants.DATE, datee);
            intent.putExtra(AppConstants.TIME, timeee);
            intent.putExtra(AppConstants.FLAGTWO, AppConstants.RESCHEDULE);
            intent.putExtra(AppConstants.BOOK_ID, mConfirmModelList.get(position).getId());
            intent.putExtra(AppConstants.RES_STATUS, mConfirmModelList.get(position).getResheduleStatus());
            mContext.startActivity(intent);
        });

        Log.e(TAG, "onBindViewHolder:===> " + getMilisecFromHours(mConfirmModelList.get(position).getHrBeforeCancel()));

        if (mConfirmModelList.get(position).getCancelBookingAllow().equals("yes")) {
            long valueforCancel = getMilisecFromHours(mConfirmModelList.get(position).getHrBeforeCancel());
            if (Utility.timeleft(mConfirmModelList.get(position).getBookingTime()) > valueforCancel) {
                holder.binding.tvCancel.setVisibility(View.VISIBLE);
                holder.binding.tvCancelDray.setVisibility(View.GONE);
            } else {
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvCancelDray.setVisibility(View.VISIBLE);
            }
            // check for reshcedule button
            if (Utility.timeleft(mConfirmModelList.get(position).getBookingTime()) > valueforCancel) {
                String status = mConfirmModelList.get(position).getStatus();
                String reschedule = mConfirmModelList.get(position).getResheduleStatus();
                if (status.equals("confirmed")) {
                    if (reschedule.equals("0")) {
                        holder.binding.btnReschedule.setVisibility(View.VISIBLE);
                        holder.binding.btnRescheduleGray.setVisibility(View.GONE);
                    } else {
                        holder.binding.btnReschedule.setVisibility(View.GONE);
                        holder.binding.btnRescheduleGray.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.binding.btnReschedule.setVisibility(View.GONE);
                holder.binding.btnRescheduleGray.setVisibility(View.VISIBLE);
            }

        } else {
            holder.binding.tvCancel.setVisibility(View.GONE);
            holder.binding.tvCancelDray.setVisibility(View.VISIBLE);
        }

        holder.binding.btnRescheduleGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellation_policy_popup("reschedule", mConfirmModelList.get(position).getHrBeforeCancel(), mConfirmModelList.get(position).getMobile(), mConfirmModelList.get(position).getBusinessName(),mConfirmModelList.get(position).getCancelBookingAllow());
            }
        });

        holder.binding.tvCancelDray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellation_policy_popup("cancel", mConfirmModelList.get(position).getHrBeforeCancel(), mConfirmModelList.get(position).getMobile(), mConfirmModelList.get(position).getBusinessName(),mConfirmModelList.get(position).getCancelBookingAllow());
            }
        });

    }

    private void cancellation_policy_popup(String x, String hr_before_cancel, String mobile, String salonname,String cancelBookingAllow) {
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.cancellation_policy_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);

        TextView head = dialog.findViewById(R.id.head);
        TextView sHead = dialog.findViewById(R.id.sHead);
        if (cancelBookingAllow.equals("no"))
        {
            head.setText("Deine Buchung bei " + salonname + " kann leider nicht durch styletimer verlegt oder storniert werden, da diese Option vom Salon deaktiviert wurde.");
            sHead.setText("Bitte kontaktiere den Salon direkt unter: " + mobile);
        }else {
            head.setText("Deine Buchung bei " + salonname + " kann leider nicht mehr durch styletimer verlegt oder storniert werden, da dein Termin in weniger als "+ hr_before_cancel +" Stunden beginnt.");
            sHead.setText("Bitte kontaktiere den Salon direkt unter: " + mobile);
        }

        yes.setText("Salon anrufen");
        no.setText("Schliessen");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                phoneCall(mobile);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void phoneCall(String mobile) {
        if (marshmallowPermission.check__call_phone_permission()) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            String a = "tel:" + mobile;
            callIntent.setData(Uri.parse(a));
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mContext.startActivity(callIntent);
        } else {
            //phoneCall();
        }
    }



    private long getMilisecFromHours(String time) {
        int acttime = Integer.parseInt(time);
        return (acttime * 3600000);
    }

    @Override
    public int getItemCount() {
        return mConfirmModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<ConfirmModel> getConfirmModel() {
        return mConfirmModelList;
    }

    public void addChatMassgeModel(ConfirmModel mConfirmModel) {
        try {
            this.mConfirmModelList.add(mConfirmModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConfirmModelList(List<ConfirmModel> mConfirmModelList) {
        this.mConfirmModelList = mConfirmModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ConfirmBookingCellBinding binding;

        public ViewHolder(final View view, final ConfirmBookingCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
            binding.tvCancel.setOnClickListener(this);
            binding.llDetail.setOnClickListener(this);
            //  binding.btnCancel.setOnClickListener(this);
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
        public void bind(final ConfirmModel mConfirmModel) {
            this.binding.setConfirmModel(mConfirmModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mConfirmModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mConfirmModelList.size());
    }
}