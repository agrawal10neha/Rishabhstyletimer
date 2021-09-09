package com.si.styletimer.utill;

import androidx.databinding.BindingAdapter;
import android.widget.ImageView;

import com.si.styletimer.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageBinding {

        private static final String TAG = "ImageAdapter";
        private  static int default_image = R.drawable.ic_image;
        private  static int client_default_image = R.drawable.ic_user_round_black;

        @BindingAdapter({"bind:imageUrl"})
        public static void loadImage(ImageView view, String imageUrl) {
            imageUrl.replaceAll(" ", "%20");
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .placeholder(default_image)
                    .centerCrop()
                    .into(view);
        }


        @BindingAdapter({"bind:imageUrl"})
        public static void loadImage(CircleImageView view, String imageUrl) {
            imageUrl.replaceAll(" ", "%20");
            Picasso.get()
                    .load(imageUrl)
                    .fit()
                    .placeholder(client_default_image)
                    .centerCrop()
                    .into(view);
        }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(CircleImageView view, File file) {
        /*file.replaceAll(" ", "%20");*/
        Picasso.get()
                .load(file)
                .fit()
                .placeholder(client_default_image)
                .centerCrop()
                .into(view);
    }

    }
