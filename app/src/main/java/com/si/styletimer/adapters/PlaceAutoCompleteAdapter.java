package com.si.styletimer.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.si.styletimer.R;
import com.si.styletimer.models.PlaceAutoCompleteModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlaceAutoCompleteAdapter extends ArrayAdapter<PlaceAutoCompleteModel> implements Filterable {

    public static final String TAG = "CustomAutoCompAdapter";
    private List<PlaceAutoCompleteModel> dataList;
    private Context mContext;
    private PlacesClient placesClient;

    public PlaceAutoCompleteAdapter(Context context) {
        super(context, android.R.layout.simple_dropdown_item_1line, new ArrayList<PlaceAutoCompleteModel>());
        mContext = context;
        placesClient = Places.createClient(context);
        dataList = new ArrayList<PlaceAutoCompleteModel>();

    }

    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }


    }

    @Override
    public PlaceAutoCompleteModel getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            TextView textView = (TextView) super.getView(position, view, parent);
            textView.setTextColor(mContext.getResources().getColor(R.color.font_black));
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
      return filter;
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    private ArrayList<PlaceAutoCompleteModel> getPredictionstwo(CharSequence constraint) {
        final ArrayList<PlaceAutoCompleteModel> resultList = new ArrayList<>();

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        //DE|AT|CH
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                // .setLocationBias(bounds)
                .setCountry("DE")
                // .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        if (autocompletePredictions.isSuccessful()) {
            FindAutocompletePredictionsResponse findAutocompletePredictionsResponse = autocompletePredictions.getResult();
            if (findAutocompletePredictionsResponse != null)
                for (com.google.android.libraries.places.api.model.AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                    Log.i(TAG, prediction.getPlaceId());
                    Log.i(TAG, prediction.getPrimaryText(null).toString());
                    resultList.add(new PlaceAutoCompleteModel(prediction.getPlaceId(), prediction.getFullText(null).toString()));
                }

            dataList = resultList;

            return resultList;
        } else {
            return resultList;
        }

    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final String searchStrLowerCase = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            // We need a separate list to store the results, since
            // this is run asynchronously.
            ArrayList<PlaceAutoCompleteModel> filterData = new ArrayList<>();

            // Skip the autocomplete query if no constraints are given.
            if (constraint != null) {
                // Query the autocomplete API for the (constraint) search string.
                filterData = getPredictionstwo(searchStrLowerCase);
            }

            results.values = filterData;
            if (filterData != null) {
                results.count = filterData.size();
            } else {
                results.count = 0;
            }

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                setNotifyOnChange(false);
                // The API returned at least one result, update the data.
                clear();
                addAll((ArrayList<PlaceAutoCompleteModel>) results.values);
                notifyDataSetChanged();
              //  notifyDataSetChanged((ArrayList<PlaceAutoCompleteModel>) results.values);
            } else {
                // The API did not return any results, invalidate the data set.
                notifyDataSetInvalidated();
            }
        }
    };




    public void notifyDataSetChanged(List<PlaceAutoCompleteModel> items) {
      //  this.dataList = items;
        this.dataList.clear();;
        this.dataList.addAll(items);
        super.notifyDataSetChanged();
    }

}


               /* if (constraint != null) {
                    FilterResults results = new FilterResults();

                    // Query the autocomplete API for the entered constraint
                    List<PlaceAutoCompleteModel> autoCompleteModelList = getPredictionstwo(searchStrLowerCase);
//                    if (autoCompleteModelList != null) {
//                        // Results
//                        results.values = dataList;
//                        results.count = dataList.size();
//                        notifyDataSetChanged();
//                    }


                    dataList = getPredictionstwo(searchStrLowerCase);
                    Log.e(TAG, "performFiltering: **********************"+dataList.toString() );
                    if (dataList != null) {
                        // Results
//                        results.values = dataList;
//                        results.count = dataList.size();
//                        notifyDataSetChanged();
                    }
                }
                return results;*/
