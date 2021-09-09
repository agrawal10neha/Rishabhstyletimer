package com.si.styletimer.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.si.styletimer.R;
import com.si.styletimer.adapters.PlaceAutoCompleteAdapter;
import com.si.styletimer.adapters.PlaceAutoCompletetwoAdapter;
import com.si.styletimer.databinding.ActivityLocationSearchBinding;
import com.si.styletimer.models.PlaceAutoCompleteModel;
import com.si.styletimer.retrofit.APIClient;
import com.si.styletimer.retrofit.ApiInterface;
import com.si.styletimer.utill.MarshmallowPermission;
import com.si.styletimer.utill.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLocationSearchBinding binding;
    private Context context;
    private static final String TAG = "LocationSearchActivity";

    private static final int AUTOCOMPLETE_REQUEST_CODE = 23487;
    private PlacesClient placesClient;
    private List<Place.Field> fields;
    private String keyword = "";
    private String address = "", Add = "";
    private String strlatitude = "";
    private String strlongitude = "";
    private String lat = "", lang = "";
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    private MarshmallowPermission marshmallowPermission;
    private List<PlaceAutoCompleteModel> autoCompleteModelList;
    private PlaceAutoCompletetwoAdapter placeAutoCompletetwoAdapter;
    private boolean writing = false, selected = false;
    ApiInterface apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_search);
        context = this;
        marshmallowPermission = new MarshmallowPermission(context);
        apiService = APIClient.getClient().create(ApiInterface.class);

        inIt();

        if (!getIntent().getStringExtra("address").equals("set Location")) {
            Log.e(TAG, "onCreate:==== " + getIntent().getStringExtra("address"));
            selected = true;
            String currAdd=getIntent().getStringExtra("address");
            String lastWord = currAdd.substring(currAdd.lastIndexOf(" ")+1);
            Log.e("last word",lastWord);
            String finalAdd=currAdd.replace(", "+lastWord," ");
            binding.etLocation.setText(finalAdd);
            binding.etLocation.selectAll();
        }

        lat = getIntent().getStringExtra("lat");
        lang = getIntent().getStringExtra("lang");

        Log.e(TAG, "onCreate: getted value - lat ->"+lat+"  lang ->"+lang );

    }

    private void inIt() {

        String apiKey = getString(R.string.google_maps_key);
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(context);
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        if (marshmallowPermission.check_location_Permission()) {
            getcurrentlocation("0");
        }

        onClick();

        binding.etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    writing = true;
                    if (writing){
                        try {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    /*binding.etLocation.clearFocus();*/
                                    //getPredictionstwo(s.toString());
                                    if(s.length()>3){
                                        queryPlaceSearch(s.toString());
                                    }
                                }
                            }, 1000);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        binding.etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    Utility.hideSoftKeyboard(LocationSearchActivity.this);
//                }
//                return false;
//            }
//        });

        setupSearchADap();

        binding.etLocation.setSelectAllOnFocus(true);
        binding.etLocation.requestFocus();

