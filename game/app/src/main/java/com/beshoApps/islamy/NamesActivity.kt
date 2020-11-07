package com.beshoApps.islamy

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.beshoApps.islamy.AdsClass.Companion.MyRewardedAd
import com.beshoApps.islamy.AdsClass.Companion.bannerAd
import com.beshoApps.islamy.AdsClass.Companion.loadRewardedVideoAd
import com.beshoApps.islamy.AdsClass.Companion.mAdView
import com.beshoApps.islamy.AdsClass.Companion.mInterstitialAd
import com.beshoApps.islamy.AdsClass.Companion.mRewardedVideoAd
import com.beshoApps.islamy.AdsClass.Companion.mobileAds
import com.beshoApps.islamy.Dialogs.Companion.getBool
import com.beshoApps.islamy.Dialogs.Companion.getInt
import com.beshoApps.islamy.Dialogs.Companion.getString
import com.beshoApps.islamy.Dialogs.Companion.playAds
import com.beshoApps.islamy.Dialogs.Companion.remove
import com.beshoApps.islamy.Dialogs.Companion.saveBool
import com.beshoApps.islamy.Dialogs.Companion.saveInt
import com.beshoApps.islamy.Dialogs.Companion.saveString
import com.beshoApps.islamy.QuestionsActivity.Companion.openDialog
import com.beshoApps.islamy.QuestionsActivity.Companion.points
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_names.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.io.FileOutputStream
import java.io.OutputStream

class NamesActivity : AppCompatActivity(), RewardedVideoAdListener {

