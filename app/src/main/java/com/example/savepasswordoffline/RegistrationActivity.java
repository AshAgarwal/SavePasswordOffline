package com.example.savepasswordoffline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    BroadcastReceiver broadcastReceiver;

    EditText mFirstName, mLastName, mEmailAddr, mEditPassword, mCnfPassword;
    Button btnSignUp;
    TextView mTogglePasword, mToggleCnfPassword, mPasswordMsg, invalidMessage;

    private static final Pattern ALPHABET_ONLY = Pattern.compile("^[a-zA-Z ]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    private Date currentTime;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        checkInternetConnectivity();
    }

    public void checkInternetConnectivity() {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int[] type = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE};
                if (InternetConnectionReceiver.isNetworkAvailable(context, type)) {
                    registrationActivity();
                } else {
                    Toast.makeText(context, "It seems that there is No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void registrationActivity() {
        /*Binding UI Widgets*/
        mFirstName = (EditText) findViewById(R.id.firstName);
        mLastName = (EditText) findViewById(R.id.lastName);
        mEmailAddr = (EditText) findViewById(R.id.emailAddr);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mCnfPassword = (EditText) findViewById(R.id.edit_cnf_password);
        mTogglePasword = (TextView) findViewById(R.id.txt_toggle_pass);
        mToggleCnfPassword = (TextView) findViewById(R.id.txt_toggle_cnf_pass);
        mPasswordMsg = (TextView) findViewById(R.id.passwordMessage);
        invalidMessage = (TextView) findViewById(R.id.invalidMessageRegist);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        progressDialog = new ProgressDialog(RegistrationActivity.this);
        currentTime = Calendar.getInstance().getTime();

        firebaseAuth = (FirebaseAuth) FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditPassword.getText().length() > 0) {
                    mTogglePasword.setVisibility(View.VISIBLE);
                } else {
                    mTogglePasword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCnfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCnfPassword.getText().length() > 0){
                    mToggleCnfPassword.setVisibility(View.VISIBLE);
                } else {
                    mToggleCnfPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*Set OnClickListener of Button and Clickable Objects in UI*/
        mTogglePasword.setOnClickListener(this);
        mToggleCnfPassword.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_toggle_pass) {

            if (mTogglePasword.getText().equals("SHOW")) {
                mTogglePasword.setText("HIDE");
                mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mEditPassword.setSelection(mEditPassword.length());
            } else {
                mTogglePasword.setText("SHOW");
                mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mEditPassword.setSelection(mEditPassword.length());
            }

        } else if (v.getId() == R.id.btnSignUp) {

            signUpActivity();

        } else if ( v.getId() == R.id.txt_toggle_cnf_pass){
            if (mToggleCnfPassword.getText().equals("SHOW")) {
                mToggleCnfPassword.setText("HIDE");
                mCnfPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                mCnfPassword.setSelection(mCnfPassword.length());
            } else {
                mToggleCnfPassword.setText("SHOW");
                mCnfPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mCnfPassword.setSelection(mCnfPassword.length());
            }
        }
    }

    /*When Button is Clicked*/
    public void signUpActivity() {

        if ( validateFirstName() && validateLastName() && validateEmailAddress() && validatePassword()){
            String userName = mFirstName.getText().toString().trim() + " " + mLastName.getText().toString().trim();
            userName.replaceAll("\\s+", "");
            String emailId = mEmailAddr.getText().toString().trim();
            String password = mEditPassword.getText().toString();

            mFirstName.setEnabled(false);
            mLastName.setEnabled(false);
            mEmailAddr.setEnabled(false);
            mEditPassword.setEnabled(false);
            mCnfPassword.setEnabled(false);
            btnSignUp.setEnabled(false);

            progressDialog.setMessage("Please Wait While we Create your Account..");
            progressDialog.show();

            /*IF VALIDATION SUCCESS*/
            firebaseAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        /*Authentication Success*/
                        sendEmailVerification();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mFirstName.setEnabled(true);
                    mLastName.setEnabled(true);
                    mEmailAddr.setEnabled(true);
                    mEditPassword.setEnabled(true);
                    mCnfPassword.setEnabled(true);
                    btnSignUp.setEnabled(true);
                    progressDialog.dismiss();

                    if (e.toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.")) {
                        invalidMessage.setVisibility(View.VISIBLE);
                    } else {
                        Log.e("Authentication Error: ", e.toString() + "");
                    }
                }
            });
        }
    }

    private void sendEmailVerification(){
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        /*Verification Email Send*/
                        registerData();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mFirstName.setEnabled(true);
                    mLastName.setEnabled(true);
                    mEmailAddr.setEnabled(true);
                    mEditPassword.setEnabled(true);
                    mCnfPassword.setEnabled(true);
                    progressDialog.dismiss();

                    Log.e("Verification Error:", e.toString() + "");
                }
            });
        } else {
            mFirstName.setEnabled(true);
            mLastName.setEnabled(true);
            mEmailAddr.setEnabled(true);
            mEditPassword.setEnabled(true);
            mCnfPassword.setEnabled(true);
            progressDialog.dismiss();
        }
    }

    private void registerData(){
        String userID = firebaseAuth.getUid();
        String userName = mFirstName.getText().toString().trim() + " " + mLastName.getText().toString().trim();
        userName.replaceAll("\\s+", "");
        String emailId = mEmailAddr.getText().toString().trim();
        String Pass = mEditPassword.getText().toString();
        String createDate = currentTime.toString();
        String modifyDate = currentTime.toString();

        databaseReference = firebaseDatabase.getReference("tbl_Users").child(userID);
        UserModelData modelData = new UserModelData(userID, userName, emailId, createDate, modifyDate, "No");
        databaseReference.setValue(modelData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                firebaseAuth.signOut();
                Toast.makeText(RegistrationActivity.this, "Registration Successfull.Please Verify Email before Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrationActivity.this, LoginActitvity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mFirstName.setEnabled(true);
                mLastName.setEnabled(true);
                mEmailAddr.setEnabled(true);
                mEditPassword.setEnabled(true);
                mCnfPassword.setEnabled(true);
                progressDialog.dismiss();
                Log.e("Is Regist Success: ?", "No " + e.toString());

            }
        });
    }

    public boolean validateFirstName() {
        /*Validation of First Name*/
        String fname = mFirstName.getText().toString().trim();

        /*
        *If First Name is Empty or First Name contains not only Alphabets then return false
        * Else Return True
        */
        if (TextUtils.isEmpty(fname)) {
            mFirstName.setError("Enter First Name");
            return false;
        } else if (!ALPHABET_ONLY.matcher(fname).matches()) {
            mFirstName.setError("First Name Should Be Valid");
            return false;
        } else {
            mFirstName.setError(null);
            return true;
        }
    }

    public boolean validateLastName() {
        /*Validation of Last Name*/
        String lname = mLastName.getText().toString().trim();

        /*
         *If Last Name is Empty or Last Name contains not only Alphabets then return false
         * Else Return True
         */
        if (TextUtils.isEmpty(lname)) {
            mLastName.setError("Enter Last Name");
            return false;
        } else if (!ALPHABET_ONLY.matcher(lname).matches()) {
            mLastName.setError("Last Name Should Be Valid");
            return false;
        } else {
            mLastName.setError(null);
            return true;
        }
    }

    public boolean validateEmailAddress() {
        /*Validation of Email Address*/
        String emailAddr = mEmailAddr.getText().toString().trim();

        /*
        * If Email Address is Empty or the given String is not in a Expression of General Email Address Format
        * then return false
        * else return true
        */
        if (TextUtils.isEmpty(emailAddr)) {
            mEmailAddr.setError("Enter Email Address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddr).matches()) {
            mEmailAddr.setError("Email Address Should be Valid");
            return false;
        } else {
            mEmailAddr.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){
        /*Validation for User Password*/
        String pass = mEditPassword.getText().toString();
        String cpass = mCnfPassword.getText().toString();

        /*
        * If password isn't matches the given password format or expression then return false
        * If Confirm Password doesn't matches with Password then return false
        * Else return true
        */
        if ( !PASSWORD_PATTERN.matcher(pass).matches() ){
            mPasswordMsg.setVisibility(View.VISIBLE);
            return false;
        } else if ( !cpass.equals(pass) ){
            mCnfPassword.setError("Enter the Same Password");
            mToggleCnfPassword.setVisibility(View.GONE);
            return false;
        } else {
            mPasswordMsg.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
