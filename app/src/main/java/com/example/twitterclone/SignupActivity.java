package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSignupUsername, edtSignupPassword, edtSignupEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtSignupUsername = findViewById(R.id.edtSignupUsername);
        edtSignupPassword = findViewById(R.id.edtSignupPassword);
        edtSignupEmail = findViewById(R.id.edtSignupEmail);

        Button mButtonSignup = findViewById(R.id.btnSignup);
        Button mButtonLogin = findViewById(R.id.btnLogin);

        edtSignupPassword.setOnKeyListener((v, keyCode, event) ->  {

            if (keyCode == KeyEvent.KEYCODE_ENTER &&
                    event.getAction() == KeyEvent.ACTION_DOWN)
                onClick(mButtonSignup);

            return false;
        });

        mButtonSignup.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) transitionToApp();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnSignup){

            if (edtSignupEmail.getText().toString().isEmpty() ||
                    edtSignupUsername.getText().toString().isEmpty() ||
                    edtSignupPassword.getText().toString().isEmpty()) {

                FancyToast.makeText(SignupActivity.this,
                        "Email, UserName and Password required", FancyToast.LENGTH_SHORT,
                        FancyToast.INFO, true).show();
            } else {

                final ParseUser user = new ParseUser();
                user.setEmail(edtSignupEmail.getText().toString());
                user.setUsername(edtSignupUsername.getText().toString());
                user.setPassword(edtSignupPassword.getText().toString());

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
                alertdialog.setMessage("Signing up " + edtSignupUsername.getText().toString());
                alertdialog.setCancelable(false);
                AlertDialog dialog = alertdialog.create();
                dialog.show();

                user.signUpInBackground(e -> {
                    if (e == null) {

                        transitionToApp();
                    } else {

                        FancyToast.makeText(SignupActivity.this, e.getMessage(),
                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                    }
                    dialog.dismiss();
                });
            }

        } else if (v.getId() == R.id.btnLogin) {

            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void rootLayoutTapped(View view) {

        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToApp() {

        Intent intent = new Intent(SignupActivity.this, SocialMediaActivity.class);
        startActivity(intent);
        finish();
    }

}