package com.elcaesar.mygame

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.elcaesar.mygame.AdsClass.Companion.MyRewardedAd
import com.elcaesar.mygame.AdsClass.Companion.bannerAd
import com.elcaesar.mygame.AdsClass.Companion.loadRewardedVideoAd
import com.elcaesar.mygame.AdsClass.Companion.mAdView
import com.elcaesar.mygame.AdsClass.Companion.mInterstitialAd
import com.elcaesar.mygame.AdsClass.Companion.mRewardedVideoAd
import com.elcaesar.mygame.AdsClass.Companion.mobileAds
import com.elcaesar.mygame.Dialogs.Companion.getInt
import com.elcaesar.mygame.Dialogs.Companion.playAds
import com.elcaesar.mygame.Dialogs.Companion.saveInt
import com.elcaesar.mygame.QuestionsActivity.Companion.points
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_true_and_false.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.io.FileOutputStream
import java.io.OutputStream

class TrueAndFalseActivity : AppCompatActivity(), RewardedVideoAdListener {

    private var mProductList: List<QuesTrueFalse>? = null
    private var mDBHelper: DatabaseHelper? = null
    lateinit var mediaPlayerC:MediaPlayer
    lateinit var mediaPlayerW:MediaPlayer
    lateinit var mediaPlayerClick:MediaPlayer
    var clicked=false

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

        var shake:Animation= AnimationUtils.loadAnimation(applicationContext, R.anim.shake)

        tv_points.text= points.toString()
        count2 = getInt(this, "count2", 0)
        setQuestions()
        Correction.visibility= View.GONE
        tv_theGames.visibility=View.GONE

        bu_back.setOnClickListener(){
            mediaPlayerClick.start()
            val intent= Intent(this, GamesActivity::class.java)
            if (playAds){
                mInterstitialAd!!.adListener = object: AdListener() {
                    override fun onAdClosed() {
                        mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                        startActivity(intent)
                        //overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
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

        bu_True.setOnClickListener(){
            if (points>0){
                if (mProductList!![count2].ans=="1"){
                    mediaPlayerC.start()
                    //bu_True.setBackgroundResource(R.drawable.button_true)
                    YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(bu_True)
                    points++
                }else{
                    mediaPlayerW.start()
                    bu_True.setBackgroundResource(R.drawable.button_false)
                    bu_True.startAnimation(shake)
                    points--
                    playAnim(Correction, 0, 250)
                }
                notClickable()
                tv_points.text= points.toString()
                count2++
                clicked=true
                if(count2==5){
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
                    //bu_False.setBackgroundResource(R.drawable.button_true)
                    YoYo.with(Techniques.Flash).duration(700).repeat(1).playOn(bu_False)
                    points++
                    playAnim(Correction, 0, 250)
                }else{
                    mediaPlayerW.start()
                    bu_False.setBackgroundResource(R.drawable.button_false)
                    bu_False.startAnimation(shake)
                    points--
                }
                notClickable()
                tv_points.text= points.toString()
                count2++
                clicked=true
                if(count2==5){
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
               clickable()
               clicked=false
               if (playAds){
                   mInterstitialAd!!.adListener = object: AdListener() {
                       override fun onAdClosed() {
                           mInterstitialAd!!.loadAd(AdRequest.Builder().build())
                           playAnim(QuesTF, 0, 500)
                           Correction.visibility= View.GONE
                           ansBackground()
                       }
                   }
                   if (mInterstitialAd!!.isLoaded) {
                       mInterstitialAd!!.show()
                   } else {
                       playAnim(QuesTF, 0, 500)
                       Correction.visibility= View.GONE
                       ansBackground()
                   }
               }else{
                   playAnim(QuesTF, 0, 500)
                   Correction.visibility= View.GONE
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
        bu_True.setBackgroundResource(R.drawable.button_question)
        bu_False.setBackgroundResource(R.drawable.button_question)
    }

    /////////////

    companion object{
        var count2=0

    }

    private fun playAnim(view: View, value: Int, der: Long) {
        view.animate().alpha(value.toFloat()).scaleX(value.toFloat()).scaleY(value.toFloat()).setDuration(der).setStartDelay(100)
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