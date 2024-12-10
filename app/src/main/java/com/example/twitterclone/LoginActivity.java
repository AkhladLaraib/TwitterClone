package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginUsername, edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        Button mButtonSignup = findViewById(R.id.btnSignup);
        Button mButtonLogin = findViewById(R.id.btnLogin);

        mButtonLogin.setOnClickListener(this);
        mButtonSignup.setOnClickListener(this);

        edtLoginPassword.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                onClick(mButtonLogin);
            }
            return false;
        });

        if (ParseUser.getCurrentUser() != null) transitionToApp();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnLogin) {


            if (edtLoginUsername.getText().toString().equals("") ||
                    edtLoginPassword.getText().toString().equals("")) {

                FancyToast.makeText(LoginActivity.this, "Email and Password required", FancyToast.LENGTH_LONG,
                        FancyToast.INFO, true).show();
            } else {

                ParseUser.logInInBackground(edtLoginUsername.getText().toString(),
                        edtLoginPassword.getText().toString(),
                        (user, e) -> {

                            if (user != null && e == null) {

                                FancyToast.makeText(LoginActivity.this, user.getUsername()
                                                + " logged in successfully", FancyToast.LENGTH_LONG,
                                        FancyToast.SUCCESS, true).show();
                                transitionToApp();
                            } else {

                                FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        });

            }

        } else if (v.getId() == R.id.btnSignup) {

            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    }

    public void loginLayoutTapped(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void transitionToApp() {

        Intent intent   = new Intent(LoginActivity.this, SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }
}