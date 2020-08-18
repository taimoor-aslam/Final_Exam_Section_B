package com.example.finalexamsectionb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText email,passcode;
    TextView signup,txt;
    Button login_btn;
    List<UserData> userData=new ArrayList<>();
    DatabaseReference mReferences;
    @Override
    public void onStart() {
        super.onStart();
        mReferences=FirebaseDatabase.getInstance().getReference("seller");
        try {
            mReferences.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).show();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        UserData myListData=data.getValue(UserData.class);
                        userData.add(myListData);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Sorry! Data couldn't be Read from database", Toast.LENGTH_LONG).show();
                }

            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText) findViewById(R.id.emaailedt);
        passcode=(EditText) findViewById(R.id.passcodeedt);
        signup=(TextView) findViewById(R.id.singuptxt);
        login_btn=(Button) findViewById(R.id.loginbtn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String Email=email.getText().toString();
                    String pass=passcode.getText().toString();
                    if(verify(Email,pass))
                    {
                       // loadFragment(new UserDataFragment());
                        // Toast.makeText(getContext(),"load fragment",Toast.LENGTH_SHORT).show();
                       // Intent it=new Intent(getApplicationContext())
                        Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                        Intent it=new Intent(getApplicationContext(),MarketingDetails.class);
                        startActivity(it);
                    }
                    else {
                        email.setError("wrong email");
                        passcode.setError("wrong password");
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString()+"abc",Toast.LENGTH_SHORT).show();
                }


            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),SignUp.class);
                startActivity(it);
            }
        });


    }
    public boolean verify(final String email, final String passcode)
    {

        //  Toast.makeText(getContext(),userData.size(),Toast.LENGTH_SHORT).show();
        //  Log.d("email1",userData.get(0).getmEmail());
        boolean flag=false;
        for(int i=0;i<userData.size();i++)
        {

            if(email.equals(userData.get(i).getmEmail())&&passcode.equals(userData.get(i).getmPasscode()))
            {
                return true;
            }
        }
        // Log.d("flag",String.valueOf(flag));
        return flag;
    }
}