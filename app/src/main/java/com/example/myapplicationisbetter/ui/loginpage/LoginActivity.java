package com.example.myapplicationisbetter.ui.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.myapplicationisbetter.R;

import com.example.myapplicationisbetter.ui.userpage.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {

    @InjectPresenter
    LoginPresenter loginPresenter;
    @BindView(R.id.Login)
    TextView loginTextBox;
    @BindView(R.id.Password)
    TextView passwordTextBox;
    @BindView(R.id.SysMess)
    TextView sysMess;
    @BindView(R.id.buttonInput)
    Button buttonInput;
    @BindView(R.id.buttonReset)
    Button buttonReset;
    @BindView(R.id.buttonfustlogin)
    Button buttonFirstLogin;

    @ProvidePresenter
    LoginPresenter provideLoginPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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
        loginPresenter.destroyLinks();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_items, menu);
//        return true;
//    }

}

