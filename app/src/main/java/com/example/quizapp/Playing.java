package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quizapp.Common.Common;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;


public class Playing extends AppCompatActivity implements View.OnClickListener
{
    final static long INTERVAL=1000,TIMEOUT=15000;
    int progressValue=0;
    Vibrator v;
    CountDownTimer mCountDown;
    int index=0,score=0,thisQuestion=0,totalQuestion,correctAnswer;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA,btnB,btnC,btnD,btncorrect;
    TextView txtScore,txtQuestionNum,question_text,show;
    String hint,ans,gotext,helplink;
    ImageButton igo,help;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        gotext="hint";
        v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        Common.bmap= BitmapFactory.decodeResource(getBaseContext().getResources(),R.drawable.bg2);
        new DownloadImageTask((RelativeLayout) findViewById(R.id.rl))
                .execute(Common.categoryBg);

        txtScore=(TextView)findViewById(R.id.txtScore);
        txtQuestionNum=(TextView)findViewById(R.id.txtTotalQuestion);
        question_text=(TextView)findViewById(R.id.question_text);
        question_image=(ImageView)findViewById(R.id.question_image);
        show=(TextView)findViewById(R.id.show);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        btnA=(Button)findViewById(R.id.btnOptionA);
        btnB=(Button)findViewById(R.id.btnOptionB);
        btnC=(Button)findViewById(R.id.btnOptionC);
        btnD=(Button)findViewById(R.id.btnOptionD);

        igo=(ImageButton)findViewById(R.id.igo);
        help=(ImageButton)findViewById(R.id.help);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

        igo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(gotext.equals("hint"))
                {
                    igo.setImageResource(R.drawable.h5);
                    igo.setElevation(10);
                    show.setVisibility(View.VISIBLE);
                    //help.setVisibility(View.VISIBLE);
                }
                else
                    showQuestion(++index);
            }
        });

        help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Intent i=new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(helplink));
//                startActivity(i);
                Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(helplink));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setPackage("com.android.chrome");
                try
                {
                    startActivity(i);
                }
                catch (ActivityNotFoundException ex)
                {
                    i.setPackage(null);
                    startActivity(i);
                }
            }
        });

        totalQuestion=Common.questionList.size();
        mCountDown=new CountDownTimer(TIMEOUT,INTERVAL)
        {
            @Override
            public void onTick(long milisec)
            {
                progressBar.setProgress(++progressValue);
            }

            @Override
            public void onFinish()
            {
                btnA.setEnabled(false);
                btnB.setEnabled(false);
                btnC.setEnabled(false);
                btnD.setEnabled(false);
                progressBar.setProgress(15);
                btncorrect.setBackgroundColor(Color.parseColor("#555555"));
                show.setVisibility(View.VISIBLE);
                if(helplink!=null)
                    help.setVisibility(View.VISIBLE);
                igo.setImageResource(R.drawable.f3);
                gotext="next";
            }
        };

        showQuestion(index);
    }

    @Override
    public void onClick(View view)
    {
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
        mCountDown.cancel();
        if (index < totalQuestion)
        {
            Button clickedButton = (Button) view;
            if (clickedButton.getText().equals(ans))
            {
                igo.setImageResource(R.drawable.c1);
                igo.setEnabled(false);
                clickedButton.setBackgroundColor(Color.parseColor("#66BB6A"));        //#81C784
                score += 10;
                correctAnswer++;

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                showQuestion(++index);
//                            }
//                        },2000);
//                    }
//                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showQuestion(++index);
                    }
                },1300);

            }
            else
            {
                clickedButton.setBackgroundColor(Color.parseColor("#FF0000"));
                btncorrect.setBackgroundColor(Color.parseColor("#66BB6A"));
                v.vibrate(400);
                show.setVisibility(View.VISIBLE);
                if(helplink!=null)
                    help.setVisibility(View.VISIBLE);
                igo.setImageResource(R.drawable.w1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        igo.setImageResource(R.drawable.f3);
                    }
                },2000);

                gotext="next";
            }
            txtScore.setText(String.format("%d",score));
        }
    }

    private void showQuestion(int index)
    {
        if(index<totalQuestion)
        {
            btnA.setBackgroundColor(Color.parseColor("#ebFF8A80"));
            btnB.setBackgroundColor(Color.parseColor("#ebFF8A80"));
            btnC.setBackgroundColor(Color.parseColor("#ebFF8A80"));
            btnD.setBackgroundColor(Color.parseColor("#ebFF8A80"));
            show.setVisibility(View.GONE);
            help.setVisibility(View.GONE);

            igo.setElevation(0);
            igo.setImageResource(R.drawable.h4);
            gotext="hint";
            thisQuestion++;
            txtQuestionNum.setText((index+1)+" / "+totalQuestion);
            progressBar.setProgress(0);
            progressValue=0;

            if(Common.questionList.get(index).getIsImageQuestion().equals("true"))
            {
                Picasso.with(getBaseContext()).load(Common.questionList.get(index).getQuestion()).into(question_image);
                question_text.setVisibility(View.GONE);
                question_image.setVisibility(View.VISIBLE);
            }
            else
            {
                question_text.setText(Common.questionList.get(index).getQuestion());
                question_image.setVisibility(View.GONE);
                question_text.setVisibility(View.VISIBLE);
            }
            btnA.setText(Common.questionList.get(index).getOptionA());
            btnB.setText(Common.questionList.get(index).getOptionB());
            btnC.setText(Common.questionList.get(index).getOptionC());
            btnD.setText(Common.questionList.get(index).getOptionD());
            btnA.setEnabled(true);
            btnB.setEnabled(true);
            btnC.setEnabled(true);
            btnD.setEnabled(true);
            igo.setEnabled(true);

            mCountDown.start();

            ans=Common.questionList.get(index).getCorrectAnswer();
            hint=Common.questionList.get(index).getHint();
            helplink=Common.questionList.get(index).getHelp();
            show.setText(hint);

            if(btnA.getText().equals(ans))
                btncorrect=btnA;
            else if(btnB.getText().equals(ans))
                btncorrect=btnB;
            else if(btnC.getText().equals(ans))
                btncorrect=btnC;
            else if(btnD.getText().equals(ans))
                btncorrect=btnD;
        }
        else
        {
            Intent intent=new Intent(Playing.this,Done.class);
            Bundle dataSend=new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        totalQuestion=Common.questionList.size();
//        mCountDown=new CountDownTimer(TIMEOUT,INTERVAL)
//        {
//                @Override
//                public void onTick(long milisec)
//                {
//                 progressBar.setProgress(++progressValue);
//                }
//
//            @Override
//            public void onFinish()
//            {
//                btnA.setEnabled(false);
//                btnB.setEnabled(false);
//                btnC.setEnabled(false);
//                btnD.setEnabled(false);
//                progressBar.setProgress(15);
//                btncorrect.setBackgroundColor(Color.parseColor("#212121"));
//                show.setVisibility(View.VISIBLE);
//                help.setVisibility(View.VISIBLE);
//                igo.setImageResource(R.drawable.f3);
//                gotext="next";
//            }
//        };
//        showQuestion(index);
//    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        RelativeLayout rl;
        Button b=(Button)findViewById(R.id.btnPlay);
        public DownloadImageTask(RelativeLayout rl) {
            this.rl = rl;
        }

        protected Bitmap doInBackground(String... urls)
        {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {}
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result)
        {
            Drawable image=new BitmapDrawable(getBaseContext().getResources(),result);
            rl.setBackground(image);
            Common.bmap=result.copy(result.getConfig(),true);
        }
    }

}