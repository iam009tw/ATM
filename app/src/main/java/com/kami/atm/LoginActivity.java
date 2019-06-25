package com.kami.atm;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getName();
    private EditText edUserid;
    private EditText edPasswd;
    private CheckBox remindUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edUserid = findViewById(R.id.edUserid);
        edPasswd = findViewById(R.id.edPasswd);
        remindUserId = findViewById(R.id.ckRemindUserid);
        boolean ck_Userid = getSharedPreferences("ATM", MODE_PRIVATE)
                .getBoolean("REMEMBER_USERID", false);

        remindUserId.setChecked(ck_Userid);

        remindUserId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences("ATM", MODE_PRIVATE)
                        .edit()
                        .putBoolean("REMEMBER_USERID", true)
                        .commit();
            }
        });

//        getSharedPreferences("ATM", MODE_PRIVATE)
//                .edit()
//                .putInt("LEVEL", 3)
//                .commit();


        String Remind_userId = getSharedPreferences("ATM", MODE_PRIVATE)
                                    .getString("USERID", "");
        edUserid.setText(Remind_userId);
    }

    public void login(View view){
        final String userid = edUserid.getText().toString();
        final String passwd = edPasswd.getText().toString();
//        寫法一
// Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("user").child(userid).child("password");
////        myRef.setValue("12345");
//
//        myRef.getRef();
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String pw = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + pw);
//                if(pw.equals(passwd)){
//                            setResult(RESULT_OK);
//                            finish();
//                        }else{
//                            new AlertDialog.Builder(LoginActivity.this)
//                                    .setTitle("登入結果")
//                                    .setMessage("帳號密碼不符合!")
//                                    .setPositiveButton("OK", null)
//                                    .show();
//
//                        }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

//寫法二
        FirebaseDatabase.getInstance().getReference("user").child(userid).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange + dataSnapshot: " + dataSnapshot);
                        String pw = (String) dataSnapshot.getValue();
                        if(pw.equals(passwd)){
                            boolean ck_UserId = getSharedPreferences("ATM", MODE_PRIVATE)
                                    .getBoolean("REMEMBER_USERID", false);

                            if(ck_UserId) {
                                getSharedPreferences("ATM", MODE_PRIVATE)
                                        .edit()
                                        .putString("USERID", userid)
                                        .commit();
                            }
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登入結果")
                                    .setMessage("帳號密碼不符合!")
                                    .setPositiveButton("OK", null)
                                    .show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: "+databaseError);
                    }
                });


//        if("jack".equals(userid) && "1234".equals(passwd)){
//            setResult(RESULT_OK);
//            finish();
//        }else{
//            new AlertDialog.Builder(LoginActivity.this)
//                    .setTitle("登入結果")
//                    .setMessage("帳號密碼不符合!")
//                    .setPositiveButton("OK", null)
//                    .show();
//        }

    }

    public void cancel(View view){
        finish();
    }
}
