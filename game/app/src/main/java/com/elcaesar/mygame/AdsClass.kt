package com.elcaesar.mygame

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener


@Suppress("DEPRECATION")
class AdsClass {

    companion object{

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

    }

}