package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.databinding.EmployeeListCellBinding;
import com.si.styletimer.models.EmployeeListModel;
import com.si.styletimer.retrofit.Environment;
import com.si.styletimer.retrofit.RetrofitServices;
import com.si.styletimer.utill.DatabindingImageAdapter;
import com.si.styletimer.utill.Utility;

import java.util.List;

public class EmployeeListModelAdapter extends RecyclerView.Adapter<EmployeeListModelAdapter.ViewHolder> {


    private final Context mContext;
    private List<EmployeeListModel> mEmployeeListModelList;
    private OnItemClickListener onItemClickListener;
    private static final String TAG = "EmployeeListModelAdapte";

    public EmployeeListModelAdapter(Context mContext, List<EmployeeListModel> mEmployeeListModelList) {
        this.mEmployeeListModelList = mEmployeeListModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final EmployeeListCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.employee_list_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EmployeeListModel mEmployeeListModel = this.mEmployeeListModelList.get(position);
        holder.bind(mEmployeeListModel);

        holder.binding.tvName.setText(Utility.capitalize(mEmployeeListModel.getFirstName()));
      //  Log.e(TAG, "onBindViewHolder: url "+ Environment.getBaseUrl() +"assets/uploads/employee/" + mEmployeeListModel.getId() + "/" + mEmployeeListModel.getProfilePic());

        String someFilepath = Environment.getBaseUrl()+"assets/uploads/employee/" + mEmployeeListModel.getId() + "/" + mEmployeeListModel.getProfilePic();
        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
        String url;
        if (extension.equals(".webp"))
        {
            url = Environment.getBaseUrl()+"assets/uploads/employee/" + mEmployeeListModel.getId() + "/" + mEmployeeListModel.getProfilePic();
        }else {
            url = Environment.getBaseUrl()+"assets/uploads/employee/" + mEmployeeListModel.getId() + "/" + mEmployeeListModel.getProfilePic()+".webp";
        }
        DatabindingImageAdapter.loadImage(holder.binding.iv, url);
    }

    @Override
    public int getItemCount() {
        return mEmployeeListModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<EmployeeListModel> getEmployeeListModel() {
        return mEmployeeListModelList;
    }

    public void addChatMassgeModel(EmployeeListModel mEmployeeListModel) {
        try {
            this.mEmployeeListModelList.add(mEmployeeListModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEmployeeListModelList(List<EmployeeListModel> mEmployeeListModelList) {
        this.mEmployeeListModelList = mEmployeeListModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final EmployeeListCellBinding binding;

        public ViewHolder(final View view, final EmployeeListCellBinding binding) {
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
        public void bind(final EmployeeListModel mEmployeeListModel) {
            this.binding.setEmployeeListModel(mEmployeeListModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private void deleteitem(int position) {
        mEmployeeListModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mEmployeeListModelList.size());
    }
}