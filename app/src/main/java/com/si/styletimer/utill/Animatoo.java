package com.si.styletimer.utill;

import android.app.Activity;
import android.content.Context;

public class Animatoo {

 /*Animatoo.animateZoom(context);
Animatoo.animateFade(context);
Animatoo.animateWindmill(context);
Animatoo.animateSpin(context);
Animatoo.animateDiagonal(context);
Animatoo.animateSplit(context);
Animatoo.animateShrink(context);
Animatoo.animateCard(context);
Animatoo.animateInAndOut(context);
Animatoo.animateSwipeLeft(context);
Animatoo.animateSwipeRight(context);
Animatoo.animateSlideLeft(context);
Animatoo.animateSlideRight(context);
Animatoo.animateSlideDown(context);
Animatoo.animateSlideUp(context);*/


    public static void animateZoom(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_zoom_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_zoom_exit);
    }

    public static void animateFade(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_fade_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_fade_exit);
    }

    public static void animateWindmill(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_windmill_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_windmill_exit);
    }

    public static void animateSpin(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_spin_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_spin_exit);
    }

    public static void animateDiagonal(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_diagonal_right_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_diagonal_right_exit);
    }

    public static void animateSplit(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_split_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_split_exit);
    }

    public static void animateShrink(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_shrink_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_shrink_exit);
    }

    public static void animateCard(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_card_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_card_exit);
    }

    public static void animateInAndOut(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_in_out_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_in_out_exit);
    }

    public static void animateSwipeLeft(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_swipe_left_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_swipe_left_exit);
    }

    public static void animateSwipeRight(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_swipe_right_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_swipe_right_exit);
    }

    public static void animateSlideLeft(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_left_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_left_exit);
    }

    public static void animateSlideRight(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    public static void animateSlideDown(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_down_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_down_exit);
    }

    public static void animateSlideUp(Context context) {
        ((Activity) context).overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_up_enter, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_up_exit);
    }
}