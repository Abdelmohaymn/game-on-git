package com.elcaesar.mygame

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.elcaesar.mygame.AdsClass.Companion.MyRewardedAd
import com.elcaesar.mygame.AdsClass.Companion.mInterstitialAd
import com.elcaesar.mygame.AdsClass.Companion.mRewardedVideoAd
import com.elcaesar.mygame.AdsClass.Companion.mobileAds
import com.elcaesar.mygame.Dialogs.Companion.getBool
import com.elcaesar.mygame.Dialogs.Companion.getInt
import com.elcaesar.mygame.Dialogs.Companion.playAds
import com.elcaesar.mygame.Dialogs.Companion.remove
import com.elcaesar.mygame.Dialogs.Companion.saveBool
import com.elcaesar.mygame.QuestionsActivity.Companion.openDialog
import com.elcaesar.mygame.QuestionsActivity.Companion.points
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*


@Suppress("DEPRECATION")
 class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getWindow.setStatusBarColor(ContextCompat.getColor(activity,R.color.my_statusbar_color))
        var mediaPlayerClick:MediaPlayer = MediaPlayer.create(this,R.raw.click)
        val amanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val bottomSheetDialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        val view=layoutInflater.inflate(R.layout.bottom_sheet,null)

        bottomSheetDialog.setContentView(view)
        //bottomSheetDialog.setCancelable(false)

        mobileAds(this)
        MyRewardedAd(this)
        mRewardedVideoAd!!.rewardedVideoAdListener = QuestionsActivity()
        points= getInt(this, "points", 10)
        playAds= getBool(this,"play Ads",false)


        bu_play.setOnClickListener(){
            mediaPlayerClick.start()
            val intent=Intent(this,GamesActivity::class.java)
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

        image_sheet.setOnClickListener(){
            mediaPlayerClick.start()
            bottomSheetDialog.show()
        }

        view.switch_disSound.isChecked= getBool(this,"sounds",false)
        view.switch_disSound.setOnCheckedChangeListener(){ _ : CompoundButton?, isChecked: Boolean ->
            if (isChecked){
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, true)
                saveBool(this,"sounds",true)
            }else{

                amanager.setStreamMute(AudioManager.STREAM_MUSIC, false)
                saveBool(this,"sounds",false)
            }
            mediaPlayerClick.start()
        }

        view.share.setOnClickListener(){
            mediaPlayerClick.start()
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"هيا العب معي هذه اللعبة"+"\n"
                    +"https://play.google.com/store/apps/com.elcaesar.mygame")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"اختر تطبيق"))
        }

        view.freePoints.setOnClickListener(){
            mediaPlayerClick.start()
            openDialog(this,tv_points,mediaPlayerClick, {empty()})
        }

        view.buRate.setOnClickListener(){
            mediaPlayerClick.start()
            val uri: Uri = Uri.parse("market://details?id=com.elcaesar.mygame")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.elcaesar.mygame")))
            }
        }

    }

    fun empty(){

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        remove(this, "bu Ads")
        remove(this, "bu Ads2")
    }


}
