package com.example.myapplicationisbetter.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.myapplicationisbetter.App;

public class MyHelper {

    public static boolean isNetworkConnected() {
        //для работы вставь  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> в манифест
        final ConnectivityManager cm = (ConnectivityManager) App.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE)||ni.getType() == ConnectivityManager.TYPE_VPN);
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)|| nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN));
                }
            }
        }

        return false;
    }

    public static float dpFromPx(final float px) {
        return px / App.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final float dp) {
        return dp * App.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static RoundedBitmapDrawable getCircleBitmap(int resourceLink) {

        Resources res = App.getInstance().getResources();
        Bitmap src = BitmapFactory.decodeResource(res, resourceLink);
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);

        return dr;
    }
    public static RoundedBitmapDrawable getCircleBitmap(Drawable drawable, Float f) {
        if (f == null) f = 1.0f;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap src = null;
        if (bitmapDrawable!=null) src = bitmapDrawable.getBitmap();
        Resources res = App.getInstance().getResources();
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius( dr.getIntrinsicWidth() /f);

        return dr;
    }
}
