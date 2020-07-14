package com.service.car.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.service.car.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_terms;
    private CheckBox cb_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cb_terms = findViewById(R.id.cb_terms);
        tv_terms = findViewById(R.id.tv_terms);
        tv_terms.setPaintFlags(tv_terms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        findViewById(R.id.card_go).setOnClickListener(this);
        findViewById(R.id.tv_terms).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_go:
                if (cb_terms.isChecked()){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "Please read terms and check the above box to continue", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_terms:
                startActivity(new Intent(SplashActivity.this,UsersActivity.class));
                break;
        }
    }
}