//todo-- keyboard collapse on ok click

        binding.etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    Utility.hideSoftKeyboard(LocationSearchActivity.this);

                }
                return false;
            }
        });


    }

    private void setupSearchADap() {
        autoCompleteModelList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.searchplace.setLayoutManager(linearLayoutManager);
        placeAutoCompletetwoAdapter = new PlaceAutoCompletetwoAdapter(context, autoCompleteModelList);
        binding.searchplace.addItemDecoration(new DividerItemDecoration( binding.searchplace.getContext(), DividerItemDecoration.VERTICAL));
        binding.searchplace.setAdapter(placeAutoCompletetwoAdapter);
        placeAutoCompletetwoAdapter.notifyDataSetChanged();

        placeAutoCompletetwoAdapter.setOnItemClickListener(new PlaceAutoCompletetwoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String place = placeAutoCompletetwoAdapter.getPlaceAutoCompleteModel().get(position).getPlaceId();
                selected = true;
                //getPlaceDetails(place);  // todo old code replaced with below code

                String coordinate[] = place.split(",");
                strlatitude = String.valueOf(coordinate[0]);
                strlongitude = String.valueOf(coordinate[1]);
                Log.e(TAG, "lat "+strlatitude+ ", lang "+strlongitude);


                binding.etLocation.setText(placeAutoCompletetwoAdapter.getPlaceAutoCompleteModel().get(position).getPlaceText());//todo new code

                Utility.hideSoftKeyboard(LocationSearchActivity.this);
                placeAutoCompletetwoAdapter.clearRecyclerView();
                Utility.hideSoftKeyboard(LocationSearchActivity.this);
            }
        });
    }

    private void getPredictionstwo(CharSequence constraint) {


        final ArrayList<PlaceAutoCompleteModel> resultList = new ArrayList<>();
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        // Create a RectangularBounds object.


        RectangularBounds bounds = RectangularBounds.newInstance(new LatLng(45.858180, 9.019715), new LatLng(54.906064, 8.663775
        ));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                .setLocationRestriction(bounds)
                // .setCountry("country:DE")
                .setTypeFilter(TypeFilter.REGIONS)

                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
                resultList.add(new PlaceAutoCompleteModel(prediction.getPlaceId(), prediction.getFullText(null).toString()));
            }

            if (selected) {
                selected = false;
            } else {
                placeAutoCompletetwoAdapter.setPlaceAutoCompleteModelList(resultList);
                placeAutoCompletetwoAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }


    private void onClick() {
        binding.tvOne.setOnClickListener(this);
        binding.tvTwo.setOnClickListener(this);
        binding.tvThree.setOnClickListener(this);
        binding.tvFour.setOnClickListener(this);
        binding.tvFive.setOnClickListener(this);
        binding.tvSix.setOnClickListener(this);
        binding.tvSeven.setOnClickListener(this);
        binding.ivClose.setOnClickListener(this);
        binding.etLocation.setOnClickListener(this);
        binding.rlNearMe.setOnClickListener(this);
        binding.btApply.setOnClickListener(this);
        binding.ivRemove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.tvOne) {
            setIntent("0.2", Add);

        } else if (v == binding.tvTwo) {
            setIntent("0.5", Add);

        } else if (v == binding.ivRemove) {
            binding.etLocation.setText("");

        } else if (v == binding.tvThree) {
            setIntent("1", Add);

        } else if (v == binding.tvFour) {
            setIntent("3", Add);

        } else if (v == binding.tvFive) {
            setIntent("5", Add);

        } else if (v == binding.tvSix) {
            setIntent("10", Add);

        } else if (v == binding.tvSeven) {
            setIntent("20", Add);

        } else if (v == binding.ivClose) {

            onBackpressed();

        }/*else if (v==binding.etLocation){
            binding.etLocation.setText("");
        }*/ else if (v == binding.rlNearMe) {

            Log.e(TAG, "onClick: clicked ");

            if (Utility.checkGPSStatus(context)) {
                getcurrentlocation("1");
            } else {
                Toast.makeText(context, "Bitte aktiviere deine Ortungsdienste", Toast.LENGTH_SHORT).show();
            }

        } else if (v == binding.btApply) {
            setIntent("", Add);
        }
    }

    public String getAddress(final double latitude, final double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {

                Address address = addresses.get(0);

                for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                    if (i == addresses.get(0).getMaxAddressLineIndex()) {
                        result.append(addresses.get(0).getAddressLine(i));
                    } else {
                        result.append(addresses.get(0).getAddressLine(i) + ",");
                    }
                }
                String addreshidden = result.toString();

                Log.e(TAG, "getAddressssssssssssssssssss : " + addreshidden);
                binding.etLocation.setText(addreshidden);
                Add = addreshidden;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private void setIntent(String value, String name) {

        if (binding.etLocation.getText().toString().equals("") || binding.etLocation.getText().toString().equals("set Location")) {
            Toast.makeText(context, "Bitte Standort auswählen", Toast.LENGTH_SHORT).show();
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("resultDistance", value);
            //returnIntent.putExtra("resultName",name);// todo -- original
            returnIntent.putExtra("resultName", binding.etLocation.getText().toString());// todo -- duplicate

            Log.e(TAG, "setIntent: LAT " + strlatitude);
            Log.e(TAG, "setIntent: LANG " + strlongitude);

            if (strlatitude == null || strlatitude.equals("")) {
                returnIntent.putExtra("lat", lat);
                returnIntent.putExtra("lang", lang);


            } else {

                returnIntent.putExtra("lat", strlatitude);
                returnIntent.putExtra("lang", strlongitude);

                Log.e(TAG, "setIntent: LAT " + strlatitude);
                Log.e(TAG, "setIntent: LANG " + strlongitude);

            }


            setResult(2, returnIntent);
            finish();
          //  Bungee.slideRight(context);

        }

    }

    private void startAutocompleteActivity() {
        Intent autocompleteIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context);
        startActivityForResult(autocompleteIntent, AUTOCOMPLETE_REQUEST_CODE);
    }

    private void getcurrentlocation(String flag) {

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

        if (marshmallowPermission.check_location_Permission()) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean once = true;
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        //  Log.e(TAG, String.format("Place '%s' has likelihood: %f", placeLikelihood.getPlace().getAddress(), placeLikelihood.getLikelihood()));
                        //binding.etLocation.setText(placeLikelihood.getPlace().getAddress());
                        address = placeLikelihood.getPlace().getAddress();

                        if (once) {
                            try {
                                LatLng latLng = placeLikelihood.getPlace().getLatLng();
                                if (latLng != null) {
                                    String lat = String.valueOf(latLng.latitude);
                                    String lon = String.valueOf(latLng.longitude);


                                    if (flag.equals("1")) {

                                        strlatitude = lat;
                                        strlongitude = lon;
                                        Log.e(TAG, "getcurrentlocation: 22" );

                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("resultDistance", "20");
                                        returnIntent.putExtra("resultName", getAddress(latLng.latitude, latLng.longitude));// todo -- duplicate

                                        returnIntent.putExtra("lat", lat);
                                        returnIntent.putExtra("lang", lon);

                                        setResult(2, returnIntent);
                                        finish();
                                      //  Bungee.slideRight(context);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            once = false;
                        }


                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                LatLng latlng = place.getLatLng();
                String lat = String.valueOf(latlng.latitude);
                String lon = String.valueOf(latlng.longitude);
                address = place.getAddress();
                keyword = address;
                binding.etLocation.setText(address);
                Add = address;

                Log.e(TAG, "onActivityResult: LATITUDE" + String.valueOf(latlng.latitude));
                Log.e(TAG, "onActivityResult: LONGITUDE" + String.valueOf(latlng.longitude));

                strlatitude = String.valueOf(latlng.latitude);
                Log.e(TAG, "onActivityResult: 33" );
                strlongitude = String.valueOf(latlng.longitude);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e(TAG, "onActivityResult: " + status);
                // responseView.setText(status.getStatusMessage());
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        onBackpressed();
    }

    private void onBackpressed() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("resultDistance", "");
        returnIntent.putExtra("resultName", "");

        returnIntent.putExtra("lat", "");
        returnIntent.putExtra("lang", "");

        setResult(2, returnIntent);
        finish();
      //  Bungee.slideRight(context);

    }

   /* private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String place = placeAutoCompleteAdapter.getItem(i).getPlaceId();
                    getPlaceDetails(place);
                }
            };*/

    private void getPlaceDetails(String placed) {
        // Define a Place ID.
        String placeId = placed;

// Specify the fields to return (in this example all fields are returned).
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            LatLng latlng = place.getLatLng();
            address = place.getAddress();
            keyword = address;


            binding.etLocation.setText(address);
            Add = address;
            strlatitude = String.valueOf(latlng.latitude);
            Log.e(TAG, "getPlaceDetails: 44" );
            strlongitude = String.valueOf(latlng.longitude);


        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.getMessage());
            }
        });
    }


    private void queryPlaceSearch(String query){

        Map<String, String> data = new HashMap<>();
        data.put("key","5eaae41f648ada");
        data.put("q",query);
       // data.put("dedupe","1");
        data.put("addressdetails", "1");
        // data.put("countrycodes","de,at,ch");
        data.put("countrycodes","de");
        data.put("accept-language", "de");
        //data.put("normalizecity","1");
        data.put("format", "json");

        Log.e(TAG, "queryPlaceSearch: "+data );

        Call<ResponseBody> call =apiService.doPlaces(data);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String resp = response.body().string().trim();
                    final ArrayList<PlaceAutoCompleteModel> resultList = new ArrayList<>();

                    JSONArray respo = new JSONArray(resp);
                    Log.e(TAG, "onResponse: response is "+respo );

                    resultList.clear();

                    if(respo.length()>0){

                        for (int i = 0; i<respo.length(); i++){

                            JSONObject inRespo = respo.getJSONObject(i);

                            String LatLang = inRespo.getString("lat")+","+inRespo.getString("lon");
                            //String ss= inRespo.getString("display_name");
                            String country= inRespo.getJSONObject("address").getString("country");
                            Log.e("Country Value",country);

                            String place= inRespo.getString("display_place");
                            String Daddress= inRespo.getString("display_address");

                            String placeName="";
                            if (!inRespo.getJSONObject("address").has("postcode")) {
                                placeName= Daddress.replace(", "+country," ");
                            } else {
                                String postcode= inRespo.getJSONObject("address").getString("postcode");
                                placeName= Daddress.replace(", "+postcode+", "+country," ");
                            }

                            String pfinal=place+"\n"+placeName;
                            resultList.add(new PlaceAutoCompleteModel(LatLang,pfinal));
                        }

                        if (selected) {
                            selected = false;
                        } else {
                            placeAutoCompletetwoAdapter.setPlaceAutoCompleteModelList(resultList);
                            placeAutoCompletetwoAdapter.notifyDataSetChanged();
                        }

                    }

                    /*JSONObject jsonObject = new JSONObject(resp);

                    JSONArray predictionsArray = jsonObject.getJSONArray("predictions");

                    String status = jsonObject.getString("status");

                    if (status.equals("OK")){
                        for (int i = 0 ;i< predictionsArray.length();i++){
                            String placeId = predictionsArray.getJSONObject(i).getString("place_id");
                            String description = predictionsArray.getJSONObject(i).getString("description");
                           // String description = predictionsArray.getJSONObject(i).getJSONArray("terms").getJSONObject(0).getString("value");
                            resultList.add(new PlaceAutoCompleteModel(placeId,description));
                        }

                        if (selected) {
                            selected = false;
                        } else {
                            placeAutoCompletetwoAdapter.setPlaceAutoCompleteModelList(resultList);
                            placeAutoCompletetwoAdapter.notifyDataSetChanged();
                        }
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
