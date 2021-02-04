package com.beshoApps.islamy

import android.media.MediaPlayer
import android.widget.Button

class Questions(var id: Int, var ques: String, var ans1: String, var ans2: String, var ans3: String,
              var ans4: String, var correct: String)

class QuesTrueFalse( var ques: String,var ans:String, var correct: String)

class Names(var ques: String, var correct: String, var a1:String, var a2:String,var a3:String,
            var a4:String,var a5:String,var a6:String,var a7:String, var a8:String,
            var a9:String, var a10:String)

class myAhadith(var hadith:String , var sanad:String)

//////////Recycler_View ////////

data class Data(var hadithRecycler:String ,var hadithNum:String,var mediaPlayer: MediaPlayer,var button: Button)

