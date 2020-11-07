package com.beshoApps.islamy

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.beshoApps.islamy.AdsClass.Companion.MyRewardedAd
import com.beshoApps.islamy.AdsClass.Companion.mInterstitialAd
import com.beshoApps.islamy.AdsClass.Companion.mRewardedVideoAd
import com.beshoApps.islamy.AdsClass.Companion.mobileAds
import com.beshoApps.islamy.Dialogs.Companion.getBool
import com.beshoApps.islamy.Dialogs.Companion.getInt
import com.beshoApps.islamy.Dialogs.Companion.playAds
import com.beshoApps.islamy.Dialogs.Companion.remove
import com.beshoApps.islamy.Dialogs.Companion.saveBool
import com.beshoApps.islamy.QuestionsActivity.Companion.openDialog
import com.beshoApps.islamy.QuestionsActivity.Companion.points
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

        //getWindow.statusBarColor(ContextCompat.getColor(MainActivity,R.color.blue))
        changeColor(R.color.blue2)
        var mediaPlayerClick:MediaPlayer = MediaPlayer.create(this, R.raw.click)
        val amanager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view=layoutInflater.inflate(R.layout.bottom_sheet, null)

        bottomSheetDialog.setContentView(view)
        //bottomSheetDialog.setCancelable(false)

        mobileAds(this)
        MyRewardedAd(this)
        mRewardedVideoAd!!.rewardedVideoAdListener = QuestionsActivity()
        points= getInt(this, "points", 5)
        playAds= getBool(this, "play Ads", false)


        bu_play.setOnClickListener(){
            mediaPlayerClick.start()
            val intent=Intent(this, GamesActivity::class.java)
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
                    overridePendingTransition(R.anim.slide_right0, R.anim.slide_right00)
                }
            }else{
                startActivity(intent)
                overridePendingTransition(R.anim.slide_right0, R.anim.slide_right00)
            }
        }

        image_sheet.setOnClickListener(){
            mediaPlayerClick.start()
            bottomSheetDialog.show()
        }

        bu_share.setOnClickListener(){
            mediaPlayerClick.start()
            share()
        }

        bu_insta.setOnClickListener(){
            mediaPlayerClick.start()
            val uri = Uri.parse("https://www.instagram.com/besho_apps/")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.setPackage("com.instagram.android")

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/besho_apps/")
                    )
                )
            }
        }

        view.switch_disSound.isChecked= getBool(this, "sounds", false)
        view.switch_disSound.setOnCheckedChangeListener(){ _: CompoundButton?, isChecked: Boolean ->
            if (isChecked){
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, true)
                saveBool(this, "sounds", true)
            }else{

                amanager.setStreamMute(AudioManager.STREAM_MUSIC, false)
                saveBool(this, "sounds", false)
            }
            mediaPlayerClick.start()
        }

        view.share.setOnClickListener(){
            mediaPlayerClick.start()
            share()
        }

        view.freePoints.setOnClickListener(){
            mediaPlayerClick.start()
            openDialog(this, tv_points, mediaPlayerClick, { empty() })
        }

        view.buRate.setOnClickListener(){
            mediaPlayerClick.start()
            val uri: Uri = Uri.parse("market://details?id=com.beshoApps.islamy")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.beshoApps.islamy")
                    )
                )
            }
        }

    }

    fun share(){
        val intent= Intent()
        intent.action=Intent.ACTION_SEND
        intent.putExtra(
            Intent.EXTRA_TEXT, "هيا العب معي هذه اللعبة" + "\n"
                    + "https://play.google.com/store/apps/com.beshoApps.islamy"
        )
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent, "اختر تطبيق"))
    }

    fun empty(){

    }

    fun changeColor(resourseColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, resourseColor)
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        remove(this, "bu Ads")
        remove(this, "bu Ads2")
    }


}
