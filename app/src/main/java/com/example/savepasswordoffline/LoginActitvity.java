package com.example.savepasswordoffline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActitvity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actitvity);

        txtsignup = (TextView) findViewById(R.id.txt_sign_up);

        txtsignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_sign_up){
            startActivity(new Intent(LoginActitvity.this, RegistrationActivity.class));
        }
    }
}
