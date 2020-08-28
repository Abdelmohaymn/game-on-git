package com.elcaesar.mygame

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*


@Suppress("DEPRECATION")
 class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        var mediaPlayerClick:MediaPlayer = MediaPlayer.create(this,R.raw.click)
        var check=false

        val bottomSheetDialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        val view=layoutInflater.inflate(R.layout.bottom_sheet,null)

        bottomSheetDialog.setContentView(view)
        //bottomSheetDialog.setCancelable(false)



        bu_play.setOnClickListener(){
            mediaPlayerClick.start()
            val intent=Intent(this,QuestionsActivity::class.java)
            intent.putExtra("check",check)
            if (MyDialogP.playAds){
                mInterstitialAd.adListener = object: AdListener() {
                    override fun onAdClosed() {
                        mInterstitialAd.loadAd(AdRequest.Builder().build())
                        startActivity(intent)
                    }
                }
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    startActivity(intent)
                }
            }else{
                startActivity(intent)
            }
        }

        image_sheet.setOnClickListener(){
            mediaPlayerClick.start()
            bottomSheetDialog.show()
        }

        view.switch_disSound.setOnCheckedChangeListener(){ _ : CompoundButton?, isChecked: Boolean ->

            if (isChecked){
                check=true
                mediaPlayerClick.setVolume(0F, 0F)
            }else{
                check=false
                mediaPlayerClick.setVolume(1F, 1F)
            }
            mediaPlayerClick.start()
        }

        view.share.setOnClickListener(){
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"هيا العب معي هذه اللعبة"+"\n"
                    +"https://play.google.com/store/apps/com.elcaesar.mygame")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"اختر تطبيق"))
        }
        //////********////////
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.visibility=View.GONE
        //////********/////////
        view.freePoints.setOnClickListener(){

            MyDialogP().openDialog(this,tv_points, {empty()})
            mediaPlayerClick.start()
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

}
