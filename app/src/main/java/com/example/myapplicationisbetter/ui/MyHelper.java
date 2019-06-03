package com.example.myapplicationisbetter.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.example.myapplicationisbetter.App;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class MyHelper {

    public static final boolean isNetworkConnected() {
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

    public static final float dpFromPx(final float px) {
        return px / App.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static final float pxFromDp(final float dp) {
        return dp * App.getInstance().getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static final RoundedBitmapDrawable getCircleBitmap(int resourceLink, Float f) {

        if (f == null) f = 1.0f;
        Bitmap src = drawableToBitmap(ResourcesCompat.getDrawable(App.getInstance().getResources(), resourceLink, null));
        Resources res = App.getInstance().getResources();
        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius( dr.getIntrinsicWidth() /f);

        return dr;
    }
    public static final RoundedBitmapDrawable getCircleBitmap(Drawable drawable, Float f) {
        if (f == null) f = 1.0f;
         BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap src = null;
        if (bitmapDrawable!=null) src = bitmapDrawable.getBitmap();
        Resources res = App.getInstance().getResources();
         RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius( dr.getIntrinsicWidth() /f);


        return dr;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
