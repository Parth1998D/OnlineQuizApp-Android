package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Trouble extends AppCompatActivity
{
    TextView reset;
    FirebaseDatabase database;
    DatabaseReference users;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trouble);

        reset=(TextView)findViewById(R.id.reset);
        final EditText emailid=(EditText)findViewById(R.id.email);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");

        final Button submit=(Button)findViewById(R.id.submit);

        emailid.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable)
            {
                submit.setVisibility(View.VISIBLE);
                reset.setVisibility(View.GONE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                submit.setVisibility(View.GONE);
                reset.setVisibility(View.VISIBLE);
            }
        });

        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                auth.sendPasswordResetEmail(emailid.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Trouble.this, "Check Your Inbox", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else
                                    Toast.makeText(Trouble.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
