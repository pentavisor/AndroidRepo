package com.example.myapplicationisbetter.ui.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.myapplicationisbetter.R;

import com.example.myapplicationisbetter.ui.userpage.MainActivity;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {

    @InjectPresenter
    LoginPresenter loginPresenter;
    TextView loginTextBox;
    TextView passwordTextBox;
    TextView sysMess;
    Button buttonInput;
    Button buttonReset;
    Button buttonFirstLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        passwordTextBox = findViewById(R.id.Password);
        loginTextBox = findViewById(R.id.Login);
        buttonInput = findViewById(R.id.buttonInput);
        buttonReset = findViewById(R.id.buttonReset);
        buttonFirstLogin = findViewById(R.id.buttonfustlogin);
        sysMess = findViewById(R.id.SysMess);
        sysMess.setMovementMethod(new ScrollingMovementMethod());
        buttonInput.setOnClickListener(x -> loginPresenter.btnSet());
        buttonReset.setOnClickListener(x -> loginPresenter.btnReset());
        buttonFirstLogin.setOnClickListener(x -> goInUserListPage());

    }

    @Override
    public void setButtonInputText(String s) {
        buttonInput.setText(s);
    }

    @Override
    public void setSystemMassage(String s) {
        sysMess.setText(s);
    }

    @Override
    public void checkLoginAndPass() {
        loginPresenter.loginInit(loginTextBox.getText().toString(), passwordTextBox.getText().toString());
    }

    @Override
    public void blockButtonReset() {
        buttonReset.setClickable(false);
    }

    @Override
    public void goInUserListPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.destroySubscribes();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_items, menu);
//        return true;
//    }

}

