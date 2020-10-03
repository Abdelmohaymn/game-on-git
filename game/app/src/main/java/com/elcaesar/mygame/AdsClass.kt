package com.elcaesar.mygame

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_questions.*
import kotlinx.android.synthetic.main.toolbar_layout.*


@Suppress("DEPRECATION")
class AdsClass()  {

    companion object {

        var mInterstitialAd: InterstitialAd? = null
        var mRewardedVideoAd: RewardedVideoAd? = null
        lateinit var mAdView : AdView

        fun bannerAd(){
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
        }

        fun mobileAds(context:Context) {
            MobileAds.initialize(context)
            mInterstitialAd = InterstitialAd(context)
            mInterstitialAd!!.adUnitId = "ca-app-pub-3940256099942544/1033173712"
            mInterstitialAd!!.loadAd(AdRequest.Builder().build())

        }

        fun loadRewardedVideoAd() {
            if (!mRewardedVideoAd!!.isLoaded) {
                mRewardedVideoAd!!.loadAd("ca-app-pub-3940256099942544/5224354917",
                    AdRequest.Builder().build())
            }
        }

        fun MyRewardedAd(context: Context){
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context)
            loadRewardedVideoAd()
        }

    }

}