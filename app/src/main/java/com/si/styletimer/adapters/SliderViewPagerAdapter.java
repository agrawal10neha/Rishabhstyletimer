package com.si.styletimer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.si.styletimer.R;
import com.si.styletimer.retrofit.RetrofitServices;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "SliderViewPagerAdapter";
    private Context context;
 //   private String[] imageUrls;
    private List<String> imageList;
    private String id = "";

    public SliderViewPagerAdapter(Context context,List<String> imageList,String id) {
        this.context = context;
      //  this.imageUrls = imageUrls;
        this.imageList = imageList;
        this.id = id;

    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        String url = "";
        try {
            String someFilepath = imageList.get(position);
            String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
           // Log.e(TAG, "instantiateItem:extension = " + extension );
            if (extension.equals(".webp"))
            {
                url = RetrofitServices.BANNERS +id+"/crop_"+imageList.get(position);
               // url = RetrofitServices.BANNERS +id+"/prof_"+imageList.get(position);
            }else {
                url = RetrofitServices.BANNERS +id+"/crop_"+imageList.get(position)+".webp";
                //url = RetrofitServices.BANNERS +id+"/prof_"+imageList.get(position)+".webp";
            }

            Log.e(TAG, "instantiateItem: url = "+url);
            imageView.setBackgroundColor(context.getResources().getColor(R.color.font_black));
            final int inPixelsW = (int) context.getResources().getDimension(R.dimen.dp_350);
            final int inPixelsH = (int) context.getResources().getDimension(R.dimen.dp_160);
            Picasso.get()
                    .load(url)
                    //.resize(inPixelsW,inPixelsH)
                    .fit()
                    .placeholder(R.drawable.no_image_available1)
                    //.centerCrop()
                    .into(imageView);
            container.addView(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
