package com.example.myapplicationisbetter.ui.userpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.ItemClickSupport;
import com.example.myapplicationisbetter.ui.usercreatepage.CreateUserActivity;
import com.example.myapplicationisbetter.ui.userpage.fragments.UserInformFragment;

import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    @InjectPresenter
    MainPresenter mainPresenter;

    TextView status;
    RecyclerView.LayoutManager layoutManager;
    UserAdapter adapter;
    RecyclerView recyclerView;
    List<UserDataModel> users = new ArrayList<>();

    UserInformFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_users);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(layoutManager);
        fragment = (UserInformFragment) getSupportFragmentManager().findFragmentById(R.id.listFragment);
        if (fragment == null)
            getSupportFragmentManager().beginTransaction().add(R.id.listFragment, fragment).commit();

        String str = "Шаловливая Aнастасия любит горячих парней, чувственных с ноткой азарта она погружается в таких людей как в дорогую вечернюю горячую ванну, в которой хочется согреться еще и еще.... ";
        users.add(new UserDataModel(-1, "asdsd", "", "", "", true, "", "", R.drawable.circle_add,"",0));
        users.add(new UserDataModel(-2, "Анастасия", "Шаловливая", "asdasdasd", "", false, "", "", R.drawable.mustache,"",mainPresenter.TEST_USER_PROFILE_DATA));

        adapter = new UserAdapter(this, users);
        //mainPresenter.adapter = adapter;
        recyclerView.setAdapter(adapter);
        mainPresenter.userListInit();
        fragment.setText(adapter.getItem(1),null);
        fragment.setOnItemDeleteListener(new UserInformFragment.OnItemDeleteListener() {
            @Override
            public void DeleteActionChecker(UserDataModel userDataModel) {
                if(userDataModel.id >= 0) {
                    recyclerView.smoothScrollToPosition( 1);
                    fragment.setText(adapter.getItem( 1),null);
                    adapter.deleteItem(userDataModel);
                }
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, view) -> {
            switch (adapter.getItem(position).id) {
                case (-1):
                    goCreateUserPage();
                    break;
                default:
                    mainPresenter.setTextInFragment(adapter.getItem(position));

            }

        });

    }

    @Override
    public void setTextInFragment(UserDataModel userDataModel, UserProperties userProperties){
        fragment.setText(userDataModel,userProperties);
    }
    @Override
    public void setUserList(List<UserDataModel> list) {
        for (UserDataModel udm : list)
            if (adapter.getItemOnIdinModel(udm.id) == null)
                adapter.addItem(new UserDataModel(
                        udm.id,
                        udm.firstName,
                        udm.lastName,
                        udm.googleMapLink,
                        udm.birthday,
                        udm.sex,
                        udm.sensorNumber,
                        udm.sensorSecretCode,
                        udm.imageLink,
                        udm.imageName,
                        udm.future_id
                ));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteUsers(List<UserDataModel> list) {
        for (int i = 0; i < list.size(); i++) {
            adapter.deleteItem(list.get(i));
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void deleteAllUsers() {
        adapter.deleteAllItems();
    }

    @Override
    public void setStatus(String s) {
        status.setText(s);
    }

    @Override
    public void goCreateUserPage(){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ItemClickSupport.removeFrom(recyclerView);
        fragment.deleteAllCallBacks();
    }



}
