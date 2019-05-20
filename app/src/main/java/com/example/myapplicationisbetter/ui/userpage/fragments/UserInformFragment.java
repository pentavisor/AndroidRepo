package com.example.myapplicationisbetter.ui.userpage.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;


import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;


public class UserInformFragment extends Fragment {

    private UserDataModel currentUserForDelete;
    private UserProperties currentUserProperties;

    Map<String, Object> callbacks = new HashMap<>();

    private boolean firstSpinnerBreaker = false;

    CustomSpinner settingSpinner;

    public void setOnItemDeleteListener(OnItemChangeListener callback) {
        this.callbacks.put("deleteCallbacks",callback);
    }
    public void setOnOpenUpdateWindowListener(OnItemUpdateListener callback) {
        this.callbacks.put("OpenWindowUpdateCallbacks",callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_inform, container, false);

        settingSpinner = (CustomSpinner) view.findViewById(R.id.spinnerCa);
        String[] str = {"","Изменить", "Удалить"};
        settingSpinner.initAdapter(getActivity(),str);
        settingSpinner.setSelection(0);
        settingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case(1):
                        OnItemUpdateListener  myInterface = (OnItemUpdateListener)callbacks.get("OpenWindowUpdateCallbacks");
                        myInterface.OpenUpdateWindowListener(currentUserForDelete,currentUserProperties);
                        break;
                    case(2):

                        Completable.fromAction(()->{
                            App.getInstance().getDatabase().userDao().deleteOnId(currentUserForDelete);
                        }).subscribeOn(Schedulers.single()).subscribe();
                        OnItemChangeListener  myInterface1 = (OnItemChangeListener)callbacks.get("deleteCallbacks");
                        myInterface1.DeleteActionChecker(currentUserForDelete);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return view;
    }


    public void setText(UserDataModel udm, UserProperties userProperties) {
        if (userProperties == null){
            userProperties = new UserProperties(0,false,false,false,false);
        }

        TextView sportText = (TextView) getView().findViewById(R.id.sportText);
        TextView flowerText = (TextView) getView().findViewById(R.id.flowerText);
        TextView mushroomsText = (TextView) getView().findViewById(R.id.mushroomsText);
        TextView crazyText = (TextView) getView().findViewById(R.id.crazyText);
        sportText.setText(userProperties.sport?"Занимается спортом":"Не занимается спортом");
        flowerText.setText(userProperties.flowers?"Любит цветы":"Не любит цветы");
        mushroomsText.setText(userProperties.mushrooms?"Собирает грибы":"Не собирает грибы");
        crazyText.setText(userProperties.funnyCat?"Гламурное Кисо":"Не гломурное кисо");

        ImageView photoImage = (ImageView) getView().findViewById(R.id.PhotoImageView);
        TextView firstname = (TextView) getView().findViewById(R.id.firstname);
        TextView lastname = (TextView) getView().findViewById(R.id.lastname);
        TextView sex = (TextView) getView().findViewById(R.id.sex);

        if(udm.imageName.equals("")){
            photoImage.setImageResource(udm.imageLink);
        }else{
            Picasso.get()
                    .load(udm.imageName)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(photoImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            photoImage.setImageDrawable(MyHelper.getCircleBitmap(photoImage.getDrawable(),3.0f));
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
                                            photoImage.setImageDrawable(MyHelper.getCircleBitmap(photoImage.getDrawable(),3.0f));
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.v("Picasso","Could not fetch image");
                                        }
                                    });
                        }
                    });
        }

        firstname.setText(udm.firstName);
        lastname.setText(udm.lastName);
        sex.setText(udm.sex ? "Прекрасный мужчина" : "Великолепная женщина");
        currentUserForDelete = udm;
        currentUserProperties = userProperties;
    }
    public void deleteAllCallBacks(){
        callbacks.clear();
    }


    public interface OnItemChangeListener {
         void DeleteActionChecker(UserDataModel userDataModel);
    }
    public interface OnItemUpdateListener {
        void OpenUpdateWindowListener(UserDataModel userDataModel, UserProperties userProperties);

    }
}