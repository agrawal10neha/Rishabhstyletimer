package com.si.styletimer.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.si.styletimer.R;
import com.si.styletimer.app_constants.AppConstants;
import com.si.styletimer.databinding.ServiceGroupCellBinding;
import com.si.styletimer.databinding.ServiceItemCellBinding;
import com.si.styletimer.models.salonsdetails.MatchSubcatModel;
import com.si.styletimer.models.salonsdetails.ValueModel;

import java.util.List;

public class ServicesListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ServicesListAdapter";
    private final Context mContext;
    private List<MatchSubcatModel> matchSubcatModelList;

    public ServicesListAdapter(Context mContext, List<MatchSubcatModel> matchSubcatModelList) {
        this.mContext = mContext;
        this.matchSubcatModelList = matchSubcatModelList;
    }

    public List<MatchSubcatModel> getMatchSubcatModelList() {
        return matchSubcatModelList;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ServiceGroupCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.service_group_cell, parent, false);
        final MatchSubcatModel matchSubcatModel = this.matchSubcatModelList.get(groupPosition);
        binding.tvgroupname.setText(matchSubcatModel.getKey());
        //binding.tvgroupmin.setText(matchSubcatModel.getStartPrice());
        binding.tvgroupprice.setText(matchSubcatModel.getStartPrice());
        return binding.getRoot();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ServiceItemCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.service_item_cell, parent, false);
        final ValueModel valueModel = this.matchSubcatModelList.get(groupPosition).getValue().get(childPosition);
        binding.tvitemname.setText(valueModel.getName());
        binding.tvitemprice.setText(AppConstants.EURO+" "+valueModel.getDiscountPrice());
        binding.tvitemmin.setText(valueModel.getBufferTime()+" Min.");
        binding.tvitempricemain.setText(AppConstants.EURO+" "+valueModel.getPrice());
        binding.tvitempricemain.setPaintFlags(binding.tvitempricemain.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        return binding.getRoot();
    }


    @Override
    public int getGroupCount() {
        return matchSubcatModelList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (matchSubcatModelList.get(groupPosition).getValue() != null) {
            return this.matchSubcatModelList.get(groupPosition).getValue().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.matchSubcatModelList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.matchSubcatModelList.get(groupPosition).getValue().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setMatchSubcatModelList(List<MatchSubcatModel> matchSubcatModelList1) {
        this.matchSubcatModelList = matchSubcatModelList1;
        notifyDataSetChanged();
    }




}
