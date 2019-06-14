package com.example.myapplicationisbetter.ui.mappoint;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.databinding.ActivityListWithUsersBinding;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.example.myapplicationisbetter.ui.userpage.BindingAdapters;
import com.example.myapplicationisbetter.ui.userpage.MainActivity;
import com.example.myapplicationisbetter.ui.userpage.MainPresenter;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.plugins.localization.MapLocale;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
   // private LocalizationPlugin localizationPlugin;
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;
    private UserDataModel userDataModel;
    private String[] coordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle arguments = getIntent().getExtras();
        userDataModel = (UserDataModel) arguments.get(App.getInstance().getResources().getString(R.string.user_data_model));
        String[] coordinates = userDataModel.googleMapLink.split(":");
        Mapbox.getInstance(this, "pk.eyJ1IjoibXlhbmRyb2lkbWFwIiwiYSI6ImNqd3A2NXlsZzFoZ3Y0NG9senF5YjdsNGcifQ.UsoIV_RpNSgMJJiUUGOAIA");
        setContentView(R.layout.map_activity);
        mapView = findViewById(R.id.mapView);

        MapboxMapOptions options = new MapboxMapOptions()
                .camera(new CameraPosition.Builder()
                        .target(new LatLng(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1])))
                        .zoom(6)
                        .build());
        mapView = new MapView(this, options);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        markerViewManager = new MarkerViewManager(mapView, mapboxMap);

                        View customView = LayoutInflater.from(MapActivity.this).inflate(R.layout.marker_view_bubble, null);
                        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));


                        ImageView photoImage = customView.findViewById(R.id.imagemainbubble);
                        if (userDataModel.imageName.equals("")) {
                                photoImage.setImageDrawable(MyHelper.getCircleBitmap(userDataModel.imageLink, 0.8f));
                        } else {
                            Picasso.get()
                                    .load(userDataModel.imageName)
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
                                                    .load(userDataModel.imageName)
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

                        TextView titleTextView = customView.findViewById(R.id.marker_window_title);
                        titleTextView.setText(userDataModel.firstName);

                        TextView snippetTextView = customView.findViewById(R.id.marker_window_snippet);
                        snippetTextView.setText(userDataModel.lastName);

                        markerView = new MarkerView(new LatLng(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1])), customView);
                        markerViewManager.addMarker(markerView);
                    }
                });
            }
        });
        setContentView(mapView);
    }



    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (markerViewManager != null) {
            markerViewManager.onDestroy();
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}
