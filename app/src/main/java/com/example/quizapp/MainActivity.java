package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.example.quizapp.Common.Common.currentUser;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail, edtEmail, edtPassword;
    Button btnSignUp, btnSignIn;
    FirebaseDatabase database;
    DatabaseReference users;
    TextView trouble;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(!isTaskRoot())
        {
            finish();
        }
        else
        {
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            users = database.getReference("Users");
            currentUser = mAuth.getCurrentUser();

            if (currentUser != null)
            {
                final String username = currentUser.getDisplayName();
                Toast.makeText(MainActivity.this, "Welcome back " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                        Intent homeActivity = new Intent(MainActivity.this, Home.class);
                        startActivity(homeActivity);
                        finish();
            }
            else
            {
                // registerAlarm();
                setContentView(R.layout.activity_main);
                edtEmail = (MaterialEditText) findViewById(R.id.edtEmail);
                edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);

                btnSignIn = (Button) findViewById(R.id.btn_sign_in);
                btnSignUp = (Button) findViewById(R.id.btn_sign_up);

                btnSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSignUpDialog();
                    }
                });
                btnSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signIn(edtEmail.getText().toString(), edtPassword.getText().toString());
                    }
                });

                trouble = (TextView) findViewById(R.id.trouble);
                trouble.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, Trouble.class);
                        startActivity(i);
                    }
                });
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>()
//                {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task)
//                    {
//                        if (!task.isSuccessful())
//                        {
//                            Log.w("jhjhk", "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//                        Log.d("vgvg",token);
//                    }
//                });
            }
        }
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up").setMessage("Please enter all details");
        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);
        edtNewUser = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword = (MaterialEditText) sign_up_layout.findViewById(R.id.edtNewPassword);
        alertDialog.setView(sign_up_layout).setIcon(R.drawable.ic_account_circle_black_24dp);
        alertDialog.setNegativeButton("Cancel", null);
        alertDialog.setPositiveButton("Ok", null);

        final AlertDialog ad = alertDialog.create();
        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ad.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        final String U = edtNewUser.getText().toString();
                        final String E = edtNewEmail.getText().toString();
                        final String P = edtNewPassword.getText().toString();

                        if (U.isEmpty() || E.isEmpty() || P.isEmpty())
                            Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                        else
                        {
                            users.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(U).exists())
                                        Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                                    else
                                    {
                                        mAuth.createUserWithEmailAndPassword(E, P)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                                                {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task)
                                                    {
                                                        if (task.isSuccessful())
                                                        {
                                                            currentUser=mAuth.getCurrentUser();
                                                            ad.dismiss();
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(U)
                                                                    .build();
                                                            currentUser.updateProfile(profileUpdates)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                                                    {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                        {
                                                                            if (task.isSuccessful())
                                                                            {
                                                                                currentUser
                                                                                        .sendEmailVerification()
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                                                                        {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task)
                                                                                            {
                                                                                                if (task.isSuccessful())
                                                                                                {
                                                                                                    Toast.makeText(MainActivity.this, "Account created !", Toast.LENGTH_SHORT).show();
                                                                                                    users.child(U).setValue(U);
                                                                                                    Toast.makeText(MainActivity.this, "check your inbox for verification link", Toast.LENGTH_SHORT).show();
                                                                                                    mAuth.signOut();
                                                                                                }
                                                                                                else
                                                                                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                mAuth.signOut();
                                                                            }
                                                                        }
                                                                    });
                                                            mAuth.signOut();
                                                        }
                                                        else
                                                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
            }
        });
        ad.show();
    }

    private void signIn(final String email, final String pwd)
    {
        if (email.isEmpty() || pwd.isEmpty())
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        else
        {
            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                currentUser=mAuth.getCurrentUser();
                                if (currentUser.isEmailVerified())
                                {
                                    Toast.makeText(MainActivity.this, "Welcome back " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                                    Intent homeActivity = new Intent(MainActivity.this, Home.class);
                                    startActivity(homeActivity);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "your email id is not verified", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                }
                            }
                            else
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}