    private var mProductList: List<Names>? = null
    private var mDBHelper: DatabaseHelper? = null
    private var myButtons :ArrayList<Button> = arrayListOf()
    lateinit var mediaPlayerC: MediaPlayer
    lateinit var mediaPlayerClick: MediaPlayer
    private var myAns=""
    var correct=false
    var counter=0
    var help=false
    var videoLoaded=false

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_names)

        mAdView = findViewById(R.id.banner_Names)
        bannerAd()
        mobileAds(this)
        MyRewardedAd(this)
        mRewardedVideoAd!!.rewardedVideoAdListener = this


        mDBHelper = DatabaseHelper(this)

        val database = applicationContext.getDatabasePath(DatabaseHelper.DBNAME)
        if (!database.exists()) {
            mDBHelper!!.readableDatabase
            //Copy db
            copyDatabase(this)
        }

        //Get product list in db when db exists
        mProductList = mDBHelper!!.listProduct3
        ///////////////////

        myButtons= arrayListOf(bu_A1,bu_A2,bu_A3,bu_A4,bu_A5,bu_A6,bu_A7,bu_A8,bu_A9,bu_A10)


        mediaPlayerC = MediaPlayer.create(this, R.raw.correctt)
        mediaPlayerClick = MediaPlayer.create(this, R.raw.click)

        tv_points.text= points.toString()
        tv_theGames.visibility= View.GONE


        //YoYo.with(Techniques.FadeOut).onEnd { restore() }.duration(10).playOn(tv_theGames)


        count3= getInt(this,"count3",0)/////////
        counter= getInt(this,"counter",0)
        myAns= getString(this,"myAns","").toString()
        correct= getBool(this,"correct_Names",false)
        setQuestions()
        answer_Names.text=myAns

        if (correct){
            YoYo.with(Techniques.FadeOut).onEnd { notClickable()
                bu_Next_Names.isClickable=false}.duration(10).playOn(tv_theGames)
            YoYo.with(Techniques.Flash).onEnd { bu_Next_Names.isClickable=true }.repeat(0).duration(4000).playOn(bu_Next_Names)
        }else{
            YoYo.with(Techniques.FadeOut).onEnd { restore() }.duration(10).playOn(tv_theGames)
        }

        bu_back.setOnClickListener(){
            mediaPlayerClick.start()
            val intent= Intent(this, GamesActivity::class.java)
            if (playAds){
                mInterstitialAd!!.adListener = object: AdListener() {
                    override fun onAdClosed() {
                        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                        startActivity(intent)
                    }
                }
                if (mInterstitialAd!!.isLoaded) {
                    mInterstitialAd!!.show()
                } else {
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_left, R.anim.slide_right)
                }
            }else{
                startActivity(intent)
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right)
            }
        }

        iv_points_toolbar.setOnClickListener(){
            mediaPlayerClick.start()
            openDialog(this, tv_points, mediaPlayerClick) { clickable() }
        }
        tv_points.setOnClickListener(){
            mediaPlayerClick.start()
            openDialog(this, tv_points, mediaPlayerClick) { clickable() }
        }


        bu_A1.setOnClickListener(){
            answers(bu_A1)
        }
        bu_A2.setOnClickListener(){
            answers(bu_A2)
        }
        bu_A3.setOnClickListener(){
            answers(bu_A3)
        }
        bu_A4.setOnClickListener(){
            answers(bu_A4)
        }
        bu_A5.setOnClickListener(){
            answers(bu_A5)
        }
        bu_A6.setOnClickListener(){
            answers(bu_A6)
        }
        bu_A7.setOnClickListener(){
            answers(bu_A7)
        }
        bu_A8.setOnClickListener(){
            answers(bu_A8)
        }
        bu_A9.setOnClickListener(){
            answers(bu_A9)
        }
        bu_A10.setOnClickListener(){
            answers(bu_A10)
        }

        iv_delete.setOnClickListener(){

                if(counter>0){
                    YoYo.with(Techniques.FadeIn).duration(1000).repeat(0).playOn(answer_Names)
                    when(counter){
                        1 -> {
                            myAns = mProductList!![count3].a1
                        }
                        2 -> {
                            myAns = mProductList!![count3].a1+mProductList!![count3].a2
                        }
                        3 -> {
                            myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3
                        }
                        4 -> {
                            myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4
                        }
                        5 -> {
                            myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4+
                                    mProductList!![count3].a5
                        }
                        6 -> {
                            myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4+
                                    mProductList!![count3].a5+mProductList!![count3].a6
                        }
                        7 -> {
                            myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4+mProductList!![count3].a5+
                                    mProductList!![count3].a6+mProductList!![count3].a7
                        }
                        8 -> {
                            myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4+mProductList!![count3].a5+
                                    mProductList!![count3].a6+mProductList!![count3].a7+mProductList!![count3].a8 }

                    }
                    for (i in 0..9){
                        if (myButtons[i].background.alpha == 100){
                            myButtons[i].background.alpha=255
                        }
                    }
                }else{
                    if(myAns!=""){
                        YoYo.with(Techniques.FlipInX).duration(1000).playOn(answer_Names)
                        myAns=""
                        for (i in 0..9){
                            if (!myButtons[i].isClickable){
                                YoYo.with(Techniques.FadeIn).onEnd{myButtons[i].isClickable=true}.duration(1000).repeat(0).playOn(myButtons[i])
                            }else{
                                myButtons[i].background.alpha=255
                            }
                        }
                    }
                }
            answer_Names.text=myAns

        }

        help_Names.setOnClickListener(){
            if (videoLoaded){
                mRewardedVideoAd!!.show()
                help=true
                videoLoaded=false
                counter++
                saveInt(this,"counter",counter)
            }else{
                if (points>=3){
                    counter++
                    saveInt(this,"counter",counter)
                    help()
                    buttonsAlpha()
                    if (counter<=8){
                        points-=3
                        tv_points.text= points.toString()
                        saveInt(this, "points", points)
                    }
                }else{
                    Toast.makeText(this,"يجب عليك الحصول علي 3 محاولات كحد أدني",Toast.LENGTH_SHORT).show()
                }
            }
        }

        ask_Names.setOnClickListener(){
            mediaPlayerClick.start()
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"${bu_ques_Names.text}"+"؟"+"\n"
                    +"حمل لعبة إسلامي من هنا"+"\n"+
                    "https://play.google.com/store/apps/com.beshoApps.islamy")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"اسأل أصدقائك"))

        }

        bu_Next_Names.setOnClickListener(){
            if (correct){
                mediaPlayerClick.seekTo(0)
                mediaPlayerClick.start()
                correct=false
                counter=0
                clickable()
                myAns=""
                count3++
                saveInt(this, "count3", count3)
                remove(this,"counter")
                remove(this,"myAns")
                remove(this,"iv_delete")
                remove(this,"correct_Names")
                if (count3==100){
                    Dialogs().openSuccessDialog(this,mediaPlayerClick, { anim() })
                }else{
                    if (playAds){
                        mInterstitialAd!!.adListener = object: AdListener() {
                            override fun onAdClosed() {
                                mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                                buttonsAlpha()
                                answer_Names.text=myAns
                                buttonsAnim()
                            }
                        }
                        if (mInterstitialAd!!.isLoaded) {
                            mInterstitialAd!!.show()
                        } else {
                            buttonsAlpha()
                            answer_Names.text=myAns
                            buttonsAnim()
                        }
                    }else{
                        buttonsAlpha()
                        answer_Names.text=myAns
                        buttonsAnim()
                    }
                }

            }else{
                Toast.makeText(this, "يجب عليك معرفة الجواب اصحيح", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun setQuestions(){
        bu_ques_Names.text=mProductList!![count3].ques
        var rand= (1 until 6).random()
        when(rand){
            1 -> {
                bu_A1.text=mProductList!![count3].a3
                bu_A2.text=mProductList!![count3].a1
                bu_A3.text=mProductList!![count3].a4
                bu_A4.text=mProductList!![count3].a6
                bu_A5.text=mProductList!![count3].a7
                bu_A6.text=mProductList!![count3].a9
                bu_A7.text=mProductList!![count3].a5
                bu_A8.text=mProductList!![count3].a2
                bu_A9.text=mProductList!![count3].a8
                bu_A10.text=mProductList!![count3].a10
            }
            2 -> {
                bu_A1.text=mProductList!![count3].a10
                bu_A2.text=mProductList!![count3].a5
                bu_A3.text=mProductList!![count3].a1
                bu_A4.text=mProductList!![count3].a7
                bu_A5.text=mProductList!![count3].a3
                bu_A6.text=mProductList!![count3].a9
                bu_A7.text=mProductList!![count3].a6
                bu_A8.text=mProductList!![count3].a2
                bu_A9.text=mProductList!![count3].a4
                bu_A10.text=mProductList!![count3].a8
            }
            3 -> {
                bu_A1.text=mProductList!![count3].a1
                bu_A2.text=mProductList!![count3].a8
                bu_A3.text=mProductList!![count3].a2
                bu_A4.text=mProductList!![count3].a6
                bu_A5.text=mProductList!![count3].a10
                bu_A6.text=mProductList!![count3].a4
                bu_A7.text=mProductList!![count3].a3
                bu_A8.text=mProductList!![count3].a5
                bu_A9.text=mProductList!![count3].a7
                bu_A10.text=mProductList!![count3].a9
            }
            4 -> {
                bu_A1.text=mProductList!![count3].a5
                bu_A2.text=mProductList!![count3].a1
                bu_A3.text=mProductList!![count3].a9
                bu_A4.text=mProductList!![count3].a6
                bu_A5.text=mProductList!![count3].a4
                bu_A6.text=mProductList!![count3].a2
                bu_A7.text=mProductList!![count3].a8
                bu_A8.text=mProductList!![count3].a10
                bu_A9.text=mProductList!![count3].a3
                bu_A10.text=mProductList!![count3].a7
            }
            5 -> {
                bu_A1.text=mProductList!![count3].a3
                bu_A2.text=mProductList!![count3].a9
                bu_A3.text=mProductList!![count3].a4
                bu_A4.text=mProductList!![count3].a8
                bu_A5.text=mProductList!![count3].a2
                bu_A6.text=mProductList!![count3].a6
                bu_A7.text=mProductList!![count3].a10
                bu_A8.text=mProductList!![count3].a7
                bu_A9.text=mProductList!![count3].a5
                bu_A10.text=mProductList!![count3].a1
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun answers(button: Button){
        if (button.background.alpha==100){
            button.background.alpha=255
            var txt=button.text.toString()
            myAns=myAns.replace(txt, "")
            answer_Names.text=myAns
            if(answer_Names.text==mProductList!![count3].correct){
                correct=true
                saveBool(this,"correct_Names",true)
                points++
                tv_points.text= points.toString()
                saveInt(this, "points", points)
                saveString(this,"myAns",myAns)
                notClickable()
                bu_Next_Names.isClickable=false
                YoYo.with(Techniques.RubberBand).duration(700).onEnd { bu_Next_Names.isClickable=true }.repeat(1).playOn(answer_Names)
                mediaPlayerC.start()
            }else{
                YoYo.with(Techniques.Shake).duration(700).playOn(answer_Names)
            }
        }else{
            button.background.alpha=100
            myAns+=button.text
            answer_Names.text=myAns
            if(answer_Names.text==mProductList!![count3].correct){
                correct=true
                saveBool(this,"correct_Names",true)
                points++
                tv_points.text= points.toString()
                saveInt(this, "points", points)
                saveString(this,"myAns",myAns)
                notClickable()
                bu_Next_Names.isClickable=false
                YoYo.with(Techniques.RubberBand).duration(700).onEnd { bu_Next_Names.isClickable=true }.repeat(1).playOn(answer_Names)
                mediaPlayerC.start()
            }
        }

    }


    private fun buttonsAlpha(){
        for (i in 0..9){
            myButtons[i].background.alpha=255
        }
    }

    private fun notClickable(){
        for (i in 0..9){
            myButtons[i].isClickable=false
        }
        iv_delete.isClickable=false
        help_Names.isClickable=false
    }

    private fun clickable(){
        for (i in 0..9){
            myButtons[i].isClickable=true
        }
        iv_delete.isClickable=true
        help_Names.isClickable=true
    }

    private fun buttonsAnim(){
        YoYo.with(Techniques.ZoomOut).onEnd{setQuestions()
            YoYo.with(Techniques.ZoomIn).duration(500).playOn(bu_ques_Names)}.duration(500).playOn(
            bu_ques_Names
        )

        YoYo.with(Techniques.FlipInX).duration(1000).playOn(answer_Names)
        for (i in 0..9){
            YoYo.with(Techniques.ZoomInUp).duration(1000).playOn(myButtons[i])
        }

    }

    private fun help(){
        when(counter){
            1 -> {
                myAns = ""
                check(mProductList!![count3].a1)
            }
            2 -> {
                myAns = mProductList!![count3].a1
                check(mProductList!![count3].a2)
            }
            3 -> {
                myAns = mProductList!![count3].a1+mProductList!![count3].a2
                check(mProductList!![count3].a3)
            }
            4 -> {
                myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3
                check(mProductList!![count3].a4)
            }
            5 -> {
                myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4
                check(mProductList!![count3].a5)
            }
            6 -> {
                myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4+mProductList!![count3].a5
                check(mProductList!![count3].a6)
            }
            7 -> {
                myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4+mProductList!![count3].a5+
                        mProductList!![count3].a6
                check(mProductList!![count3].a7)
            }
            8 -> {
                myAns = mProductList!![count3].a1+mProductList!![count3].a2+mProductList!![count3].a3+mProductList!![count3].a4+mProductList!![count3].a5+
                        mProductList!![count3].a6+mProductList!![count3].a7
                check(mProductList!![count3].a8)
            }

        }
    }

    private fun check(string: String){
        for (i in 0..9){
            if (myButtons[i].text==string){
                YoYo.with(Techniques.FadeOut).
                    onEnd{myButtons[i].isClickable=false}.duration(1000).repeat(0).playOn(myButtons[i])
                myAns+=myButtons[i].text
                answer_Names.text=myAns
                saveString(this,"myAns",myAns)
                YoYo.with(Techniques.FadeIn).duration(1000).repeat(0).playOn(answer_Names)
                if(answer_Names.text==mProductList!![count3].correct){
                    correct=true
                    points++
                    tv_points.text= points.toString()
                    saveInt(this, "points", points)
                    saveString(this,"myAns",myAns)
                    notClickable()
                    bu_Next_Names.isClickable=false
                    YoYo.with(Techniques.RubberBand).duration(700).onEnd { bu_Next_Names.isClickable=true }.repeat(1).playOn(answer_Names)
                    mediaPlayerC.start()
                }
                break
            }
        }
    }

    fun restore(){
        when (counter){
            1 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                        break
                    }
                }
            }
            2 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1||myButtons[i].text==mProductList!![count3].a2){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                    }
                }
            }
            3 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1||myButtons[i].text==mProductList!![count3].a2
                        ||myButtons[i].text==mProductList!![count3].a3){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                    }
                }
            }
            4 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1||myButtons[i].text==mProductList!![count3].a2
                        ||myButtons[i].text==mProductList!![count3].a3||myButtons[i].text==mProductList!![count3].a4){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                    }
                }
            }
            5 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1||myButtons[i].text==mProductList!![count3].a2
                        ||myButtons[i].text==mProductList!![count3].a3||myButtons[i].text==mProductList!![count3].a4
                        ||myButtons[i].text==mProductList!![count3].a5){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                    }
                }
            }
            6 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1||myButtons[i].text==mProductList!![count3].a2
                        ||myButtons[i].text==mProductList!![count3].a3||myButtons[i].text==mProductList!![count3].a4
                        ||myButtons[i].text==mProductList!![count3].a5||myButtons[i].text==mProductList!![count3].a6){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                    }
                }
            }
            7 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1||myButtons[i].text==mProductList!![count3].a2
                        ||myButtons[i].text==mProductList!![count3].a3||myButtons[i].text==mProductList!![count3].a4
                        ||myButtons[i].text==mProductList!![count3].a5||myButtons[i].text==mProductList!![count3].a6
                        ||myButtons[i].text==mProductList!![count3].a7){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                    }
                }
            }
            8 -> {
                for (i in 0..9){
                    if (myButtons[i].text==mProductList!![count3].a1||myButtons[i].text==mProductList!![count3].a2
                        ||myButtons[i].text==mProductList!![count3].a3||myButtons[i].text==mProductList!![count3].a4
                        ||myButtons[i].text==mProductList!![count3].a5||myButtons[i].text==mProductList!![count3].a6
                        ||myButtons[i].text==mProductList!![count3].a7||myButtons[i].text==mProductList!![count3].a8){
                        YoYo.with(Techniques.FadeOut).onEnd { myButtons[i].isClickable=false }.duration(1000).playOn(myButtons[i])
                    }
                }
            }
        }
    }


    companion object{
        var count3=0////////////
    }

    private fun copyDatabase(context: Context) {
        return try {
            val inputStream = context.assets.open(DatabaseHelper.DBNAME)
            val outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME
            val outputStream: OutputStream = FileOutputStream(outFileName)
            val buff = ByteArray(1024)
            var length: Int
            while (inputStream.read(buff).also { length = it } > 0) {
                outputStream.write(buff, 0, length)
            }
            outputStream.flush()
            outputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent=Intent(this, GamesActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right)
        mediaPlayerClick.start()
    }

    fun anim(){
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
    }

    override fun onRewardedVideoAdLoaded() {
        videoLoaded=true
    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
        help=false
        videoLoaded=false
    }

    override fun onRewarded(p0: RewardItem?) {
        if (help){
            help()
            buttonsAlpha()
            saveBool(this,"iv_delete",false)
        }else{
            points+=2
            tv_points.text = points.toString()
        }
        saveInt(this, "points", points)
    }

    override fun onRewardedVideoAdLeftApplication() {

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {

    }

    override fun onRewardedVideoCompleted() {

    }

}