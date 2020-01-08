package com.example.savepasswordoffline;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class UpdateAccountDialog extends Dialog {

    private Activity activity;
    private String accountID, applicatioName, userName, userPassword;

    TextView dialogTitle;
    EditText applicationName, userid, userPass;
    Button btnAddAccount;

    private Date currentTime;

    public UpdateAccountDialog(Activity activity, String accountID, String appName, String userName, String accountPassword) {
        super(activity);
        this.activity = activity;
        this.accountID = accountID;
        this.applicatioName = appName;
        this.userName = userName;
        this.userPassword = accountPassword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_add_dialog_layout);

        dialogTitle = (TextView) findViewById(R.id.dialog_Title);
        applicationName = (EditText) findViewById(R.id.applicationName);
        userid = (EditText) findViewById(R.id.userid);
        userPass = (EditText) findViewById(R.id.userPass);
        btnAddAccount = (Button) findViewById(R.id.btnAddAccount);

        currentTime = Calendar.getInstance().getTime();

        btnAddAccount.setText("Update");
        dialogTitle.setText("Update Account");
        applicationName.setText(applicatioName);
        userid.setText(userName);
        userPass.setText(userPassword);

        applicationName.setEnabled(false);

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditText(userid) && validateEditText(userPass)) {
                    DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("tbl_user_account").child(accountID);
                    updateRef.child("accountName").setValue(userid.getText().toString().trim());
                    updateRef.child("accountPassword").setValue(userPass.getText().toString().trim());
                    updateRef.child("accountUpdateDate").setValue(currentTime.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            UserAccountAdapter.updateAccountDialog.dismiss();
                            Toast.makeText(activity, "Update Successfull", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validateEditText(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Enter Application Name");
            return false;
        } else {
            return true;

        }
    }
}
