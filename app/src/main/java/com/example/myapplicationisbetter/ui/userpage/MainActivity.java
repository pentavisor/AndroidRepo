package com.example.myapplicationisbetter.ui.userpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.ItemClickSupport;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.example.myapplicationisbetter.ui.updatepage.UpdateActivity;
import com.example.myapplicationisbetter.ui.usercreatepage.CreateUserActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

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


    TextView sportText;
    TextView flowerText;
    TextView mushroomsText;
    TextView crazyText;

    ImageView photoImage;
    TextView firstname;
    TextView lastname;
    TextView sex;

    CustomSpinner settingSpinner;

    private UserDataModel currentUserForDelete;
    private UserProperties currentUserProperties;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_users);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        settingSpinner = (CustomSpinner) findViewById(R.id.spinnerCa);

         sportText = (TextView) findViewById(R.id.sportText);
         flowerText = (TextView) findViewById(R.id.flowerText);
         mushroomsText = (TextView) findViewById(R.id.mushroomsText);
         crazyText = (TextView) findViewById(R.id.crazyText);

         photoImage = (ImageView) findViewById(R.id.PhotoImageView);
         firstname = (TextView) findViewById(R.id.firstname);
         lastname = (TextView) findViewById(R.id.lastname);
         sex = (TextView) findViewById(R.id.sex);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(layoutManager);

        String str = "Шаловливая Aнастасия любит горячих парней, чувственных с ноткой азарта она погружается в таких людей как в дорогую вечернюю горячую ванну, в которой хочется согреться еще и еще.... ";
        users.add(new UserDataModel(-1, "asdsd", "", "", "", true, "", "", R.drawable.circle_add,"",0));
        users.add(new UserDataModel(-2, "Анастасия", "Шаловливая", "asdasdasd", "", false, "", "", R.drawable.mustache,"",mainPresenter.TEST_USER_PROFILE_DATA));

        adapter = new UserAdapter(this, users);
        //mainPresenter.adapter = adapter;
        recyclerView.setAdapter(adapter);
        mainPresenter.userListInit();
        setText(adapter.getItem(1),null);

        mainPresenter.userListInit();



        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, view) -> {
            switch (adapter.getItem(position).id) {
                case (-1):
                    goCreateUserPage();
                    settingSpinner.setSelection(0);
                    break;
                default:
                    mainPresenter.setTextInFeature(adapter.getItem(position));
                    settingSpinner.setSelection(0);


            }

        });

        String[] spinnerChoiseArray = {"","Изменить", "Удалить"};
        settingSpinner.initAdapter(this,spinnerChoiseArray);
        settingSpinner.setSelection(0);
        settingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case(1):
                        if(currentUserForDelete.id >= 0) {
                            goUpdateUserPage(currentUserForDelete,currentUserProperties);
                        }else {
                            settingSpinner.setSelection(0);
                        }
                        break;
                    case(2):
                        if(currentUserForDelete.id >= 0) {
                            mainPresenter.deleteUser(currentUserForDelete);
                            recyclerView.smoothScrollToPosition( 1);
                            adapter.deleteItem(currentUserForDelete);
                            setText(adapter.getItem( 1),null);
                            settingSpinner.setSelection(0);
                            adapter.notifyDataSetChanged();

                        }else{
                            settingSpinner.setSelection(0);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

    }
    public void goUpdateUserPage( UserDataModel userDataModel, UserProperties userProperties){
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("currentUserDataModel", userDataModel);
        intent.putExtra("currentUserProperties", userProperties);
        startActivity(intent);
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
    }
    @Override
    public void setText(UserDataModel udm, UserProperties userProperties) {
        if (userProperties == null){
            userProperties = new UserProperties(0,false,false,false,false);
        }
        sportText.setText(userProperties.sport?"Занимается спортом":"Не занимается спортом");
        flowerText.setText(userProperties.flowers?"Любит цветы":"Не любит цветы");
        mushroomsText.setText(userProperties.mushrooms?"Собирает грибы":"Не собирает грибы");
        crazyText.setText(userProperties.funnyCat?"Гламурное Кисо":"Не гломурное кисо");

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
    @Override
    protected void onResume(){
        super.onResume();
        settingSpinner.setSelection(0);
        adapter.notifyDataSetChanged();
        mainPresenter.userListInit();
    }



}
