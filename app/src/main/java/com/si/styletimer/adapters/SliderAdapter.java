package com.si.styletimer.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.si.styletimer.R;
import com.si.styletimer.utill.PersmissionUtils;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter implements View.OnClickListener{

    private ArrayList<Integer> images;
    private LayoutInflater inflater;
    ArrayList<String> Heading;
    ArrayList<String> Contents;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int currentPos = 0;
    public SliderAdapter(Context context, ArrayList<Integer> images, ArrayList<String> Heading, ArrayList<String> Contents) {
        this.context = context;
        this.images = images;
        this.Heading = Heading;
        this.Contents = Contents;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        try {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, currentPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slider_cell, view, false);

        ImageView myImage = myImageLayout.findViewById(R.id.iv);
        TextView Title = myImageLayout.findViewById(R.id.tvTitle);
        TextView content = myImageLayout.findViewById(R.id.tvContent);
        TextView skip = myImageLayout.findViewById(R.id.tvSkip);
        TextView tvHeir = myImageLayout.findViewById(R.id.tvHeir);
        Button btSlider = myImageLayout.findViewById(R.id.btSlider);

        myImage.setImageResource(images.get(position));
        Title.setText(Heading.get(position));
        content.setText(Contents.get(position));
        skip.setOnClickListener(this);
        btSlider.setOnClickListener(this);
        tvHeir.setOnClickListener(this);

     /*
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DashboardActivity.class);
                context.startActivity(in);
                ((Activity) context).finish();
            }
        });
    */

        if (position == 0) {
            btSlider.setText("WEITER");
            skip.setVisibility(View.GONE);
            tvHeir.setVisibility(View.GONE);
            currentPos = position;
        } else if (position == 1) {
            btSlider.setText("STANDORT TEILEN");
            skip.setVisibility(View.VISIBLE);
            tvHeir.setVisibility(View.GONE);
            currentPos = position;
        } else if (position == 2) {
            btSlider.setText("Mittelungen aktivieren");
            skip.setVisibility(View.VISIBLE);
            tvHeir.setVisibility(View.GONE);
            currentPos = position;
        } else if (position == 3) {
            btSlider.setText("Zeig' mir Salons!");
            skip.setVisibility(View.GONE);
            tvHeir.setVisibility(View.VISIBLE);
            currentPos = position;
        }
/*
        btSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSlider.getText().toString().equals("WEITER")) {

                }
                if (btSlider.getText().toString().equals("STANDORT TEILEN")) {
                    askLocationPermissions();
                }
                if (btSlider.getText().toString().equals("Mittelungen aktivieren")) {
                    Intent in = new Intent(context, DashboardActivity.class);
                    context.startActivity(in);
                    ((Activity) context).finish();
                }
                if (btSlider.getText().toString().equals("Zeig' mir Salons!")) {
                    Intent in = new Intent(context, DashboardActivity.class);
                    context.startActivity(in);
                    ((Activity) context).finish();
                }
            }
        });
*/

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    public void askLocationPermissions() {
        if (Build.VERSION.SDK_INT > 15) {
            final String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(((Activity) context), permissionsToRequest.toArray(new String[permissionsToRequest.size()]), PersmissionUtils.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }
        }

    }
}
