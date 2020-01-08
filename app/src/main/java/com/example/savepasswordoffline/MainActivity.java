package com.example.savepasswordoffline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
                String userId = firebaseAuth.getUid();
                addNewAccountDialog = new AddNewAccountDialog(MainActivity.this, userId);
                addNewAccountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addNewAccountDialog.setCancelable(true);
                addNewAccountDialog.show();
            }
        });
    }
}
