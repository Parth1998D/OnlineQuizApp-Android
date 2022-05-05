package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizapp.Common.Common;
import com.example.quizapp.Interface.ItemClickListener;
import com.example.quizapp.Interface.RankingCallBack;
import com.example.quizapp.Model.QuestionScore;
import com.example.quizapp.Model.Ranking;
import com.example.quizapp.ViewHolder.RankingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RankingFragment extends Fragment {
    View myFragment;
    FirebaseDatabase database;
    DatabaseReference rankingTbl;
//    DatabaseReference questionScore;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

//    int sum=0;

    public static RankingFragment newInstance()
    {
        RankingFragment rankingFragment=new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database=FirebaseDatabase.getInstance();
//        questionScore=database.getReference("Question_Score");
        rankingTbl=database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myFragment= inflater.inflate(R.layout.fragment_ranking,container,false);
        layoutManager=new LinearLayoutManager(getActivity());
        rankingList=(RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setHasFixedSize(true);
        rankingList.setLayoutManager(layoutManager);

//        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
//            @Override
//            public void callBack(Ranking ranking) {
//                rankingTbl.child(ranking.getUserName()).setValue(ranking);
//                //showRanking();
//            }
//        });

        adapter=new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,rankingTbl.orderByChild("score"))
        {
            @Override
            protected void populateViewHolder(RankingViewHolder rankingViewHolder, final Ranking ranking, int i)
            {
                rankingViewHolder.txt_name.setText(ranking.getUserName());
                rankingViewHolder.txt_score.setText(String.valueOf(ranking.getScore()));

                rankingViewHolder.setItemClickListener(new ItemClickListener()
                {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick)
                    {
                        Intent scoreDetail=new Intent(getActivity(),ScoreDetail.class);
                        scoreDetail.putExtra("viewUser",ranking.getUserName());
                        startActivity(scoreDetail);
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);
        return myFragment;
    }

//    private void updateScore(final String userName, final RankingCallBack<Ranking> callback) {
//        questionScore.orderByChild("user").equalTo(userName)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot data:dataSnapshot.getChildren())
//                        {
//                            QuestionScore ques=data.getValue(QuestionScore.class);
//                            sum+=Integer.parseInt(ques.getScore());
//                        }
//                        Ranking ranking=new Ranking(userName,sum);
//                        callback.callBack(ranking);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }


}

