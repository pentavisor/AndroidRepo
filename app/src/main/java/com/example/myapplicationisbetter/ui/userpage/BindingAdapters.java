package com.example.myapplicationisbetter.ui.userpage;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class BindingAdapters {

    public static final int ADD_BUTTON_ID = -1;//need to be ADD_BUTTON_ID<0


    @BindingAdapter({"app:setUrl"})
    public static void loadImage(ImageView photoImage, UserDataModel udm) {
        if (udm.imageName.equals("")) {
            if (udm.id == ADD_BUTTON_ID) {
                photoImage.setImageResource(udm.imageLink);
            } else {
                photoImage.setImageDrawable(MyHelper.getCircleBitmap(udm.imageLink, 0.8f));

            }
        } else {
            Picasso.get()
                    .load(udm.imageName)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(photoImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            photoImage.setImageDrawable(MyHelper.getCircleBitmap(photoImage.getDrawable(), 0.8f));
                        }

                        @Override
                        public void onError(Exception e) {
                            //Try again online if cache failed
                            Picasso.get()
                                    .load(udm.imageName)
                                    .error(R.drawable.mustache)
                                    .into(photoImage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            photoImage.setImageDrawable(MyHelper.getCircleBitmap(photoImage.getDrawable(), 0.8f));
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
        }
    }

    @BindingAdapter({"app:setIndicator"})
    public static void loadIndicator(ImageView indicatorImage, UserDataModel udm) {
        indicatorImage.setImageResource(R.drawable.greencircle);
    }
}

