package com.example.myapplicationisbetter.ui.updatepage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.myapplicationisbetter.App;
import com.example.myapplicationisbetter.R;
import com.example.myapplicationisbetter.data.models.UserDataModel;
import com.example.myapplicationisbetter.data.models.UserProperties;
import com.example.myapplicationisbetter.ui.MyHelper;
import com.example.myapplicationisbetter.ui.userpage.MainActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateActivity extends MvpAppCompatActivity implements UpdateView {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    @InjectPresenter
    UpdatePresenter updatePresenter;

    @BindView(R.id.secret_code)
    RippleView secretCodeView;
    @BindView(R.id.sensor_number)
    RippleView sensorNumberView;

    @BindView(R.id.UserImage)
    ImageView userImage;
    @BindView(R.id.ButtonUserImage)
    Button buttonUserImage;

    @BindView(R.id.lastname)
    EditText lastName;
    @BindView(R.id.firstName)
    EditText firstName;
    @BindView(R.id.Birthday)
    TextView birthday;
    @BindView(R.id.sexChoise)
    Spinner spinner;

    @BindView(R.id.sportSwith)
    Switch sportSw;
    @BindView(R.id.flowerSwith)
    Switch flowersSw;
    @BindView(R.id.mushroomSwith)
    Switch mushroomsSw;
    @BindView(R.id.crazySwith)
    Switch crazySw;
    @BindView(R.id.save_button)
    Button startButton;

    UserDataModel userDataModel;
    UserProperties userProperties;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);

        Bundle arguments = getIntent().getExtras();
        userDataModel = (UserDataModel) arguments.get(App.getInstance().getResources().getString(R.string.user_data_model));
        userProperties = (UserProperties) arguments.get(App.getInstance().getResources().getString(R.string.user_properties));
        updatePresenter.calendarInit();

        sportSw.setChecked(userProperties.sport);
        flowersSw.setChecked(userProperties.flowers);
        mushroomsSw.setChecked(userProperties.mushrooms);
        crazySw.setChecked(userProperties.funnyCat);

        insertImageFromView();

        firstName.setText(userDataModel.firstName);
        lastName.setText(userDataModel.lastName);
        birthday.setText(userDataModel.birthday);
        if (userDataModel.sex) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(1);
        }


        sportSw.setOnCheckedChangeListener((c, b) -> userProperties.sport = b);
        flowersSw.setOnCheckedChangeListener((c, b) -> userProperties.flowers = b);
        mushroomsSw.setOnCheckedChangeListener((c, b) -> userProperties.mushrooms = b);
        crazySw.setOnCheckedChangeListener((c, b) -> userProperties.funnyCat = b);

        buttonUserImage.setOnClickListener(x -> dispatchTakePictureIntent());

        sensorNumberView.setOnClickListener(v -> showAddSensorNumberWindow(UpdateActivity.this));

        secretCodeView.setOnClickListener(v -> showAddSecretCodeWindow(UpdateActivity.this));
        birthday.setOnClickListener(v -> {
            if ((updatePresenter.date != null) & (updatePresenter.myCalendar != null))
                new DatePickerDialog(UpdateActivity.this, updatePresenter.date, updatePresenter.myCalendar
                        .get(Calendar.YEAR), updatePresenter.myCalendar.get(Calendar.MONTH),
                        updatePresenter.myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });


        startButton.setOnClickListener((x) -> {
            userDataModel.birthday = birthday.getText().toString();
            userDataModel.lastName = lastName.getText().toString();
            userDataModel.firstName = firstName.getText().toString();
            if (spinner.getSelectedItemPosition() == 0) {
                userDataModel.sex = true;
            } else {
                userDataModel.sex = false;
            }
            updatePresenter.updateUserInDataBase(userDataModel, userProperties);
        });
    }

    private void insertImageFromView() {
        if (userDataModel.imageName.equals("")) {
            userImage.setImageResource(userDataModel.imageLink);
        } else {
            Picasso.get()
                    .load(userDataModel.imageName)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(userImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            userImage.setImageDrawable(MyHelper.getCircleBitmap(userImage.getDrawable(), 3.0f));
                        }

                        @Override
                        public void onError(Exception e) {
                            //Try again online if cache failed
                            Picasso.get()
                                    .load(userDataModel.imageName)
                                    .error(R.drawable.mustache)
                                    .into(userImage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            userImage.setImageDrawable(MyHelper.getCircleBitmap(userImage.getDrawable(), 3.0f));
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

    private void showAddSensorNumberWindow(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(App.getInstance().getResources().getString(R.string.set_sensor))
                .setMessage(App.getInstance().getResources().getString(R.string.set_sensor_number))
                .setView(taskEditText)
                .setPositiveButton(
                        App.getInstance().getResources().getString(R.string.put_one),
                        (dialog1, which) -> userDataModel.sensorNumber = String.valueOf(taskEditText.getText())
                )
                .setNegativeButton(App.getInstance().getResources().getString(R.string.cancel), null)
                .create();
        dialog.show();
    }

    private void showAddSecretCodeWindow(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(App.getInstance().getResources().getString(R.string.check_sensor))
                .setMessage(App.getInstance().getResources().getString(R.string.set_secret_code))
                .setView(taskEditText)
                .setPositiveButton(
                        App.getInstance().getResources().getString(R.string.put_one),
                        (dialog1, which) -> userDataModel.sensorSecretCode = String.valueOf(taskEditText.getText())
                )
                .setNegativeButton(App.getInstance().getResources().getString(R.string.cancel), null)
                .create();
        dialog.show();
    }

    @Override
    public void goToUserList() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                userDataModel.imageName = photoURI.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            CropImage.activity(photoURI).setOutputUri(photoURI)
                    .setFixAspectRatio(true)
                    .setOutputCompressQuality(20)
                    .setRequestedSize(500, 500)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

            insertImageFromView();
        }

    }

    @Override
    public void setBirthdayText(String string) {
        birthday.setText(string);
    }

    @Override
    public void setSystemText(String str) {
        Toast toast = Toast.makeText(this,
                str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
