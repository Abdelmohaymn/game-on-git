package com.beshoApps.islamy

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
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
import com.beshoApps.islamy.Dialogs.Companion.playAds
import com.beshoApps.islamy.Dialogs.Companion.saveInt
import com.beshoApps.islamy.QuestionsActivity.Companion.points
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.activity_true_and_false.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.io.FileOutputStream
import java.io.OutputStream
import com.beshoApps.islamy.R.color.colorPrimaryDark as colorPrimaryDark1

@Suppress("DEPRECATION")
class TrueAndFalseActivity : AppCompatActivity(), RewardedVideoAdListener {

    private var mProductList: List<QuesTrueFalse>? = null
    private var mDBHelper: DatabaseHelper? = null
    lateinit var mediaPlayerC:MediaPlayer
    lateinit var mediaPlayerW:MediaPlayer
    lateinit var mediaPlayerClick:MediaPlayer
    var clicked=false
    var anm=false
    var backAnswers=R.drawable.button_question

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_true_and_false)

        mAdView = findViewById(R.id.TF_Ad)
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
        mProductList = mDBHelper!!.listProduct2
        ///////////////////

        mediaPlayerC = MediaPlayer.create(this, R.raw.correctt)
        mediaPlayerW = MediaPlayer.create(this, R.raw.incorrectt)
        mediaPlayerClick = MediaPlayer.create(this, R.raw.click)

        /////////dark state///////////

        if (getBool(this, "dark state", false)){
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            TF_TBar.setBackgroundColor(Color.parseColor("#494545"))
            backAnswers=R.drawable.bu_white
            TrueFalse_Layout.setBackgroundColor(Color.parseColor("#494545"))
            Correction.setBackgroundResource(R.drawable.bu_answer_dark)
            Correction.setTextColor(Color.parseColor("#000000"))
            QuesTF.setBackgroundResource(backAnswers)
            QuesTF.setTextColor(Color.parseColor("#000000"))
            bu_True.setBackgroundResource(backAnswers)
            bu_True.setTextColor(Color.parseColor("#000000"))
            bu_False.setBackgroundResource(backAnswers)
            bu_False.setTextColor(Color.parseColor("#000000"))
            bu_Next_TF.setImageResource(R.drawable.next_dark)

        }

        tv_points.text= points.toString()
        count2 = getInt(this, "count2", 0)
        setQuestions()
        YoYo.with(Techniques.FadeOut).duration(10).playOn(Correction)
        tv_theGames.visibility=View.GONE

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
            QuestionsActivity.openDialog(this, tv_points, mediaPlayerClick, { clickable() })
        }
        tv_points.setOnClickListener(){
            mediaPlayerClick.start()
            QuestionsActivity.openDialog(this, tv_points, mediaPlayerClick, { clickable() })
        }

        bu_True.setOnClickListener(){
            if (points>0){
                if (mProductList!![count2].ans=="1"){
                    mediaPlayerC.start()
                    //lay_under.visibility=View.VISIBLE
                    bu_True.setBackgroundResource(R.drawable.button_true)
                    //bu_True.setTextColor(Color.parseColor("#000000"))
                    bu_Next_TF.isClickable=false
                    YoYo.with(Techniques.Flash).onEnd {bu_True.setBackgroundResource(R.drawable.button_true)
                                                        bu_Next_TF.isClickable=true}
                        .duration(1000).repeat(0).playOn(bu_True)
                    points++
                }else{
                    mediaPlayerW.start()
                    //lay_under.visibility=View.GONE
                    bu_True.setBackgroundResource(R.drawable.button_false)
                    bu_Next_TF.isClickable=false
                    YoYo.with(Techniques.Shake).onEnd{bu_Next_TF.isClickable=true}
                        .duration(1000).repeat(0).playOn(bu_True)
                    //bu_True.startAnimation(shake)
                    points--
                    playAnim(Correction, 0, 250)
                    anm=true
                }
                notClickable()
                tv_points.text= points.toString()
                count2++
                clicked=true
                if(count2==150){
                    Dialogs().openSuccessDialog(this, mediaPlayerClick, { anim() })
                }
                saveInt(this, "count2", count2)
                saveInt(this, "points", points)
            }else{
                Toast.makeText(this, "يجب عليك إضافة محاولة واحدة علي الأقل", Toast.LENGTH_LONG).show()
            }
        }

        bu_False.setOnClickListener(){
            if (points>0){
                if (mProductList!![count2].ans=="0"){
                    mediaPlayerC.start()
                    //lay_under.visibility=View.VISIBLE
                    bu_False.setBackgroundResource(R.drawable.button_true)
                    //bu_False.setTextColor(Color.parseColor("#000000"))
                    bu_Next_TF.isClickable=false
                    YoYo.with(Techniques.Flash).onEnd { bu_False.setBackgroundResource(R.drawable.button_true)
                                                        bu_Next_TF.isClickable=true}
                        .duration(1000).repeat(0).playOn(bu_False)
                    points++
                    playAnim(Correction, 0, 250)
                    anm=true
                }else{
                    mediaPlayerW.start()
                    //lay_under.visibility=View.GONE
                    bu_False.setBackgroundResource(R.drawable.button_false)
                    bu_Next_TF.isClickable=false
                    YoYo.with(Techniques.Shake).onEnd{bu_Next_TF.isClickable=true}
                        .duration(1000).repeat(0).playOn(bu_False)
                    //bu_False.startAnimation(shake)
                    points--
                }
                notClickable()
                tv_points.text= points.toString()
                count2++
                clicked=true
                if(count2==150){
                    Dialogs().openSuccessDialog(this, mediaPlayerClick, { anim() })
                }
                saveInt(this, "count2", count2)
                saveInt(this, "points", points)
            }else{
                Toast.makeText(this, "يجب عليك إضافة محاولة واحدة علي الأقل", Toast.LENGTH_LONG).show()
            }
        }

        bu_Next_TF.setOnClickListener(){
           if (clicked){
               mediaPlayerClick.seekTo(0)
               mediaPlayerClick.start()
               clicked=false
               if (playAds){
                   mInterstitialAd!!.adListener = object: AdListener() {
                       override fun onAdClosed() {
                           mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                           playAnim(QuesTF, 0, 500)
                           if (anm){
                               YoYo.with(Techniques.FadeOut).duration(500).playOn(Correction)
                               anm=false
                           }
                           ansBackground()
                       }
                   }
                   if (mInterstitialAd!!.isLoaded) {
                       mInterstitialAd!!.show()
                   } else {
                       playAnim(QuesTF, 0, 500)
                       if (anm){
                           YoYo.with(Techniques.FadeOut).duration(500).playOn(Correction)
                           anm=false
                       }
                       ansBackground()
                   }
               }else{
                   playAnim(QuesTF, 0, 500)
                   if (anm){
                       YoYo.with(Techniques.FadeOut).duration(500).playOn(Correction)
                       anm=false
                   }
                   ansBackground()
               }
           }else{
               if (points>0){
                   Toast.makeText(this, "يجب عليك معرفة الجواب الصحيح", Toast.LENGTH_LONG).show()
               }else{
                   Toast.makeText(this, "يجب عليك إضافة محاولة واحدة علي الأقل", Toast.LENGTH_LONG).show()
               }
           }

        }

    }

    private fun setQuestions(){
        QuesTF.text=mProductList!![count2].ques
        Correction.text="التصحيح : "+mProductList!![count2].correct
    }

    fun notClickable(){
        bu_True.isClickable=false
        bu_False.isClickable=false
    }

    fun clickable(){
        bu_True.isClickable=true
        bu_False.isClickable=true
    }

    private fun ansBackground(){
        bu_True.setBackgroundResource(backAnswers)
        bu_False.setBackgroundResource(backAnswers)
    }

    /////////////

    companion object{
        var count2=0

    }

    private fun playAnim(view: View, value: Int, der: Long) {
        view.animate().alpha(value.toFloat()).scaleX(value.toFloat()).scaleY(value.toFloat()).setDuration(
            der
        ).setStartDelay(100)
            .setInterpolator(DecelerateInterpolator()).setListener(object :
                Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    if (value == 0) {
                        playAnim(view, 1, der)
                    }
                    if (view == QuesTF) {
                        setQuestions()
                    }
                    if (view == QuesTF&&value==1) {
                        clickable()
                    }
                    if (view == Correction) {
                        Correction.visibility = View.VISIBLE
                    }

                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }

    fun anim(){
        overridePendingTransition(R.anim.slide_left, R.anim.slide_right)
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

    override fun onRewardedVideoAdLoaded() {

    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
    }

    override fun onRewarded(p0: RewardItem?) {
        points+=2
        tv_points.text = points.toString()
        saveInt(this, "points", points)
    }

    override fun onRewardedVideoAdLeftApplication() {

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {

    }

    override fun onRewardedVideoCompleted() {

    }

}