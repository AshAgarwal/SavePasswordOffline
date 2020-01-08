package com.example.savepasswordoffline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActitvity extends AppCompatActivity implements View.OnClickListener {

    private BroadcastReceiver broadcastReceiver;
    private EditText mEmailAddr, mPassword;
    private Button btnSignIn;
    private TextView txtsignup, txtInvalidMsg, txtForgetPass;
    private TextInputLayout passwordToggle;

    private FirebaseAuth firebaseAuthLogin;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actitvity);
        checkInternetActivity();
    }

    private void checkInternetActivity() {
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int[] type = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE};

                if (InternetConnectionReceiver.isNetworkAvailable(context, type)) {
                    Toast.makeText(LoginActitvity.this, "Connected", Toast.LENGTH_SHORT).show();
                    loginActivity();
                } else {
                    Toast.makeText(context, "Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);

    }

    public void loginActivity(){
        firebaseAuthLogin = (FirebaseAuth) FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("tbl_Users");

        firebaseUser = firebaseAuthLogin.getCurrentUser();

        if (firebaseUser != null){
            firebaseAuthLogin.signOut();
        }

        if ( !checkUserSignInOrNot() ){
            mEmailAddr = (EditText) findViewById(R.id.login_email);
            mPassword = (EditText) findViewById(R.id.login_password);
            txtInvalidMsg = (TextView) findViewById(R.id.invalidPass);
            txtForgetPass = (TextView) findViewById(R.id.forgotPass);
            txtsignup = (TextView) findViewById(R.id.txt_sign_up);
            btnSignIn = (Button) findViewById(R.id.btnSignIn);
            passwordToggle = (TextInputLayout) findViewById(R.id.inputLayoutPassword);

            progressDialog = new ProgressDialog(this);

            mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    passwordToggle.setPasswordVisibilityToggleEnabled(true);
                    mPassword.setError(null);
                }
            });

            txtsignup.setOnClickListener(this);
            txtForgetPass.setOnClickListener(this);
            btnSignIn.setOnClickListener(this);
        }
    }

    private boolean checkUserSignInOrNot(){
        firebaseUser = firebaseAuthLogin.getCurrentUser();

        if (firebaseUser != null){
            Boolean userIsVerfied = firebaseUser.isEmailVerified();

            if (userIsVerfied){
                String userID = firebaseUser.getUid();
                databaseReference.child(userID).child("userIsVerified").setValue("Yes");
                startActivity(new Intent(LoginActitvity.this, MainActivity.class));
                finish();
                return true;
            } else {
                Toast.makeText(this, "Please Verify your Email First..", Toast.LENGTH_SHORT).show();
                firebaseAuthLogin.signOut();
                return false;
            }
        } else {
            return false;
        }
    }

    private void getSignIn(){
        if (validateEmailAddress() && validatePassword()){
            progressDialog.setMessage("Please Wait.....");
            progressDialog.show();

            mEmailAddr.setEnabled(false);
            mPassword.setEnabled(false);
            btnSignIn.setEnabled(false);

            String email = mEmailAddr.getText().toString().trim();
            String password = mPassword.getText().toString();

            firebaseAuthLogin.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.dismiss();
                    if (!checkUserSignInOrNot()){
                        mEmailAddr.setEnabled(true);
                        mPassword.setEnabled(true);
                        btnSignIn.setEnabled(true);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    mEmailAddr.setEnabled(true);
                    mPassword.setEnabled(true);
                    btnSignIn.setEnabled(true);
                    txtInvalidMsg.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public boolean validateEmailAddress() {
        String email = mEmailAddr.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mEmailAddr.setError("Please Enter Email Address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailAddr.setError("Please Enter Valid Email");
            return false;
        } else {
            mEmailAddr.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            passwordToggle.setPasswordVisibilityToggleEnabled(false);
            mPassword.setError("Please Enter Password");
            return false;
        } else {
            passwordToggle.setPasswordVisibilityToggleEnabled(true);
            return true;
        }
    }

    private void forgotPassword(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActitvity.this);
        alertDialog.setMessage("Are you Sure you want to reset your Password?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = mEmailAddr.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActitvity.this, "Enter Email to Reset Password", Toast.LENGTH_SHORT).show();
                    mEmailAddr.setFocusable(true);
                } else {
                    if (validateEmailAddress()){
                        firebaseAuthLogin.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Snackbar.make(findViewById(android.R.id.content).getRootView(), "Email Sent. Please Reset Your Password", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.setTitle("Reset Password");
        alertDialog1.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_sign_up){
            startActivity(new Intent(LoginActitvity.this, RegistrationActivity.class));
            finish();
        } else if (v.getId() == R.id.btnSignIn){
            getSignIn();
        } else if (v.getId() == R.id.forgotPass){
            forgotPassword();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
