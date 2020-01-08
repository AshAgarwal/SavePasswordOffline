package com.example.savepasswordoffline;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

class AddNewAccountDialog extends Dialog {
    private Activity activity;
    private String userID;

    EditText applicationName, userid, userPass;
    Button btnAddAccount;

    private Date currentTime;

    AddNewAccountDialog(MainActivity mainActivity, String userId) {
        super(mainActivity);

        this.activity = mainActivity;
        this.userID = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_add_dialog_layout);

        applicationName = (EditText) findViewById(R.id.applicationName);
        userid = (EditText) findViewById(R.id.userid);
        userPass = (EditText) findViewById(R.id.userPass);
        btnAddAccount = (Button) findViewById(R.id.btn_AddAcc);

        currentTime = Calendar.getInstance().getTime();

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( validateEditText(applicationName) && validateEditText(userid) && validateEditText(userPass) ){
                    DatabaseReference addAccRef = FirebaseDatabase.getInstance().getReference("tbl_user_account").child(userID);
                    String userAccountID = addAccRef.push().getKey();

                    AccountModelData modelData = new AccountModelData(userAccountID, applicationName.getText().toString().trim(), userid.getText().toString().trim(), userPass.getText().toString().trim(), currentTime.toString(), currentTime.toString() );
                    addAccRef.child(userAccountID).setValue(modelData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            MainActivity.addNewAccountDialog.dismiss();
                            Toast.makeText(activity, "Your Account Successfully Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public boolean validateEditText(EditText editText){
        if (editText.getText().toString().isEmpty()){
            editText.setError("Enter Application Name");
            return false;
        } else {
            return true;
        }
    }

}
