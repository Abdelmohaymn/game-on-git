package com.elcaesar.mygame

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.elcaesar.mygame.AdsClass.Companion.mInterstitialAd
import com.elcaesar.mygame.Dialogs.Companion.getInt
import com.elcaesar.mygame.Dialogs.Companion.playAds
import com.elcaesar.mygame.QuestionsActivity.Companion.count
import com.elcaesar.mygame.TrueAndFalseActivity.Companion.count2
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_games.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class GamesActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        var mediaPlayerClick: MediaPlayer = MediaPlayer.create(this,R.raw.click)

        iv_points_toolbar.visibility= View.GONE
        count=getInt(this,"count",0)
        tv_num1.text=count.toString()+"/200"
        count2=getInt(this,"count2",0)
        tv_num2.text=count2.toString()+"/5"
        //toolbar.setBackgroundColor(R.color.material_on_surface_stroke);


        bu_back.setOnClickListener(){
            mediaPlayerClick.start()
            val intent=Intent(this,MainActivity::class.java)
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
                    overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
                }
            }else{
                startActivity(intent)
                overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
            }
        }

        first_game.setOnClickListener(){
            mediaPlayerClick.start()
            if (count==200){
                Dialogs().openAgainDialog(this,mediaPlayerClick,QuestionsActivity(),{anim()},1)
            }else{
                val intent= Intent(this,QuestionsActivity::class.java)
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
                        overridePendingTransition(R.anim.slide_right0,R.anim.slide_right00)
                    }
                }else{
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_right0,R.anim.slide_right00)
                }
            }
        }

        second_game.setOnClickListener(){
            mediaPlayerClick.start()
            if (count2==5){
                Dialogs().openAgainDialog(this,mediaPlayerClick,TrueAndFalseActivity(),{anim()},2)
            }else{
                val intent= Intent(this,TrueAndFalseActivity::class.java)
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
                        overridePendingTransition(R.anim.slide_right0,R.anim.slide_right00)
                    }
                }else{
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_right0,R.anim.slide_right00)
                }
            }
        }

        third_game.setOnClickListener(){
            Toast.makeText(this,"vvvvv",Toast.LENGTH_LONG).show()
            //val intent= Intent(this,TrueAndFalseActivity::class.java)
            //startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left,R.anim.slide_right)
    }

    fun anim(){
        overridePendingTransition(R.anim.slide_right0,R.anim.slide_right00)
    }

}
