package com.si.styletimer.adapters;

import android.content.Context;


import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.si.styletimer.R;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.BookdetailreceiptCellBinding;
import com.si.styletimer.models.receiptdetail.BookDetail;
import com.si.styletimer.utill.Utility;

import java.util.List;

/**
 * Developed by : Kamal Patidar
 * Author : Shanti Infotech Pvt. Ltd.
 * Email : kamal.shantiinfotech@gmail.com
 * Website : https://shantiinfotech.com/
 * Created on : 24,April,2020
 */
public class BookDetailReceiptAdapter extends RecyclerView.Adapter<BookDetailReceiptAdapter.ViewHolder> {
    private static final String TAG = "BookDetailReceiptAdapte";
    private final Context mContext;
    private List<BookDetail> mBookDetailList;
    private OnItemClickListener onItemClickListener;
    private int currentPos = -1;
    private String emname = "";

    public BookDetailReceiptAdapter(Context mContext, List<BookDetail> mBookDetailList,String empname) {
        this.mBookDetailList = mBookDetailList;
        this.mContext = mContext;
        this.emname = empname;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final BookdetailreceiptCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.bookdetailreceipt_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BookDetail mBookDetail = this.mBookDetailList.get(position);
        holder.bind(mBookDetail);
        if (position == 0)
        {
            holder.binding.tvItem.setText("Position 1:");
        }
        else {
            int cc = position+1;
            holder.binding.tvItem.setText("Position "+cc+":");
        }
        //holder.binding.tvItem.setText("Position:");
        holder.binding.tvName.setText(mBookDetail.getServiceName());
        holder.binding.tvPrice.setText(Utility.getPriceReplaceDotWithComma(mBookDetail.getPrice())+" " +AppConstants.EURO);
        if (mBookDetail.getTax() != null && !mBookDetail.getTax().equals("")) {
            String tax[] = mBookDetail.getTax().split(":");
            holder.binding.tvTakes.setText("inkl. "+tax[0].replace(" ", " ") + " " + tax[1]);
        }
        String a2[] = mBookDetail.getSetuptimeStart().split(" ");
        String b2[] = a2[1].split(":");
        //holder.binding.tvTime.setText(Utility.formatDate(a2[0])+", "+b2[0]+":"+b2[1]+"Uhr with "+emname);
        holder.binding.tvTime.setText("bei "+emname);

        if (mBookDetail.getDiscountPrice() !=null && mBookDetail.getDiscountPrice().equals("0"))
        {
            holder.binding.tvDiscount.setVisibility(View.GONE);
            holder.binding.tvInclude.setVisibility(View.GONE);
        }else {
            holder.binding.tvDiscount.setVisibility(View.GONE);
            holder.binding.tvInclude.setVisibility(View.VISIBLE);
            holder.binding.tvDiscount.setText("Special discount "+Utility.getPriceReplaceDotWithComma(mBookDetail.getDiscountPrice())+" " +AppConstants.EURO+"off");
            holder.binding.tvInclude.setPaintFlags(holder.binding.tvInclude.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           // int tot = Integer.parseInt(mBookDetail.getDiscountPrice())+Integer.parseInt(mBookDetail.getPrice());
            double tot = Double.parseDouble(mBookDetail.getDiscountPrice())+Double.parseDouble(mBookDetail.getPrice());
            if (tot>0) {
                String dd = String.valueOf(tot);
                holder.binding.tvInclude.setText(Utility.getPriceReplaceDotWithComma(dd) +" " + AppConstants.EURO);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mBookDetailList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<BookDetail> getBookDetail() {
        return mBookDetailList;
    }

    public void addChatMassgeModel(BookDetail mBookDetail) {
        try {
            this.mBookDetailList.add(mBookDetail);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBookDetailList(List<BookDetail> mBookDetailList) {
        this.mBookDetailList = mBookDetailList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final BookdetailreceiptCellBinding binding;

        public ViewHolder(final View view, final BookdetailreceiptCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (onItemClickListener != null) {
                    setCurrentPos(getAdapterPosition());
                    notifyItemRangeChanged(0, mBookDetailList.size());
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final BookDetail mBookDetail) {
            // this.binding.setBookDetailReceipt(mBookDetail);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void deleteitem(int position) {
        mBookDetailList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mBookDetailList.size());
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public void clearAdapter() {
        int size = mBookDetailList.size();
        mBookDetailList.clear();
        notifyItemRangeRemoved(0, size);
    }

}