package com.example.myapplicationisbetter.ui.userpage.fragments;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;


import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;


public class UserInformFragment extends Fragment {

    private UserDataModel currentUserForDelete;

    OnItemDeleteListener callback;

    private boolean firstSpinnerBreaker = false;

    CustomSpinner settingSpinner;

    public void setOnItemDeleteListener(OnItemDeleteListener callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_inform, container, false);

        settingSpinner = (CustomSpinner) view.findViewById(R.id.spinnerCa);
        String[] str = {"Изменить", "Удалить"};
        settingSpinner.initAdapter(getActivity(),str);
        settingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!firstSpinnerBreaker){
                    firstSpinnerBreaker = true;
                    return;
                }
                switch (i){
                    case(1):

                        Completable.fromAction(()->{
                            App.getInstance().getDatabase().userDao().deleteOnId(currentUserForDelete);
                        }).subscribeOn(Schedulers.single()).subscribe();
                        callback.DeleteActionChecker(currentUserForDelete);
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
            ContextWrapper cw = new ContextWrapper(App.getInstance().getApplicationContext());
            File directory = cw.getDir(App.getInstance().getResources().getString(R.string.userImageFolder), Context.MODE_PRIVATE);
            File myImageFile = new File(directory, udm.imageName+".png");
            Picasso.get().load(myImageFile).into(photoImage);
        }

        firstname.setText(udm.firstName);
        lastname.setText(udm.lastName);
        sex.setText(udm.sex ? "Прекрасный мужчина" : "Великолепная женщина");
        currentUserForDelete = udm;
    }
    public void deleteAllCallBacks(){
        callback = null;
    }


    public interface OnItemDeleteListener{
         void DeleteActionChecker(UserDataModel userDataModel);
    }
}