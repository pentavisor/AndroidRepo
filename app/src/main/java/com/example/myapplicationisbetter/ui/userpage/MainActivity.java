package com.example.myapplicationisbetter.ui.userpage;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.databinding.ActivityListWithUsersBinding;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.example.myapplicationisbetter.ui.updatepage.UpdateActivity;
import com.example.myapplicationisbetter.ui.usercreatepage.CreateUserActivity;
import com.example.myapplicationisbetter.ui.userpage.CastomSpinner.Category;
import com.example.myapplicationisbetter.ui.userpage.CastomSpinner.CategoryDropdownAdapter;
import com.example.myapplicationisbetter.ui.userpage.CastomSpinner.CategoryDropdownMenu;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends MvpAppCompatActivity implements MainView {

    @InjectPresenter
    MainPresenter mainPresenter;

    @BindView(R.id.sportText)
    TextView sportText;
    @BindView(R.id.flowerText)
    TextView flowerText;
    @BindView(R.id.mushroomsText)
    TextView mushroomsText;
    @BindView(R.id.crazyText)
    TextView crazyText;

    @BindView(R.id.PhotoImageView)
    ImageView photoImage;
    @BindView(R.id.firstname)
    TextView firstname;
    @BindView(R.id.lastname)
    TextView lastname;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.MySpinnerButton)
    View mySpinnerButton;
    @BindView(R.id.triangleSpinner)
    ImageView triangleSpinner;


    private UserDataModel currentUserForDelete;
    private UserProperties currentUserProperties;
    private List<UserDataModel> users = new ArrayList<>();

    private ActivityListWithUsersBinding binding;

    private CategoryDropdownMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_with_users);
        ButterKnife.bind(this);

        String str = "Шаловливая Aнастасия любит горячих парней, чувственных с ноткой азарта она погружается в таких людей как в дорогую вечернюю горячую ванну, в которой хочется согреться еще и еще.... ";
        users.add(new UserDataModel(BindingAdapters.ADD_BUTTON_ID, "asdsd", "", "", "", true, "", "", R.drawable.circle_add, "", 0));
        users.add(new UserDataModel(-2, "Анастасия", "Шаловливая", "asdasdasd", "", false, "", "", R.drawable.mustache, "", mainPresenter.TEST_USER_PROFILE_DATA));

        binding.setMyAdapter(new UsersRecyclerViewAdapter(users,this, mainPresenter));

        mySpinnerButton.setBackgroundResource(R.drawable.circle_spinner_angle);
        mySpinnerButton.setOnClickListener(x->{
            showCategoryMenu(x);
            triangleSpinner.setRotation(180);
        });


    }


    public void goUpdateUserPage(UserDataModel userDataModel, UserProperties userProperties) {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(App.getInstance().getResources().getString(R.string.user_data_model), userDataModel);
        intent.putExtra(App.getInstance().getResources().getString(R.string.user_properties), userProperties);
        startActivity(intent);
    }

    private void showCategoryMenu(View button){
        menu = new CategoryDropdownMenu(this);
        menu.setWidth((int)(MyHelper.pxFromDp(140.0f)));
        menu.showAsDropDown(button,(int)(MyHelper.pxFromDp(-60.0f)),0);
        menu.setCategorySelectedListener(new CategoryDropdownAdapter.CategorySelectedListener() {
            @Override
            public void onCategorySelected(int position, Category category) {
                triangleSpinner.setRotation(0);
                menu.dismiss();
                Toast.makeText(MainActivity.this, "Your favourite programming language : "+ category.category, Toast.LENGTH_SHORT).show();
            }
        });
        menu.setWindowClosedListener(new CategoryDropdownMenu.CloseWindowHandler() {
            @Override
            public void onWindowClosed() {
                triangleSpinner.setRotation(0);
            }
        } );
    }



    @Override
    public void goCreateUserPage() {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void setText(UserDataModel udm, UserProperties userProperties) {
        if (userProperties == null) {
            userProperties = new UserProperties(0, false, false, false, false);
        }
        sportText.setText(userProperties.sport ? App.getInstance().getResources().getString(R.string.sport_sw) : App.getInstance().getResources().getString(R.string.sport_sw_not));
        flowerText.setText(userProperties.flowers ? App.getInstance().getResources().getString(R.string.flowers_sw) : App.getInstance().getResources().getString(R.string.flowers_sw_not));
        mushroomsText.setText(userProperties.mushrooms ? App.getInstance().getResources().getString(R.string.mushrooms_sw) : App.getInstance().getResources().getString(R.string.mushrooms_sw_not));
        crazyText.setText(userProperties.funnyCat ? App.getInstance().getResources().getString(R.string.crazy_sw) : App.getInstance().getResources().getString(R.string.crazy_sw_not));

        if (udm.imageName.equals("")) {
            photoImage.setImageResource(udm.imageLink);
        } else {
            Picasso.get()
                    .load(udm.imageName)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(photoImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            photoImage.setImageDrawable(MyHelper.getCircleBitmap(photoImage.getDrawable(), 3.0f));
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
                                            photoImage.setImageDrawable(MyHelper.getCircleBitmap(photoImage.getDrawable(), 3.0f));
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
        }

        firstname.setText(udm.firstName);
        lastname.setText(udm.lastName);
        sex.setText(udm.sex ? App.getInstance().getResources().getString(R.string.sex_choice_msg_man) : App.getInstance().getResources().getString(R.string.sex_choice_msg_woman));
        currentUserForDelete = udm;
        currentUserProperties = userProperties;
    }

    @Override
    protected void onResume() {
        binding.setMyAdapter(new UsersRecyclerViewAdapter(users,this, mainPresenter));
        mySpinnerButton.setOnClickListener(x->{
            showCategoryMenu(x);
            triangleSpinner.setRotation(180);
        });
        super.onResume();

    }
    @Override
    protected void onStop(){
        binding.setMyAdapter(null);
        menu = null;
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
