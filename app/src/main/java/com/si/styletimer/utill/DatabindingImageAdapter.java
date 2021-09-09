package com.si.styletimer.utill;

import androidx.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;
import com.si.styletimer.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class DatabindingImageAdapter {
    private static final String TAG = "DatabindingCustomAdapte";

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
       // Log.e(TAG, "loadImage() ImageView = [" + view + "], imageUrl = [" + imageUrl + "]");
        if (imageUrl.isEmpty()) {
            imageUrl = "https://share.okdothis.com/assets/defaultfood-avatar-c246f74ff56d382424ac2c750e40b0a6.png?w=64&h=64";

            imageUrl.replaceAll(" ", "%20");
            Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available1).error(R.drawable.no_image_available1).into(view);
            //Picasso.with(view.getContext()).load(imageUrl).placeholder(R.drawable.defaultfood).fit().error(R.drawable.defaultfood).into(view);
        } else {
            imageUrl.replaceAll(" ", "%20");
            Picasso.get().load(imageUrl).placeholder(R.drawable.no_image_available1).error(R.drawable.no_image_available1).into(view);
        }
    }


    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(CircleImageView view, String imageUrl) {
        imageUrl.replaceAll(" ", "%20");
        // Log.e(TAG, "loadImage() CircleImageView  = [" + view + "], imageUrl = [" + imageUrl+ "]");
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_user_round_black).error(R.drawable.ic_user_round_black)
                .into(view);

    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImageFile(CircleImageView view, File imageUrl) {
         Log.e(TAG, "loadImage() CircleImageView  = [" + view + "], imageUrl = [" + imageUrl+ "]");
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_user_round_black).error(R.drawable.ic_user_round_black)
                .into(view);

    }


}
