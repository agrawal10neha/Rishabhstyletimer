package com.si.styletimer.adapters;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.si.styletimer.R;
import com.si.styletimer.activity.FilterReviewActivity;

import java.util.ArrayList;

public class ReviewFilterAdapter extends RecyclerView.Adapter<ReviewFilterAdapter.ViewHolder> {

    public Context context;
    ArrayList<String> name;
    ArrayList<String> id;
    ArrayList<String> FinalId;
    CheckBox checkBox;

    public ReviewFilterAdapter(ArrayList<String> name, ArrayList<String> id, ArrayList<String> FinalId, Context context,CheckBox checkBox) {
        this.name = name;
        this.id=id;
        this.FinalId=FinalId;
        this.context=context;
        this.checkBox=checkBox;
    }

    @NonNull
    @Override
    public ReviewFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_filter_cell, parent, false);
        return new ReviewFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewFilterAdapter.ViewHolder viewHolder, int i) {


        viewHolder.tvName.setText(name.get(i));

        if(FinalId.contains(id.get(i))){
            viewHolder.cb.setChecked(true);
        }else {
            viewHolder.cb.setChecked(false);
        }

        viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!FinalId.contains(id.get(i))){
                        FinalId.add(id.get(i));
                      //  notifydataset();
                    }
                }else if(!isChecked){
                    if (FinalId.contains(id.get(i))){
                        int j = FinalId.indexOf(id.get(i));
                        FinalId.remove(j);
                      //  notifydataset();
                    }
                }

                if(FinalId.size()==id.size()){
                    FilterReviewActivity.click=false;
                    checkBox.setChecked(true);
                }else {
                    checkBox.setChecked(false);
                }

            }
        });


    }


    private void notifydataset(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 1000);

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            cb=itemView.findViewById(R.id.cb);
        }
    }
}
