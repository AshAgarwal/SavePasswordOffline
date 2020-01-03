package com.example.savepasswordoffline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    EditText mEditPassword;
    TextView mTogglePasword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mTogglePasword = (TextView) findViewById(R.id.txt_toggle_pass);

        mEditPassword.addTextChangedListener(this);
        mTogglePasword.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mEditPassword.getText().length() > 0){
            mTogglePasword.setVisibility(View.VISIBLE);
        } else {
            mTogglePasword.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_toggle_pass){
            if (mTogglePasword.getText().equals("SHOW")){
                mTogglePasword.setText("HIDE");
                mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mEditPassword.setSelection(mEditPassword.length());
            } else {
                mTogglePasword.setText("SHOW");
                mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mEditPassword.setSelection(mEditPassword.length());
            }
        }
    }
}
