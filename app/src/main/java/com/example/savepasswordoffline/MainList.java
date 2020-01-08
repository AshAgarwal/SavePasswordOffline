package com.example.savepasswordoffline;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainList extends Fragment {
    private View view;
    ListView accountList;
    LinearLayout notFoundLayout;

    private FirebaseDatabase accountDatabase;
    private DatabaseReference accountReference;
    private FirebaseAuth userAuthenticationID;

    UserAccountAdapter adapter;
    List<AccountModelData> dataList = new ArrayList<>();



    public MainList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_list, container, false);
        accountList = (ListView) view.findViewById(R.id.accountList);
        notFoundLayout = (LinearLayout) view.findViewById(R.id.notFoundLayout);

        accountDatabase = FirebaseDatabase.getInstance();
        accountReference = accountDatabase.getReference("tbl_user_account");
        userAuthenticationID = FirebaseAuth.getInstance();

        adapter = new UserAccountAdapter(getActivity(), dataList);
        accountList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        accountReference.orderByChild("userID").equalTo(userAuthenticationID.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();

                if (dataSnapshot.exists()) {
                    notFoundLayout.setVisibility(View.GONE);
                    accountList.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AccountModelData modelData = snapshot.getValue(AccountModelData.class);
                        dataList.add(modelData);
                    }

                    adapter.notifyDataSetChanged();

                } else {
                    notFoundLayout.setVisibility(View.VISIBLE);
                    accountList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        accountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
