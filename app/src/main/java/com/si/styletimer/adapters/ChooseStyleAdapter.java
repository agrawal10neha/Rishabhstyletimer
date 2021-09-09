package com.si.styletimer.adapters;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.Session.Session;
import com.si.styletimer.databinding.ChoosestyleCellBinding;
import com.si.styletimer.models.ChooseStyleModel;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.DatabindingImageAdapter;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class ChooseStyleAdapter extends RecyclerView.Adapter<ChooseStyleAdapter.ViewHolder> {
    private static final String TAG = "ChooseStyleAdapter";
    private final Context mContext;
    private List<ChooseStyleModel> mChooseStyleModelList;
    private OnItemClickListener onItemClickListener;
    private String id;
    private String str_salon_id;
    private String type = "";
    private Boolean x = true;

    public int mSelectedItem = 0;

    public ChooseStyleAdapter(Context mContext, List<ChooseStyleModel> mChooseStyleModelList, String id, String str_salon_id) {
        this.mChooseStyleModelList = mChooseStyleModelList;
        this.id = id;
        this.str_salon_id = str_salon_id;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ChoosestyleCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.choosestyle_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChooseStyleModel mChooseStyleModel = this.mChooseStyleModelList.get(position);
        holder.bind(mChooseStyleModel);

        if (mSelectedItem == 0) {
            if (x) {
                if (mChooseStyleModelList.size() > 1) {
                    Session.settylername(mContext, "Any Employee");
                } else if (mChooseStyleModelList.size() == 1) {
                    Session.settylername(mContext, Utility.capitalize(mChooseStyleModelList.get(0).getFirstName()));
                }
                x = false;
            }
        }

        if (position == mChooseStyleModelList.size() - 1) {
            holder.binding.View.setVisibility(View.GONE);
        } else holder.binding.View.setVisibility(View.VISIBLE);

        if (getItemCount() > 1) {
            if (position == 0) {
                holder.binding.tvstylename.setText(mContext.getResources().getString(R.string.first_available_stylist));
                holder.binding.tvText.setVisibility(View.VISIBLE);
                holder.binding.tvText.setText(mContext.getResources().getString(R.string.see_all_available));
                holder.binding.rlstyler.setVisibility(View.GONE);
                holder.binding.llfirstavailable.setVisibility(View.VISIBLE);

            } else {
                holder.binding.rlstyler.setVisibility(View.VISIBLE);
                holder.binding.llfirstavailable.setVisibility(View.GONE);
                holder.binding.tvstylenametwo.setText(Utility.capitalize(mChooseStyleModel.getFirstName()) /*+" "+mChooseStyleModel.getLastName()*/);
                holder.binding.tvText.setVisibility(View.GONE);
            }
        } else {
            holder.binding.tvstylename.setText(Utility.capitalize(mChooseStyleModel.getFirstName()) /*+" "+mChooseStyleModel.getLastName()*/);
        }

        String strfname = mChooseStyleModel.getFirstName().trim();
        String strlname = mChooseStyleModel.getLastName().trim();
        holder.binding.tvfname.setText(Utility.capitalize(strfname.substring(0, 1)));
        //holder.binding.tvlanme.setText(Utility.capitalize(strlname.substring(0, 1)));

       // String url = RetrofitServices.UPLOADS + "employee/" + mChooseStyleModel.getId() + "/icon_" + mChooseStyleModel.getProfilePic();
        // Log.e(TAG, "onBindViewHolder:------ "+url );

        String someFilepath = RetrofitServices.UPLOADS + "employee/" + mChooseStyleModel.getId() + "/icon_" + mChooseStyleModel.getProfilePic();
        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
        String url;
        if (extension.equals(".webp"))
        {
            url = RetrofitServices.UPLOADS + "employee/" + mChooseStyleModel.getId() + "/icon_" + mChooseStyleModel.getProfilePic();
        }else {
            url = RetrofitServices.UPLOADS + "employee/" + mChooseStyleModel.getId() + "/icon_" + mChooseStyleModel.getProfilePic()+".webp";
        }
        DatabindingImageAdapter.loadImage(holder.binding.ivstylerimage, url);

        if (mChooseStyleModel.getProfilePic().equals("")) {
            holder.binding.rltxtname.setVisibility(View.VISIBLE);
            holder.binding.ivstylerimage.setVisibility(View.GONE);
        } else {
            holder.binding.rltxtname.setVisibility(View.GONE);
            holder.binding.ivstylerimage.setVisibility(View.VISIBLE);
        }

        if (position == mSelectedItem) {
            holder.binding.ivright.setVisibility(View.VISIBLE);
            holder.binding.rlpictext.setVisibility(View.GONE);
        } else {
            holder.binding.rlpictext.setVisibility(View.VISIBLE);
            holder.binding.ivright.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChooseStyleModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<ChooseStyleModel> getChooseStyleModel() {
        return mChooseStyleModelList;
    }

    public void addChatMassgeModel(ChooseStyleModel mChooseStyleModel) {
        try {
            this.mChooseStyleModelList.add(mChooseStyleModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setChooseStyleModelList(List<ChooseStyleModel> mChooseStyleModelList, String type) {
        this.mChooseStyleModelList = mChooseStyleModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ChoosestyleCellBinding binding;

        public ViewHolder(final View view, final ChoosestyleCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fname = getChooseStyleModel().get(getAdapterPosition()).getFirstName();
                    String lname = getChooseStyleModel().get(getAdapterPosition()).getLastName();
                    if (fname.equals("any")) {
                        Session.settylername(mContext, "Any Employee");
                    } else {
                        Session.settylername(mContext, fname);
                    }
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, mChooseStyleModelList.size());
                }
            };
            itemView.setOnClickListener(clickListener);

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
        public void bind(final ChooseStyleModel mChooseStyleModel) {
            this.binding.setChooseStyleModel(mChooseStyleModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public int getmSelectedItem() {
        return mSelectedItem;
    }

}