package com.example.savepasswordoffline;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class AccountDialog extends Dialog {

    private Activity activity;
    private String appName, user, password;

    EditText applicationName, userid, userPass;
    ImageButton copyUserData, copyUserPass;

    public AccountDialog(Activity activity, String appName, String user, String accountPassword) {
        super(activity);
        this.activity = activity;
        this.appName = appName;
        this.user = user;
        this.password = accountPassword;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.account_dialog_layout);

        applicationName = (EditText) findViewById(R.id.applicationName);
        applicationName.setEnabled(false);
        userid = (EditText) findViewById(R.id.userid);
        userid.setEnabled(false);
        userPass = (EditText) findViewById(R.id.userPass);
        userPass.setEnabled(false);

        copyUserData = (ImageButton) findViewById(R.id.copyUserName);
        copyUserPass = (ImageButton) findViewById(R.id.copyUserPass);

        applicationName.setText(appName);
        userid.setText(user);
        userPass.setText(password);

        copyUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", userid.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(activity, "Your Account Name is Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        copyUserPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", userPass.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(activity, "Your Account Password is Copied!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
