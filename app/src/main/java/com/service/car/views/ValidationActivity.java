package com.service.car.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.service.car.R;

public class ValidationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ValidationActivity.class.getSimpleName();
    private TextView tv_terms;
    private CheckBox cb_terms;
    private EditText etUserLogin;
    private TextInputLayout txtip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        etUserLogin = findViewById(R.id.et_login);
        txtip = findViewById(R.id.txtipuserlogin);
        cb_terms = findViewById(R.id.cb_terms);
        tv_terms = findViewById(R.id.tv_terms);
        tv_terms.setPaintFlags(tv_terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        findViewById(R.id.card_go).setOnClickListener(this);
        findViewById(R.id.tv_terms).setOnClickListener(this);
        findViewById(R.id.ll_termsnconditions).setOnClickListener(this);
        Log.v(TAG,"--->> Application_ID "+ getAppUserType());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_go:
                if (getAppUserType().contains("admin") && isValidNumber(etUserLogin.getText().toString())){
                    startActivity(new Intent(ValidationActivity.this, UsersActivity.class));
                    finish();
                }else if (getAppUserType().contains("provider") && isValidNumber(etUserLogin.getText().toString())){
                    startActivity(new Intent(ValidationActivity.this, MainActivity.class));
                    finish();
                }else if (getAppUserType().contains("customer")) {
                    if (cb_terms.isChecked()) {
                        startActivity(new Intent(ValidationActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Please read terms and check the above box to continue", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.ll_termsnconditions:
                if (!cb_terms.isChecked()){
                    cb_terms.setChecked(true);
                }else{
                    cb_terms.setChecked(false);
                }
                break;
            case R.id.tv_terms:
                    startActivity(new Intent(ValidationActivity.this, WebViewActivity.class));
                break;
            case R.id.et_login:
                txtip.setHint("Enter Your Mobile Number");
                break;

        }
    }

    private boolean isValidNumber(String mobileNumber) {
        if (!TextUtils.isEmpty(mobileNumber) && mobileNumber.length() == 10){
            return true;
        }else{
            txtip.setHint("Invalid Number");
        }
        return false;
    }

    private String getAppUserType() {
        return getApplicationContext().getPackageName();
    }
}


