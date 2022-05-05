package com.example.quizapp.Common;

import android.graphics.Bitmap;

import com.example.quizapp.Model.Question;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;

public class Common {
    public static String categoryId,categoryName,categoryBg;
    //public static User currentUser;
    public static List<Question> questionList=new ArrayList<>();
    public static Bitmap bmap;
    public static FirebaseUser currentUser;
}
