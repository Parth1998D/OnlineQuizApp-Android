package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizapp.Common.Common;
import com.example.quizapp.Interface.RankingCallBack;
import com.example.quizapp.Model.QuestionScore;
import com.example.quizapp.Model.Ranking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.quizapp.Common.Common.currentUser;

public class Done extends AppCompatActivity
{
    Button btnTryAgain,btnExit;
    TextView txtResultScore,getTxtResultQuestion,signout;
    ProgressBar progressBar;
    int sum=0;

    FirebaseDatabase database;
    DatabaseReference question_score,rankingTbl;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        database=FirebaseDatabase.getInstance();
        question_score=database.getReference("Question_Score");
        rankingTbl=database.getReference("Ranking");

        LinearLayout ll=(LinearLayout)findViewById(R.id.done);

        Drawable image=new BitmapDrawable(getBaseContext().getResources(),Common.bmap);
        ll.setBackground(image);

        txtResultScore=(TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion=(TextView)findViewById(R.id.txtTotalQuestion);
        progressBar=(ProgressBar)findViewById(R.id.doneProgressBar);
        btnTryAgain=(Button)findViewById(R.id.btnTryAgain);
        btnExit=(Button)findViewById(R.id.btnExit);
        signout=(TextView)findViewById(R.id.signout);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder b;
                b = new AlertDialog.Builder(Done.this);
                b.setMessage("Are you sure you want to exit?")
                        .setPositiveButton("exit", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface di, int i)
                            {
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("cancel",null);
                AlertDialog ad=b.create();
                ad.show();
            }
        });

        signout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
                startActivity(new Intent(Done.this,MainActivity.class));
            }
        });

        Bundle extra=getIntent().getExtras();
        if(extra!=null)
        {
            int score=extra.getInt("SCORE");
            int totalQuestion=extra.getInt("TOTAL");
            int correctAnswer=extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d",score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d",correctAnswer,totalQuestion) );

            progressBar.setMax(totalQuestion);
            progressBar.setProgress(correctAnswer);

            question_score.child(String.format("%s_%s", currentUser.getDisplayName(),Common.categoryId))
                    .setValue(new QuestionScore(correctAnswer+"/"+totalQuestion,
                            currentUser.getDisplayName(),String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));


            updateScore(currentUser.getDisplayName(), new RankingCallBack<Ranking>() {
                @Override
                public void callBack(Ranking ranking) {
                    rankingTbl.child(ranking.getUserName()).setValue(ranking);
                }
            });

        }
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callback)
    {
        question_score.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            QuestionScore ques=data.getValue(QuestionScore.class);
                            sum+=Integer.parseInt(ques.getScore());
                        }
                        Ranking ranking=new Ranking(userName,sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

//    public void onBackPressed()
//    {
//        AlertDialog.Builder b;
//        b = new AlertDialog.Builder(Done.this);
//        b.setMessage("Are you sure you want to exit?")
//                .setPositiveButton("exit", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface di, int i)
//                    {
//                        finishAffinity();
//                    }
//                })
//                .setNegativeButton("cancel",null);
//        AlertDialog ad=b.create();
//        ad.show();
//    }

}
