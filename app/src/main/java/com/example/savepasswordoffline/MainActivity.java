package com.example.savepasswordoffline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    FloatingActionButton addPassword;
    public static AddNewAccountDialog addNewAccountDialog;

    DatabaseReference addReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        addReference = FirebaseDatabase.getInstance().getReference();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        addPassword = (FloatingActionButton) findViewById(R.id.addPassword);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainList());
        viewPager.setAdapter(adapter);

        addPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = firebaseAuth.getUid();
                addNewAccountDialog = new AddNewAccountDialog(MainActivity.this, userId);
                addNewAccountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addNewAccountDialog.setCancelable(true);
                addNewAccountDialog.show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage("Are you sure you want to Exit?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Yes & Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.setTitle("Save Password Offline");
        alertDialog1.show();
    }
}
