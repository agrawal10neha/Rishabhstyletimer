package com.si.styletimer.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Point;
import androidx.annotation.UiThread;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.si.styletimer.R;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.WeekcalendarCellBinding;
import com.si.styletimer.listener.OnSevenPositionListener;
import com.si.styletimer.models.WeekCalendarModel;
import com.si.styletimer.utill.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeekCalendarAdapter extends RecyclerView.Adapter<WeekCalendarAdapter.ViewHolder> {
    private static final String TAG = "WeekCalendarAdapter";
View view;
    private final Context mContext;
    private List<WeekCalendarModel> mWeekCalendarModelList;
    private OnItemClickListener onItemClickListener;
    public int mSelectedItem = 0;
    private String montChane = Utility.getCurrentDateMM();
    private OnSevenPositionListener onSevenPositionListener;
    public WeekCalendarAdapter(Context mContext, List<WeekCalendarModel> mWeekCalendarModelList) {
        this.mWeekCalendarModelList = mWeekCalendarModelList;
        this.mContext = mContext;
    }
    int centerOfScreen;
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final WeekcalendarCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.weekcalendar_cell, parent, false);
       binding.getRoot().getLayoutParams().width = (int) (getScreenWidth()/7); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS

        return new ViewHolder(binding.getRoot(), binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final WeekCalendarModel mWeekCalendarModel = this.mWeekCalendarModelList.get(position);
        holder.bind(mWeekCalendarModel);

        String dayname =  Utility.change_format(mWeekCalendarModel.getmDate(), "yyyy-MM-dd", "EEE");

        holder.binding.tvDay.setText(dayname.replace(".",""));
        Log.e(TAG, "onBindViewHolder: " +dayname);

        String month = Utility.change_format(mWeekCalendarModel.getmDate(), "yyyy-MM-dd", "MM");
        if (!month.equals(montChane)) {
            montChane = Utility.change_format(mWeekCalendarModel.getmDate(), "yyyy-MM-dd", "MM");
            String mmmm = Utility.change_format(mWeekCalendarModel.getmDate(), "yyyy-MM-dd", "MMMM");
            Intent intent = new Intent(AppConstants.BROADCAST_KEY);
            intent.putExtra(AppConstants.MAG1, mmmm);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }

        if (getCurrentPos() == position)
        {
           // holder.binding.llMain.scrollTo(position,3);
            holder.binding.llDate.setBackground(mContext.getResources().getDrawable(R.drawable.datedrawable));
            holder.binding.tvDate.setTextColor(mContext.getResources().getColor(R.color.white));

        } else {
            holder.binding.llDate.setBackground(null);
            holder.binding.tvDate.setTextColor(mContext.getResources().getColor(R.color.font_grey));
        }

        if (mWeekCalendarModel.isClosed()) {
            if (getCurrentPos() == position) {
              //  holder.binding.llMain.scrollTo(position,3);
                holder.binding.llDate.setBackground(mContext.getResources().getDrawable(R.drawable.datedrawable));
                holder.binding.tvDate.setTextColor(mContext.getResources().getColor(R.color.white));

            } else {
                holder.binding.tvDate.setTextColor(mContext.getResources().getColor(R.color.disableDay));
                holder.binding.tvDay.setTextColor(mContext.getResources().getColor(R.color.disableDay));
            }
            // holder.binding.llMain.setBackgroundColor(mContext.getResources().getColor(R.color.grayLight));
        } else {
            // holder.binding.llMain.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            if (getCurrentPos() == position) {
              //  holder.binding.llMain.scrollTo(position,3);
                holder.binding.llDate.setBackground(mContext.getResources().getDrawable(R.drawable.datedrawable));
                holder.binding.tvDate.setTextColor(mContext.getResources().getColor(R.color.white));

            } else {
                holder.binding.tvDate.setTextColor(mContext.getResources().getColor(R.color.font_grey));
                holder.binding.tvDay.setTextColor(mContext.getResources().getColor(R.color.font_grey));
            }
        }

    }

    @Override
    public int getItemCount() {
        //    Log.e(TAG, "getItemCount: "+mWeekCalendarModelList.size() );
        return mWeekCalendarModelList.size();
    }

    public void setOnSevenPositionListener(OnSevenPositionListener onSevenPositionListener) {
        this.onSevenPositionListener = onSevenPositionListener;
    }

    public int getCurrentPos() {
        return mSelectedItem;
    }

    public void setCurrentPos(int currentPos) {
        this.mSelectedItem = currentPos;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<WeekCalendarModel> getWeekCalendarModel() {
        return mWeekCalendarModelList;
    }

    public void addChatMassgeModel(WeekCalendarModel mWeekCalendarModel) {
        try {
            this.mWeekCalendarModelList.add(mWeekCalendarModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWeekCalendarModelList(List<WeekCalendarModel> mWeekCalendarModelList) {
        this.mWeekCalendarModelList = mWeekCalendarModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final WeekcalendarCellBinding binding;

        public ViewHolder(final View view, final WeekcalendarCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);

           /* View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, mWeekCalendarModelList.size());
                }
            };
            itemView.setOnClickListener(clickListener);*/
        }

        @Override
        public void onClick(View v) {
            try {

                String mydate =  getWeekCalendarModel().get(getAdapterPosition()).getDates();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String datestr = df.format(Date.parse(mydate));
                String value = (Utility.comparedates(Utility.getCurrentDateYY(),datestr));
                if (value.equals(Utility.DATEEQUAL) || value.equals(Utility.DATEBEFORE) ){
                    if (onItemClickListener != null) {
                        setCurrentPos(getAdapterPosition());
                        mSelectedItem = getAdapterPosition();

                        notifyItemRangeChanged(0, mWeekCalendarModelList.size());
                        onItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }else {
                    Log.e(TAG, "onClick:****************************** " );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final WeekCalendarModel mWeekCalendarModel) {
            this.binding.setWeekCalendarModel(mWeekCalendarModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }



    private void deleteitem(int position) {
        mWeekCalendarModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mWeekCalendarModelList.size());
    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }
}