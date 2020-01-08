package com.example.savepasswordoffline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class UserAccountAdapter extends BaseAdapter {

    private Activity activity;
    private List<AccountModelData> dataList;
    private LayoutInflater inflater;

    public static UpdateAccountDialog updateAccountDialog;
    public static AccountDialog accountDialog;

    public UserAccountAdapter(Activity activity, List<AccountModelData> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
        final TextView mAppName = (TextView) convertView.findViewById(R.id.appName);
        final TextView mUserName = (TextView) convertView.findViewById(R.id.userName);
        ImageButton btnEditData = (ImageButton) convertView.findViewById(R.id.updateData);
        ImageButton btnDeleteData = (ImageButton) convertView.findViewById(R.id.deleteData);
        TextView mShowData = (TextView) convertView.findViewById(R.id.txt_show);

        final AccountModelData model = dataList.get(position);
        mAppName.setText(model.getApplicationName());
        mUserName.setText(model.getAccountName());

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slid_left);
        convertView.setAnimation(animation);

        btnEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountID = model.getAccountID();
                updateAccountDialog = new UpdateAccountDialog(activity, accountID, mAppName.getText().toString(), mUserName.getText().toString(), model.getAccountPassword());
                updateAccountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                updateAccountDialog.setCancelable(true);
                updateAccountDialog.show();
            }
        });

        btnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setMessage("Are you Sure you want to Delete?");
                alertDialog.setCancelable(true);

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String accountID = model.getAccountID();

                        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference("tbl_user_account").child(accountID);

                        deleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(activity, "Account Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.setTitle("Delete Account");
                alertDialog1.show();
            }
        });

        mShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountId = model.getAccountID();

                accountDialog = new AccountDialog(activity, mAppName.getText().toString(), mUserName.getText().toString(), model.getAccountPassword());
                accountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                accountDialog.setCancelable(true);
                accountDialog.show();
            }
        });

        return convertView;
    }
}
